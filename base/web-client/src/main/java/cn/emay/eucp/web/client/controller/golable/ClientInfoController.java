package cn.emay.eucp.web.client.controller.golable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.eucp.web.common.auth.annotation.PageAuth;

/**
 * 客户信息管理
 * 
 * @author Frank
 *
 */
@PageAuth("SYS_CLIENT_INFO")
@RequestMapping("/sys/client/info")
@Controller
public class ClientInfoController {

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/clientinfo";
	}

}
