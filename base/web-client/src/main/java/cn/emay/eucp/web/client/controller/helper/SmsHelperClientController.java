package cn.emay.eucp.web.client.controller.helper;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Foa;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;
import cn.emay.eucp.common.moudle.db.system.User;
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
@RequestMapping("/helperClient")
@Controller
public class SmsHelperClientController {

	@Resource(name = "foaService")
	FoaService foaService;

	@Resource(name = "updateInfoService")
	UpdateInfoService updateInfoService;

	@RequestMapping("/fqa")
	public String indexFoa(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/system/foaHelper";
	}

	@RequestMapping("/updateInfo")
	public String indexUpdateInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/system/updateHelper";
	}

	/**
	 * 客户端 获取foa列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/listFqa/client")
	public void listManngerFoa(HttpServletRequest request, HttpServletResponse response) {
		Integer businessType = RequestUtils.getIntParameter(request, "businessType", 0);
		List<Foa> list = foaService.findBySystemType(Foa.STATE_SUB_SYSTEM_CLIENT, businessType);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 客户端 获取更新记录列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/listUpdateInfo/client")
	public void listManngerinfo(HttpServletRequest request, HttpServletResponse response) {
		Page<UpdateInfo> info = new Page<UpdateInfo>();
		List<UpdateInfo> list = updateInfoService.findBySystemType(UpdateInfo.STATE_SUB_SYSTEM_CLIENT);
		if (null != list && list.size() > 0) {
			info.setList(list);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(info));
	}

	/**
	 * 客户端 用户首次登陆弹框获取更新记录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/updateInfoFirst/client")
	public void listManngerinfoFirst(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		UpdateInfo info = null;
		List<UpdateInfo> list = updateInfoService.findBySystemType(UpdateInfo.STATE_SUB_SYSTEM_CLIENT);
		if (null != list && list.size() > 0) {
			info = list.get(0);
			Boolean isRead = updateInfoService.getIsReadNewUpdateInfo(info.getId(), user.getId(), "client");
			if (!isRead) {
				ResponseUtils.outputWithJson(response, Result.rightResult());
			} else {
				ResponseUtils.outputWithJson(response, Result.rightResult(info));
			}
		} else {
			ResponseUtils.outputWithJson(response, Result.rightResult());
		}
	}
}
