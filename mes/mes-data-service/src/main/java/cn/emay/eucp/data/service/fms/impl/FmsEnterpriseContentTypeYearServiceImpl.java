package cn.emay.eucp.data.service.fms.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeYearDao;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeYearService;

@Service("fmsEnterpriseContentTypeYearService")
public class FmsEnterpriseContentTypeYearServiceImpl implements FmsEnterpriseContentTypeYearService {
	@Resource
	private FmsEnterpriseContentTypeYearDao fmsEnterpriseContentTypeYearDao;

	@Override
	public void deleteYear(String year) {
		fmsEnterpriseContentTypeYearDao.deleteYear(year);
	}

	@Override
	public void saveList(List<FmsEnterpriseContentTypeYear> list) {
		fmsEnterpriseContentTypeYearDao.saveBatch(list);
	}

}
