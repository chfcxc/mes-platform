package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;

public interface FmsChannelInfoService {

	public List<FmsChannelInfo> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	List<FmsChannelInfo> findByFmsChannelId(Long channelId);

}
