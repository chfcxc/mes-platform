package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeChannelDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsServiceCodeChannelDao extends BaseSuperDao<FmsServiceCodeChannel> {

	List<FmsServiceCodeChannelDto> findChannelByAppIds(String... appIds);

	List<FmsServiceCodeChannel> getListByServiceCodeId(Long serviceCodeId);

	void deletebyServiceId(Long serviceCodeId);

	void saveServiceCodeChannel(List<FmsServiceCodeChannel> list);

	List<FmsServiceCodeChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	List<FmsServiceCodeChannelDto> findBindingChannel(Long serviceCodeId);

	FmsServiceCodeChannel findFmsServiceCodeChannel(String appId, int templateType, String operatorCode);

}
