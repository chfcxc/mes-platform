package cn.emay.eucp.web.client.controller.sys;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.DateUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;
/**
 * 日志
 * @author yimei
 *
 */
@PageAuth("SYS_CLIENT_LOG")
@RequestMapping("/sys/client/log")
@Controller
public class SysClientLogController {
	@Resource
	private ClientUserOperLogService clientUserOperLogService;
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
		return "/sys/log/log";
		
	}
	@RequestMapping("findall")
	public void  findall(HttpServletRequest request, HttpServletResponse response, Model model){
		Date date=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -90);
		String day = DateUtil.toString(cal.getTime(), "yyyy-MM-dd") + " 00:00:00";
		Date time = DateUtil.parseDate(day, "yyyy-MM-dd HH:mm:ss");
		int start =RequestUtils.getIntParameter(request, "start", 1);
		int limit= RequestUtils.getIntParameter(request, "limit", 20);
		String username=RequestUtils.getParameter(request, "username");
		String content=RequestUtils.getParameter(request, "content");
		Date startDate=RequestUtils.getDateParameter(request, "startDate", "yyyy-MM-dd HH:mm:ss",time);
		Date endDate=RequestUtils.getDateParameter(request, "endDate", "yyyy-MM-dd HH:mm:ss", date);
		User user = WebUtils.getCurrentUser(request, response);
		if(startDate.getTime()<time.getTime()){
			ResponseUtils.outputWithJson(response, Result.badResult("开始时间范围超过90天，暂不支持查询"));	
		}
		Page<ClientUserOperLog> page = clientUserOperLogService.findall(start, limit, username, content, startDate, endDate,user.getId());
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
}
