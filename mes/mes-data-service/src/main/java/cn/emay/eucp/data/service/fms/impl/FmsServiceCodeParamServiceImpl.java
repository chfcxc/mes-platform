package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeParamDao;
import cn.emay.eucp.data.service.fms.FmsServiceCodeParamService;

@Service("fmsServiceCodeParamService")
public class FmsServiceCodeParamServiceImpl implements FmsServiceCodeParamService {
	@Resource
	private FmsServiceCodeParamDao fmsServiceCodeParamDao;

	@Override
	public Map<String, String> findbyAppid(List<String> list) {
		List<FmsServiceCodeParam> appid = fmsServiceCodeParamDao.findByAppid(list);
		Map<String, String> map = new HashMap<String, String>();
		if (null != appid) {
			for (FmsServiceCodeParam fmsServiceCodeParam : appid) {
				map.put(fmsServiceCodeParam.getAppId(), fmsServiceCodeParam.getIpConfiguration());
			}
		}
		return map;
	}

	@Override
	public List<FmsServiceCodeParam> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsServiceCodeParamDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

}
