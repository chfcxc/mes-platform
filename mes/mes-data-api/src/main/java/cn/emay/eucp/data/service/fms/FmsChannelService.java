package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.channel.FmsChannelDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;

public interface FmsChannelService {

	FmsChannel findById(Long id);

	Page<FmsChannelDTO> findPage(String channeName, String channelNumber, int state, int start, int limit, Long businessId);

	List<FmsChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	FmsChannel findIdandState(Long id, String controlType);

	void updateChannelState(Long id, int state);

	Long isExist(Long id, String channeName);

	Result addChannnel(FmsChannel fmsChannel, String channelParams);

	Result update(FmsChannel fmsChannel, String channelParams);

	List<FmsChannel> findAll(int state);
}
