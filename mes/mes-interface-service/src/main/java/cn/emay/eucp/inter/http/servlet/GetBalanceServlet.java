package cn.emay.eucp.inter.http.servlet;

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
public class GetBalanceServlet {

	private static Logger logger = Logger.getLogger(GetBalanceServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		// 客户IP
		String remoteIp = RequestUtils.getRemoteRealIp(request);
		try {
			// 请求数据
			String datajson = HttpUtils.requestParam(request);
			// 数据校验
			CheckResult<FmsBaseRequest> result = CheckUtils.checkData(datajson, FmsBaseRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, response, null);
				logger.info("get balance request fail : " + result.getLog());
				return;
			}

			FmsBaseRequest requestParams = result.getData();

			// 基础信息及签名校验
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBase(requestParams.getAppId(), remoteIp, requestParams.getRequestTime(), requestParams.getRequestValidPeriod(), "balance");
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, response, null);
				logger.info("[" + requestParams.getAppId() + "] get balance request fail: " + baseResult.getLog());
				return;
			}

			String balance = FmsInterfaceFrameWork.getService().getServiceCodeByAppIdBalance(requestParams.getAppId());
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, balance, response, null);
			logger.info("[" + requestParams.getAppId() + "] get balance request success , spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【GetBalanceServlet】get balance exception", e);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, response, null);
		}

	}

}
