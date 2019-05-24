package cn.emay.eucp.web.common.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.emay.common.Result;
import cn.emay.common.http.client.EmayHttpClient;
import cn.emay.common.http.common.EmayHttpResultCode;
import cn.emay.common.http.request.EmayHttpRequest;
import cn.emay.common.http.request.impl.EmayHttpRequestKV;
import cn.emay.common.http.response.impl.string.EmayHttpResponseString;
import cn.emay.common.http.response.impl.string.EmayHttpResponseStringPraser;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.auth.Token;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.AuthInterceptorService;
import cn.emay.eucp.data.service.system.PassportService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.common.constant.CommonConstants;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private final Logger log = Logger.getLogger(AuthInterceptor.class);

	@Resource(name = "passportService")
	private PassportService passportService;
	@Resource(name = "authInterceptorService")
	private AuthInterceptorService authInterceptorService;

	private EmayHttpClient http = new EmayHttpClient();

	private long cacheTime = 0;
	private String cacheAuthHtml = null;
	private String cacheSimpleAuthHtml = null;

	/**
	 * 拦截没有权限的请求
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String tokenId = WebUtils.getCurrentTokenId(request);
		String tokenStr = passportService.getCurrentTokenStr(tokenId, true);
		Token token = JsonHelper.fromJson(Token.class, tokenStr);
		Result result = this.authErrorHandle(request, response, tokenId, CommonConstants.PASSPORT_PATH, CommonConstants.LOCAL_SYSTEM, WebUtils.getLocalAddress(request));
		if (!result.getSuccess()) {
			return false;
		}
		request.setAttribute(WebUtils.REQUEST_TOKEN, token);

		// 检测资源是否需要权限
		Method method = ((HandlerMethod) handler).getMethod();
		String[] operationAuthCode = null;// 操作权限code
		OperationAuth oa = method.getAnnotation(OperationAuth.class);
		if (oa != null) {
			operationAuthCode = oa.value();
		}
		String[] pageAuthCode = null;// 页面权限code
		List<String> pageAuthCodeList = new ArrayList<>();
		PageAuth pa = method.getAnnotation(PageAuth.class);
		if (pa != null) {
			String[] pageCode = pa.value();
			pageAuthCodeList.addAll(Arrays.asList(pageCode));
		}
		pa = method.getDeclaringClass().getAnnotation(PageAuth.class);
		if (pa != null) {
			String[] pageCode = pa.value();
			pageAuthCodeList.addAll(Arrays.asList(pageCode));
		}
		pageAuthCode = pageAuthCodeList.toArray(new String[pageAuthCodeList.size()]);

		User user = WebUtils.getCurrentUser(request, response);
		result = this.authHandle(request, response, WebUtils.isAjaxRequest(request), tokenStr, user, pageAuthCode, operationAuthCode);
		if (!result.getSuccess()) {
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (ex != null) {
			log.error(ex.getMessage(), ex);
		}
	}

	private Result authErrorHandle(HttpServletRequest request, HttpServletResponse response, String tokenId, String passportPath, String localSystem, String fromUrl) throws Exception {
		Result result = authInterceptorService.authErrorHandle(tokenId, CommonConstants.PASSPORT_PATH, CommonConstants.LOCAL_SYSTEM, WebUtils.getLocalAddress(request));
		if (!result.getSuccess()) {
			if ("noLogin".equals(result.getCode())) {// 未登录，去登录页面
				WebUtils.sendNoLogin(request, response, CommonConstants.LOCAL_SYSTEM, WebUtils.getLocalAddress(request));
				return Result.badResult();
			} else if ("error".equals(result.getCode())) {// 判断用户状态是否正常
				WebUtils.sendError(request, response, result.getMessage());
				return Result.badResult();
			} else if ("restPassword".equals(result.getCode())) {// 用户被管理员重置密码
				WebUtils.sendRestPassword(request, response, result.getMessage());
				return Result.badResult();
			} else if ("redirect".equals(result.getCode())) {// 第一次登陆，需要修改密码
				response.sendRedirect(result.getMessage());
				return Result.badResult();
			} else if ("noLoginAuth".equals(result.getCode())) {// 没有登录权限
				WebUtils.sendError(request, response, result.getMessage());
				return Result.badResult();
			}
			return Result.badResult();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> requestAttributeMap = (Map<String, Object>) result.getResult();
		for (Entry<String, Object> ram : requestAttributeMap.entrySet()) {
			request.setAttribute(ram.getKey(), ram.getValue());
		}
		return Result.rightResult();
	}

	private Result authHandle(HttpServletRequest request, HttpServletResponse response, Boolean isAjaxRequest, String tokenStr, User user, String[] pageAuthCode, String[] operationAuthCode)
			throws Exception {
		String context = request.getContextPath();
		String uri = request.getRequestURI() + "/";
		Result result = authInterceptorService.authHandle(isAjaxRequest, tokenStr, user, pageAuthCode, operationAuthCode, context, uri, CommonConstants.LOCAL_SYSTEM,
				CommonConstants.WEB_BUSINESS_CODE, WebUtils.getLocalDomainWithPort(request));
		if (!result.getSuccess()) {
			WebUtils.sendError(request, response, result.getMessage());
			return Result.badResult();
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> requestAttributeMap = (Map<String, Object>) result.getResult();
		Result re = httprequest(WebUtils.getLocalDomainWithPort(request), requestAttributeMap);
		if (!re.getSuccess()) {
			WebUtils.sendError(request, response, re.getMessage());
			return Result.badResult();
		}
		for (Entry<String, Object> ram : requestAttributeMap.entrySet()) {
			request.setAttribute(ram.getKey(), ram.getValue());
		}

		return Result.rightResult();
	}

	private Result httprequest(String domainPath, Map<String, Object> requestAttributeMap) {
		long time = System.currentTimeMillis();
		if (time - cacheTime > 60 * 1000) {
			Map<String, String> mapsimple = new HashMap<String, String>();
			mapsimple.put("systemType", CommonConstants.LOCAL_SYSTEM);
			mapsimple.put("businessType", CommonConstants.WEB_BUSINESS_CODE);
			mapsimple.put("isSimple", "true");
			mapsimple.put("domainPath", domainPath);
			Map<String, String> map = new HashMap<String, String>();
			map.put("systemType", CommonConstants.LOCAL_SYSTEM);
			map.put("businessType", CommonConstants.WEB_BUSINESS_CODE);
			map.put("isSimple", "false");
			map.put("domainPath", domainPath);
			String path = CommonConstants.LocalPASSPORT_PATH + "/authTemple";
			EmayHttpRequest<Map<String, String>> requestsimple = new EmayHttpRequestKV(path, "UTF-8", "get", null, null, mapsimple);
			EmayHttpRequest<Map<String, String>> request = new EmayHttpRequestKV(path, "UTF-8", "get", null, null, map);

			EmayHttpResponseString simple = http.service(requestsimple, new EmayHttpResponseStringPraser());
			if (simple == null || simple.getHttpCode() != 200 || !EmayHttpResultCode.SUCCESS.equals(simple.getResultCode())) {
				return Result.badResult("passport auth temple error");
			}
			EmayHttpResponseString full = http.service(request, new EmayHttpResponseStringPraser());
			if (full == null || full.getHttpCode() != 200 || !EmayHttpResultCode.SUCCESS.equals(full.getResultCode())) {
				return Result.badResult("passport auth temple error");
			}
			cacheAuthHtml = full.getResultString();
			cacheSimpleAuthHtml = simple.getResultString();
			cacheTime = time;
		}
		requestAttributeMap.put("fullhtml", cacheAuthHtml);
		requestAttributeMap.put("simplehtml", cacheSimpleAuthHtml);
		return Result.rightResult();
	}
}
