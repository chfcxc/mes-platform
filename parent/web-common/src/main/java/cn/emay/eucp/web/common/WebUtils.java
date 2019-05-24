package cn.emay.eucp.web.common;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import cn.emay.common.Result;
import cn.emay.eucp.common.auth.Token;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.ResponseUtils;

public class WebUtils {

	public final static String COOKIE_TOKEN_ID = "EUCP-NOISSES-UE";

	public final static String X_REQUESTED_WITH = "X-Requested-With";
	public final static String XMLHTTPREQUEST = "XMLHttpRequest";

	public final static String REQUEST_USER = "USER";
	public final static String REQUEST_TOKEN = "TOKEN";
	public final static String REQUEST_ENTERPRISE = "ENTERPRISE";

	private static Logger log = Logger.getLogger(WebUtils.class);

	/**
	 * 获取当前TOKEN
	 */
	public static String getCurrentTokenId(HttpServletRequest request) {
		Cookie tokencookie = findCookie(request, COOKIE_TOKEN_ID);
		if (tokencookie == null) {
			return null;
		}
		return tokencookie.getValue();
	}

	/**
	 * 获取 当前URL
	 */
	public static String getLocalAddress(HttpServletRequest request) {
		String uri = request.getRequestURI() == null ? "" : request.getRequestURI();
		String url = request.getRequestURL() == null ? "" : request.getRequestURL().toString();
		String contextPath = request.getContextPath() == null ? "" : request.getContextPath();
		return url.replace(uri, "") + contextPath;
	}

	/**
	 * 获取 当前域名【带http与port】
	 */
	public static String getLocalDomainWithPort(HttpServletRequest request) {
		String uri = request.getRequestURI() == null ? "" : request.getRequestURI();
		String url = request.getRequestURL() == null ? "" : request.getRequestURL().toString();
		return url.replace(uri, "");
	}

	/**
	 * 获取当前系统的访问的域名
	 */
	public static String getLocalDomain(HttpServletRequest request) {
		String uri = request.getRequestURI() == null ? "" : request.getRequestURI();
		String url = request.getRequestURL() == null ? "" : request.getRequestURL().toString();
		String domain = url.replace(uri, "").replace("https://", "").replace("http://", "").split(":")[0];
		return domain;
	}

	/**
	 * 获取当前系统的访问一級域名
	 */
	public static String getLocalMainDomain(HttpServletRequest request) {
		String uri = request.getRequestURI() == null ? "" : request.getRequestURI();
		String url = request.getRequestURL() == null ? "" : request.getRequestURL().toString();
		String domain = url.replace(uri, "").replace("https://", "").replace("http://", "").split(":")[0];
		String regExp = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(domain);
		if (m.find()) {// IP直接当成域名
			return domain;
		} else {
			String[] nodes = domain.split("\\.");
			if (nodes.length < 2) {
				return null;
			}
			domain = nodes[nodes.length - 2] + "." + nodes[nodes.length - 1];
			return domain;
		}
	}

	/**
	 * 跳转到登陆页面或者提示未登陆信息
	 */
	public static void sendNoLogin(HttpServletRequest request, HttpServletResponse response, String system, String fromUrl) throws IOException {
		if (isAjaxRequest(request)) {
			ResponseUtils.outputWithJson(response, Result.badResult("-222", "您还未登陆，请先登录", system));
		} else {
			response.sendRedirect(CommonConstants.PASSPORT_PATH + "/toLogin?system=" + system + "&fromUrl=" + fromUrl);
		}
	}

	/**
	 * 跳到错误页面或者提示错误信息
	 */
	public static void sendError(HttpServletRequest request, HttpServletResponse response, String AjaxErrorMassage) throws IOException {
		if (isAjaxRequest(request)) {
			ResponseUtils.outputWithJson(response, Result.badResult("-111", AjaxErrorMassage, null));
		} else {
			response.sendRedirect(CommonConstants.PASSPORT_PATH + "/error?msg=" + AjaxErrorMassage);
		}
	}

	/**
	 * 跳转到登陆页面或者提示重置密码信息
	 */
	public static void sendRestPassword(HttpServletRequest request, HttpServletResponse response, String AjaxErrorMassage) throws IOException {
		if (isAjaxRequest(request)) {
			ResponseUtils.outputWithJson(response, Result.badResult("-333", AjaxErrorMassage, null));
		} else {
			response.sendRedirect(CommonConstants.PASSPORT_PATH + "/resterror?msg=" + AjaxErrorMassage);
		}
	}

	/**
	 * 加入Cookie
	 */
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
		response.addCookie(cookie);
	}

	/**
	 * 加入Cookie
	 */
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int time) {
		Cookie cookie = new Cookie(name, value);
		if (time >= 0) {
			cookie.setMaxAge(time);
		}
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}

	/**
	 * 查询Cookie
	 */
	public static Cookie findCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 删除Cookie
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookie = findCookie(request, name);
		if (cookie != null) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	/**
	 * 是否AJAX请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return XMLHTTPREQUEST.equalsIgnoreCase(request.getHeader(X_REQUESTED_WITH));
	}

	/**
	 * 判断是否VIP用户
	 */
	public static boolean isVip(HttpServletRequest request, HttpServletResponse response) {
		Enterprise enterprise = getCurrentEnterprise(request, response);
		if (null == enterprise || !enterprise.getIsVip()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否代理商
	 */
	public static boolean IS_AGENT(HttpServletRequest request, HttpServletResponse response) {
		Enterprise enterprise = getCurrentEnterprise(request, response);
		if (null == enterprise || Enterprise.TYPE_NORMAL.equals(enterprise.getType())) {
			return false;
		}
		return true;
	}

	/**
	 * 获取当前登录用户
	 */
	public static User getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
		return (User) request.getAttribute(REQUEST_USER);
	}

	/**
	 * 获取当前登录用户的企业
	 */
	public static Enterprise getCurrentEnterprise(HttpServletRequest request, HttpServletResponse response) {
		return (Enterprise) request.getAttribute(REQUEST_ENTERPRISE);
	}

	/**
	 * 获取当前TOKEN
	 */
	public static Token getCurrentToken(HttpServletRequest request, HttpServletResponse response) {
		return (Token) request.getAttribute(REQUEST_TOKEN);
	}

	/**
	 * 是否有用户登录
	 */
	public static boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
		return getCurrentUser(request, response) != null;
	}

	/**
	 * 导入Excel
	 */
	public static Result importExcel(HttpServletRequest request, int maxSize) throws FileUploadException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory(0, null);
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setHeaderEncoding("UTF-8");
		List<?> itemList = sfu.parseRequest(request);
		DiskFileItem disk = (DiskFileItem) itemList.get(0);
		File f = disk.getStoreLocation();
		log.info("路径:" + f.getAbsolutePath());
		FileItem excel = null;
		for (Object obj : itemList) {
			FileItem fileItem = (FileItem) obj;
			if (!fileItem.isFormField()) {
				excel = fileItem;
				break;
			}
		}
		if (excel == null) {
			return Result.badResult("请上传excle格式文件");
		}
		Long size = excel.getSize();
		if (size > maxSize) {
			return Result.badResult("文件过大");
		}
		String fileName = excel.getName();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			return Result.badResult("请上传excle格式文件");
		}
		String newName = "";
		if (fileName.endsWith(".xls")) {
			newName = f.getAbsolutePath().replaceAll(".tmp", ".xls");
		}
		if (fileName.endsWith(".xlsx")) {
			newName = f.getAbsolutePath().replaceAll(".tmp", ".xlsx");
		}

		File file = new File(f.getAbsolutePath());
		file.renameTo(new File(newName));
		log.info("路径:" + newName);
		return Result.rightResult(newName);
	}
}
