package cn.emay.eucp.data.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.eucp.common.moudle.db.system.ContactGroup;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ContactGroupDao;

@Repository
public class ContactGroupDaoImpl extends PojoDaoImpl<ContactGroup> implements ContactGroupDao {
	
	@Override
	public List<ContactGroup> findList(Integer groupType, Long userId, Long enterpriseId,Boolean isOwn){
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "from ContactGroup where isDelete = :isDelete and groupType = :groupType";
		params.put("isDelete", false);
		params.put("groupType", groupType);
		if(groupType.intValue() == ContactGroup.GROUP_TYPE_PERSONAL) {
			hql += " and userId = :userId";
			params.put("userId", userId);
		} else {
			if(isOwn) {//只查询当前用户创建的
				hql += " and userId = :userId";
				params.put("userId", userId);
			} else {//查询共享的
				hql += " and enterpriseId = :enterpriseId";
				params.put("enterpriseId", enterpriseId);
			}
		}
		return this.getListResult(ContactGroup.class, hql, params);
	}
	
	@Override
	public ContactGroup getContactGroup(Integer groupType, Long userId, Long enterpriseId,String groupName, Long id) {
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "from ContactGroup where isDelete = :isDelete and groupType = :groupType";
		params.put("isDelete", false);
		params.put("groupType", groupType);
		if(groupType.intValue() == ContactGroup.GROUP_TYPE_PERSONAL) {
			hql += " and userId = :userId";
			params.put("userId", userId);
		} else {
			hql += " and enterpriseId = :enterpriseId";
			params.put("enterpriseId", enterpriseId);
		}
		
		if(!StringUtils.isEmpty(groupName)) {
			hql += " and groupName = :groupName";
			params.put("groupName", groupName);
		}
		
		if(id != null && id.longValue() > 0l) {
			hql += " and id != :id";
			params.put("id", id);
		}
		return this.getUniqueResult(ContactGroup.class, hql, params);
	}

}