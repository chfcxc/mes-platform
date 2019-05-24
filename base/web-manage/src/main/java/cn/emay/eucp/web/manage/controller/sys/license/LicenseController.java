package cn.emay.eucp.web.manage.controller.sys.license;


import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.encryption.AES;
import cn.emay.common.encryption.HexByte;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.GlobalConstants;
import cn.emay.eucp.common.dto.license.LicenseDTO;
import cn.emay.eucp.common.dto.license.LicenseDownLoadDTO;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.License;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.LicenseService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.DateUtil;
import cn.emay.util.PropertiesUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * license管理
 * 
 * @author lijunjian
 *
 */
@PageAuth("SYS_SERIAL_NUMBER")
@RequestMapping("/sys/license/recode")
@Controller
public class LicenseController {

	private static Logger log = Logger.getLogger(LicenseController.class);
	
	@Resource(name = "licenseService")
	private LicenseService licenseService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/license/list";
	}
	
	@OperationAuth("SYS_SERIAL_NUMBER_ADD")
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/license/add";
	}

	@OperationAuth("SYS_SERIAL_NUMBER_UPDATE")
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		LicenseDTO license=licenseService.findInfoById(id);
		String product=license.getProduct();
		license.setProduct(product.toUpperCase());
		request.setAttribute("license", license);
		return "sys/license/modify";
	}

	
	/**
	 * 列表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String operatorName = RequestUtils.getParameter(request, "operatorName");
		String mac = RequestUtils.getParameter(request, "mac");
		Integer termOfValidity=RequestUtils.getIntParameter(request, "termOfValidity", -1);
		Date startDate=RequestUtils.getDateParameter(request, "startDate", GlobalConstants.PARAMETER_DATE_FORMAT, null);
		Date endDate=RequestUtils.getDateParameter(request, "endDate", GlobalConstants.PARAMETER_DATE_FORMAT, null);
		Page<LicenseDTO> page=licenseService.findByPage(operatorName,mac,termOfValidity,startDate, endDate, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
	
	/**
	 * 新增
	 * @param request
	 * @param response
	 */
	@OperationAuth("SYS_SERIAL_NUMBER_ADD")
	@RequestMapping("/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		Long systemEnterpriseId = RequestUtils.getLongParameter(request, "systemEnterpriseId", 0l);
		String product=RequestUtils.getParameter(request, "product");
		String version=RequestUtils.getParameter(request, "version");
		Date beginTime=RequestUtils.getDateParameter(request, "beginTime", GlobalConstants.PARAMETER_DATE_FORMAT, null);
		Date endTime=RequestUtils.getDateParameter(request, "endTime", GlobalConstants.PARAMETER_DATE_FORMAT, null);
		String mac=RequestUtils.getParameter(request, "mac");
		String serviceType=RequestUtils.getParameter(request, "serviceType");
		
		String errorMessage=checkParameters(systemEnterpriseId, product, version, beginTime, endTime, mac, serviceType);
		if(!StringUtils.isEmpty(errorMessage)){
			ResponseUtils.outputWithJson(response, Result.badResult(errorMessage));
			return;
		}
		
		User currentUser=WebUtils.getCurrentUser(request, response);
		License license=new License();
		license.setSystemEnterpriseId(systemEnterpriseId);
		license.setProduct(product.toUpperCase());
		license.setVersion(version);
		license.setBeginTime(beginTime);
		license.setEndTime(endTime);
		license.setServiceType(serviceType);
		license.setMac(mac);
		license.setOperatorId(currentUser.getId());
		license.setOperatorName(currentUser.getUsername());
		license.setCreateTime(new Date());
		
		licenseService.addLicense(license);
		Enterprise enterprise=enterpriseService.getEnterpriseById(systemEnterpriseId);
		String service = "公共服务";
		String context = "新增license,客户名称:{0}";
		String module = "序列号管理";
		manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {enterprise.getNameCn()}), ManageUserOperLog.OPERATE_MODIFY);
		log.info("用户:" + currentUser.getUsername() + "新增license,客户名称:" + enterprise.getNameCn());	
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}
	
	/**
	 * 修改
	 * @param request
	 * @param response
	 */
	@OperationAuth("SYS_SERIAL_NUMBER_UPDATE")
	@RequestMapping("/ajax/update")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		Long id=RequestUtils.getLongParameter(request, "id", 0l);
		Long systemEnterpriseId = RequestUtils.getLongParameter(request, "systemEnterpriseId", 0l);
		String product=RequestUtils.getParameter(request, "product");
		String version=RequestUtils.getParameter(request, "version");
		Date beginTime=RequestUtils.getDateParameter(request, "beginTime", GlobalConstants.PARAMETER_DATE_FORMAT, null);
		Date endTime=RequestUtils.getDateParameter(request, "endTime", GlobalConstants.PARAMETER_DATE_FORMAT, null);
		String mac=RequestUtils.getParameter(request, "mac");
		String serviceType=RequestUtils.getParameter(request, "serviceType");
		
		if(id<=0l){
			ResponseUtils.outputWithJson(response, Result.badResult("数据不存在"));
			return;
		}
		License license=licenseService.findById(id);
		if(null==license){
			ResponseUtils.outputWithJson(response, Result.badResult("数据不存在"));
			return;
		}
		String errorMessage=checkParameters(systemEnterpriseId, product, version, beginTime, endTime, mac, serviceType);
		if(!StringUtils.isEmpty(errorMessage)){
			ResponseUtils.outputWithJson(response, Result.badResult(errorMessage));
			return;
		}
		
		User currentUser=WebUtils.getCurrentUser(request, response);
		license.setSystemEnterpriseId(systemEnterpriseId);
		license.setProduct(product);
		license.setVersion(version);
		license.setBeginTime(beginTime);
		license.setEndTime(endTime);
		license.setServiceType(serviceType);
		license.setMac(mac);
		license.setOperatorId(currentUser.getId());
		license.setOperatorName(currentUser.getUsername());
		
		licenseService.updateLicense(license);
		Enterprise enterprise=enterpriseService.getEnterpriseById(systemEnterpriseId);
		String service = "公共服务";
		String context = "修改license,客户名称:{0}";
		String module = "序列号管理";
		manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {enterprise.getNameCn()}), ManageUserOperLog.OPERATE_MODIFY);
		log.info("用户:" + currentUser.getUsername() + "修改license,客户名称:" + enterprise.getNameCn());	
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}
	
	/**
	 * 下载
	 * @param request
	 * @param response
	 */
	@OperationAuth("SYS_SERIAL_NUMBER_DOWNLOAD")
	@RequestMapping("/ajax/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		Long id=RequestUtils.getLongParameter(request, "id", 0l);
		
		if(id<=0l){
			ResponseUtils.outputWithJson(response, Result.badResult("数据不存在"));
			return;
		}
		LicenseDownLoadDTO license=licenseService.downloadFindById(id);
		if(null==license){
			ResponseUtils.outputWithJson(response, Result.badResult("数据不存在"));
			return;
		}
		Enterprise enterprise=enterpriseService.findEnterpriseByName(license.getNameCn());
		if(null==enterprise){
			ResponseUtils.outputWithJson(response, Result.badResult("企业不存在"));
			return;
		}
		String algorithm = PropertiesUtil.getProperty("emas.algorithm", "eucp.service.properties");
		String secretKey = PropertiesUtil.getProperty("emas.secretKey", "eucp.service.properties");
		if(StringUtils.isEmpty(algorithm)||StringUtils.isEmpty(secretKey)){
			ResponseUtils.outputWithJson(response, Result.badResult("请配置密钥"));
			return;
		}
        //导出txt文件  
        response.setContentType("text/plain");     
        String dateStr=DateUtil.toString(new Date(), "yyyyMMddHHmmss");
        String fileName="license"+dateStr;  
        try {  
            fileName = URLEncoder.encode(fileName, "UTF-8");  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        }      
        response.setHeader("Content-Disposition","attachment; filename=" + fileName + ".txt");      
        BufferedOutputStream buff = null;         
        StringBuffer write = new StringBuffer();         
        ServletOutputStream outSTr = null;         
        try {         
             outSTr = response.getOutputStream();  // 建立         
             buff = new BufferedOutputStream(outSTr);  
             //把内容写入文件  
             String json=JsonHelper.toJsonString(license);
     		 String str = HexByte.byte2Hex(AES.encrypt(json.getBytes(), secretKey.getBytes(), algorithm));
             write.append(str);
             buff.write(write.toString().getBytes("UTF-8"));         
             buff.flush();         
             buff.close();         
        } catch (Exception e) {         
            e.printStackTrace();         
        } finally {         
            try {         
                buff.close();         
                outSTr.close();         
            } catch (Exception e) {         
                e.printStackTrace();         
           }         
       }   
		User currentUser=WebUtils.getCurrentUser(request, response);
		String service = "公共服务";
		String context = "导出license,客户名称:{0}";
		String module = "序列号管理";
		manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {enterprise.getNameCn()}), ManageUserOperLog.OPERATE_MODIFY);
		log.info("用户:" + currentUser.getUsername() + "导出license,客户名称:" + enterprise.getNameCn());	
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}
	
	/**
	 * 客户列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/listClient")
	public void listClient(HttpServletRequest request, HttpServletResponse response) {
		String clientName = RequestUtils.getParameter(request, "clientName");
		int start = RequestUtils.getIntParameter(request, "start", GlobalConstants.DEFAULT_PAGE_START);
		int limit = RequestUtils.getIntParameter(request, "limit", GlobalConstants.DEFAULT_PAGE_LIMIT);
		Page<Map<String, Object>> page = enterpriseService.findPage(start, limit, clientName);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
	
	public String checkParameters(Long systemEnterpriseId,String product,String version,Date beginTime,Date endTime,String mac,String serviceType){
		StringBuffer message=new StringBuffer("");
		if(systemEnterpriseId<0l){
			message.append("企业不存在");
			return message.toString();
		}
		Enterprise enterprise=enterpriseService.getEnterpriseById(systemEnterpriseId);
		if(null==enterprise){
			message.append("企业不存在");
			return message.toString();
		}
		if(!StringUtils.isEmpty(product)){
			if(product.length()>50){
				message.append("产品名超过50字符");
				return message.toString();	
			}
		}
		if(!StringUtils.isEmpty(version)){
			if(version.length()>50){
				message.append("版本号超过50字符");
				return message.toString();	
			}
		}
		if(null==beginTime){
			message.append("请输入生效时间");
			return message.toString();	
		}
		if(null==endTime){
			message.append("请输入失效时间");
			return message.toString();	
		}
		if(StringUtils.isEmpty(mac)){
			message.append("MAC地址不能为空");
			return message.toString();	
		}
		if(!RegularUtils.checkMacAddress(mac)){
			message.append("MAC地址格式错误");
			return message.toString();	
		}	
		if(StringUtils.isEmpty(serviceType)){
			message.append("服务类型不能为空");
			return message.toString();	
		}
		return null;
	}
}
