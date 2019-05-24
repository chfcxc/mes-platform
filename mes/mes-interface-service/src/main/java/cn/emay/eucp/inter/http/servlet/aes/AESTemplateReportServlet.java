package cn.emay.eucp.inter.http.servlet.aes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.util.TemplateInfoParseUtil;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.framework.dto.TemplateReportResult;
import cn.emay.eucp.inter.http.constant.HttpInterfaceConstant;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.FmsTemplateReportRequest;
import cn.emay.eucp.inter.http.dto.response.TemplateReportResponse;
import cn.emay.eucp.inter.http.utils.CheckUtils;
import cn.emay.eucp.inter.http.utils.HttpUtils;
import cn.emay.utils.web.servlet.RequestUtils;

/**
 * 模板报备接口
 * 
 * @author dinghaijiao
 *
 */
public class AESTemplateReportServlet {

	private static Logger logger = Logger.getLogger(AESTemplateReportServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		String appId = request.getHeader("appId");
		String gzip = request.getHeader("gzip");
		String encode = request.getHeader("encode");
		if (encode == null || encode.trim().length() == 0) {
			encode = "UTF-8";
		}
		// 客户IP
		String remoteIp = RequestUtils.getRemoteRealIp(request);
		FmsServiceCodeDto serviceCode = null;
		try {
			// 基础信息及签名校验
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBaseAndSign(appId, remoteIp, "template");
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, baseResult.getData(), response, gzip, "on", encode);
				logger.info("[" + appId + "] report template request fail: " + baseResult.getLog());
				return;
			}

			serviceCode = baseResult.getData();

			String datajson = HttpUtils.requestParam(request, serviceCode.getSecretKey(), gzip, encode);
			// 数据校验
			CheckResult<FmsTemplateReportRequest> result = CheckUtils.checkData(datajson, FmsTemplateReportRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, response, null);
				logger.info("report template " + datajson + " remoteIP：" + remoteIp + " fail : " + result.getLog());
				return;
			}
			FmsTemplateReportRequest requestParams = result.getData();

			// 基础信息
			CheckResult<Object> fmsResult = CheckUtils.check(requestParams.getRequestTime(), requestParams.getRequestValidPeriod());
			if (!fmsResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(fmsResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] report template : " + datajson + " remoteIP：" + remoteIp + " fail : " + fmsResult.getLog());
				return;
			}
			// 模板校验
			CheckResult<String> templateResult = check(appId, requestParams.getTemplateInfo());
			if (!templateResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(templateResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] report template " + datajson + " remoteIP：" + remoteIp + " fail : " + templateResult.getLog());
				return;
			}
			// 发送
			TemplateReportResponse templateResponse = save(appId, requestParams.getTemplateInfo(), templateResult.getData());
			// 响应
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, templateResponse, serviceCode, response, gzip, "on", encode);
			logger.info("[" + appId + "] report template " + datajson + "remoteIP：" + remoteIp + " success! spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【TemplateReportServlet】 report template exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, serviceCode, response, gzip, "on", encode);
		}

	}

	/**
	 * 保存
	 * 
	 * @param appId
	 * @param templateInfo
	 * @param variable
	 * @return
	 */
	private TemplateReportResponse save(String appId, String templateInfo, String variable) {
		TemplateReportResult result = FmsInterfaceFrameWork.getService().templateReport(appId, templateInfo, variable, 0);
		return new TemplateReportResponse(result.getTemplateId(), variable);
	}

	/**
	 * 校验模板
	 * 
	 * @param templateInfo
	 * @return
	 */
	private CheckResult<String> check(String appId, String templateInfo) {
		if (templateInfo == null || templateInfo.isEmpty()) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_EMPTY, null, "templateInfo is null");
		}
		if (templateInfo.length() > HttpInterfaceConstant.TEMPLATE_MAX_LENGTH) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_ABOVE_LENGTH, null, "templateInfo too lang");
		}
		List<String> varList = TemplateInfoParseUtil.getVarList(templateInfo);
		if (varList.size() > HttpInterfaceConstant.TEMPLATE_VAR_MAX_NUM) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_VAR_MORE, null, "var too many");
		}
		Set<String> set = new HashSet<String>();
		set.addAll(varList);
		if (varList.size() != set.size()) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_VAR_REPEAT, null, "var repeated");
		}
		String variable = org.apache.commons.lang3.StringUtils.join(varList.toArray(), ",");
		// int templateType = (variable == null || variable.isEmpty()) ? FmsTemplate.TEMPLATE_TYPE_ORDINAY : FmsTemplate.TEMPLATE_TYPE_PERSONALITY;
		// Map<String, Long> map = FmsInterfaceFrameWork.getDataStore().getChannelId(appId, templateType);
		// if (map.isEmpty()) {
		// return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_APPID_NO_SUPPORT_CHANNEL, null, "no support channel");
		// }

		return new CheckResult<String>(EucpInterFmsReponseCode.SUCCESS, variable, null);
	}

}
