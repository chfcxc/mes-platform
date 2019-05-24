package cn.emay.eucp.web.client.controller.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.mrp.dto.ClientIndexDTO;
import cn.emay.eucp.common.mrp.dto.ClientMrpTransferDTO;
import cn.emay.eucp.data.service.fms.FmsServiceCodeParamService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionMonthService;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.util.DateUtil;
import cn.emay.util.ResponseUtils;

/**
 * 对client-mrp提供的短信数据接口
 *
 * @author dinghaijiao
 *
 */
@RequestMapping("/basesupport")
@Controller
public class BaseFmsController {

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "fmsServiceCodeService")
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource
	private FmsServicecodeConsumptionMonthService fmsServicecodeConsumptionMonthService;
	@Resource
	private FmsUserServiceCodeAssignService fmsUserServiceCodeAssignService;
	@Resource
	private FmsServiceCodeParamService fmsServiceCodeParamService;

	@RequestMapping("/getServiceCodeInfo")
	public void getServiceCodeInfo(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String serviceModule = "0";
		ClientIndexDTO clientIndexDTO = new ClientIndexDTO();
		clientIndexDTO.setServiceModules(serviceModule);
		Long enterpriseId = userService.findenterpriseIdbyUser(user.getId());
		clientIndexDTO.setEnterpriseId(enterpriseId);

		List<String> serviceTypeList = new ArrayList<String>();
		String[] ob = (serviceModule).split(",");
		for (String o : ob) {
			if (!StringUtils.isEmpty(o)) {
				serviceTypeList.add(o);
			}
		}
		clientIndexDTO.setServiceCount(0);// 默认值为0
		if (null != serviceTypeList && serviceTypeList.size() > 0) {
			clientIndexDTO.setServiceCount(serviceTypeList.size());
		}
		// 服务号信息
		// List<List<ClientMrpTransferDTO>> serviceCodeList = new ArrayList<List<ClientMrpTransferDTO>>();
		// 短信服务号--首页展示3条
		int limit = 3;
		List<FmsServiceCode> mmsServiceCodeList = fmsUserServiceCodeAssignService.findByAssignUserId(user.getId(), 1, 3);
		// List<?> accountServiceCodeList = smsServiceCodeService.findClientAccountServiceCode(clientIndexDTO.getEnterpriseId(), null, user.getId(), -1, 0, limit);
		Map<String, String> appidMap = new HashMap<String, String>();
		if (null != mmsServiceCodeList & mmsServiceCodeList.size() > 0) {
			List<String> appid = new ArrayList<String>();
			for (FmsServiceCode ferviceCode : mmsServiceCodeList) {
				appid.add(ferviceCode.getAppId());
			}
			appidMap = fmsServiceCodeParamService.findbyAppid(appid);
		}
		List<List<ClientMrpTransferDTO>> serviceCodeList = new ArrayList<List<ClientMrpTransferDTO>>();
		for (FmsServiceCode object : mmsServiceCodeList) {
			List<ClientMrpTransferDTO> serviceCodeInfoList = new ArrayList<ClientMrpTransferDTO>();
			serviceCodeInfoList.add(new ClientMrpTransferDTO("serviceCode", "彩信服务号", object.getServiceCode()));
			String string = appidMap.get(object.getAppId());
			serviceCodeInfoList.add(new ClientMrpTransferDTO("ip", "绑定IP", string));
			serviceCodeInfoList.add(new ClientMrpTransferDTO("appId", "APPID", object.getAppId()));
			serviceCodeInfoList.add(new ClientMrpTransferDTO("secretKey", "秘钥", object.getSecretKey()));
			serviceCodeList.add(serviceCodeInfoList);
		}

		Long imsCount = 0L;
		Date startTime = DateUtil.getNowMonthFirstDay();
		Date endTime = DateUtil.getNowMonthLastDay();
		List<FmsServiceCode> dtoList = null;
		int currentPage = 1;
		do {
			dtoList = fmsUserServiceCodeAssignService.findByAssignUserId(user.getId(), currentPage, currentPage * limit);
			if (dtoList != null && dtoList.size() > 0) {
				List<Long> scList = new ArrayList<Long>();// 服务号
				for (FmsServiceCode dto : dtoList) {
					scList.add(dto.getId());
				}
				// 目前查ImsMessage后期报表完成 可优化为查服务号报表
				Long countNumber = fmsServicecodeConsumptionMonthService.countByServiceCode(scList, startTime, endTime);
				if (null == countNumber) {
					countNumber = 0L;
				}
				imsCount += countNumber;
			}
			currentPage++;
		} while (dtoList != null && dtoList.size() == limit);
		clientIndexDTO.setCount(imsCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("serviceCodeList", serviceCodeList);
		map.put("clientInfo", clientIndexDTO);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}
}
