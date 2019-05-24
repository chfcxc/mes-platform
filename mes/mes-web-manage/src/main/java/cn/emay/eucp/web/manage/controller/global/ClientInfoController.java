package cn.emay.eucp.web.manage.controller.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.mrp.dto.BindListInfoDTO;
import cn.emay.eucp.common.mrp.dto.ClientServiceCodeCellDTO;
import cn.emay.eucp.common.mrp.dto.ServiceCodeAndPlatformDTO;
import cn.emay.eucp.common.mrp.dto.ServiceCodeAndPlatformMapDTO;
import cn.emay.eucp.common.mrp.dto.ServiceCodeDTO;
import cn.emay.eucp.common.mrp.dto.ServiceCodeMapDTO;
import cn.emay.eucp.data.service.fms.FmsServiceCodeParamService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 客户信息管理
 *
 * @author Frank
 *
 */
@PageAuth("SYS_CLIENT_INFO")
@RequestMapping("/basesupport")
@Controller
public class ClientInfoController {

	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "fmsServiceCodeService")
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource(name = "fmsUserServiceCodeAssignService")
	private FmsUserServiceCodeAssignService fmsUserServiceCodeAssignService;
	@Resource
	private FmsServiceCodeParamService fmsServiceCodeParamService;

	/**
	 * 查看客户账号详情--关联短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_ACCOUNT_CLIENT")
	@RequestMapping("/getServiceCode")
	public void getServiceCode(HttpServletRequest request, HttpServletResponse response) {
		// 查询关联服务号，即是查询主账号关联服务号，主账号默认绑定所有属于该企业的服务号
		Long id = RequestUtils.getLongParameter(request, "id", 0L);// 企业id
		// 主账号用户id
		Object object = userService.findMangerAccount(id);
		if (object == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("数据不存在"));
			return;
		}
		/*
		 * Object[] mangerAccount = (Object[]) object; Long userId = Long.valueOf(mangerAccount[0].toString());
		 */
		// 管理账户关联特服号
		List<List<ClientServiceCodeCellDTO>> columns = new ArrayList<List<ClientServiceCodeCellDTO>>();
		// List<?> accountServiceCodeList = imsServiceCodeService.findManageAccountServiceCode(userId);

		List<FmsServiceCode> dtoList = fmsServiceCodeService.findByEnterId(id, -1);
		Map<String, String> map = new HashMap<String, String>();
		if (null != dtoList & dtoList.size() > 0) {
			List<String> appid = new ArrayList<String>();
			for (FmsServiceCode ferviceCode : dtoList) {
				appid.add(ferviceCode.getAppId());
			}
			map = fmsServiceCodeParamService.findbyAppid(appid);
		}
		for (FmsServiceCode temp : dtoList) {
			List<ClientServiceCodeCellDTO> column = new ArrayList<ClientServiceCodeCellDTO>();// 一行的数据
			column.add(new ClientServiceCodeCellDTO("服务号", "serviceCode", temp.getServiceCode()));
			column.add(new ClientServiceCodeCellDTO("服务模块", "module", "闪推服务"));
			column.add(new ClientServiceCodeCellDTO("APPID", "appId", temp.getAppId()));
			column.add(new ClientServiceCodeCellDTO("秘钥", "secretKey", temp.getSecretKey()));
			column.add(new ClientServiceCodeCellDTO("绑定IP", "requestIps", map.get(temp.getAppId())));
			int state = 0;
			if (temp.getState() != null) {
				state = temp.getState();
			}
			String stateStr = "启用";
			if (state == 1) {
				stateStr = "停用";
			}
			column.add(new ClientServiceCodeCellDTO("状态", "state", stateStr));
			columns.add(column);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(columns));
	}

	/**
	 * 查看子账户详情 查询绑定的短信服务号、平台代码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/info/getBindServiceCode")
	public void getBindServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		if (enterpriseId == 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		String type = userService.findenterpriseTypebyUserId(userId);
		BindListInfoDTO dto = new BindListInfoDTO();
		dto.setType(type);
		dto.setCode(CommonConstants.WEB_BUSINESS_CODE);
		List<FmsServiceCode> dtoList = fmsUserServiceCodeAssignService.findByAssignUserId(userId);
		List<ServiceCodeDTO> bindServiceCodeList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : dtoList) {
			ServiceCodeDTO dto2 = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			bindServiceCodeList.add(dto2);
		}
		// List<ServiceCodeDTO> bindServiceCodeList = imsServiceCodeService.findUserServiceCodeBindClinet(userId);
		dto.setBindingList(bindServiceCodeList);

		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	/**
	 * 创建子账号页面--查询短信服务号、平台代码
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_CREAT_ACCOUNT")
	@RequestMapping("/subaccount/serviceCode")
	public void findSmsServiceCodeAndPlatformCode(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);// 企业id
		Enterprise enterprise = enterpriseService.findById(id);
		if (enterprise == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("数据不存在"));
			return;
		}
		ServiceCodeAndPlatformDTO dto = new ServiceCodeAndPlatformDTO();
		// 服务号（非平台下的服务号）
		List<FmsServiceCode> dtoList = fmsServiceCodeService.findByEnterId(id, FmsServiceCode.STATE_ENABLE);
		List<ServiceCodeDTO> bindServiceCodeList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : dtoList) {
			ServiceCodeDTO dto2 = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getAppId());
			bindServiceCodeList.add(dto2);
		}

		// List<ServiceCodeDTO> smsServiceCodeList = smsServiceCodeService.findByEnterpriseIdAndNotPlatform(id);
		dto.setServiceCodeList(bindServiceCodeList);
		dto.setType("0");
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	/**
	 * 创建子账号--绑定短信服务号、平台代码
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_CREAT_ACCOUNT")
	@RequestMapping("/subaccount/assignServiceCodeDo")
	public void assignSmsServiceCodeDo(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);// 子账号id
		Long enterpriseId = RequestUtils.getLongParameter(request, "enterpriseId", 0L);// 企业id
		String serviceCodeIds = RequestUtils.getParameter(request, "serviceCodeIds");
		if (userId == null || enterpriseId == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		// 校验子账号是否属于该企业
		Long enterId = userService.findenterpriseIdbyUser(userId);
		if (enterId == null || enterId.longValue() != enterpriseId.longValue()) {
			ResponseUtils.outputWithJson(response, Result.badResult("数据不正确"));
			return;
		}
		Result result = fmsUserServiceCodeAssignService.addFmsUserSCAssign(userId, enterpriseId, serviceCodeIds);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	/**
	 * 修改子账号页面--短信服务号、平台代码
	 *
	 * @param request
	 * @param response
	 *
	 */
	@SuppressWarnings("unchecked")
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping("/subaccount/edit/selectServiceCode")
	public void selectSmsServiceCodeAndPlatformCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		if (enterpriseId == 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		ServiceCodeAndPlatformMapDTO result = new ServiceCodeAndPlatformMapDTO();

		Map<String, Object> fmsUserSCAssignMap = fmsUserServiceCodeAssignService.findBinding(userId, enterpriseId);
		List<FmsServiceCode> notList = (List<FmsServiceCode>) fmsUserSCAssignMap.get("notBoundList");
		List<ServiceCodeDTO> notBoundList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : notList) {
			ServiceCodeDTO dto = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			notBoundList.add(dto);
		}
		List<FmsServiceCode> binList = (List<FmsServiceCode>) fmsUserSCAssignMap.get("bindingList");
		List<ServiceCodeDTO> bindingList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : binList) {
			ServiceCodeDTO dto = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			bindingList.add(dto);
		}
		List<FmsServiceCode> allList = (List<FmsServiceCode>) fmsUserSCAssignMap.get("allEffectiveList");
		List<ServiceCodeDTO> allEffectiveList = new ArrayList<ServiceCodeDTO>();
		for (FmsServiceCode imsServiceCode : allList) {
			ServiceCodeDTO dto = new ServiceCodeDTO(imsServiceCode.getId(), imsServiceCode.getServiceCode());
			allEffectiveList.add(dto);
		}
		ServiceCodeMapDTO mapDTO = new ServiceCodeMapDTO();
		mapDTO.setNotBoundList(notBoundList);
		mapDTO.setBindingList(bindingList);
		mapDTO.setAllEffectiveList(allEffectiveList);
		// ServiceCodeMapDTO userServiceCodeAssignMap = smsUserServiceCodeAssignService.findBindingForBaseSupport(userId, enterpriseId);
		result.setUserServiceCodeAssignMap(mapDTO);
		String type = userService.findenterpriseTypebyUserId(userId);
		result.setType(type);
		ResponseUtils.outputWithJson(response, Result.rightResult(result));
	}

	/**
	 * 修改子账号--修改关联的短信服务号、平台代码
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping("/subaccount/edit/modifyServiceCodeDo")
	public void modifySmsServiceCodeDo(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0L);
		String serviceCodeIds = RequestUtils.getParameter(request, "serviceCodeIds");
		Boolean isClientUser = userService.isClientUser(userId);
		if (!isClientUser) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Boolean isMangerAccount = userService.isMangerAccount(userId);
		if (isMangerAccount) {
			ResponseUtils.outputWithJson(response, Result.badResult("管理账号不能修改"));
			return;
		}
		// 平台
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		Enterprise enterprise = enterpriseService.findById(enterpriseId);
		if (null == enterprise) {
			ResponseUtils.outputWithJson(response, Result.badResult("客户信息不存在"));
			return;
		}
		// 服务号
		// Result result = smsServiceCodeService.modifyBindClient(serviceCodeIds, userId, enterpriseId);

		Result result = fmsUserServiceCodeAssignService.modifyfmsUserSCAssign(userId, enterpriseId, serviceCodeIds);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

}
