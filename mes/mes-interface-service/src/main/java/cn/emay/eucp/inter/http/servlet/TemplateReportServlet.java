package cn.emay.eucp.inter.http.servlet;

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
public class TemplateReportServlet {

	private static Logger logger = Logger.getLogger(TemplateReportServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		// 客户IP
		String remoteIp = RequestUtils.getRemoteRealIp(request);
		try {
			// 请求数据
			String datajson = HttpUtils.requestParam(request);
			// 数据校验
			CheckResult<FmsTemplateReportRequest> result = CheckUtils.checkData(datajson, FmsTemplateReportRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, response, null);
				logger.info("report template " + datajson + " remoteIP：" + remoteIp + " fail : " + result.getLog());
				return;
			}
			FmsTemplateReportRequest requestParams = result.getData();

			// 基础信息
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBase(requestParams.getAppId(), remoteIp, requestParams.getRequestTime(), requestParams.getRequestValidPeriod(), "template");
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, response, null);
				logger.info("[" + requestParams.getAppId() + "] report template " + datajson + " remoteIP：" + remoteIp + " fail : " + baseResult.getLog());
				return;
			}
			// 短信校验
			CheckResult<String> templateResult = check(requestParams.getAppId(), requestParams.getTemplateInfo());
			if (!templateResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(templateResult.getCode(), null, response, null);
				logger.info("[" + requestParams.getAppId() + "] report template " + datajson + " remoteIP：" + remoteIp + " fail : " + templateResult.getLog());
				return;
			}
			// 发送
			TemplateReportResponse templateResponse = save(requestParams.getAppId(), requestParams.getTemplateInfo(), templateResult.getData());
			// 响应
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, templateResponse, response, null);
			logger.info("[" + requestParams.getAppId() + "] report template " + datajson + "remoteIP：" + remoteIp + " success! spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【TemplateReportServlet】 report template exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, response, null);
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
