package cn.emay.eucp.inter.http.servlet.aes;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.constant.InterfaceConstant;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.FmsReportRequest;
import cn.emay.eucp.inter.http.dto.response.ReportResponse;
import cn.emay.eucp.inter.http.utils.BuildReportUtil;
import cn.emay.eucp.inter.http.utils.CheckUtils;
import cn.emay.eucp.inter.http.utils.HttpUtils;
import cn.emay.json.JsonHelper;
import cn.emay.utils.web.servlet.RequestUtils;

/**
 * 获取状态报告
 * 
 * @author dinghaijiao
 *
 */
public class AESGetReportServlet {

	private static Logger logger = Logger.getLogger(AESGetReportServlet.class);

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
		List<FmsReportDTO> receives = new ArrayList<FmsReportDTO>();
		FmsServiceCodeDto serviceCode = null;
		try {

			// 基础信息及签名校验
			CheckResult<FmsServiceCodeDto> baseResult = CheckUtils.checkBaseAndSign(appId, remoteIp, "report");
			if (!baseResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(baseResult.getCode(), null, baseResult.getData(), response, gzip, "on", encode);
				logger.info("[" + appId + "] get fms report request fail: " + baseResult.getLog());
				return;
			}

			serviceCode = baseResult.getData();
			// 请求数据
			String datajson = HttpUtils.requestParam(request, serviceCode.getSecretKey(), gzip, encode);
			// 数据校验
			CheckResult<FmsReportRequest> result = CheckUtils.checkData(datajson, FmsReportRequest.class);
			if (!result.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(result.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("get fms report request fail : " + result.getLog());
				return;
			}
			FmsReportRequest requestParams = result.getData();
			// 数据校验
			CheckResult<Object> fmsResult = CheckUtils.check(requestParams.getRequestTime(), requestParams.getRequestValidPeriod());
			if (!fmsResult.getCode().equals(EucpInterFmsReponseCode.SUCCESS)) {
				HttpUtils.responseResult(fmsResult.getCode(), null, serviceCode, response, gzip, "on", encode);
				logger.info("[" + appId + "] get fms report request : " + datajson + " remoteIP：" + remoteIp + " fail : " + fmsResult.getLog());
				return;
			}

			// 获取数据
			List<ReportResponse> reports = new ArrayList<ReportResponse>();
			FmsServiceCodeDto fmsServiceCodeDto = baseResult.getData();
			if (fmsServiceCodeDto.getIsNeedReport() != null && fmsServiceCodeDto.getIsNeedReport().intValue() != 1) {// 需要状态报告
				int number = (requestParams.getNumber() <= 0 || requestParams.getNumber() > InterfaceConstant.GET_REPORT_MAX_NUMBER) ? InterfaceConstant.GET_REPORT_MAX_NUMBER : requestParams
						.getNumber();
				receives = FmsInterfaceFrameWork.getService().getReport(appId, remoteIp, number);
				BuildReportUtil.buildReport(receives, reports);
			}
			HttpUtils.responseResult(EucpInterFmsReponseCode.SUCCESS, reports, serviceCode, response, gzip, "on", encode);
			// 获取成功后保存获取记录
			if (reports.size() > 0) {
				FmsInterfaceFrameWork.getService().saveReportRecord(appId, remoteIp, JsonHelper.toJsonString(reports));
			}
			logger.info("[" + appId + "] get fms report request success : number : " + receives.size() + " spent time " + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("【GetReportServlet】get fms report exception", e);
			FmsInterfaceFrameWork.getService().rollBackReport(receives);
			HttpUtils.responseResult(EucpInterFmsReponseCode.ERROR_PARAMS, null, serviceCode, response, gzip, "on", encode);
		}

	}

}
