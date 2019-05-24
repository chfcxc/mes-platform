package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsBatchPageDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBatch;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsBatchDao extends BaseSuperDao<FmsBatch> {

	public Page<FmsBatchPageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, List<Long> contentIds, int start, int limit, Date startTime,
			Date endTime, Set<Long> set);

	public FmsBatch findBySerialNumber(String serialNumber);
}
