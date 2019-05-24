package cn.emay.eucp.inter.http.servlet.aes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.eucp.common.dto.fms.mt.FmsIdAndMobile;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.util.TemplateIdUtil;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.framework.dto.CustomFmsIdAndMobile;
import cn.emay.eucp.inter.http.constant.HttpInterfaceConstant;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.FmsBatchRequest;
import cn.emay.eucp.inter.http.dto.response.SendFmsResponse;
import cn.emay.eucp.inter.http.utils.CheckUtils;
import cn.emay.eucp.inter.http.utils.HttpUtils;
import cn.emay.utils.web.servlet.RequestUtils;

public class AESSendBatchFmsServlet {

	private static Logger logger = Logger.getLogger(AESSendBatchFmsServlet.class);

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
				logger.info("[" + appId + "] send batch fms fail: " + baseResult.getLog());
				return;
			}

			serviceCode = baseResult.getData();
			// 请求数据
			String datajson = HttpUtils.requestParam(request, serviceCode.getSecretKey(), gzip, encode);
			// 数据校验
			CheckResult<FmsBatchRequest> result = CheckUtils.checkData(datajson, FmsBatchRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, baseResult.getData(), response, gzip, "on", encode);
				logger.info("send batch fms " + datajson + " remoteIP：" + remoteIp + " fail : " + result.getLog());
				return;
			}

			FmsBatchRequest requestParams = result.getData();
			// 数据校验
			CheckResult<Object> timeResult = CheckUtils.check(requestParams.getRequestTime(), requestParams.getRequestValidPeriod());
			if (!timeResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(timeResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] send batch fms " + datajson + " remoteIP：" + remoteIp + " fail : " + timeResult.getLog());
				return;
			}

			// 业务参数校验
			CheckResult<String> fmsResult = check(appId, requestParams.getTemplateId(), requestParams.getFmses());
			if (!fmsResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(fmsResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] send batch fms " + datajson + " remoteIP：" + remoteIp + " fail : " + fmsResult.getLog());
				return;
			}
			// 发送
			SendFmsResponse[] fmsResponse = send(requestParams.getFmses(), requestParams.getTemplateId(), appId, remoteIp);
			// 响应
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, fmsResponse, serviceCode, response, gzip, "on", encode);
			logger.info("[" + appId + "] send batch fms " + datajson + "remoteIP：" + remoteIp + " success! spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【SendBatchFmsServlet】 send batch fms exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, serviceCode, response, gzip, "on", encode);
		}

	}

	/**
	 * 发送
	 * 
	 * @param fmses
	 * @param templateId
	 * @param appId
	 * @param remoteIp
	 * @return
	 */
	private SendFmsResponse[] send(CustomFmsIdAndMobile[] fmses, String templateId, String appId, String remoteIp) {
		FmsIdAndMobile[] resus = FmsInterfaceFrameWork.getService().sendBatchFms(appId, fmses, remoteIp, templateId);
		SendFmsResponse[] responseData = new SendFmsResponse[resus.length];
		int k = 0;
		for (FmsIdAndMobile sr : resus) {
			responseData[k] = new SendFmsResponse(sr.getMobile(), sr.getFmsId(), sr.getCustomFmsId());
			k++;
		}
		return responseData;
	}

	/**
	 * 校验参数
	 * 
	 * @param appId
	 * @param templateId
	 * @param fmses
	 * @return
	 */
	private CheckResult<String> check(String appId, String templateId, CustomFmsIdAndMobile[] fmses) {
		if (templateId == null || templateId.trim().isEmpty()) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_ID_EMPTY, null, "templateId empty");
		}
		// 校验模板id
		boolean existFlag = FmsInterfaceFrameWork.getDataStore().isExsit(appId, templateId);
		if (!existFlag) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_ID_ERROR, null, "templateId error");
		}
		int templateType = TemplateIdUtil.getTemplateType(templateId);
		// 校验手机号码及CustomImsId
		if (fmses == null || fmses.length == 0) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_MOBILE_EMPTY, null, "mobile empty");
		}
		if (fmses.length > HttpInterfaceConstant.MAX_NUMBER) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_MOBILE_NUMBER, null, "mobile number lager");
		}
		int errorNum = 0;
		boolean flag = false;
		if (templateType == FmsTemplate.TEMPLATE_TYPE_PERSONALITY) {
			flag = true;
		}
		for (CustomFmsIdAndMobile cm : fmses) {
			if (cm.getMobile() == null || !CheckUtils.checkMobile(cm.getMobile().trim())) {
				errorNum++;
			}
			// 判断客户自定义fmsid长度
			if (cm.getCustomFmsId() != null && cm.getCustomFmsId().length() > HttpInterfaceConstant.CUSTOMIMSID_LENGTH) {
				return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_CUSTOM_IMSID, null, "custom imsid too lang");
			}

			// if (flag) {// 如果模板是个性模板 校验变量
			// String content = cm.getContent();
			// // 判断短信内容为空
			// if (content == null || "".equals(content.trim())) {
			// return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_CONTENT_EMPTY, null, "content empty");
			// }
			// // 校验变量个数以及长度
			// Map<String, String> vars = JsonHelper.fromJson(new TypeToken<Map<String, String>>() {
			// }, content);
			// Set<String> set = vars.keySet();
			// if (set.size() > HttpInterfaceConstant.TEMPLATE_VAR_MAX_NUM) {
			// return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_VAR_MORE, null, "var too many");
			// }
			//
			// for (String key : set) {
			// String value = vars.get(key);
			// if (value == null || value.isEmpty()) {
			// return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_VAR_ERROR, null, "var error");
			// }
			// if (value.length() > HttpInterfaceConstant.TEMPLATE_VAR_MAX_LENGTH) {
			// return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_VAR_ABOVE_LENGTH, null, "var too long");
			// }
			// }
			// }
		}

		if (errorNum == fmses.length) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_MOBILE_ERROR, null, "has error mobile");
		}
		return new CheckResult<String>(EucpInterFmsReponseCode.SUCCESS, null, null);
	}

}
