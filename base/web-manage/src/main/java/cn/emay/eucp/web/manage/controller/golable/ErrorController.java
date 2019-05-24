package cn.emay.eucp.web.manage.controller.golable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.RequestUtils;

/**
 * 错误控制器<br/>
 * 
 * @author Frank
 *
 */
@Controller
public class ErrorController {

	@RequestMapping("/error")
	public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = RequestUtils.getParameter(request, "msg");
		request.setAttribute("msg", msg);
		return "redirect:" + CommonConstants.PASSPORT_PATH + "/error?system="+CommonConstants.LOCAL_SYSTEM.toLowerCase();
	}

}
