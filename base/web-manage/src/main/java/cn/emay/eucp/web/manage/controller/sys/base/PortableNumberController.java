package cn.emay.eucp.web.manage.controller.sys.base;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.data.service.system.PortableNumberService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("SYS_BASE_PORTABLENUMBER")
@RequestMapping("/sys/base/portablenumber")
@Controller
public class PortableNumberController {
	
	@Resource
	private PortableNumberService portableNumberService;
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
		return "/sys/base/portablenumber";
		
	}
	@RequestMapping("findall")
	public void findall(HttpServletRequest request, HttpServletResponse response, Model model){
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit =RequestUtils.getIntParameter(request, "limit", 20);
		String mobile=RequestUtils.getParameter(request, "mobile");	
		Page<PortableNumber> page = portableNumberService.findall(start, limit, mobile);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
}

