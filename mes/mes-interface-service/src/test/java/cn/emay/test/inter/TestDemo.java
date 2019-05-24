package cn.emay.test.inter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import cn.emay.eucp.inter.framework.dto.CustomFmsIdAndMobile;
import cn.emay.eucp.inter.framework.dto.TemplateResultDto;
import cn.emay.eucp.inter.http.dto.request.FmsBaseRequest;
import cn.emay.eucp.inter.http.dto.request.FmsBatchRequest;
import cn.emay.eucp.inter.http.dto.request.FmsReportRequest;
import cn.emay.eucp.inter.http.dto.request.FmsSingleSendRequest;
import cn.emay.eucp.inter.http.dto.request.FmsTemplateReportRequest;
import cn.emay.eucp.inter.http.dto.request.GetTemplateStatusRequest;
import cn.emay.eucp.inter.http.dto.response.ReportResponse;
import cn.emay.eucp.inter.http.dto.response.SendFmsResponse;
import cn.emay.eucp.inter.http.dto.response.TemplateReportResponse;
import cn.emay.http.client.HttpClient;
import cn.emay.http.client.common.HttpHeader;
import cn.emay.http.client.common.HttpResultCode;
import cn.emay.http.client.response.HttpResponse;
import cn.emay.json.JsonHelper;
import cn.emay.util.GZIPUtils;
import cn.emay.utils.encryption.AES;

/**
 * 
* @项目名称：eucp-fms-interface-service 
* @类描述：   接口测试类
* @创建人：dinghaijiao   
* @创建时间：2019年5月7日 下午5:17:47   
* @修改人：dinghaijiao   
* @修改时间：2019年5月7日 下午5:17:47   
* @修改备注：
 */
public class TestDemo {

	// appId
	static String appId = "EUCP-EMY-FMS1-E9BQE";
	// 密钥
	static String secretKey = "6636933421A24EB0";

	public static void main(String[] args) {
		//
		// String host = "http://emay.com";//
		String host = "http://127.0.0.1:8999";
		String templateInfo = "${name}谢谢您的支持";

		String templateId = "";

		try {
			/****************** 不加密 ********************/
			// 报备模板
			templateId = reportTemplate(host, appId, templateInfo);

			// 获取模板状态
			getTemplateStatus(host, appId, templateId);

			// 单条发送
			sendSingleFMS(appId, host, "{'name':'小强'}", "2018010709302911", "15903160501", templateId);

			// 批次发送
			sendBatchFMS(appId, host, new CustomFmsIdAndMobile[] { new CustomFmsIdAndMobile("1", "15903160501", "{'name':'小明'}"), new CustomFmsIdAndMobile("2", "15903160501", "{'name':'小红'}") },
					templateId);

			// 获取状态报告
			getFMSReport(appId, host);

			// 获取余额
			getFMSBalance(appId, host);

			/****************** 加密 ********************/
			templateId = reportTemplateAES(host, appId, secretKey, templateInfo);

			// 查询模板状态
			getTemplateStatusSecure(host, appId, secretKey, templateId);

			// 单条发送
			sendSingleFMSSecure(appId, secretKey, host, "{'name':'小强'}", "2018010709302911", "15903160561", templateId);

			// 批次发送
			sendBatchFMSSecure(appId, secretKey, host,
					new CustomFmsIdAndMobile[] { new CustomFmsIdAndMobile("1", "15903160501", "{'name':'小明'}"), new CustomFmsIdAndMobile("2", "15903160501", "{'name':'小红'}") }, templateId);

			// 获取状态报告
			getFMSReportSecure(appId, secretKey, host);

			// 获取余额
			getFMSBalanceSecure(appId, secretKey, host);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void getFMSBalanceSecure(String appId, String secretKey, String host) throws IOException {
		System.out.println("=============begin getFMSBalanceSecure==================");
		FmsBaseRequest request = new FmsBaseRequest();
		request.setAppId(appId);
		request.setRequestTime(System.currentTimeMillis());
		request.setRequestValidPeriod(10);
		List<HttpHeader> headers = new ArrayList<HttpHeader>();
		headers.add(new HttpHeader("appId", appId));
		headers.add(new HttpHeader("gzip", "on"));
		headers.add(new HttpHeader("encode", "UTF-8"));
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(request);
		String json = requestAES(requestParameterJson, headers, host + "/secure/getBalance");
		System.out.println("balance: " + json);
		System.out.println("=============end getFMSBalanceSecure==================");
	}

	private static void getFMSBalance(String appId, String host) throws UnsupportedEncodingException {
		FmsBaseRequest request = new FmsBaseRequest();
		request.setAppId(appId);
		request.setRequestTime(System.currentTimeMillis());
		request.setRequestValidPeriod(10);
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(request);
		String json = request(requestParameterJson, host + "/inter/getBalance");
		System.out.println("balance: " + json);
		System.out.println("=============end getFMSBalance==================");
	}

	private static void getFMSReportSecure(String appId, String secretKey, String host) throws IOException {
		System.out.println("=============begin getFMSReportSecure==================");
		FmsReportRequest params = new FmsReportRequest();
		params.setAppId(appId);
		params.setRequestTime(System.currentTimeMillis());
		params.setRequestValidPeriod(10);
		List<HttpHeader> headers = new ArrayList<HttpHeader>();
		headers.add(new HttpHeader("appId", appId));
		headers.add(new HttpHeader("gzip", "on"));
		headers.add(new HttpHeader("encode", "UTF-8"));
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(params);
		String json = requestAES(requestParameterJson, headers, host + "/secure/getFMSReport");
		if (json != null) {
			@SuppressWarnings("unused")
			List<ReportResponse> data = JsonHelper.fromJson(new TypeToken<List<ReportResponse>>() {
			}, json);
			System.out.println("getFMSReportSecure result: " + json);
		}
		System.out.println("=============end getFMSReportSecure==================");
	}

	private static void getFMSReport(String appId, String host) throws UnsupportedEncodingException {
		FmsReportRequest params = new FmsReportRequest();
		params.setAppId(appId);
		params.setRequestTime(System.currentTimeMillis());
		params.setRequestValidPeriod(10);
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(params);
		String json = request(requestParameterJson, host + "/inter/getFMSReport");
		if (json != null) {
			@SuppressWarnings("unused")
			List<ReportResponse> data = JsonHelper.fromJson(new TypeToken<List<ReportResponse>>() {
			}, json);
			System.out.println("sendBatchFMS result: " + json);
		}
		System.out.println("=============end sendBatchFMS==================");

	}

	private static void sendBatchFMS(String appId, String host, CustomFmsIdAndMobile[] customFmsIdAndMobiles, String templateId) throws UnsupportedEncodingException {
		FmsBatchRequest params = new FmsBatchRequest();
		params.setAppId(appId);
		params.setFmses(customFmsIdAndMobiles);
		params.setRequestTime(System.currentTimeMillis());
		params.setRequestValidPeriod(10);
		params.setTemplateId(templateId);
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(params);
		String json = request(requestParameterJson, host + "/inter/sendBatchFMS");
		if (json != null) {
			@SuppressWarnings("unused")
			SendFmsResponse[] data = JsonHelper.fromJson(new TypeToken<SendFmsResponse[]>() {
			}, json);
			System.out.println("sendBatchFMS result: " + json);
		}
		System.out.println("=============end sendBatchFMS==================");

	}

	private static void sendBatchFMSSecure(String appId, String secretKey, String host, CustomFmsIdAndMobile[] customFmsIdAndMobiles, String templateId) throws IOException {
		System.out.println("=============begin sendBatchFMSSecure==================");
		FmsBatchRequest params = new FmsBatchRequest();
		params.setAppId(appId);
		params.setFmses(customFmsIdAndMobiles);
		params.setRequestTime(System.currentTimeMillis());
		params.setRequestValidPeriod(10);
		params.setTemplateId(templateId);
		List<HttpHeader> headers = new ArrayList<HttpHeader>();
		headers.add(new HttpHeader("appId", appId));
		headers.add(new HttpHeader("gzip", "on"));
		headers.add(new HttpHeader("encode", "UTF-8"));
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(params);
		String json = requestAES(requestParameterJson, headers, host + "/secure/sendBatchFMS");
		if (json != null) {
			@SuppressWarnings("unused")
			SendFmsResponse[] data = JsonHelper.fromJson(new TypeToken<SendFmsResponse[]>() {
			}, json);
			System.out.println("sendBatchFMSSecure result: " + json);
		}
		System.out.println("=============end sendBatchFMSSecure==================");
	}

	private static void sendSingleFMSSecure(String appId2, String secretKey2, String host, String content, String customFmsId, String mobile, String templateId) throws IOException {
		System.out.println("=============begin sendSingleFMSSecure==================");
		FmsSingleSendRequest params = new FmsSingleSendRequest();
		params.setAppId(appId);
		params.setContent(content);
		params.setCustomFmsId(customFmsId);
		params.setMobile(mobile);
		params.setRequestTime(System.currentTimeMillis());
		params.setRequestValidPeriod(10);
		params.setTemplateId(templateId);
		List<HttpHeader> headers = new ArrayList<HttpHeader>();
		headers.add(new HttpHeader("appId", appId));
		headers.add(new HttpHeader("gzip", "on"));
		headers.add(new HttpHeader("encode", "UTF-8"));
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(params);
		String json = requestAES(requestParameterJson, headers, host + "/secure/sendSingleFMS");
		if (json != null) {
			@SuppressWarnings("unused")
			SendFmsResponse data = JsonHelper.fromJson(new TypeToken<SendFmsResponse>() {
			}, json);
			System.out.println("sendSingleFMSSecure result: " + json);
		}
		System.out.println("=============end sendSingleFMSSecure==================");
	}

	private static void sendSingleFMS(String appId, String host, String content, String customFmsId, String mobile, String templateId) throws UnsupportedEncodingException {
		FmsSingleSendRequest params = new FmsSingleSendRequest();
		params.setAppId(appId);
		params.setContent(content);
		params.setCustomFmsId(customFmsId);
		params.setMobile(mobile);
		params.setRequestTime(System.currentTimeMillis());
		params.setRequestValidPeriod(10);
		params.setTemplateId(templateId);
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(params);
		String json = request(requestParameterJson, host + "/inter/sendSingleFMS");
		if (json != null) {
			@SuppressWarnings("unused")
			SendFmsResponse data = JsonHelper.fromJson(new TypeToken<SendFmsResponse>() {
			}, json);
			System.out.println("sendSingleFMS result: " + json);
		}
		System.out.println("=============end sendSingleFMS==================");

	}

	private static void getTemplateStatus(String host, String appId2, String templateId) throws UnsupportedEncodingException {
		GetTemplateStatusRequest request = new GetTemplateStatusRequest(templateId);
		request.setAppId(appId);
		request.setRequestTime(System.currentTimeMillis());
		request.setRequestValidPeriod(10);
		request.setTemplateId(templateId);
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(request);
		String json = request(requestParameterJson, host + "/inter/querytemplate");
		if (json != null) {
			@SuppressWarnings("unused")
			TemplateResultDto data = JsonHelper.fromJson(new TypeToken<TemplateResultDto>() {
			}, json);
			System.out.println("getTemplateStatus result: " + json);
		}
		System.out.println("=============end getTemplateStatus==================");

	}

	private static void getTemplateStatusSecure(String host, String appId, String secretKey, String templateId) throws IOException {
		System.out.println("=============begin getTemplateStatusSecure==================");
		GetTemplateStatusRequest request = new GetTemplateStatusRequest(templateId);
		request.setAppId(appId);
		request.setRequestTime(System.currentTimeMillis());
		request.setRequestValidPeriod(10);
		request.setTemplateId(templateId);
		List<HttpHeader> headers = new ArrayList<HttpHeader>();
		headers.add(new HttpHeader("appId", appId));
		headers.add(new HttpHeader("gzip", "on"));
		headers.add(new HttpHeader("encode", "UTF-8"));
		String requestParameterJson = JsonHelper.toJsonStringWithoutNull(request);
		String json = requestAES(requestParameterJson, headers, host + "/secure/querytemplate");
		if (json != null) {
			@SuppressWarnings("unused")
			TemplateResultDto data = JsonHelper.fromJson(new TypeToken<TemplateResultDto>() {
			}, json);
			System.out.println("getTemplateStatus result: " + json);
		}
		System.out.println("=============end getTemplateStatusSecure==================");
	}

	/**
	 * 
	 * @Title: reportTemplateAES
	 * @Description: 加密报备
	 * @param @param
	 *            host
	 * @param @param
	 *            appId2
	 * @param @param
	 *            secretKey2
	 * @param @param
	 *            templateInfo
	 * @param @return
	 * @return String
	 * @throws TODO
	 */
	private static String reportTemplateAES(String host, String appId2, String secretKey2, String templateInfo) {
		try {
			FmsTemplateReportRequest requestParams = new FmsTemplateReportRequest();
			requestParams.setAppId(appId);
			requestParams.setRequestTime(System.currentTimeMillis());
			requestParams.setRequestValidPeriod(10);
			requestParams.setTemplateInfo(templateInfo);
			List<HttpHeader> headers = new ArrayList<HttpHeader>();
			headers.add(new HttpHeader("appId", appId));
			headers.add(new HttpHeader("gzip", "on"));
			headers.add(new HttpHeader("encode", "UTF-8"));
			String requestParameterJson = JsonHelper.toJsonStringWithoutNull(requestParams);
			String json = requestAES(requestParameterJson, headers, host + "/secure/templateReport");
			if (json != null) {
				TemplateReportResponse data = JsonHelper.fromJson(new TypeToken<TemplateReportResponse>() {
				}, json);
				System.out.println("reportTemplate result: " + json);
				return data.getTemplateId();
			}
			System.out.println("=============end reportTemplate==================");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: reportTemplate
	 * @Description: 不加密报备
	 * @param @param
	 *            host
	 * @param @param
	 *            appId
	 * @param @param
	 *            templateInfo
	 * @param @return
	 * @return String
	 * @throws TODO
	 */
	private static String reportTemplate(String host, String appId, String templateInfo) {
		try {
			FmsTemplateReportRequest requestParams = new FmsTemplateReportRequest();
			requestParams.setAppId(appId);
			requestParams.setRequestTime(System.currentTimeMillis());
			requestParams.setRequestValidPeriod(10);
			requestParams.setTemplateInfo(templateInfo);
			String requestParameterJson = JsonHelper.toJsonStringWithoutNull(requestParams);
			String json = request(requestParameterJson, host + "/inter/templateReport");
			if (json != null) {
				TemplateReportResponse data = JsonHelper.fromJson(new TypeToken<TemplateReportResponse>() {
				}, json);
				System.out.println("reportTemplate result: " + json);
				return data.getTemplateId();
			}
			System.out.println("=============end reportTemplate==================");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	public static String requestAES(String requestParameterJson, List<HttpHeader> headers, String url) throws IOException {
		HttpResponse response = null;
		byte[] data = requestParameterJson.getBytes("UTF-8");
		byte[] cData = GZIPUtils.compress(data);
		byte[] aesData = AES.encrypt(cData, secretKey.getBytes("UTF-8"));
		response = HttpClient.post(url, "utf-8", headers, null, aesData);
		if (response.getResultCode().getCode().equals(HttpResultCode.SUCCESS.getCode())) {
			List<HttpHeader> rheaders = response.getHeaders();
			String code = null;
			for (HttpHeader header : rheaders) {
				if (header.getName() != null && header.getName().equals("result")) {
					code = header.getValue();
					break;
				}
			}
			byte[] decData = AES.decrypt(response.getData(), secretKey.getBytes("UTF-8"));
			byte[] dcData = GZIPUtils.decompress(decData);
			String resultJson = new String(dcData, "UTF-8");
			System.out.println("resultCode: " + code);
			return resultJson;
		}
		return null;
	}

	private static String request(String requestParameterJson, String url) throws UnsupportedEncodingException {
		HttpResponse response = null;
		byte[] data = requestParameterJson.getBytes("UTF-8");
		response = HttpClient.post(url, "utf-8", new ArrayList<HttpHeader>(), null, data);
		if (response.getResultCode().getCode().equals(HttpResultCode.SUCCESS.getCode())) {
			byte[] dcData = response.getData();
			String resultJson = new String(dcData, "UTF-8");
			return resultJson;
		}
		return null;
	}

}
