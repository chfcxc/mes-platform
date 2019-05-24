package cn.emay.eucp.web.manage.controller.global.fms.servicecode;

import java.util.ArrayList;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.FmsOperater;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.channel.FmsChannelBusinessDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeChannelDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeInfoDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.GeneratorSerciceCodeDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.SimpleFmsServiceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsChannelService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeChannelService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_SERVICECODE_MANAGE")
@RequestMapping("/fms/servicecode/manage")
@Controller
public class FmsServiceCodeController {

	@Resource
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource
	private EnterpriseService enterpriseService;
	@Resource
	private UserService userService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;
	@Resource
	private FmsServiceCodeChannelService fmsServiceCodeChannelService;
	@Resource
	private FmsChannelService fmsChannelService;
	@Resource
	private RedisClient redis;
	private static final String SERVICETYPE = "7";

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
		return "fms/servicecode/list";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		String appId = RequestUtils.getParameter(request, "appId");
		String serviceCode = RequestUtils.getParameter(request, "serviceCode");
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		String enterpriseName = RequestUtils.getParameter(request, "enterpriseName");
		String enterpriseNumber = RequestUtils.getParameter(request, "enterpriseNumber");
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		int state = RequestUtils.getIntParameter(request, "state", -1);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 0);

		List<Enterprise> enterpriseList = null;
		if (!StringUtils.isEmpty(enterpriseName) || !StringUtils.isEmpty(enterpriseNumber)) {
			enterpriseList = enterpriseService.findListByNameAndClientNumber(enterpriseName, enterpriseNumber);
			if (enterpriseList == null || enterpriseList.size() == 0) {
				ResponseUtils.outputWithJson(response, Result.badResult("暂无数据"));
				return;
			}
		}
		Map<Long, Enterprise> enterpriseMap = new HashMap<Long, Enterprise>();
		if (enterpriseList != null && enterpriseList.size() > 0) {
			for (Enterprise enterprise : enterpriseList) {
				enterpriseMap.put(enterprise.getId(), enterprise);
			}
		}
		Set<Long> keySet = null;
		if (!enterpriseMap.isEmpty()) {
			keySet = enterpriseMap.keySet();
		}
		Page<FmsServiceCodeDto> page = fmsServiceCodeService.findList(appId, serviceCode, start, limit, keySet, serviceCodeId, state, businessTypeId);
		Set<Long> enterpriseIdList = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsServiceCodeDto dto : page.getList()) {
				enterpriseIdList.add(dto.getEnterpriseId());
			}
		}
		List<Enterprise> findByIds = null;
		if (null != enterpriseIdList && enterpriseIdList.size() > 0) {
			findByIds = enterpriseService.findByIds(enterpriseIdList);
		}
		Map<Long, Enterprise> map = new HashMap<Long, Enterprise>();
		if (null != findByIds) {
			for (Enterprise enterprise : findByIds) {
				map.put(enterprise.getId(), enterprise);
			}
		}
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsServiceCodeDto dto : page.getList()) {
				Enterprise enterprise = map.get(dto.getEnterpriseId());
				dto.setEnterpriseName(enterprise.getNameCn());
				dto.setEnterpriseNumber(enterprise.getClientNumber());
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));

	}

	/**
	 * 客户列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/listclient")
	public void listClient(HttpServletRequest request, HttpServletResponse response) {
		String clientName = RequestUtils.getParameter(request, "clientName");
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<Map<String, Object>> page = enterpriseService.findPageByServiceType(start, limit, clientName, FmsServiceCodeController.SERVICETYPE);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	@RequestMapping("/to/create")
	public String create(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
		Map<String, List<FmsBusinesTypeDto>> map = new HashMap<String, List<FmsBusinesTypeDto>>();
		for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
			if (map.isEmpty()) {
				List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
				list2.add(fmsBusinesTypeDto);
				map.put(fmsBusinesTypeDto.getParentName(), list2);
			} else {
				if (!map.containsKey(fmsBusinesTypeDto.getParentName())) {
					List<FmsBusinesTypeDto> list1 = new ArrayList<FmsBusinesTypeDto>();
					list1.add(fmsBusinesTypeDto);
					map.put(fmsBusinesTypeDto.getParentName(), list1);
				} else {
					String parentName = fmsBusinesTypeDto.getParentName();
					List<FmsBusinesTypeDto> list3 = map.get(parentName);
					list3.add(fmsBusinesTypeDto);
					map.put(parentName, list3);
				}

			}
		}
		model.addAttribute("map", map);
		return "fms/servicecode/create";
	}

	@RequestMapping("/ajax/save")
	public void addService(HttpServletRequest request, HttpServletResponse response) {
		String serviceCode = RequestUtils.getParameter(request, "serviceCode", "");
		String agentAbbr = RequestUtils.getParameter(request, "agentAbbr", "EMY");
		String snType = RequestUtils.getParameter(request, "snType", "EUCP");
		Boolean isTest = RequestUtils.getBooleanParameter(request, "isTest", true);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		Long enterpriseId = RequestUtils.getLongParameter(request, "enterpriseId", 0L);
		String remark = RequestUtils.getParameter(request, "remark");
		Result result = this.check(serviceCode, enterpriseId);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		Long userId = (Long) result.getResult();// 客户管理账号id
		SimpleFmsServiceCodeDTO scDTO = new SimpleFmsServiceCodeDTO(serviceCode, enterpriseId, agentAbbr);
		scDTO.setSnType(snType);
		scDTO.setEnterpriseId(enterpriseId);
		scDTO.setUserId(userId);
		scDTO.setRemark(remark);
		scDTO.setBusinessTypeId(businessTypeId);
		if (isTest) {// 测试账号
			scDTO.setVersion("FMS0");
		} else {// 正式账号
			scDTO.setVersion("FMS1");
		}
		result = fmsServiceCodeService.saveCreateSericeCodeNew(scDTO);
		GeneratorSerciceCodeDTO dto = (GeneratorSerciceCodeDTO) result.getResult();
		redis.hset(RedisConstants.FMS_SERVICE_CODE_BALANCE_HASH, dto.getSn(), 0L, -1);
		ResponseUtils.outputWithJson(response, result);
	}

	private Result check(String serviceCode, Long enterpriseId) {
		if (StringUtils.isEmpty(serviceCode)) {
			return Result.badResult("服务号不能为空");
		}
		if (enterpriseId <= 0L) {
			return Result.badResult("客户信息不正确");
		}
		Boolean isExist = enterpriseService.isExistByProperty("id", enterpriseId);
		if (!isExist) {
			return Result.badResult("客户信息不存在");
		}
		// 校验企业管理账号，生成特服号绑定企业管理账号
		Object object = userService.findMangerAccount(enterpriseId);
		if (null == object) {
			return Result.badResult("客户管理账号不存在");
		}
		Object[] mangerAccount = (Object[]) object;
		if (null == mangerAccount || mangerAccount.length == 0) {
			return Result.badResult("客户管理账号不存在");
		}
		Long userId = Long.valueOf(mangerAccount[0].toString());// 客户管理账号id
		return Result.rightResult(userId);
	}

	@RequestMapping("/to/rule")
	public String rule(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		Map<String, String> map = fmsServiceCodeChannelService.getListByServiceCodeId(id);
		model.addAttribute("serviceCodeChannel", map);
		FmsServiceCodeInfoDTO codeInfoDTO = fmsServiceCodeService.findbyid(id);
		model.addAttribute("serviceCodeinfo", codeInfoDTO);
		List<FmsChannel> all = fmsChannelService.findAll(-1);
		List<FmsServiceCodeChannelDto> bingChannel = fmsServiceCodeChannelService.findBindingChannel(id);
		model.addAttribute("bingChannel", bingChannel);
		List<FmsChannel> channelListCmcc = new ArrayList<FmsChannel>();
		List<FmsChannel> channelListCucc = new ArrayList<FmsChannel>();
		List<FmsChannel> channelListCtcc = new ArrayList<FmsChannel>();
		List<FmsChannel> personChannelListCmcc = new ArrayList<FmsChannel>();
		List<FmsChannel> personChannelListCucc = new ArrayList<FmsChannel>();
		List<FmsChannel> personChannelListCtcc = new ArrayList<FmsChannel>();
		for (FmsChannel fmsChannel : all) {
			if (fmsChannel.getTemplateType().contains("0")) {
				if (fmsChannel.getProviders().contains(FmsOperater.CMCC.getCode())) {
					channelListCmcc.add(fmsChannel);
				}
				if (fmsChannel.getProviders().contains(FmsOperater.CUCC.getCode())) {
					channelListCucc.add(fmsChannel);
				}
				if (fmsChannel.getProviders().contains(FmsOperater.CTCC.getCode())) {
					channelListCtcc.add(fmsChannel);
				}
			}
			if (fmsChannel.getTemplateType().contains("1")) {
				if (fmsChannel.getProviders().contains(FmsOperater.CMCC.getCode())) {
					personChannelListCmcc.add(fmsChannel);
				}
				if (fmsChannel.getProviders().contains(FmsOperater.CUCC.getCode())) {
					personChannelListCucc.add(fmsChannel);
				}
				if (fmsChannel.getProviders().contains(FmsOperater.CTCC.getCode())) {
					personChannelListCtcc.add(fmsChannel);
				}
			}
		}
		model.addAttribute("cmccAllChannel", channelListCmcc);
		model.addAttribute("cuccAllChannel", channelListCucc);
		model.addAttribute("ctccAllChannel", channelListCtcc);
		model.addAttribute("personChannelListCmcc", personChannelListCmcc);
		model.addAttribute("personChannelListCucc", personChannelListCucc);
		model.addAttribute("personChannelListCtcc", personChannelListCtcc);
		return "fms/servicecode/rule";
	}

	@RequestMapping("ajax/ruleinfo")
	public void ruleinfo(HttpServletRequest request, HttpServletResponse response) {
		Long channelId = RequestUtils.getLongParameter(request, "id", 0L);
		FmsChannel findById = fmsChannelService.findById(channelId);
		FmsChannelBusinessDTO businessDTO = new FmsChannelBusinessDTO();
		if (null != findById) {
			FmsBusinesTypeDto findbyBusiness = fmsBusinessTypeService.findbyBusiness(findById.getBusinessTypeId());
			businessDTO.setContentType(findbyBusiness.getName());
			businessDTO.setBusinessType(findbyBusiness.getParentName());
			businessDTO.setSaveType(findbyBusiness.getSaveType());
			if (findbyBusiness.getSaveType() == 1) {
				businessDTO.setSaveDesc("可保存");
			} else {
				businessDTO.setSaveDesc("不可保存");
			}
			businessDTO.setReportType(findById.getReportType());
			if (findById.getReportType() == 0) {
				businessDTO.setReportTypeDesc("线上报备线上回模板ID");
			} else if (findById.getReportType() == 1) {
				businessDTO.setReportTypeDesc("线上报备线下回模板ID");
			} else if (findById.getReportType() == 2) {
				businessDTO.setReportTypeDesc("线下报备线上回模板ID");
			} else {
				businessDTO.setReportTypeDesc("线下报备线下回模板ID");
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(businessDTO));
	}

	@RequestMapping("ajax/saveInfo")
	public void saveServiceCodeInfo(HttpServletRequest request, HttpServletResponse response) {
		Long serviceCodeId = RequestUtils.getLongParameter(request, "id", 0L);
		Integer isNeedReport = RequestUtils.getIntParameter(request, "isNeedReport", 0);
		String ipConfiguration = RequestUtils.getParameter(request, "ipConfiguration", "");
		String serviceCodeChannel = RequestUtils.getParameter(request, "serviceCodeChannel", "");// 普通
		String psersonServiceCodeChannel = RequestUtils.getParameter(request, "psersonServiceCodeChannel", "");// 个性
		if (serviceCodeId <= 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("服务号不能为空"));
			return;
		}
		Result result = fmsServiceCodeService.updateServiceCode(serviceCodeId, isNeedReport, ipConfiguration, serviceCodeChannel, psersonServiceCodeChannel);
		ResponseUtils.outputWithJson(response, result);

	}

	@RequestMapping("/reset")
	public void resetSecretKey(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		String reason = RequestUtils.getParameter(request, "reason");
		if (id <= 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		if (StringUtils.isEmpty(reason)) {
			ResponseUtils.outputWithJson(response, Result.badResult("原因不能为空"));
			return;
		}
		if (reason.length() > 100) {
			ResponseUtils.outputWithJson(response, Result.badResult("原因不能超过100字符"));
			return;
		}
		Result result = fmsServiceCodeService.updateSecretKey(id);
		ResponseUtils.outputWithJson(response, result);
	}

	@RequestMapping("/ajax/on")
	public void on(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		Result result = fmsServiceCodeService.modifyOn(id);
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 禁用
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/off")
	public void off(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		Result result = fmsServiceCodeService.modifyOff(id);
		ResponseUtils.outputWithJson(response, result);
	}

}
