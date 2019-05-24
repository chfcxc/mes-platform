package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.data.dao.system.BusinessDao;
import cn.emay.eucp.data.service.system.BusinessService;

@Service("businessService")
public class BusinessServiceImpl implements BusinessService {

	@Resource
	private BusinessDao businessDao;
	@Override
	public List<Business> findAllBusiness() {
		return businessDao.findAllBusiness();
	}
	
	@Override
	public List<String> findBusinessCodeByIds(String ids){
		if(StringUtils.isEmpty(ids)){
			return null;
		}
		List<Long> idList = new ArrayList<Long>();
		String[] idArr = ids.split(",");
		for(String id :idArr){
			idList.add(Long.valueOf(id));
		}
		
		List<String> codeList = new ArrayList<String>();
		List<Business> list = businessDao.findByIds(idList);
		for(Business business :list){
			codeList.add(business.getBusinessCode());
		}
		return codeList;
	}

	@Override
	public List<Business> findUserOpenBusinessList(String ids) {
		List<Long> idList = parseIds(ids);
		if (idList == null || idList.isEmpty()) {
			return null;
		}
		return businessDao.findByIds(idList);
	}

	private List<Long> parseIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return null;
		}
		List<Long> idList = new ArrayList<Long>();
		String[] idArr = ids.split(",");
		for (String id : idArr) {
			if (StringUtils.isEmpty(ids)) {
				continue;
			}
			idList.add(Long.valueOf(id));
		}
		return idList;
	}

}
