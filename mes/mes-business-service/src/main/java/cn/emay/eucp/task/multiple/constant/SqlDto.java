package cn.emay.eucp.task.multiple.constant;

import java.io.Serializable;

public class SqlDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sql;
	private Object[] args;

	public SqlDto() {
		super();
	}

	public SqlDto(String sql, Object[] args) {
		super();
		this.sql = sql;
		this.args = args;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}
