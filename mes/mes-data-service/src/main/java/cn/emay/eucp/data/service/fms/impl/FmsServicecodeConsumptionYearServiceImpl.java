package cn.emay.eucp.data.service.fms.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionYearDao;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionYearService;

@Service("fmsServicecodeConsumptionYearService")
public class FmsServicecodeConsumptionYearServiceImpl implements FmsServicecodeConsumptionYearService {
	@Resource
	private FmsServicecodeConsumptionYearDao fmsServicecodeConsumptionYearDao;

	@Override
	public void delete(String year) {
		fmsServicecodeConsumptionYearDao.delete(year);

	}

	@Override
	public void bachSave(List<FmsServicecodeConsumptionYear> insertList) {
		fmsServicecodeConsumptionYearDao.bachSave(insertList);
	}

}
