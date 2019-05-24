package cn.emay.eucp.web.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.constant.CommonConstants;

public class CommonConstantsInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("WEB_SERVER_PATH", WebUtils.getLocalAddress(request));
		request.setAttribute("WEB_STATIC_PATH", CommonConstants.WEB_STATIC_PATH);
		request.setAttribute("PASSPORT_PATH", CommonConstants.PASSPORT_PATH);
		request.setAttribute("LOCAL_SYSTEM", CommonConstants.LOCAL_SYSTEM);
		request.setAttribute("DOMAIN_PATH", WebUtils.getLocalDomainWithPort(request));
		request.setAttribute("WEB_BUSINESS_CODE", CommonConstants.WEB_BUSINESS_CODE);
		User user = WebUtils.getCurrentUser(request, response);
		if (user != null) {
			request.setAttribute("CURRENT_USER_ID", user.getId());
			request.setAttribute("CURRENT_USER_USERNAME", user.getUsername());
			request.setAttribute("CURRENT_USER_REALNAME", user.getRealname());
		} else {
			request.setAttribute("CURRENT_USER_ID", "");
			request.setAttribute("CURRENT_USER_USERNAME", "");
			request.setAttribute("CURRENT_USER_REALNAME", "");
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

}
