package cn.emay.eucp.data.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;
import cn.emay.eucp.data.dao.system.ContactGroupAssignDao;
import cn.emay.eucp.data.service.system.ContactGroupAssignService;

@Service("contactGroupAssignService")
public class ContactGroupAssignServiceImpl implements ContactGroupAssignService {

	@Resource
	private ContactGroupAssignDao contactGroupAssignDao;
	
	@Override
	public Boolean isContactExist(Long groupId) {
		ContactGroupAssign entity = contactGroupAssignDao.findByGroupId(groupId);
		if(entity != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public ContactGroupAssign findById(Long id) {
		return contactGroupAssignDao.findById(id);
	}
	
	@Override
	public void saveBatchAssign(List<Long> contactIds,Long groupId) {
		contactGroupAssignDao.saveBatchAssign(contactIds, groupId);
	}
	
	
}