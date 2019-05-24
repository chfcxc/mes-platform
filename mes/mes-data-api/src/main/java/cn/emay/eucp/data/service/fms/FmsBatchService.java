package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsBatchPageDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBatch;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午3:49:44 类说明 */
public interface FmsBatchService {

	public Long saveBatch(FmsBatch fmsBatch);

	public Page<FmsBatchPageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, Long businessTypeId, int saveType, Long contentTypeId, int start,
			int limit, Date startTime, Date endTime, List<Long> userIds);

	public FmsBatch findBySerialNumber(String serialNumber);

	public void update(FmsBatch entity);

}
