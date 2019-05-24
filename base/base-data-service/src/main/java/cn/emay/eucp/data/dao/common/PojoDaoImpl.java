package cn.emay.eucp.data.dao.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.emay.common.db.Page;
import cn.emay.common.db.PojoDaoSupport;

/**
 * 针对单一pojo dao
 *
 * @author Frank
 *
 * @param <E>
 */
public class PojoDaoImpl<E extends java.io.Serializable> extends PojoDaoSupport<E> {

	@Resource
	protected JdbcTemplate jdbcTemplate;
	@Resource
	protected HibernateTemplate hibernateTemplate;
	@Resource
	protected SessionFactory sessionFactory;

	@Override
	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	protected HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 返回分页list DTO对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findSqlForPageForMysql(Class<T> clazz, String sql, List<Object> parameters, int start, int limit) {
		return this.findSqlForPageForMysql(sql, parameters, start, limit, new BeanPropertyRowMapper<T>(clazz));
	}

	public Page<Map<String, Object>> findSqlForPageMapForMysql(String sql, List<Object> parameters, int start, int limit) {
		return this.findSqlForPageForMysql(sql, parameters, start, limit, getColumnMapRowMapper());
	}

	/**
	 * 按页获取数据
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> List<T> findSqlForListForMysql(Class<T> clazz, String sql, List<Object> parameters, int currentPage, int pageSize) {
		StringBuffer querySql = new StringBuffer(sql);
		if (pageSize > 0) {
			int start = (currentPage - 1) * pageSize;
			querySql.append(" LIMIT " + start + "," + pageSize + "");
		}
		List<T> list = findSqlForListObj(querySql.toString(), parameters, new BeanPropertyRowMapper<T>(clazz));
		return list;
	}

	/**
	 * 返回分页list DTO对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findSqlForPageForMysql(String sql, List<Object> parameters, int start, int limit, RowMapper<T> rowMapper) {
		Integer totalCount = queryTotalCount(sql, parameters);
		StringBuffer querySql = new StringBuffer(sql);
		querySql.append(" LIMIT " + start + "," + limit + "");
		List<T> list = findSqlForListObj(querySql.toString(), parameters, rowMapper);
		Page<T> page = new Page<T>();
		page.setList(list);
		page.setNumByStartAndLimit(start, limit, totalCount);
		return page;
	}

	/**
	 * 返回List dto
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> List<T> findSqlForListObj(String sql, List<Object> parameters, RowMapper<T> rowMapper) {
		Object[] args = params(parameters);
		List<T> list = jdbcTemplate.query(sql, args, rowMapper);
		return list;
	}

	public <T> List<T> findSqlForListObj(Class<T> clazz, String sql, List<Object> parameters) {
		return this.findSqlForListObj(sql, parameters, new BeanPropertyRowMapper<T>(clazz));
	}

	/**
	 * 返回单个对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> T findSqlForObj(Class<T> clazz, String sql, List<Object> parameters) {
		Object[] args = params(parameters);
		BeanPropertyRowMapper<T> argTypes = new BeanPropertyRowMapper<T>(clazz);
		T t = jdbcTemplate.queryForObject(sql, argTypes, args);
		return t;
	}

	/**
	 * 返回单个对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> List<T> findSqlForSingleColumn(String sql, List<Object> parameters) {
		Object[] args = params(parameters);
		SingleColumnRowMapper<T> singleColumnRowMapper = new SingleColumnRowMapper<T>();
		List<T> t = jdbcTemplate.query(sql, singleColumnRowMapper, args);
		return t;
	}

	private Object[] params(List<Object> parameters) {
		if (null == parameters) {
			parameters = new ArrayList<Object>();
		}
		Object[] args = parameters.toArray();
		return args;
	}

	private Integer queryTotalCount(String sql, List<Object> parameters) {
		// 寻找from
		int fromindex = sql.toLowerCase().indexOf(" from ");
		if (fromindex < 0) {
			throw new RuntimeException("sql" + " has no from");
		}
		// 判断是否能截取最后的order
		boolean isHasOrder = false;
		int orderbyindex = sql.toLowerCase().indexOf(" order ");
		if (orderbyindex > 0) {
			isHasOrder = !sql.toLowerCase().substring(orderbyindex).contains(")");
		}
		// 截取order
		String countsql = sql;
		if (isHasOrder) {
			orderbyindex = countsql.toLowerCase().indexOf(" order ");
			countsql = countsql.substring(0, orderbyindex);
		}
		// 拼接SQL
		countsql = "select count(*)  from ( " + countsql + " ) total ";
		// System.out.println("PojoDaoImpl.class selct count sql is : " + countsql);
		Object[] args = params(parameters);
		Integer totalCount = jdbcTemplate.queryForObject(countsql, args, Integer.class);
		return totalCount;
	}

	private RowMapper<Map<String, Object>> getColumnMapRowMapper() {
		return new ColumnMapRowMapper();
	}

	/**
	 * 返回分页list DTO对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findSqlForPageForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, Class<T> clazz, String sql, List<Object> parameters, int start, int limit) {
		return this.findSqlForPageForOracle(v8smsBusinessjdbcTemplate, sql, parameters, start, limit, new BeanPropertyRowMapper<T>(clazz));
	}

	/**
	 * 返回分页list DTO对象
	 * 
	 * @return
	 */
	private <T> Page<T> findSqlForPageForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, String sql, List<Object> parameters, int start, int end, RowMapper<T> rowMapper) {
		Integer totalCount = queryTotalCountForOracle(v8smsBusinessjdbcTemplate, sql, parameters);
		String querySql = "SELECT * " + "  FROM (select ROWNUM rn, A.* from (" + sql + ")  A where  ROWNUM <= " + end + ") " + " WHERE RN >= " + start;
		List<T> list = findSqlForListObjForOralce(v8smsBusinessjdbcTemplate, querySql.toString(), parameters, rowMapper);
		Page<T> page = new Page<T>();
		page.setList(list);
		page.setNumByStartAndLimit(start, end - start, totalCount);
		return page;
	}

	/**
	 * 返回分页list DTO对象(适配系统start,limit)
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findSqlForPageForOracleV8(JdbcTemplate v8smsBusinessjdbcTemplate, Class<T> clazz, String sql, List<Object> parameters, int start, int limit) {
		return this.findSqlForPageForOracleV8(v8smsBusinessjdbcTemplate, sql, parameters, start, limit, new BeanPropertyRowMapper<T>(clazz));
	}

	/**
	 * 返回分页list DTO对象(适配系统start,limit)
	 * 
	 * @return
	 */
	private <T> Page<T> findSqlForPageForOracleV8(JdbcTemplate v8smsBusinessjdbcTemplate, String sql, List<Object> parameters, int start, int limit, RowMapper<T> rowMapper) {
		Integer totalCount = queryTotalCountForOracle(v8smsBusinessjdbcTemplate, sql, parameters);
		int end = start + limit;
		String querySql = "SELECT * " + "  FROM (select ROWNUM rn, A.* from (" + sql + ")  A where  ROWNUM <= " + end + ") " + " WHERE RN > " + start;
		List<T> list = findSqlForListObjForOralce(v8smsBusinessjdbcTemplate, querySql.toString(), parameters, rowMapper);
		Page<T> page = new Page<T>();
		page.setList(list);
		page.setNumByStartAndLimit(start, end - start, totalCount);
		return page;
	}

	// todo
	private Integer queryTotalCountForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, String sql, List<Object> parameters) {
		// 寻找from
		int fromindex = sql.toLowerCase().indexOf(" from ");
		if (fromindex < 0) {
			throw new RuntimeException("sql" + " has no from");
		}
		// 判断是否能截取最后的order
		boolean isHasOrder = false;
		int orderbyindex = sql.toLowerCase().indexOf(" order ");
		if (orderbyindex > 0) {
			isHasOrder = !sql.toLowerCase().substring(orderbyindex).contains(")");
		}
		// 截取order
		String countsql = sql;
		if (isHasOrder) {
			orderbyindex = countsql.toLowerCase().indexOf(" order ");
			countsql = countsql.substring(0, orderbyindex);
		}
		// 拼接SQL
		countsql = "select count(*)  from ( " + countsql + " ) total ";
		// System.out.println("PojoDaoImpl.class selct count sql is : " + countsql);
		Object[] args = params(parameters);
		Integer totalCount = v8smsBusinessjdbcTemplate.queryForObject(countsql, args, Integer.class);
		return totalCount;
	}

	/**
	 * oracle按页获取数据
	 *
	 */
	public <T> List<T> findSqlForListForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, Class<T> clazz, String sql, List<Object> parameters, int start, int end) {
		String querySql = "SELECT * " + "  FROM (select ROWNUM rn, A.* from (" + sql + ")  A where  ROWNUM <= " + end + ") " + " WHERE RN >= " + start;
		Object[] args = params(parameters);
		List<T> list = v8smsBusinessjdbcTemplate.query(querySql, args, new BeanPropertyRowMapper<T>(clazz));
		return list;
	}

	/**
	 * oracle返回List dto
	 */
	public <T> List<T> findSqlForListObjForOralce(JdbcTemplate v8smsBusinessjdbcTemplate, String sql, List<Object> parameters, RowMapper<T> rowMapper) {
		Object[] args = params(parameters);
		List<T> list = v8smsBusinessjdbcTemplate.query(sql, args, rowMapper);
		return list;
	}

	public <T> List<T> findSqlForListObjForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, Class<T> clazz, String sql, List<Object> parameters) {
		return this.findSqlForListObjForOralce(v8smsBusinessjdbcTemplate, sql, parameters, new BeanPropertyRowMapper<T>(clazz));
	}

	/**
	 * 返回单个对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> T findSqlForObjForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, Class<T> clazz, String sql, List<Object> parameters) {
		Object[] args = params(parameters);
		BeanPropertyRowMapper<T> argTypes = new BeanPropertyRowMapper<T>(clazz);
		T t = v8smsBusinessjdbcTemplate.queryForObject(sql, argTypes, args);
		return t;
	}

	/**
	 * 返回单个对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> List<T> findSqlForSingleColumnForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, String sql, List<Object> parameters) {
		Object[] args = params(parameters);
		SingleColumnRowMapper<T> singleColumnRowMapper = new SingleColumnRowMapper<T>();
		List<T> t = v8smsBusinessjdbcTemplate.query(sql, singleColumnRowMapper, args);
		return t;
	}

	private static String fileToConvert(String fileName) {
		int fileNameLength = fileName.length();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < fileNameLength; i++) {
			char c = fileName.charAt(i);
			if (Character.isUpperCase(c)) {
				buff.append("_");
			}
			buff.append(c);
		}
		return buff.toString().toUpperCase();
	}

	private static String classNameToDbTableName(String fileName) {
		int fileNameLength = fileName.length();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < fileNameLength; i++) {
			char c = fileName.charAt(i);
			if (Character.isUpperCase(c) && i > 1) {
				buff.append("_");
			}
			buff.append(c);
		}
		return buff.toString().toUpperCase();
	}

	/**
	 * @param clazz
	 * @param tableName
	 * @param useId
	 *            是否跳过ID字段
	 * @return
	 */
	private static <T> String transBean2Sql(T clazz, Boolean useId) {
		if (null == clazz)
			return null;
		List<String> tableColumns = new ArrayList<String>();
		List<String> modelColumns = new ArrayList<String>();
		String simpleName = clazz.getClass().getSimpleName();
		String dbTableName = classNameToDbTableName(simpleName);
		try {
			String fileToConvert = "";
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz.getClass());

			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String filedName = property.getName();
				if (!filedName.equals("class")) {
					if ("id".equals(filedName) && useId) {
						continue;
					}
					fileToConvert = fileToConvert(filedName); // 处理大小写字段
					modelColumns.add(":" + filedName);
					tableColumns.add(fileToConvert);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer buff = new StringBuffer();
		buff.append("insert into ").append(dbTableName).append(" ");
		buff.append("(").append(StringUtils.join(tableColumns, ",")).append(")");
		buff.append(" values ");
		buff.append("(").append(StringUtils.join(modelColumns, ",")).append(")");
		return buff.toString();
	}

	/**
	 * nameJdbcBatchExec(asList, "table_info", jdbcTemplate, false);
	 *
	 * @param listAll
	 * @param jdbcTemplate
	 * @param useId
	 */
	public <T> void nameJdbcBatchExec(List<T> listAll, JdbcTemplate jdbcTemplate, boolean useId) {
		if (listAll.size() == 0) {
			throw new IllegalStateException("list is empty. ");
		}
		String sql = transBean2Sql(listAll.get(0), useId);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<SqlParameterSource> insertParam = new ArrayList<SqlParameterSource>();
		for (int i = 0; i < listAll.size(); i++) {
			T t = listAll.get(i);
			SqlParameterSource sps = new BeanPropertySqlParameterSource(t);
			insertParam.add(sps);
		}
		BeanPropertySqlParameterSource[] array = insertParam.toArray(new BeanPropertySqlParameterSource[] {});
		namedParameterJdbcTemplate.batchUpdate(sql, array);
	}
}
