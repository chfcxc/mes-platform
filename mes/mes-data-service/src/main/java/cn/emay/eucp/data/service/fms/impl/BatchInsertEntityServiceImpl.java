package cn.emay.eucp.data.service.fms.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.data.dao.fms.BatchInsertEntityDao;
import cn.emay.eucp.data.service.fms.BatchInsertEntityService;

@Service("batchInsertEntityService")
public class BatchInsertEntityServiceImpl implements BatchInsertEntityService {

	@Resource
	private BatchInsertEntityDao batchInsertEntityDao;

	@Override
	public void saveBatchList(List<?> beanList, String dbTableName, boolean isIgnore, boolean useId) {
		batchInsertEntityDao.saveBatchList(beanList, dbTableName, isIgnore, useId);
	}

}
