package cn.emay.eucp.web.manage.controller.sys.base;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.Operator;
import cn.emay.eucp.common.moudle.db.system.BaseSectionNumber;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.BaseSectionNumberService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 基础号段管理
 * 
 * @author 徐亚光
 *
 */

@PageAuth("SYS_BASE_BASESECTION")
@RequestMapping("/sys/base/basesection")
@Controller
public class BaseSectionNumberController {
	private static Logger log = Logger.getLogger(BaseSectionNumberController.class);
	@Resource(name = "baseSectionNumberService")
	BaseSectionNumberService baseSectionNumberService;
	
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;
	
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/sys/base/basesectionnumber";
	}
	

	/**
	 * 查询
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("findlist")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String number = RequestUtils.getParameter(request, "number");
		String operatorCode = RequestUtils.getParameter(request, "operatorCode");
		Page<BaseSectionNumber> page = baseSectionNumberService.findBaseSectionNumber(number, operatorCode, start, limit);
		for (BaseSectionNumber sec : page.getList()) {
			sec.setOperatorCode(Operator.findNameByCode(sec.getOperatorCode()));
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 
	 * 增加
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_BASESECTION_ADD")
	@RequestMapping("add")
	public void addbaseSectionNumber(HttpServletRequest request, HttpServletResponse response) {
		String operatorCode = RequestUtils.getParameter(request, "operatorCode");
		String number=RequestUtils.getParameter(request, "number");
		String regex = "^1\\d{2,4}$";
		if(StringUtils.isEmpty(number)){
			ResponseUtils.outputWithJson(response, Result.badResult("号段不能为空"));
			return;
		}
		if (!number.matches(regex)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请输入以1开头的3到5位数字"));
			return;
		}
		if (StringUtils.isEmpty(operatorCode)) {
			ResponseUtils.outputWithJson(response, Result.badResult("运营商不为空"));
			return;
		}
		if(null==Operator.findNameByCode(operatorCode)  ){
			ResponseUtils.outputWithJson(response, Result.badResult("运营商选择错误"));
			return;
		}
		
		
		boolean bsn = baseSectionNumberService.findByNumber(number,null);
		if (!bsn) {
			BaseSectionNumber baseSectionNumber = new BaseSectionNumber();
			baseSectionNumber.setNumber(number);
			baseSectionNumber.setOperatorCode(operatorCode);
			baseSectionNumber.setIsDelete(false);
			Result result=baseSectionNumberService.addBaseSectionNumber(baseSectionNumber);
			User currentUser = WebUtils.getCurrentUser(request, response);
			if (result.getSuccess()) {
				String service = "公共服务";
				String context = "增加基础号段{0}";
				String module = "基础号段";
				manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, number ), ManageUserOperLog.OPERATE_ADD);
				log.info("用户:"+currentUser.getUsername()+"增加号段,号段名称为:"+number);
			}
			ResponseUtils.outputWithJson(response, Result.rightResult());
		} else {
			ResponseUtils.outputWithJson(response, Result.badResult("数据已存在"));
		}
	}

	/**
	 * 
	 * 修改
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_BASESECTION_UPDATE")
	@RequestMapping("update")
	public void updatebaseSectionNumber(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String number = RequestUtils.getParameter(request, "number");
		String operatorCode = RequestUtils.getParameter(request, "operatorCode");
		String regex = "^1\\d{2,4}$";
		if(StringUtils.isEmpty(number)){
			ResponseUtils.outputWithJson(response, Result.badResult("号段不能为空"));
			return;
		}
		if (!number.matches(regex)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请输入以1开头的3到5位数字"));
			return;
		}
		if(null==Operator.findNameByCode(operatorCode) ){
			ResponseUtils.outputWithJson(response, Result.badResult("运营商选择错误"));
			return;
		}
		
		boolean bsn = baseSectionNumberService.findByNumber(number,id);
		BaseSectionNumber baseSectionNumber=new BaseSectionNumber();
		if(!bsn){
			baseSectionNumber.setId(id);
			baseSectionNumber.setNumber(number);
			baseSectionNumber.setOperatorCode(operatorCode);
			baseSectionNumber.setIsDelete(false);
			Result result=baseSectionNumberService.updateBaseSectionNumber(baseSectionNumber);
			User currentUser = WebUtils.getCurrentUser(request, response);
			if (result.getSuccess()) {
				String service = "公共服务";
				String context = "修改基础号段{0}";
				String module = "基础号段";
				manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, number), ManageUserOperLog.OPERATE_MODIFY);
				log.info("用户:"+currentUser.getUsername()+"修改号段,号段名称为:"+number);
			}
			ResponseUtils.outputWithJson(response, result);
		}else{
			ResponseUtils.outputWithJson(response, Result.badResult("数据已存在"));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult("修改成功"));
		
	}

	/**
	 * 
	 * 根据id查询
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("findId")
	public void findByid(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		if(!StringUtils.isEmpty(id)){
			BaseSectionNumber findById = baseSectionNumberService.findById(id);
			ResponseUtils.outputWithJson(response, Result.rightResult(findById));
		}
		
		
	}

	/**
	 * 
	 * 删除
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_BASESECTION_DELETE")
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		Result result=baseSectionNumberService.deleteBaseSectionNumber(id);
		BaseSectionNumber findById = baseSectionNumberService.findById(id);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "删除基础号段{0}";
			String module = "基础号段";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, findById.getNumber()), ManageUserOperLog.OPERATE_DELETE);
			log.info("用户:"+currentUser.getUsername()+"删除号段,号段名称为:"+findById.getNumber());
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

}
