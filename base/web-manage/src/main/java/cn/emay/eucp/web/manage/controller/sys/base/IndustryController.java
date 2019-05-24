package cn.emay.eucp.web.manage.controller.sys.base;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Industry;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.IndustryService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 行业维护
 *
 */

@PageAuth("SYS_BASE_INDUSTRY")
@RequestMapping("/sys/base/industry")
@Controller
public class IndustryController {

	private Logger log = Logger.getLogger(IndustryController.class);

	@Resource
	private IndustryService industryService;
	@Resource
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/sys/base/industry";
	}

	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String industry = RequestUtils.getParameter(request, "industry");// 行业
		Page<Industry> page = industryService.findPage(industry, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
	
	@OperationAuth("OPER_SYS_BASE_INDUSTRY_ADD")
	@RequestMapping("/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String industry = RequestUtils.getParameter(request, "industry");// 行业
		Result result = this.check(industry, null);
		if(!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		result = industryService.addIndustry(industry);
		if(result.getSuccess()) {
			User currentUser = WebUtils.getCurrentUser(request, response);
			String service = "公共服务";
			String module = "行业维护";
			String context = "添加行业：{0}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { industry}), ManageUserOperLog.OPERATE_ADD);
			log.info("用户:"+currentUser.getUsername()+"添加行业:"+industry);
		}
		ResponseUtils.outputWithJson(response, result);
	}
	
	@OperationAuth("OPER_SYS_BASE_INDUSTRY_MODIFY")
	@RequestMapping("/ajax/modify")
	public void modify(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String industry = RequestUtils.getParameter(request, "industry");// 行业
		if(id.longValue() <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Result result = this.check(industry, id);
		if(!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		result = industryService.modifyIndustry(id,industry);
		if(result.getSuccess()) {
			User currentUser = WebUtils.getCurrentUser(request, response);
			String service = "公共服务";
			String module = "行业维护";
			String context = "修改行业：{0}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { industry}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:"+currentUser.getUsername()+"修改行业:"+industry);
		}
		ResponseUtils.outputWithJson(response, result);
	}
	
//	@OperationAuth("OPER_SYS_BASE_INDUSTRY_DELETE")
//	@RequestMapping("/ajax/delete")
//	public void delete(HttpServletRequest request, HttpServletResponse response) {
//		Long id = RequestUtils.getLongParameter(request, "id", 0l);
//		if(id.longValue() <= 0l) {
//			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
//			return;
//		}
//		Result result = null;
////		if(!result.getSuccess()) {
////			ResponseUtils.outputWithJson(response, result);
////			return;
////		}
//		result = industryService.deleteIndustry(id);
//		if(result.getSuccess()) {
//			User currentUser = WebUtils.getCurrentUser(request, response);
//			String service = "公共服务";
//			String module = "行业维护";
//			String context = "删除行业：{0}";
//			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult()}), ManageUserOperLog.OPERATE_DELETE);
//			log.info("用户:"+currentUser.getUsername()+"删除行业:"+result.getResult());
//		}
//		ResponseUtils.outputWithJson(response, result);
//	}
	
	private Result check(String industry,Long id) {
		if(StringUtils.isEmpty(industry)) {
			return Result.badResult("行业不能为空");
		}
		if(industry.length() > 30) {
			return Result.badResult("行业不能超过30个字符");
		}
		
		Boolean isExist = industryService.isExist(industry, id);
		if(isExist) {
			return Result.badResult("该行业分类已存在");
		}
		return Result.rightResult();
	}
	
	@RequestMapping("/ajax/info")
	public void info(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		if(id.longValue() <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Industry entity = industryService.findById(id);
		ResponseUtils.outputWithJson(response, Result.rightResult(entity));
	}
	
	

}
