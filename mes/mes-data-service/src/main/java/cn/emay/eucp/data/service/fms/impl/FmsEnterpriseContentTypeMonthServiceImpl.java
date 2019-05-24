package cn.emay.eucp.data.service.fms.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeMonthDao;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeMonthService;

@Service("fmsEnterpriseContentTypeMonthService")
public class FmsEnterpriseContentTypeMonthServiceImpl implements FmsEnterpriseContentTypeMonthService {
	@Resource
	private FmsEnterpriseContentTypeMonthDao fmsEnterpriseContentTypeMonthDao;

	@Override
	public void deleteMonth(String month) {
		fmsEnterpriseContentTypeMonthDao.deleteMonth(month);
	}

	@Override
	public void saveList(List<FmsEnterpriseContentTypeMonth> list) {
		fmsEnterpriseContentTypeMonthDao.saveList(list);
	}

	@Override
	public List<FmsEnterpriseContentTypeYear> findYear(String year, int start, int limit) {
		return fmsEnterpriseContentTypeMonthDao.findYear(year, start, limit);
	}

}
