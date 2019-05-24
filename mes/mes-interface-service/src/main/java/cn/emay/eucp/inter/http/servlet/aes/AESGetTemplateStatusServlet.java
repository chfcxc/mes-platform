package cn.emay.eucp.inter.http.servlet.aes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.framework.dto.TemplateResultDto;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.GetTemplateStatusRequest;
import cn.emay.eucp.inter.http.utils.CheckUtils;
import cn.emay.eucp.inter.http.utils.HttpUtils;
import cn.emay.utils.web.servlet.RequestUtils;

/**
 * 获取模板状态
 * 
 * @author dinghaijiao
 *
 */
public class AESGetTemplateStatusServlet {

	private static Logger logger = Logger.getLogger(AESGetTemplateStatusServlet.class);

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
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBaseAndSign(appId, remoteIp, null);
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, baseResult.getData(), response, gzip, "on", encode);
				logger.info("[" + appId + "] get template status request fail: " + baseResult.getLog());
				return;
			}

			serviceCode = baseResult.getData();
			// 请求数据
			String datajson = HttpUtils.requestParam(request, serviceCode.getSecretKey(), gzip, encode);
			// 请求数据
			CheckResult<GetTemplateStatusRequest> result = CheckUtils.checkData(datajson, GetTemplateStatusRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("get template status " + datajson + " remoteIP：" + remoteIp + " fail : " + result.getLog());
				return;
			}
			GetTemplateStatusRequest requestParams = result.getData();

			// 数据校验
			CheckResult<Object> fmsResult = CheckUtils.check(requestParams.getRequestTime(), requestParams.getRequestValidPeriod());
			if (!fmsResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(fmsResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] get template status " + datajson + " remoteIP：" + remoteIp + " fail : " + fmsResult.getLog());
				return;
			}

			String templateId = requestParams.getTemplateId();

			if (templateId == null || templateId.trim().isEmpty()) {
				HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_TEMPLATE_ID_EMPTY, null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] get template status " + datajson + " remoteIP：" + remoteIp + " fail : templateId empty");
				return;
			}

			CheckResult<String> existResult = check(appId, templateId);
			if (!existResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(existResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] get template status " + datajson + " remoteIP：" + remoteIp + " fail : " + existResult.getLog());
				return;
			}

			TemplateResultDto statusReponse = FmsInterfaceFrameWork.getService().getTemplateStatus(appId, templateId);
			if (statusReponse == null) {
				HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_TEMPLATE_EMPTY, null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] get template status " + datajson + " remoteIP：" + remoteIp + " fail : template error");
				return;
			}
			// 响应
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, statusReponse, serviceCode, response, gzip, "on", encode);
			logger.info("[" + appId + "] get template status " + datajson + "remoteIP：" + remoteIp + " success! spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【GetTemplateStatusServlet】get template status exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, serviceCode, response, gzip, "on", encode);
		}

	}

	private CheckResult<String> check(String appId, String templateId) {
		boolean flag = FmsInterfaceFrameWork.getDataStore().isExsit(appId, templateId);
		if (!flag) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_ID_ERROR, null, "templateId error");
		}
		return new CheckResult<String>(EucpInterFmsReponseCode.SUCCESS, null, "success");
	}

}
