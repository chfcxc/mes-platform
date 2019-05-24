package cn.emay.eucp.web.client.controller.sys;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.FeedbackService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.util.PropertiesUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 问题反馈
 *
 */
@RequestMapping("/sys/client/feedback")
@Controller
public class SysFeedbackController {

	@Resource(name = "feedbackService")
	private FeedbackService feedbackService;

	@RequestMapping("/save")
	public void saveFeedback(HttpServletRequest request, HttpServletResponse response) {
		String content = RequestUtils.getParameter(request, "content");
		String email = RequestUtils.getParameter(request, "email");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String qq = RequestUtils.getParameter(request, "qq");
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result = this.check(content, email, mobile, qq);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		feedbackService.saveFeedback(content, email, mobile, qq, currentUser.getId());
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 校验
	 * 
	 * @param content
	 * @param email
	 * @param mobile
	 * @param qq
	 * @return
	 */
	public Result check(String content, String email, String mobile, String qq) {
		if (StringUtils.isEmpty(content)) {
			return Result.badResult("反馈内容不能为空");
		}
		boolean flag = true;
		if (!StringUtils.isEmpty(email)) {
			flag = RegularUtils.checkEmail(email);
			if (!flag) {
				return Result.badResult("邮箱格式不正确");
			}
		}
		if (!StringUtils.isEmpty(mobile)) {
			flag = RegularUtils.checkMobileFormat(mobile);
			if (!flag) {
				return Result.badResult("手机号格式不正确");
			}
		}
		if (!StringUtils.isEmpty(qq)) {
			flag = RegularUtils.checkQQ(qq);
			if (!flag) {
				return Result.badResult("QQ格式不正确");
			}
		}
		return Result.rightResult();
	}

	/**
	 * 客服咨询
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/customerService")
	public void customerService(HttpServletRequest request, HttpServletResponse response) {
		String qqs = PropertiesUtil.getProperty("customer.service.qq", "eucp.service.properties");
		if (StringUtils.isEmpty(qqs)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请配置客服咨询qq"));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(qqs));
	}

}
