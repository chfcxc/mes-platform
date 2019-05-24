package cn.emay.eucp.web.manage.controller.sys.base;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.SystemSetting;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.SystemSettingService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 系统配置
 * 
 * @author Frank
 *
 */
@PageAuth("SYS_BASE_INFO")
@RequestMapping("/sys/base/info")
@Controller
public class SystemSettingController {

	private static Logger log = Logger.getLogger(SystemSettingController.class);

	@Resource(name = "systemSettingService")
	private SystemSettingService systemSettingService;
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/sys/base/systeminfo";
	}

	/**
	 * 配置列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		List<SystemSetting> list = systemSettingService.findList();// 系统配置
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 修改系统配置
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_BASE_INFO_UPDATE")
	@RequestMapping("/ajax/modify")
	public void modify(HttpServletRequest request, HttpServletResponse response) {
		// Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String settingKey = RequestUtils.getParameter(request, "settingKey");
		String settingValue = RequestUtils.getParameter(request, "settingValue");
		if (StringUtils.isEmpty(settingValue)) {
			ResponseUtils.outputWithJson(response, Result.badResult("修改信息不能为空"));
			return;
		}
		if (StringUtils.isEmpty(settingKey)) {
			ResponseUtils.outputWithJson(response, Result.badResult("系统配置不存在"));
			return;
		}
		Result result = null;
		String depict = "";
		SystemSetting ssetting = systemSettingService.findSystemSettingByProperty(settingKey);
		if (ssetting == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("系统配置不存在"));
			return;
		}
		if (settingKey.equals("sms_export_file_keep_time")){
			if (!RegularUtils.checkPositiveInteger(settingValue)) {
				ResponseUtils.outputWithJson(response, Result.badResult("请输入正整数"));
				return;
			}
			if (settingValue.length() > 9) {
				ResponseUtils.outputWithJson(response, Result.badResult("修改内容不能超过9位"));
				return;
			}
		}
		if (ssetting.getSettingKey().equals("sms_report_timeout")) {
			if (!RegularUtils.checkPositiveInteger(settingValue)) {
				ResponseUtils.outputWithJson(response, Result.badResult("请输入正整数"));
				return;
			}
			if (settingValue.length() > 9) {
				ResponseUtils.outputWithJson(response, Result.badResult("修改内容不能超过9位"));
				return;
			}
		}
		if (ssetting.getSettingKey().equals("sms_unsubscribe_code")) {// 退订码
			String[] unsubscribeCodeArr = settingValue.split(",", -1);
			Set<String> unsubscribeCodeSet = new HashSet<String>();
			for (String uc : unsubscribeCodeArr) {
				if (!RegularUtils.checkUnsubscribeCode(uc)) {
					ResponseUtils.outputWithJson(response, Result.badResult("退订码为长度最多5位的中英文、数字"));
					return;
				}
				unsubscribeCodeSet.add(uc.toLowerCase());
			}
			if (unsubscribeCodeArr.length != unsubscribeCodeSet.size()) {
				ResponseUtils.outputWithJson(response, Result.badResult("退订码有重复"));
				return;
			}
			if (unsubscribeCodeSet.size() > 10) {
				ResponseUtils.outputWithJson(response, Result.badResult("退订码最多可以设置10个"));
				return;
			}
		}

		ssetting.setSettingValue(settingValue);
		result = systemSettingService.update(ssetting);
		depict = ssetting.getDepict();
		// 修改状态报告超时时间、退订码同时更新system_settings
		/*
		 * if (settingKey.equals("sms_report_timeout") ||
		 * settingKey.equals("sms_unsubscribe_code")) { SmsSystemSetting setting =
		 * smsSystemSettingService.findSystemSettingByProperty(settingKey); if (setting
		 * == null) { ResponseUtils.outputWithJson(response,
		 * Result.badResult("系统配置不存在")); return; }
		 * setting.setSettingValue(settingValue); result =
		 * smsSystemSettingService.update(setting); }
		 */
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "修改系统配置,配置名称为 {0}";
			String module = "系统配置";
			User currentUser = WebUtils.getCurrentUser(request, response);
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { depict }), ManageUserOperLog.OPERATE_MODIFY);

			log.info("用户:" + currentUser.getUsername() + "修改基础数据中的系统配置:" + depict);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 配置详细信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/detail")
	public void detail(HttpServletRequest request, HttpServletResponse response) {
		// Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String settingKey = RequestUtils.getParameter(request, "settingKey");
		if (StringUtils.isEmpty(settingKey)) {
			ResponseUtils.outputWithJson(response, Result.badResult("系统配置不存在"));
			return;
		}
		SystemSetting setting = systemSettingService.findSystemSettingByProperty(settingKey);
		ResponseUtils.outputWithJson(response, Result.rightResult(setting));
	}
}
