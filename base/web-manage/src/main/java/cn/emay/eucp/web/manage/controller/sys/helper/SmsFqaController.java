package cn.emay.eucp.web.manage.controller.sys.helper;

import java.text.MessageFormat;
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
import cn.emay.eucp.common.moudle.db.system.Foa;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.FoaService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.util.SpecialSymbolUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 *
 * FQA
 * 
 * @author gonghui
 *
 */
@PageAuth("SYS_FQA")
@RequestMapping("/sys/system/fqa")
@Controller
public class SmsFqaController {

	private static Logger log = Logger.getLogger(SmsFqaController.class);

	@Resource(name = "foaService")
	FoaService foaService;

	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/system/foa";
	}

	/**
	 * 获取foa列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/listNoPage")
	public void listNoPage(HttpServletRequest request, HttpServletResponse response) {
		String descProblem = RequestUtils.getParameter(request, "descProblem");// 问题描述
		List<Foa> list = foaService.findListByDescProblem(descProblem);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 获取foa列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String descProblem = RequestUtils.getParameter(request, "descProblem");// 问题描述
		String info = SpecialSymbolUtil.getNormalString(descProblem);
		if (!"".equals(info) && null != info) {
			info = info.replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}
		Page<Foa> page = foaService.selectFoa(info, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 添加foa
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_FQA_ADD")
	@RequestMapping("/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		Integer subSystem = RequestUtils.getIntParameter(request, "subSystem", 0);
		Integer businessType = RequestUtils.getIntParameter(request, "businessType", 0);
		String descProblem = RequestUtils.getParameter(request, "descProblem");
		String reply = RequestUtils.getParameter(request, "reply");

		if (StringUtils.isEmpty(descProblem) || StringUtils.isEmpty(reply) || subSystem == 0 || businessType == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("字段不能为空"));
			return;
		}

		if (!"".equals(descProblem) && null != descProblem) {
			descProblem = SpecialSymbolUtil.getNormalString(descProblem).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}

		if (!"".equals(reply) && null != reply) {
			reply = SpecialSymbolUtil.getNormalString(reply).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}

		foaService.saveFoa(subSystem, businessType, descProblem, reply);
		String service = "公共服务";
		String context = "增加FQA:所属系统为{0},业务类型为{1},问题描述为{2}";
		String module = "FQA";
		String businessTypeInfo = "";
		if (businessType.intValue() == Foa.BUSINESS_TYPE_SMS) {
			businessTypeInfo = "短信";
		} else if (businessType.intValue() == Foa.BUSINESS_TYPE_FLOW) {
			businessTypeInfo = "流量";
		} else if (businessType == Foa.BUSINESS_TYPE_MMS) {
			businessTypeInfo = "彩信";
		} else if (businessType == Foa.BUSINESS_TYPE_IMS) {
			businessTypeInfo = "国际短信";
		}else if (businessType == Foa.BUSINESS_TYPE_VOICE) {
			businessTypeInfo = "语音";
		}

		String subSysteminfo = "";
		if (subSystem.intValue() == Foa.STATE_SUB_SYSTEM_MANNGER) {
			subSysteminfo = "管理端";
		} else if (subSystem.intValue() == Foa.STATE_SUB_SYSTEM_SALE) {
			subSysteminfo = "销售端";
		} else if (subSystem.intValue() == Foa.STATE_SUB_SYSTEM_CLIENT) {
			subSysteminfo = "客户端";
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { subSysteminfo, businessTypeInfo, descProblem }), ManageUserOperLog.OPERATE_ADD);
		log.info("用户:" + currentUser.getUsername() + "增加FQA记录");
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
		Foa dto = foaService.findById(id);
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_FQA_UPDATE")
	@RequestMapping("/ajax/update")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		Integer subSystem = RequestUtils.getIntParameter(request, "subSystem", 0);
		Integer businessType = RequestUtils.getIntParameter(request, "businessType", 0);
		String descProblem = RequestUtils.getParameter(request, "descProblem");
		String reply = RequestUtils.getParameter(request, "reply");

		if (id <= 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("FQA记录不存在"));
			return;
		}
		Foa foa = foaService.findById(id);
		if (null == foa) {
			ResponseUtils.outputWithJson(response, Result.badResult("FQA记录不存在"));
			return;
		}

		if (StringUtils.isEmpty(descProblem) || StringUtils.isEmpty(reply) || subSystem == 0 || businessType == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("字段不能为空"));
			return;
		}

		if (!"".equals(descProblem) && null != descProblem) {
			descProblem = SpecialSymbolUtil.getNormalString(descProblem).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}

		if (!"".equals(reply) && null != reply) {
			reply = SpecialSymbolUtil.getNormalString(reply).replaceAll("&nbsp;", " ").replaceAll("<br/>", "\r");
		}

		foaService.updateFoa(id, subSystem, businessType, descProblem, reply);

		User user = WebUtils.getCurrentUser(request, response);
		String service = "公共服务";
		String module = "FQA";
		String context = "修改FQA:所属系统为{0},业务类型为{1},问题描述为{2}";
		String businessTypeInfo = "";
		if (businessType.intValue() == 1) {
			businessTypeInfo = "短信";
		} else if (businessType.intValue() == 3) {
			businessTypeInfo = "流量";
		}else if (businessType.intValue() == 4) {
			businessTypeInfo = "国际短信";
		}else if (businessType.intValue() == 5) {
			businessTypeInfo = "语音";
		} else {
			businessTypeInfo = "彩信";
		}
		String subSysteminfo = "";
		if (subSystem.intValue() == 1) {
			subSysteminfo = "管理端";
		} else if (subSystem.intValue() == 2) {
			subSysteminfo = "销售端";
		} else {
			subSysteminfo = "客户端";
		}
		manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, new Object[] { subSysteminfo, businessTypeInfo, descProblem }), ManageUserOperLog.OPERATE_ADD);
		log.info("用户:" + user.getUsername() + "修改FQA");
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

}