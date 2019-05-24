package cn.emay.eucp.web.client.controller.base;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.mrp.dto.BindListInfoDTO;
import cn.emay.eucp.common.mrp.dto.ServiceCodeDTO;
import cn.emay.eucp.common.mrp.dto.ServiceCodeMapDTO;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 账号与特服号绑定
 * 
 * @author dinghaijiao
 * 
 */
@RequestMapping("/basesupport/account")
@Controller
public class BaseFmsAccountController {
	private Logger log = Logger.getLogger(BaseFmsAccountController.class);

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "fmsServiceCodeService")
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource(name = "clientUserOperLogService")
	private ClientUserOperLogService clientUserOperLogService;
	@Resource(name = "fmsUserServiceCodeAssignService")
	private FmsUserServiceCodeAssignService fmsUserServiceCodeAssignService;

	/**
	 * 查看用户已经绑定的短信服务号、平台代码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/getBindServiceCode")
	@PageAuth({ "SYS_CLIENT_ACCOUNT", "SYS_CLIENT_ADMINISTRA" })
	@OperationAuth({ "SYS_CLIENT_ACCOUNT_CHECK", "OPER_SYS_CLIENT_ADMINISTRA_BIND" })
	public void getBindServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, Result.rightResult(result2));
			return;
		}
		BindListInfoDTO dto = new BindListInfoDTO();
		List<FmsServiceCode> dtoList = fmsUserServiceCodeAssignService.findByAssignUserId(userId);
		List<ServiceCodeDTO> list = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : dtoList) {
			ServiceCodeDTO dto2 = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			list.add(dto2);
		}
		dto.setBindingList(list);
		dto.setType("0");
		dto.setCode(CommonConstants.WEB_BUSINESS_CODE);
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	/**
	 * 获取所有的 特服数据显示
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ajax/getServiceCode")
	@PageAuth({ "SYS_CLIENT_ACCOUNT", "SYS_CLIENT_ADMINISTRA" })
	@OperationAuth({ "SYS_CLIENT_ACCOUNT_BINDING", "OPER_SYS_CLIENT_ADMINISTRA_BIND" })
	public void getServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, Result.rightResult(result2));
			return;
		}
		Map<String, Object> map = fmsUserServiceCodeAssignService.findBinding(userId, (Long) result2.getResult());
		List<FmsServiceCode> notList = (List<FmsServiceCode>) map.get("notBoundList");
		List<ServiceCodeDTO> notBoundList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : notList) {
			ServiceCodeDTO dto = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			notBoundList.add(dto);
		}
		List<FmsServiceCode> binList = (List<FmsServiceCode>) map.get("bindingList");
		List<ServiceCodeDTO> bindingList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : binList) {
			ServiceCodeDTO dto = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			bindingList.add(dto);
		}
		List<FmsServiceCode> allList = (List<FmsServiceCode>) map.get("allEffectiveList");
		List<ServiceCodeDTO> allEffectiveList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : allList) {
			ServiceCodeDTO dto = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			allEffectiveList.add(dto);
		}
		ServiceCodeMapDTO mapDTO = new ServiceCodeMapDTO();
		mapDTO.setNotBoundList(notBoundList);
		mapDTO.setBindingList(bindingList);
		mapDTO.setAllEffectiveList(allEffectiveList);
		ResponseUtils.outputWithJson(response, Result.rightResult(mapDTO));
	}

	/**
	 * 用户绑定特服号
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/bindServiceCode")
	@PageAuth({ "SYS_CLIENT_ACCOUNT", "SYS_CLIENT_ADMINISTRA" })
	@OperationAuth({ "SYS_CLIENT_ACCOUNT_BINDING", "OPER_SYS_CLIENT_ADMINISTRA_BIND" })
	public void bindServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		String serviceCodeIds = RequestUtils.getParameter(request, "serviceCodeIds", "");
		if (RegularUtils.isEmpty(serviceCodeIds)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Result result = fmsUserServiceCodeAssignService.modifyfmsUserSCAssign(userId, (Long) result2.getResult(), serviceCodeIds);
		if (result.getSuccess()) {
			User user = userService.findById(userId);
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			String service = "系统服务";
			String module = "账号绑定";
			String context = "修改{0}的绑定闪推服务号";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(context, new Object[] { user.getUsername() }), ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->用户:" + currentUser.getUsername() + "为:" + result.getResult() + "修改绑定闪推服务号");
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	/**
	 * 用户解除单个短信服务号绑定
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/deleteServiceCode")
	@PageAuth({ "SYS_CLIENT_ACCOUNT", "SYS_CLIENT_ADMINISTRA" })
	@OperationAuth({ "SYS_CLIENT_ACCOUNT_BINDING", "OPER_SYS_CLIENT_ADMINISTRA_BIND" })
	public void deleteServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		FmsServiceCode serviceCode = fmsServiceCodeService.findById(serviceCodeId);
		if (null == serviceCode) {
			ResponseUtils.outputWithJson(response, Result.badResult("该服务号不存在"));
			return;
		}
		if (serviceCodeId == null || serviceCodeId.longValue() == 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Result result = fmsUserServiceCodeAssignService.deleteImsBind(userId, serviceCodeId);
		if (result.getSuccess()) {
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			User user = userService.findUserById(userId);
			FmsServiceCode imsServiceCode = fmsServiceCodeService.findById(serviceCodeId);
			String service = "系统服务";
			String module = "账号绑定";
			String context = "解除【" + user.getUsername() + "】关联的闪推服务号:【" + imsServiceCode.getServiceCode() + "】";
			clientUserOperLogService.save(service, module, currentUser, context, ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->" + context);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

}
