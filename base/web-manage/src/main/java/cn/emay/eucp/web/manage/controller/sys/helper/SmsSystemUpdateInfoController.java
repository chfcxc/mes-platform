package cn.emay.eucp.web.manage.controller.sys.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.UpdateInfoService;
import cn.emay.eucp.util.RegularCheckUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.util.SpecialSymbolUtil;
import cn.emay.util.DateUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 *
 * 系统更新日志
 * 
 * @author gonghui
 *
 */
@PageAuth("SYS_UPDATE_INFO")
@RequestMapping("/sys/system/updateInfo")
@Controller
public class SmsSystemUpdateInfoController {

	private static Logger log = Logger.getLogger(SmsSystemUpdateInfoController.class);

	@Resource(name = "updateInfoService")
	UpdateInfoService updateInfoService;

	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/system/updateInfo";
	}

	/**
	 * 获取更新日志信息列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/listNoPage")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		String versionInfo = RequestUtils.getParameter(request, "version");// 版本
		String updateInfo = RequestUtils.getParameter(request, "updateInfo");// 版本
		if (!"".equals(updateInfo) && null != updateInfo) {
			updateInfo = SpecialSymbolUtil.getNormalString(updateInfo).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}
		List<UpdateInfo> list = updateInfoService.selectInfoByCondition(versionInfo, RegularCheckUtils.specialCodeEscape(updateInfo));
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 获取更新日志信息列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void listPage(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String versionInfo = RequestUtils.getParameter(request, "version");// 版本
		String updateInfo = RequestUtils.getParameter(request, "updateInfo");// 版本
		if (!"".equals(updateInfo) && null != updateInfo) {
			updateInfo = SpecialSymbolUtil.getNormalString(updateInfo).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}
		Page<UpdateInfo> page = updateInfoService.selectUpdateInfo(versionInfo, updateInfo, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 获取版本号列表
	 */
	@RequestMapping("/ajax/versionList")
	public void versionList(HttpServletRequest request, HttpServletResponse response) {
		List<String> list = updateInfoService.findVersionList();
		List<String> versionList = new ArrayList<String>();
		for (String str : list) {
			if (!versionList.contains(str)) {
				versionList.add(str);
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(versionList));
	}

	/**
	 * 添加版本更新信息
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_UPDATE_INFO_ADD")
	@RequestMapping("/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String versionInfo = RequestUtils.getParameter(request, "version");
		String pubTime = RequestUtils.getParameter(request, "pubTime");
		Integer subSystem = RequestUtils.getIntParameter(request, "subSystem", 0);
		Integer businessType = RequestUtils.getIntParameter(request, "businessType", 0);
		String updateInfo = RequestUtils.getParameter(request, "updateInfo");

		if (StringUtils.isEmpty(versionInfo) || StringUtils.isEmpty(pubTime) || StringUtils.isEmpty(updateInfo) || subSystem == 0 || businessType == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("字段不能为空"));
			return;
		}

		if (!"".equals(updateInfo) && null != updateInfo) {
			updateInfo = SpecialSymbolUtil.getNormalString(updateInfo).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}

		Date pubDate = DateUtil.parseDate(pubTime, "yyyy-MM-dd");
		updateInfoService.saveUpdateInfo(versionInfo, pubDate, subSystem, businessType, updateInfo);

		String service = "公共服务";
		String module = "系统更新";
		String context = "增加系统更新:版本号为 {0},所属系统为{1},业务类型为{2}";
		String businessTypeInfo = "";
		if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_SMS) {
			businessTypeInfo = "短信";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_FLOW) {
			businessTypeInfo = "流量";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_MMS) {
			businessTypeInfo = "彩信";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_IMS) {
			businessTypeInfo = "国际短信";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_VOICE) {
			businessTypeInfo = "语音";
		}
		String subSysteminfo = "";
		if (subSystem.intValue() == UpdateInfo.STATE_SUB_SYSTEM_MANNGER) {
			subSysteminfo = "管理端";
		} else if (subSystem.intValue() == UpdateInfo.STATE_SUB_SYSTEM_SALE) {
			subSysteminfo = "销售端";
		} else if (subSystem.intValue() == UpdateInfo.STATE_SUB_SYSTEM_CLIENT) {
			subSysteminfo = "客户端";
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { versionInfo, subSysteminfo, businessTypeInfo }), ManageUserOperLog.OPERATE_ADD);
		log.info("用户:" + currentUser.getUsername() + "增加更新记录,版本号为:" + (versionInfo));
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	/**
	 * 系统更新详情
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/info")
	public void info(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		UpdateInfo dto = updateInfoService.findById(id);
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_UPDATE_INFO_UPDATE")
	@RequestMapping("/ajax/update")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String versionInfo = RequestUtils.getParameter(request, "version");
		String pubTime = RequestUtils.getParameter(request, "pubTime");
		Integer subSystem = RequestUtils.getIntParameter(request, "subSystem", 0);
		Integer businessType = RequestUtils.getIntParameter(request, "businessType", 0);
		String updateInfo = RequestUtils.getParameter(request, "updateInfo");

		if (id <= 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("系统更新记录不存在"));
			return;
		}
		UpdateInfo info = updateInfoService.findById(id);
		if (null == info) {
			ResponseUtils.outputWithJson(response, Result.badResult("系统更新记录不存在"));
			return;
		}

		if (StringUtils.isEmpty(versionInfo) || StringUtils.isEmpty(pubTime) || StringUtils.isEmpty(updateInfo) || subSystem == 0 || businessType == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("字段不能为空"));
			return;
		}

		if (!"".equals(updateInfo) && null != updateInfo) {
			updateInfo = SpecialSymbolUtil.getNormalString(updateInfo).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}

		info.setBusniessType(businessType);
		info.setCreateTime(new Date());
		info.setPubTime(pubTime);
		info.setSubSystem(subSystem);
		info.setUpdateInfo(updateInfo);
		info.setVersionInfo(versionInfo);

		updateInfoService.updateSystemInfo(info);

		User user = WebUtils.getCurrentUser(request, response);
		String service = "公共服务";
		String module = "系统更新";
		String context = "修改系统更新:版本号为 {0},所属系统为{1},业务类型为{2}";
		String businessTypeInfo = "";
		if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_SMS) {
			businessTypeInfo = "短信";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_FLOW) {
			businessTypeInfo = "流量";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_MMS) {
			businessTypeInfo = "彩信";
		} else if (businessType.intValue() == UpdateInfo.BUSINESS_TYPE_IMS) {
			businessTypeInfo = "国际短信";
		}
		String subSysteminfo = "";
		if (subSystem.intValue() == UpdateInfo.STATE_SUB_SYSTEM_MANNGER) {
			subSysteminfo = "管理端";
		} else if (subSystem.intValue() == UpdateInfo.STATE_SUB_SYSTEM_SALE) {
			subSysteminfo = "销售端";
		} else if (subSystem.intValue() == UpdateInfo.STATE_SUB_SYSTEM_CLIENT) {
			subSysteminfo = "客户端";
		}
		manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, new Object[] { versionInfo, subSysteminfo, businessTypeInfo }), ManageUserOperLog.OPERATE_ADD);
		log.info("用户:" + user.getUsername() + "修改更新记录,版本号为:" + (versionInfo));
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

}
