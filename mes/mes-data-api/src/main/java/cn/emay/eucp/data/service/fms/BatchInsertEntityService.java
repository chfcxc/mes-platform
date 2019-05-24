package cn.emay.eucp.data.service.fms;

import java.util.List;

public interface BatchInsertEntityService {

	public void saveBatchList(List<?> beanList, String dbTableName, boolean isIgnore, boolean useId);

}
