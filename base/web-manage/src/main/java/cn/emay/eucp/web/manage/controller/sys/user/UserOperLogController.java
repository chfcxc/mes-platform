package cn.emay.eucp.web.manage.controller.sys.user;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 用户日志
 * 
 * @author Frank
 *
 */
@PageAuth("SYS_USER_LOG")
@RequestMapping("/sys/user/log")
@Controller
public class UserOperLogController {

	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/sys/user/log/log";
	}

	/**
	 * 日志列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String username = RequestUtils.getParameter(request, "username");// 用户名
		String content = RequestUtils.getParameter(request, "content");// 内容
		Date startDate=RequestUtils.getDateParameter(request, "startDate", "yyyy-MM-dd HH:mm:ss",null);
		Date endDate=RequestUtils.getDateParameter(request, "endDate", "yyyy-MM-dd HH:mm:ss",null);
		Page<ManageUserOperLog> page = manageUserOperLogService.findByPage(username, content, startDate, endDate, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
}
