package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.eucp.data.dao.fms.FmsChannelInfoDao;
import cn.emay.eucp.data.service.fms.FmsChannelInfoService;

@Service("fmsChannelInfoService")
public class FmsChannelInfoServiceImpl implements FmsChannelInfoService {
	@Resource
	private FmsChannelInfoDao fmsChannelInfoDao;

	@Override
	public List<FmsChannelInfo> findByFmsChannelId(Long channelId) {
		return fmsChannelInfoDao.findByFmsChannelId(channelId);
	}

	@Override
	public List<FmsChannelInfo> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsChannelInfoDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

}
