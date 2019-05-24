package cn.emay.eucp.web.manage.controller.global.fms.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.account.AccountDTO;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsAccount;
import cn.emay.eucp.common.moudle.db.fms.FmsAccountDetails;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.fms.FmsAccountDetailService;
import cn.emay.eucp.data.service.fms.FmsAccountService;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_ACCOUNT_BALANCE")
@RequestMapping("/fms/account/balance")
@Controller
public class FmsAccountController {
	@Resource
	private FmsAccountService fmsAccountService;
	@Resource
	private EnterpriseService enterpriseService;
	@Resource
	private FmsAccountDetailService fmsAccountDetailService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;
	@Resource
	private RedisClient redis;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
		Map<String, List<FmsBusinesTypeDto>> map = new HashMap<String, List<FmsBusinesTypeDto>>();
		for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
			if (!map.isEmpty()) {
				if (!map.containsKey(fmsBusinesTypeDto.getParentName())) {
					List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
					list2.add(fmsBusinesTypeDto);
					map.put(fmsBusinesTypeDto.getParentName(), list2);
				} else {
					String parentName = fmsBusinesTypeDto.getParentName();
					List<FmsBusinesTypeDto> list3 = map.get(parentName);
					list3.add(fmsBusinesTypeDto);
					map.put(parentName, list3);
				}
			} else {
				List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
				list2.add(fmsBusinesTypeDto);
				map.put(fmsBusinesTypeDto.getParentName(), list2);
			}
		}
		model.addAttribute("map", map);
		return "fms/account/balance";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		String appId = RequestUtils.getParameter(request, "appId");
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 0);
		String enterpriseName = RequestUtils.getParameter(request, "enterpriseName");
		List<Long> enterpriseId = null;
		if (null != enterpriseName) {
			enterpriseId = enterpriseService.findEnterpriseIdListByName(enterpriseName);
		}
		Page<AccountDTO> page = fmsAccountService.findList(appId, serviceCodeId, start, limit, businessTypeId, enterpriseId);
		Set<Long> enterpriseIdList = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDTO dto : page.getList()) {
				enterpriseIdList.add(dto.getEnterpriseId());
			}
		}
		List<Enterprise> findByIds = null;
		if (null != enterpriseIdList && enterpriseIdList.size() > 0) {
			findByIds = enterpriseService.findByIds(enterpriseIdList);
		}
		Map<Long, String> map = new HashMap<Long, String>();
		if (null != findByIds) {
			for (Enterprise enterprise : findByIds) {
				map.put(enterprise.getId(), enterprise.getNameCn());
			}
		}
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDTO dto : page.getList()) {
				dto.setEnterpriseName(map.get(dto.getEnterpriseId()));
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	@RequestMapping("ajax/operation")
	private void operation(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		Long balance = RequestUtils.getLongParameter(request, "balance", 0L);
		String remark = RequestUtils.getParameter(request, "remark");
		int type = RequestUtils.getIntParameter(request, "type", 0);
		FmsAccount account = fmsAccountService.findbyId(id);
		if (account == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("此账单不存在"));
			return;
		}
		account.setRemark(remark);
		FmsAccountDetails details = new FmsAccountDetails();
		if (type == 0 || type == 2) {
			account.setBalance(account.getBalance() + balance);
			details.setRemainingNumber(account.getBalance());
		} else {
			if (account.getBalance().longValue() > balance.longValue()) {
				account.setBalance(account.getBalance() - balance);
				details.setRemainingNumber(account.getBalance());
			} else {
				ResponseUtils.outputWithJson(response, Result.badResult("扣款金额不能大于剩余金额"));
				return;
			}
		}
		details.setAppId(account.getAppId());
		details.setEnterpriseId(account.getEnterpriseId());
		details.setOperationTime(new Date());
		details.setOperationType(type);
		details.setChangeNumber(balance);
		details.setRemark(remark);
		details.setUserId(user.getId());
		details.setServiceCodeId(account.getServiceCodeId());
		fmsAccountService.update(account);
		fmsAccountDetailService.save(details);
		redis.hset(RedisConstants.FMS_SERVICE_CODE_BALANCE_HASH, account.getAppId(), account.getBalance(), -1);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

}
