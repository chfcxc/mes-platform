package cn.emay.eucp.web.manage.controller.sys.client;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.GlobalConstants;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 客户用户管理
 * 
 * @author Frank
 *
 */
@PageAuth("SYS_CLIENT_USER")
@RequestMapping("/sys/client/user")
@Controller
public class ClientUserController {

	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/client/user";
	}

	/**
	 * 客户用户
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		String userName = RequestUtils.getParameter(request, "userName");
		String realName = RequestUtils.getParameter(request, "realName");
		String mobile = RequestUtils.getParameter(request, "mobile");
		Integer accountType = RequestUtils.getIntParameter(request, "accountType", 0);
		Integer state = RequestUtils.getIntParameter(request, "state", 0);
		String clientName = RequestUtils.getParameter(request, "clientName");
		String clientNumber = RequestUtils.getParameter(request, "clientNumber");
		int start = RequestUtils.getIntParameter(request, "start", 1);
		int limit = RequestUtils.getIntParameter(request, "limit", GlobalConstants.DEFAULT_PAGE_LIMIT);
		Page<UserDTO> userpage = userService.findUserPage(userName, realName, mobile, accountType, state, User.USER_TYPE_CLIENT,clientName, clientNumber,start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(userpage));
	}
	/**
	 * 
	 * 查看详情跳转页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/ajax/to/detail")
	public String todetail(HttpServletRequest request, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes){
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		model.addAttribute("id", id);
		Boolean isClientUser = userService.isClientUser(id);
		if(!isClientUser){
			redirectAttributes.addFlashAttribute("msg", "参数错误");
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		return "sys/client/userdetail";
	}
	
	/**
	 * 详情
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/ajax/detail")
	public void detail(HttpServletRequest request, HttpServletResponse response,Model model){
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		if(id<= 0l){
			ResponseUtils.outputWithJson(response, Result.badResult("客户不存在"));
			return;
		}
		UserDTO dto = userService.findClientUserById(id);
		if(null==dto){
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}
	
}
