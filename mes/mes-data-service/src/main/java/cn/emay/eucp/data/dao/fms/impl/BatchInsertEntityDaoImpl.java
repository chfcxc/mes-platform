package cn.emay.eucp.data.dao.fms.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.BatchInsertEntityDao;

@Repository
public class BatchInsertEntityDaoImpl extends PojoDaoImpl<String> implements BatchInsertEntityDao {

	@Override
	public void saveBatchList(List<?> beanList, String dbTableName, boolean isIgnore, boolean useId) {
		this.nameJdbcBatchExec(beanList, namedParameterJdbcTemplate, dbTableName, isIgnore, useId);
	}

}
