package cn.emay.eucp.data.dao.fms;

import java.util.List;

public interface BatchInsertEntityDao {

	public void saveBatchList(List<?> beanList, String dbTableName, boolean isIgnore, boolean useId);

}
