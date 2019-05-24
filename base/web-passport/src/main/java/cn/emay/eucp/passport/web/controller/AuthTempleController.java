package cn.emay.eucp.passport.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.emay.eucp.common.auth.ResourceTreeNode;
import cn.emay.eucp.common.auth.ResourceTreeNodeGenerater;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.data.service.system.PassportService;
import cn.emay.eucp.data.service.system.ResourceService;
import cn.emay.util.RequestUtils;

@Controller
@RequestMapping("/authTemple")
public class AuthTempleController {

	@javax.annotation.Resource
	private PassportService passportService;
	@javax.annotation.Resource
	private ResourceService resourceService;

	/**
	 * 
	 * 管理端AuthTemple
	 */
	@RequestMapping("")
	public String manageAuthTemple(HttpServletRequest request, HttpServletResponse response, Model model) {
		String systemType = RequestUtils.getParameter(request, "systemType");// 系统类型
		String businessType = RequestUtils.getParameter(request, "businessType");// 业务类型-公共服务为空
		boolean isSimple = RequestUtils.getBooleanParameter(request, "isSimple", false);
		String domainPath = RequestUtils.getParameter(request, "domainPath");// 域名

		if(StringUtils.isEmpty(businessType)){
			businessType="";
		}
		request.setAttribute("BUS_TYPE", businessType);
		request.setAttribute("BUS_SYSTEM", systemType);
		request.setAttribute("BUS_DOMAIN_PATH", domainPath);
		
		List<Resource> systemResources= resourceService.findByConditions(systemType, businessType);
		ResourceTreeNode root = ResourceTreeNodeGenerater.genResourceTree(systemResources);
		request.setAttribute("TREE", root);

		if (EucpSystem.管理系统.getCode().equalsIgnoreCase(systemType)) {
			if (isSimple) {
				return "/manage/temple/auth/simpleAuthTemple";
			} else {
				return "/manage/temple/auth/authTemple";
			}
		} else if (EucpSystem.客户系统.getCode().equalsIgnoreCase(systemType)) {
			if (isSimple) {
				return "/client/temple/auth/simpleAuthTemple";
			} else {
				return "/client/temple/auth/authTemple";
			}
		} else if (EucpSystem.销售系统.getCode().equalsIgnoreCase(systemType)) {
			if (isSimple) {
				return "/sales/temple/auth/simpleAuthTemple";
			} else {
				return "/sales/temple/auth/authTemple";
			}
		} else if (EucpSystem.运维系统.getCode().equalsIgnoreCase(systemType)) {
			if (isSimple) {
				return "/opers/temple/auth/simpleAuthTemple";
			} else {
				return "/opers/temple/auth/authTemple";
			}
		} else {
			return "redirect:error";
		}
	}

}
