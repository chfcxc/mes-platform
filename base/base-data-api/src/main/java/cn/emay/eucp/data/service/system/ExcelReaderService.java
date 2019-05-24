package cn.emay.eucp.data.service.system;

import java.util.List;

public interface ExcelReaderService {

	public int saveBatchPreparedStatement(String sql, List<Object[]> batchArgs);

	public int saveBatchPreparedStatementForSign(String sql, List<Object[]> batchArgs);

	public int[] saveBatchPreparedStatement2(String sql, List<Object[]> batchArgs);

}
