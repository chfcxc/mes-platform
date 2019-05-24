package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Industry;
import cn.emay.eucp.data.dao.system.IndustryDao;
import cn.emay.eucp.data.service.system.IndustryService;

@Service("industryService")
public class IndustryServiceImpl implements IndustryService {

	@Resource
	private IndustryDao industryDao;
	
	@Override
	public Page<Industry> findPage (String industry,int start,int limit){
		return industryDao.findPage(industry, start, limit);
	}
	
	@Override
	public Boolean isExist(String industry,Long id) {
		Industry entity = industryDao.getIndustry(industry, id);
		if(entity == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public Result addIndustry(String industry) {
		Industry entity = new Industry();
		entity.setIndustry(industry);
		entity.setIsDelete(false);
		entity.setCreateTime(new Date());
		industryDao.save(entity);
		return Result.rightResult();
	}
	
	@Override
	public Result modifyIndustry(Long id,String industry) {
		Industry entity = industryDao.findById(id);
		if(entity == null || entity.getIsDelete()) {
			return Result.badResult("数据不存在");
		}
		entity.setIndustry(industry);
		industryDao.update(entity);
		return Result.rightResult();
	}
	
	@Override
	public Result deleteIndustry(Long id) {
		Industry entity = industryDao.findById(id);
		if(entity == null || entity.getIsDelete()) {
			return Result.badResult("数据不存在");
		}
		entity.setIsDelete(true);
		industryDao.update(entity);
		return Result.rightResult(entity.getIndustry());
	}
	
	@Override
	public List<Industry> findAllIndustry(){
		return industryDao.findAllIndustry();
	}
	
	@Override
	public Industry findById(Long id) {
		return industryDao.findById(id);
	}

	@Override
	public List<Industry> findList(int currentPage, int pageSize, Date lastUpdateTime) {
		return industryDao.findList(currentPage, pageSize, lastUpdateTime);
	}
}
