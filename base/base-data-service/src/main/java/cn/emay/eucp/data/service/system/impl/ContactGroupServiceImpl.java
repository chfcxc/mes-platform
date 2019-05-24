package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.ContactGroup;
import cn.emay.eucp.data.dao.system.ContactGroupDao;
import cn.emay.eucp.data.service.system.ContactGroupService;

@Service("contactGroupService")
public class ContactGroupServiceImpl implements ContactGroupService {

	@Resource
	private ContactGroupDao contactGroupDao;
	
	@Override
	public List<ContactGroup> findList(Integer groupType, Long userId, Long enterpriseId,Boolean isOwn){
		return contactGroupDao.findList(groupType, userId, enterpriseId,isOwn);
	}
	
	@Override
	public ContactGroup findById(Long id) {
		return contactGroupDao.findById(id);
	}
	
	@Override
	public Boolean isExist(Integer groupType, Long userId, Long enterpriseId, String groupName, Long id) {
		ContactGroup entity = contactGroupDao.getContactGroup(groupType, userId, enterpriseId, groupName, id);
		if(entity != null) {
			return true;
		}
		return false;
	}
	
	@Override
	public Result addGroup(Integer groupType,Long userId, Long enterpriseId, String groupName) {
		ContactGroup entity = new ContactGroup();
		entity.setGroupName(groupName);
		entity.setGroupType(groupType);
		entity.setUserId(userId);
		entity.setEnterpriseId(enterpriseId);
		entity.setCreateTime(new Date());
		entity.setIsDelete(false);
		contactGroupDao.save(entity);
		return Result.rightResult();
	}

	@Override
	public Result modifyGroup(ContactGroup entity) {
		contactGroupDao.update(entity);
		return Result.rightResult();
	}
}