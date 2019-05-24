package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Foa;
import cn.emay.eucp.data.dao.system.FoaDao;
import cn.emay.eucp.data.service.system.FoaService;

@Service("foaService")
public class FoaServiceImpl implements FoaService {
	@Resource
	private FoaDao foaDao;

	@Override
	public List<Foa> findBySystemType(Integer subSystem,Integer businessType) {
		return foaDao.findBySystemType(subSystem,businessType);
	}

	@Override
	public List<Foa> findAllList() {
		return foaDao.findAllList();
	}

	@Override
	public int[] saveBatchFoa(List<Object[]> params) {
		return foaDao.saveBatchFoa(params);
	}

	@Override
	public List<Foa> findListByDescProblem(String desc) {
		return  foaDao.findListByDescProblem(desc);
	}

	@Override
	public void saveFoa(Integer subSystem,Integer businessType,String  descProblem,String  reply) {
		Foa  foa=new Foa();
		Date date = new Date();
		foa.setSubSystem(subSystem);
		foa.setBusinessType(businessType);
		foa.setDescProblem(descProblem);
		foa.setReply(reply);
		foa.setCreateTime(date);
		foaDao.save(foa);
	}

	@Override
	public Page<Foa> selectFoa(String desc, Integer start, Integer limit) {
		return foaDao.selectFoa(desc, start, limit);
	}

	public Foa findById(Long id) {
		return foaDao.findById(id);
	}

	@Override
	public void updateFoa(Long id, Integer subSystem, Integer businessType, String descProblem,String reply) {
		Foa fqa = this.foaDao.findById(id);
		fqa.setBusinessType(businessType);
		fqa.setDescProblem(descProblem);
		fqa.setReply(reply);
		fqa.setSubSystem(subSystem);
		foaDao.update(fqa);
	}
}
