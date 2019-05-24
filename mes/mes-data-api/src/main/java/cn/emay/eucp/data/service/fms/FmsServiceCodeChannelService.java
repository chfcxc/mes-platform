package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeChannelDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;

public interface FmsServiceCodeChannelService {

	void add(FmsServiceCodeChannel entity);

	void update(FmsServiceCodeChannel entity);

	Map<String, String> getListByServiceCodeId(Long serviceCodeId);

	List<FmsServiceCodeChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	List<FmsServiceCodeChannelDto> findBindingChannel(Long serviceCodeId);

}
