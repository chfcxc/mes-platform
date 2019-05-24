package cn.emay.eucp.sale.web.controller.golable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.constant.CommonConstants;

/**
 * 首页
 * 
 * @author Frank
 *
 */
@Controller
public class IndexController {
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!WebUtils.isLogin(request, response)) {
			return "redirect:" + CommonConstants.PASSPORT_PATH + "/toLogin";
		}
		return "golable/index";
	}

	@RequestMapping("/")
	public String indexline(HttpServletRequest request, HttpServletResponse response, Model model) {
		return index(request, response, model);
	}

}
