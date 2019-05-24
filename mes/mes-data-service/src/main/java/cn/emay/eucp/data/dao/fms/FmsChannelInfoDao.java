package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsChannelInfoDao extends BaseSuperDao<FmsChannelInfo> {
	List<FmsChannelInfo> findByFmsChannelId(Long channelId);

	void deleteByChannelId(Long channelId);

	List<FmsChannelInfo> findByLastUpdateTime(Date date, int currentPage, int pageSize);

}
