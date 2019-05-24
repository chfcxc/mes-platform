package cn.emay.eucp.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 * txt文件读写工具类
 *
 */
public class TxtFileUtil {
	
	private static Logger logger = Logger.getLogger(TxtFileUtil.class);
	
	public static String readTxt(String filePath){
		  String content = "";
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
				File file = new File(filePath);
				if (file.isFile() && file.exists()) {
					isr = new InputStreamReader(new FileInputStream(file), "utf-8");
					br = new BufferedReader(isr);
					String lineTxt = null;
					while ((lineTxt = br.readLine()) != null) {
						content += lineTxt;
					}
				} else {
					logger.error("文件不存在!");
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (isr != null) {
						isr.close();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return content;
	   }
	   
}
