package cn.emay.eucp.inter.http.servlet.aes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.FmsBaseRequest;
import cn.emay.eucp.inter.http.utils.CheckUtils;
import cn.emay.eucp.inter.http.utils.HttpUtils;
import cn.emay.utils.web.servlet.RequestUtils;

/**
 * 获取余额
 * 
 * @author dinghaijiao
 *
 */
public class AESGetBalanceServlet {

	private static Logger logger = Logger.getLogger(AESGetBalanceServlet.class);

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
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBaseAndSign(appId, remoteIp, "balance");
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, baseResult.getData(), response, gzip, "on", encode);
				logger.info("[" + appId + "] get balance request fail: " + baseResult.getLog());
				return;
			}

			serviceCode = baseResult.getData();

			String datajson = HttpUtils.requestParam(request, serviceCode.getSecretKey(), gzip, encode);
			// 数据校验
			CheckResult<FmsBaseRequest> result = CheckUtils.checkData(datajson, FmsBaseRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("get balance request fail : " + result.getLog());
				return;
			}
			FmsBaseRequest requestParams = result.getData();
			// 数据校验
			CheckResult<Object> fmsResult = CheckUtils.check(requestParams.getRequestTime(), requestParams.getRequestValidPeriod());
			if (!fmsResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(fmsResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] get balance : " + datajson + " remoteIP：" + remoteIp + " fail : " + fmsResult.getLog());
				return;
			}

			String balance = FmsInterfaceFrameWork.getService().getServiceCodeByAppIdBalance(appId);
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, balance, serviceCode, response, gzip, "on", encode);
			logger.info("[" + appId + "] get balance request success , spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【GetBalanceServlet】get balance exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, serviceCode, response, gzip, "on", encode);
		}

	}



}
