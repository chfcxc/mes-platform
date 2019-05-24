package cn.emay.eucp.web.util.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class OnlyReadFirstRowUtil {

	public static String[] getFirstRowFromXlsx(File file) throws Exception {
		File xlsxFile = file;
		XLSX2CSV read = new XLSX2CSV();
		read.setOnlyReadFirstRow(true);
		List<String[]> list = read.readExcel07Bycsv(xlsxFile);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static String[] getFirstRowFromXls(File file) throws Exception {
		XLS2CSV xls2csv = new XLS2CSV(file);
		xls2csv.setOnlyReadFirstRow(true);
		xls2csv.process();
		List<String[]> list = xls2csv.getStringList();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static String[] getFirstRowFromTxt(File file) throws Exception {
		BufferedReader reader = null;
		String temp = null;
		String[] titles = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
			while ((temp = reader.readLine()) != null) {
				titles = temp.split(",");
				if (null != titles) {
					return titles;
				}
			}
			return titles;
		} catch (Exception e) {
			e.printStackTrace();
			return titles;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
