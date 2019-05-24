package cn.emay.eucp.inter.http.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.emay.common.encryption.AES;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.json.JsonHelper;
import cn.emay.util.GZIPUtils;

public class HttpUtils {

	private static String algorithm = "AES/ECB/PKCS5Padding";
	private static String GIZP_ON = "on";
	private static String ENCRYPTION_ON = "on";// 加密

	/** 获取请求JSON
	 * 
	 * @param request
	 * @return */
	public static String requestParam(HttpServletRequest request) {
		DataInputStream in = null;
		String json = null;
		try {
			int totalbytes = request.getContentLength();
			if (totalbytes <= 0) {
				return null;
			}
			byte[] data = new byte[totalbytes];
			in = new DataInputStream(request.getInputStream());
			in.readFully(data);
			if (data == null || data.length == 0) {
				return null;
			}
			json = new String(data, "UTF-8");
		} catch (Exception e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return json;
	}

	/**
	 * 获取请求byte[]
	 * 
	 * @param request
	 * @return
	 */
	public static byte[] requestBytes(HttpServletRequest request) {
		DataInputStream in = null;
		byte[] data = null;
		try {
			int totalbytes = request.getContentLength();
			if (totalbytes <= 0) {
				return null;
			}
			data = new byte[totalbytes];
			in = new DataInputStream(request.getInputStream());
			in.readFully(data);
			if (data == null || data.length == 0) {
				return null;
			}
			return data;
		} catch (Exception e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return data;
	}

	/**
	 * 
	 * @param code
	 * @param data
	 * @param serviceCode
	 * @param response
	 * @param gzipOnOrNot
	 *            on 压缩 not 不压缩
	 * @param encryptionOnOrNot
	 *            on 加密 not 不加密
	 * @param encode
	 */
	public static void responseResult(EucpInterFmsReponseCode code, Object data, FmsServiceCodeDto serviceCode, HttpServletResponse response, String gzipOnOrNot, String encryptionOnOrNot, String encode) {
		OutputStream out = null;
		try {
			response.setContentType("text/plain;charset=" + encode);
			response.setHeader("result", code.getCode());
			if (EucpInterFmsReponseCode.SUCCESS.equals(code)) {
				String js = JsonHelper.toJsonString(data);
				byte[] bytes = js.getBytes(encode);
				if (GIZP_ON.equalsIgnoreCase(gzipOnOrNot)) {
					bytes = GZIPUtils.compress(bytes);
				}
				byte[] result = bytes;
				if (ENCRYPTION_ON.equalsIgnoreCase(encryptionOnOrNot)) {
					result = AES.encrypt(bytes, serviceCode.getSecretKey().getBytes(), algorithm);
				}
				out = response.getOutputStream();
				out.write(result);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void responseResult(EucpInterFmsReponseCode code, Object data, HttpServletResponse response, String encode) {
		OutputStream out = null;
		try {
			if (encode == null) {
				encode = "utf-8";
			}
			response.setContentType("text/plain;charset=" + encode);
			response.setHeader("result", code.getCode());
			if (EucpInterFmsReponseCode.SUCCESS.equals(code)) {
				String js = JsonHelper.toJsonString(data);
				byte[] result = js.getBytes(encode);
				out = response.getOutputStream();
				out.write(result);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** @Title: reportResponseResult
	 * @Description: 响应
	 * @param @param
	 *            code
	 * @param @param
	 *            response
	 * @return void */
	public static void reportResponseResult(String code, HttpServletResponse response) {
		OutputStream out = null;
		try {
			response.setContentType("text/plain;charset=utf-8");
			String js = code;
			byte[] bytes = js.getBytes("UTF-8");
			out = response.getOutputStream();
			out.write(bytes);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 先解密，再解压
	 * 
	 * @param request
	 * @param secretKey
	 * @param gzip
	 * @param encode
	 * @return
	 */
	public static String requestParam(HttpServletRequest request, String secretKey, String gzip, String encode) {
		DataInputStream in = null;
		String json = null;
		try {
			int totalbytes = request.getContentLength();
			if (totalbytes <= 0) {
				return null;
			}
			byte[] data = new byte[totalbytes];
			in = new DataInputStream(request.getInputStream());
			in.readFully(data);
			if (data == null || data.length == 0) {
				return null;
			}
			byte[] jsonBytes = AES.decrypt(data, secretKey.getBytes(), algorithm);
			if (jsonBytes == null) {
				return null;
			}
			if (GIZP_ON.equalsIgnoreCase(gzip)) {
				jsonBytes = GZIPUtils.decompress(jsonBytes);
			}
			if (jsonBytes == null) {
				return null;
			}
			json = new String(jsonBytes, encode);
		} catch (Exception e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return json;
	}

}
