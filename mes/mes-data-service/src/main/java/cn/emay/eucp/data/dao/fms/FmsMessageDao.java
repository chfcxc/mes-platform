package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsMessagePageDto;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsMessageDao extends BaseSuperDao<FmsMessage> {

	void saveBatchByBeans(List<FmsMessage> beans);

	void updateFmsResponse(List<UpdateFmsDTO> updateFmsDTOs);

	void updateFmsReport(List<UpdateFmsDTO> updateFmsDTOs);

	public Page<FmsMessagePageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, List<Long> contentIds, int start, int limit, Date startTime,
			Date endTime, Set<Long> set, int operator);

	public List<FmsServicecodeConsumptionDay> findConsumptionReport(String day, int currentPage, int pageSize);

	public List<FmsEnterpriseContentTypeDay> findContentTypeReport(String day, int start, int limit);
}
