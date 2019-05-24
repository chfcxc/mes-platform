package cn.emay.eucp.web.manage.controller.global;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.web.common.WebUtils;

/**
 * 首页
 * 
 * @author Frank
 *
 */
@Controller
public class IndexController {

	@RequestMapping("/")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		Resource newMoudlList = (Resource) request.getAttribute("THIS_MOUDLE");
		return "redirect:" + WebUtils.getLocalAddress(request) + newMoudlList.getResourcePath();
	}

}
