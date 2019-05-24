package cn.emay.eucp.web.common.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

public class CustomHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

	private final Logger log = Logger.getLogger(CustomHandlerExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex != null) {
			log.error(request.getRequestURI(), ex);
		}
		return super.doResolveException(request, response, handler, ex);
	}

}