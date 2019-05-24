package cn.emay.eucp.web.manage.controller.sys.helper;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.common.moudle.db.system.Foa;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.BusinessService;
import cn.emay.eucp.data.service.system.FoaService;
import cn.emay.eucp.data.service.system.UpdateInfoService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 *
 * 帮助
 * 
 * @author gonghui
 *
 */
@RequestMapping("/helper")
@Controller
public class SmsHelperController {

	@Resource(name = "foaService")
	FoaService foaService;
	@Resource(name = "updateInfoService")
	UpdateInfoService updateInfoService;
	@Resource(name = "businessService")
	BusinessService businessService;

	@RequestMapping("/fqa")
	public String indexFoa(HttpServletRequest request, HttpServletResponse response, Model model) {
		//所有业务模块
		List<Business> businessList = businessService.findAllBusiness();
		model.addAttribute("businessList", businessList);
		return "sys/system/foaHelper";
	}

	@RequestMapping("/updateInfo")
	public String indexUpdateInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		//所有业务模块
		List<Business> businessList = businessService.findAllBusiness();
		model.addAttribute("businessList", businessList);
		return "sys/system/updateHelper";
	}

	/**
	 * 管理端 获取foa列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/listFqa/mannger")
	public void listManngerFoa(HttpServletRequest request, HttpServletResponse response) {
		Integer businessType = RequestUtils.getIntParameter(request, "businessType", 0);
		List<Foa> list = foaService.findBySystemType(Foa.STATE_SUB_SYSTEM_MANNGER, businessType);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 管理端 获取更新记录列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/listUpdateInfo/mannger")
	public void listManngerinfo(HttpServletRequest request, HttpServletResponse response) {
		Page<UpdateInfo> info = new Page<UpdateInfo>();
		List<UpdateInfo> list = updateInfoService.findBySystemType(UpdateInfo.STATE_SUB_SYSTEM_MANNGER);
		if (null != list && list.size() > 0) {
			info.setList(list);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(info));
	}

	/**
	 * 管理端端 用户首次登陆弹框获取更新记录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/updateInfoFirst/manage")
	public void listManngerinfoFirst(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		UpdateInfo info = null;
		List<UpdateInfo> list = updateInfoService.findBySystemType(UpdateInfo.STATE_SUB_SYSTEM_MANNGER);
		if (null != list && list.size() > 0) {
			info = list.get(0);
			Boolean isRead = updateInfoService.getIsReadNewUpdateInfo(info.getId(), user.getId(), "manage");
			if (!isRead) {
				ResponseUtils.outputWithJson(response, Result.rightResult());
			} else {
				ResponseUtils.outputWithJson(response, Result.rightResult(info));
			}
		} else {
			ResponseUtils.outputWithJson(response, Result.rightResult());
		}
	}
	
	@RequestMapping("/ajax/listAllBusiness")
	public void listAllBusiness(HttpServletRequest request, HttpServletResponse response) {
		//所有业务模块
		List<Business> businessList = businessService.findAllBusiness();
		ResponseUtils.outputWithJson(response, Result.rightResult(businessList));
	}
}
