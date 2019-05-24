package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.channel.FmsChannelDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsChannelDao extends BaseSuperDao<FmsChannel> {

	Page<FmsChannelDTO> findPage(String channeName, String channelNumber, int state, int start, int limit, Long businessId);

	List<FmsChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	FmsChannel findIdandState(Long id, String controlType);

	void updateChannelState(Long id, int state);

	Long isExist(Long id, String channeName);

	public List<FmsChannel> findAll(int state);
}
