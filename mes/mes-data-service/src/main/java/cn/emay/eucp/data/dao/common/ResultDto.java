package cn.emay.eucp.data.dao.common;

import java.lang.reflect.Method;
import java.util.List;

public class ResultDto {

	private String sql;

	private List<Method> methods;

	public ResultDto() {

	}

	public ResultDto(String sql, List<Method> methods) {
		super();
		this.sql = sql;
		this.setMethods(methods);
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
