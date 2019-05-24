package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsMessagePageDto;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;

/**
 * @author dejun
 * @version 创建时间：2019年4月26日 下午3:49:44 类说明
 */
public interface FmsMessageService {

	void saveBatchByBeans(List<FmsMessage> beans);

	void updateFmsResponse(List<UpdateFmsDTO> updateFmsDTOs);

	void updateFmsReport(List<UpdateFmsDTO> updateFmsDTOs);

	public Page<FmsMessagePageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, Long businessTypeId, int saveType, Long contentTypeId,
			int start, int limit, Date startTime, Date endTime, List<Long> userIds, int operator);

	List<FmsServicecodeConsumptionDay> findConsumptionReport(String day, Date d, int currentPage, int pageSize);

	List<FmsEnterpriseContentTypeDay> findContentTypeReport(String day, Date d, int currentPage, int pageSize);
}
