package cn.emay.eucp.web.manage.controller.sys.client;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.EnterpriseBindingSale;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.EnterpriseBindingSaleService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("SYS_CLIENT_RELATIONSHIP")
@RequestMapping("/sys/client/relationship")
@Controller
public class ClientRelationshipController {

	private static Logger log = Logger.getLogger(ClientRelationshipController.class);

	@Resource(name = "enterpriseBindingSaleService")
	private EnterpriseBindingSaleService enterpriseBindingSaleService;
	@Resource(name = "userService")
	private UserService userService;
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
		return "sys/client/relationship/relationship";
	}

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("selectRelationship")
	public void selectRelationship(HttpServletRequest request, HttpServletResponse response) {
		String saleMan = RequestUtils.getParameter(request, "saleMan");// 销售
		String clientUserNameAndCode = RequestUtils.getParameter(request, "clientUserNameAndCode");// 客户名称/编号
		Integer start = RequestUtils.getIntParameter(request, "start", 1);
		Integer limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<EnterpriseBindingSale> page = enterpriseBindingSaleService.selectRelationship(clientUserNameAndCode, saleMan, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 查询人员
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String realname = RequestUtils.getParameter(request, "realname");
		Page<UserDTO> userpage = userService.findUserPageByRoleType(EucpSystem.销售系统.getCode(), User.USER_TYPE_EMAY, realname, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(userpage));
	}

	/**
	 * 调整绑定关系
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/relationship")
	@OperationAuth("OPER_SYS_CLIENT_RELATIONSHIP_ADD")
	public void relationship(HttpServletRequest request, HttpServletResponse response) {
		Result result = null;
		User user = WebUtils.getCurrentUser(request, response);
		String saleMan = RequestUtils.getParameter(request, "saleMan");// 销售
		String clientUserNameAndCode = RequestUtils.getParameter(request, "clientUserNameAndCode");// 客户名称/编号
		String username = RequestUtils.getParameter(request, "username");
		String enterpriseIds = RequestUtils.getParameter(request, "enterpriseIds");
		String isAll = RequestUtils.getParameter(request, "isAll");
		String retMsg = enterpriseBindingSaleService.relationship(clientUserNameAndCode, saleMan, username, enterpriseIds, user, isAll);
		User retUser = userService.findUserById(Long.valueOf(username));
		result = Result.rightResult();
		String service = "公共服务";
		String module = "企业销售关系";
		String context = "企业销售关系：绑定人{0}，企业编号{1}";
		manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, retUser.getRealname(), retMsg), ManageUserOperLog.OPERATE_ADD);
		log.info("用户:" + user.getUsername() + "保存企业销售关系,绑定人:" + user.getRealname() + ",企业编号:" + retMsg + "");
		ResponseUtils.outputWithJson(response, Result.rightResult(result));
	}
}
