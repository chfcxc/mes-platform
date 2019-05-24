package cn.emay.eucp.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SqlFileExecutorUtil {

	public static Map<String, List<String>> loadSql(String sqlFile, String[] listTableName) throws Exception {
		Map<String, List<String>> hashSql = new HashMap<String, List<String>>();
		InputStream sqlFileIn = null;
		try {
			sqlFileIn = SqlFileExecutorUtil.class.getClassLoader().getResourceAsStream(sqlFile);

			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
			byte tmp[] = new byte[99999];
			byte context[];
			while (sqlFileIn.read(tmp) != -1) {
				byteout.write(tmp);
			}
			context = byteout.toByteArray();
			String sqls = new String(context, "UTF-8");
			String[] sqlArr = sqls.split("(;\\s*\\r\\n)|(;\\s*\\n)");
			for (int i = 0; i < sqlArr.length; i++) {
				String sql = sqlArr[i].replaceAll("--.*", "").trim();
				if (!"".equals(sql)) {
					for (String tableName : listTableName) {
						if (sql.toUpperCase().contains(tableName.toUpperCase())) {
							if (hashSql.containsKey(tableName)) {
								List<String> list = hashSql.get(tableName);
								list.add(sql);
							} else {
								List<String> sqlList = new ArrayList<String>();
								sqlList.add(sql);
								hashSql.put(tableName, sqlList);
							}
							break;
						}
					}
				}
			}
			return hashSql;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		} finally {
			if (null != sqlFileIn) {
				sqlFileIn.close();
			}
		}
	}
}