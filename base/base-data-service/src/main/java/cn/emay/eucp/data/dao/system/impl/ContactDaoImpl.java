package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.contact.ContactDTO;
import cn.emay.eucp.common.dto.contact.ContactParamsDTO;
import cn.emay.eucp.common.moudle.db.system.Contact;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ContactDao;
import cn.emay.eucp.util.RegularCheckUtils;

@Repository
public class ContactDaoImpl extends PojoDaoImpl<Contact> implements ContactDao {

	@Override
	public List<ContactParamsDTO> findByPage(Long groupId, String trueName, String mobile, int currentPage, int pageSize) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select c.id,c.mobile,c.real_name,c.email,c.qq,c.birthday,c.company,c.position,c.company_address,ass.id as assignId from system_contact_group_assign ass,system_contact c where ass.contact_id = c.id and ass.group_id = ? and ass.is_delete = 0 and c.is_delete = 0";
		params.add(groupId);
		if(!StringUtils.isEmpty(trueName)) {
			sql += " and c.real_name like ?";
			params.add("%" +RegularCheckUtils.specialCodeEscape(trueName)+ "%");
		}
		if(!StringUtils.isEmpty(mobile)) {
			sql += " and c.mobile = ?";
			params.add(mobile);
		}
		return this.findSqlForListForMysql(ContactParamsDTO.class, sql, params, currentPage, pageSize);
	}
	
	@Override
	public Page<ContactDTO> findPage(Integer groupType, List<Long> groupIds, String trueName, String mobile, Long userId, Long enterpriseId, Date startTime, Date endTime, int start, int limit) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select c.id,c.mobile,c.real_name,c.email,c.qq,c.user_id,date_format(c.birthday,'%Y-%m-%d') as birthday,ass.id as assignId,ass.group_id from system_contact_group_assign ass,system_contact c where ass.contact_id = c.id and ass.is_delete = 0 and c.is_delete = 0";
		if(!StringUtils.isEmpty(trueName)) {
			sql += " and c.real_name like ?";
			params.add("%" +RegularCheckUtils.specialCodeEscape(trueName)+ "%");
		}
		if(!StringUtils.isEmpty(mobile)) {
			sql += " and c.mobile = ?";
			params.add(mobile);
		}
		if(!groupIds.isEmpty()) {
			sql += " and ass.group_id in ("+org.apache.commons.lang3.StringUtils.join(groupIds, ",") + ")";
		}
		if(startTime != null) {
			sql += " and c.birthday >= ?";
			params.add(startTime);
		}
		if(endTime != null) {
			sql += " and c.birthday <= ?";
			params.add(endTime);
		}
		return this.findSqlForPageForMysql(ContactDTO.class, sql, params, start, limit);
	}
	
	@Override
	public Boolean isExist(Long groupId,String mobile,Long id) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select c.id,c.mobile from system_contact_group_assign ass,system_contact c where ass.contact_id = c.id and ass.group_id = ? and c.mobile = ? and ass.is_delete = 0 and c.is_delete = 0";
		params.add(groupId);
		params.add(mobile);
		if(id != null && id.longValue() > 0l) {
			sql += " and ass.id != ?";
			params.add(id);
		}
		sql += " limit 0,1";
		List <ContactDTO> list = this.findSqlForListObj(ContactDTO.class, sql, params);
		if(list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	@Override
	public Contact getContact(Long userId,String mobile) {
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "from Contact where isDelete = :isDelete and userId = :userId ";
		params.put("isDelete", false);
		params.put("userId", userId);
		if(!StringUtils.isEmpty(mobile)) {
			hql += " and mobile = :mobile";
			params.put("mobile", mobile);
		}
		return this.getUniqueResult(Contact.class, hql, params);
	}
	
	@Override
	public ContactParamsDTO findByAssignId(Long assignId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select c.id,c.mobile,c.real_name,c.email,c.qq,c.birthday,c.company,c.position,c.company_address,c.user_id,ass.id as assignId,ass.group_id from system_contact_group_assign ass,system_contact c where ass.contact_id = c.id and ass.id = ? and ass.is_delete = 0 and c.is_delete = 0";
		params.add(assignId);
		List<ContactParamsDTO> list = this.findSqlForListObj(ContactParamsDTO.class, sql, params);
		if(list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<Long> findIdsByMobiles(List<String> mobiles,Long userId){
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "select id from Contact where isDelete = :isDelete and userId = :userId";
		params.put("isDelete", false);
		params.put("userId", userId);
		
		hql += " and mobile in (:mobiles)";
		params.put("mobiles", mobiles);
		return this.getListResult(Long.class, hql, params);
	}
	
	@Override
	public List<String> findByGroupIds(List<Long> groupIds){
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "select c.mobile from ContactGroupAssign a,Contact c where a.contactId = c.id and a.groupId in (:groupIds) and a.isDelete = false and c.isDelete = false";
		params.put("groupIds", groupIds);
		return this.getListResult(String.class, hql, params);
	}

}