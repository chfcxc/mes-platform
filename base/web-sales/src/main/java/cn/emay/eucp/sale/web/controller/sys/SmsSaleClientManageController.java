package cn.emay.eucp.sale.web.controller.sys;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.EnterpriseBindingSaleService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("SYS_SALE_CLIENT")
@RequestMapping("/sys/sale/client")
@Controller
public class SmsSaleClientManageController {

	private Logger log = Logger.getLogger(SmsSaleClientManageController.class);

	@Resource(name = "enterpriseBindingSaleService")
	private EnterpriseBindingSaleService enterpriseBindingSaleService;
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;


	/**
	 * 页面跳转
	 * 
	 * @param request
	 * @param response
	 * @param model1
	 * @return
	 */
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sms/client/list";
	}

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param response
	 */
	/*
	 * @RequestMapping("selectCientManage") public void
	 * selectCientManage(HttpServletRequest request, HttpServletResponse response) {
	 * User user = WebUtils.getCurrentUser(request, response); String ids =
	 * SaleUtil.getEnterpriseIds(user.getId()); String clientUserNameAndCode =
	 * RequestUtils.getParameter(request, "clientUserNameAndCode");// 客户名称/编号
	 * Integer start = RequestUtils.getIntParameter(request, "start", 1); Integer
	 * limit = RequestUtils.getIntParameter(request, "limit", 20);
	 * Page<EnterpriseBindingSale> page =
	 * enterpriseBindingSaleService.selectCientManage(user.getUsername(), ids,
	 * clientUserNameAndCode, start, limit); if (null != page.getList()) { for
	 * (EnterpriseBindingSale enterpriseBindingSale : page.getList()) { List<String>
	 * smsServiceCodeList =
	 * smsServiceCodeService.findServiceCodeByEnterpriseId(String.valueOf(
	 * enterpriseBindingSale.getSystemEnterpriseId())); if (null !=
	 * smsServiceCodeList && smsServiceCodeList.size() != 0) {
	 * enterpriseBindingSale.setServiceCodeCount(smsServiceCodeList.size()); } else
	 * { enterpriseBindingSale.setServiceCodeCount(Integer.valueOf(0)); } } }
	 * ResponseUtils.outputWithJson(response, Result.rightResult(page)); }
	 */

	/**
	 * 置顶
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/stickCientManage")
	public void stickCientManage(HttpServletRequest request, HttpServletResponse response) {
		Result result = null;
		User user = WebUtils.getCurrentUser(request, response);
		String enterpriseBindingSaleId = RequestUtils.getParameter(request, "id");
		String retMsg = enterpriseBindingSaleService.stickCientManage(user.getUsername(), enterpriseBindingSaleId);
		result = Result.rightResult();
		String service = "销售端服务";
		String module = "置顶";
		String context = "置顶：置顶人{0}，企业编号{1}";
		manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, user.getRealname(), retMsg), ManageUserOperLog.OPERATE_ADD);
		log.info("用户:" + user.getUsername() + "置顶,置顶人:" + user.getRealname() + ",企业编号:" + retMsg + "");
		ResponseUtils.outputWithJson(response, Result.rightResult(result));
	}
}
