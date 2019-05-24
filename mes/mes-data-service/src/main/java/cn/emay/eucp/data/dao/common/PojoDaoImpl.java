package cn.emay.eucp.data.dao.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
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
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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
	public <T> T findSqlForObjForOracle(JdbcTemplate jdbcTemplate, Class<T> clazz, String sql, List<Object> parameters) {
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
	public <T> List<T> findSqlForSingleColumnForOracle(JdbcTemplate v8smsBusinessjdbcTemplate, String sql, List<Object> parameters) {
		Object[] args = params(parameters);
		SingleColumnRowMapper<T> singleColumnRowMapper = new SingleColumnRowMapper<T>();
		List<T> t = v8smsBusinessjdbcTemplate.query(sql, singleColumnRowMapper, args);
		return t;
	}

	/**
	 * @Title: transBean2Sql
	 * @Description: 实体转换sql :name格式
	 * @param @param clazz
	 * @param @param useId 是否id入库
	 * @param @return
	 * @return String
	 */
	private static <T> String transBean2Sql(T clazz, String dbTableName, boolean isIgnore, Boolean useId) {
		if (null == clazz) {
			return null;
		}
		List<String> tableColumns = new ArrayList<String>();
		List<String> modelColumns = new ArrayList<String>();
		String simpleName = clazz.getClass().getSimpleName();
		if (StringUtils.isEmpty(dbTableName)) {
			dbTableName = classNameToDbTableName(simpleName);
		}
		try {
			String fileToConvert = "";
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String filedName = property.getName();
				if (!"class".equals(filedName)) {
					if ("id".equals(filedName) && !useId) {
						continue;
					}
					// 处理大小写字段
					fileToConvert = fileToConvert(filedName);
					modelColumns.add(":" + filedName);
					tableColumns.add(fileToConvert);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer buff = new StringBuffer();
		if (isIgnore) {
			buff.append("insert ignore into ");
		} else {
			buff.append("insert into ");
		}
		buff.append(dbTableName).append(" ");
		buff.append("(").append(StringUtils.join(tableColumns, ",")).append(")");
		buff.append(" values ");
		buff.append("(").append(StringUtils.join(modelColumns, ",")).append(")");
		return buff.toString();
	}

	/**
	 * @Title: nameJdbcBatchExec
	 * @Description: 实体批量入库
	 * @param @param beanList
	 * @param @param jdbcTemplate
	 * @param @param useId
	 * @return void
	 */
	public <T> void nameJdbcBatchExec(List<T> beanList, JdbcTemplate jdbcTemplate, String dbTableName, Boolean isIgnore, boolean useId) {
		if (beanList.size() == 0) {
			throw new IllegalStateException("list is empty. ");
		}
		String sql = transBean2Sql(beanList.get(0), dbTableName, isIgnore, useId);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(beanList.toArray());
		namedParameterJdbcTemplate.batchUpdate(sql, params);
	}

	public <T> void nameJdbcBatchExec(List<T> beanList, NamedParameterJdbcTemplate namedParameterJdbcTemplate, String dbTableName, Boolean isIgnore, boolean useId) {
		if (beanList.size() == 0) {
			throw new IllegalStateException("list is empty. ");
		}
		String sql = transBean2Sql(beanList.get(0), dbTableName, isIgnore, useId);
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(beanList.toArray());
		namedParameterJdbcTemplate.batchUpdate(sql, params);
	}

	private static <T> ResultDto transBean2Sql2ForBatchPrepared(T clazz, String dbTableName, Boolean isIgnore, Boolean useId) {
		if (null == clazz) {
			return null;
		}
		List<String> tableColumns = new ArrayList<String>();
		List<String> modelColumns = new ArrayList<String>();
		String simpleName = clazz.getClass().getSimpleName();
		if (StringUtils.isEmpty(dbTableName)) {
			dbTableName = classNameToDbTableName(simpleName);
		}
		List<Method> methods = new ArrayList<Method>();
		try {
			String fileToConvert = "";
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String filedName = property.getName();
				if (!"class".equals(filedName)) {
					if ("id".equals(filedName) && !useId) {
						continue;
					}
					// 处理大小写字段
					fileToConvert = fileToConvert(filedName);
					modelColumns.add("?");
					tableColumns.add(fileToConvert);
				}
				Method method = property.getReadMethod();
				if (!"getClass".equals(method.getName())) {
					methods.add(method);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer buff = new StringBuffer();
		if (isIgnore) {
			buff.append("insert ignore into ");
		} else {
			buff.append("insert into ");
		}
		buff.append(dbTableName).append(" ");
		buff.append("(").append(StringUtils.join(tableColumns, ",")).append(")");
		buff.append(" values ");
		buff.append("(").append(StringUtils.join(modelColumns, ",")).append(")");
		return new ResultDto(buff.toString(), methods);
	}

	public <T> void jdbcBatchExec(final List<T> beanList, JdbcTemplate jdbcTemplate, String dbTableName, Boolean isIgnore, boolean useId) {
		if (beanList.size() == 0) {
			throw new IllegalStateException("list is empty. ");
		}
		ResultDto resultDto = transBean2Sql2ForBatchPrepared(beanList.get(0), dbTableName, isIgnore, useId);
		final String sql = resultDto.getSql();
		final List<Method> methods = resultDto.getMethods();
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Object entity = beanList.get(i);
				int j = 1;
				for (Method method : methods) {
					try {
						Object object = method.invoke(entity);
						String methodName = method.getReturnType().getSimpleName();
						if ("string".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setString(j, (String) object);
							} else {
								ps.setNull(j, Types.VARCHAR);
							}
						} else if ("integer".equalsIgnoreCase(methodName) || "int".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setInt(j, (Integer) object);
							} else {
								ps.setNull(j, Types.INTEGER);
							}
						} else if ("long".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setLong(j, (Long) object);
							} else {
								ps.setNull(j, Types.BIGINT);
							}
						} else if ("double".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setLong(j, (Long) object);
							} else {
								ps.setNull(j, Types.DECIMAL);
							}
						} else if ("BigDecimal".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setBigDecimal(j, (BigDecimal) object);
							} else {
								ps.setNull(j, Types.DECIMAL);
							}
						} else if ("date".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setTimestamp(j, new Timestamp(((Date) object).getTime()));
							} else {
								ps.setNull(j, Types.TIMESTAMP);
							}
						} else if ("boolean".equalsIgnoreCase(methodName)) {
							if (null != object) {
								ps.setBoolean(j, (Boolean) object);
							} else {
								ps.setNull(j, Types.BOOLEAN);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					j++;
				}
			}

			@Override
			public int getBatchSize() {
				return beanList.size();
			}
		});
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

}
