package cn.emay.eucp.data.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.emay.eucp.data.service.system.ExcelReaderService;

@Service("baseExcelReaderService")
public class ExcelReaderServiceImpl implements ExcelReaderService {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public int saveBatchPreparedStatement(String sql, List<Object[]> batchArgs) {
		int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);
		int count = 0;
		if (null != batchUpdate && batchUpdate.length > 0) {
			for (int s : batchUpdate) {
				if (1 == s) {
					++count;
				}
			}
		}
		return count;
	}

	@Override
	public int saveBatchPreparedStatementForSign(String sql, List<Object[]> batchArgs) {
		return jdbcTemplate.batchUpdate(sql + " ON DUPLICATE KEY UPDATE SIGN=VALUES(SIGN)", batchArgs).length;
	}

	@Override
	public int[] saveBatchPreparedStatement2(String sql, List<Object[]> batchArgs) {
		int[] batchUpdate = jdbcTemplate.batchUpdate(sql, batchArgs);
		return batchUpdate;
	}
}
