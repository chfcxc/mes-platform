package cn.emay.eucp.inter.http.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.eucp.common.dto.fms.mt.FmsIdAndMobile;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.util.TemplateIdUtil;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.http.constant.HttpInterfaceConstant;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.FmsSingleSendRequest;
import cn.emay.eucp.inter.http.dto.response.SendFmsResponse;
import cn.emay.eucp.inter.http.utils.CheckUtils;
import cn.emay.eucp.inter.http.utils.HttpUtils;
import cn.emay.utils.web.servlet.RequestUtils;

/**
 * 发送单条闪推接口
 * 
 * @author dinghaijiao
 *
 */
public class SendSingleFmsServlet {

	private static Logger logger = Logger.getLogger(SendSingleFmsServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		// 客户IP
		String remoteIp = RequestUtils.getRemoteRealIp(request);
		try {
			// 请求数据
			String datajson = HttpUtils.requestParam(request);
			// 数据校验
			CheckResult<FmsSingleSendRequest> result = CheckUtils.checkData(datajson, FmsSingleSendRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, response, null);
				logger.info("send single fms " + datajson + " remoteIP：" + remoteIp + " fail : " + result.getLog());
				return;
			}
			FmsSingleSendRequest requestParams = result.getData();

			// 基础信息
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBase(requestParams.getAppId(), remoteIp, requestParams.getRequestTime(), requestParams.getRequestValidPeriod(), null);
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, response, null);
				logger.info("[" + requestParams.getAppId() + "] send single fms " + datajson + " remoteIP：" + remoteIp + " fail : " + baseResult.getLog());
				return;
			}
			// 短信校验
			CheckResult<String> templateResult = check(requestParams.getAppId(), requestParams.getTemplateId(), requestParams.getContent(), requestParams.getMobile(), requestParams.getCustomFmsId());
			if (!templateResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(templateResult.getCode(), null, response, null);
				logger.info("[" + requestParams.getAppId() + "] send single fms " + datajson + " remoteIP：" + remoteIp + " fail : " + templateResult.getLog());
				return;
			}
			// 发送
			SendFmsResponse sendResponse = send(requestParams.getAppId(), requestParams.getTemplateId(), requestParams.getCustomFmsId(), remoteIp, requestParams.getContent(), requestParams.getMobile());
			// 响应
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, sendResponse, response, null);
			logger.info("[" + requestParams.getAppId() + "] send single fms " + datajson + "remoteIP：" + remoteIp + " success! spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【SendSingleFmsServlet】send single fms exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, response, null);
		}

	}

	/**
	 * 发送
	 * 
	 * @param appId
	 * @param templateId
	 * @param customFmsId
	 * @param remoteIp
	 * @param content
	 * @param mobile
	 * @return
	 */
	private SendFmsResponse send(String appId, String templateId, String customFmsId, String remoteIp, String content, String mobile) {
		FmsIdAndMobile fm = FmsInterfaceFrameWork.getService().sendSingleFms(appId, templateId, customFmsId, remoteIp, content, mobile);
		return new SendFmsResponse(mobile, fm.getFmsId(), customFmsId);
	}

	/**
	 * 校验参数
	 * 
	 * @param appId
	 * @param templateId
	 * @param content
	 * @param mobile
	 * @param customFmsId
	 * @return
	 */
	private CheckResult<String> check(String appId, String templateId, String content, String mobile, String customFmsId) {
		// 校验模板id
		if (templateId == null || templateId.trim().isEmpty()) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_ID_EMPTY, null, "templateId empty");
		}
		// 校验模板id
		boolean flag = FmsInterfaceFrameWork.getDataStore().isExsit(appId, templateId);
		if (!flag) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_TEMPLATE_ID_ERROR, null, "templateId error");
		}

		int templateType = TemplateIdUtil.getTemplateType(templateId);
		// 判断手机号码
		if (mobile == null || "".equals(mobile.trim())) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_MOBILE_EMPTY, null, "mobile empty");
		}
		if (!CheckUtils.checkMobile(mobile.trim())) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_MOBILE_ERROR, null, "mobile error");
		}

		if (templateType == FmsTemplate.TEMPLATE_TYPE_PERSONALITY) {
			// 判断短信内容为空
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
		}

		// 判断客户自定义fmsid长度
		if (customFmsId != null && customFmsId.length() > HttpInterfaceConstant.CUSTOMIMSID_LENGTH) {
			return new CheckResult<String>(EucpInterFmsReponseCode.ERROR_CUSTOM_IMSID, null, "custom fmsid too long");
		}
		return new CheckResult<String>(EucpInterFmsReponseCode.SUCCESS, null, null);
	}

}
