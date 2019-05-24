package cn.emay.channel.framework.utils;

import java.io.DataInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

	/**
	 * 获取请求JSON
	 * 
	 * @param request
	 * @return
	 */
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


	
}
