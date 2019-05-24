package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ContactGroupAssignDao;

@Repository
public class ContactGroupAssignDaoImpl extends PojoDaoImpl<ContactGroupAssign> implements ContactGroupAssignDao {

	@Override
	public ContactGroupAssign findByGroupId(Long groupId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from ContactGroupAssign where isDelete = :isDelete and groupId = :groupId";
		params.put("isDelete", false);
		params.put("groupId", groupId);
		return this.getUniqueResult(ContactGroupAssign.class, hql, params);
	}

	@Override
	public void updateAssign(Long contactId, Long oldContactId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "update ContactGroupAssign set contactId = :contactId where contactId =:oldContactId and isDelete =:isDelete";
		params.put("contactId", contactId);
		params.put("oldContactId", oldContactId);
		params.put("isDelete", false);
		this.execByHql(hql, params);
	}

	@Override
	public void deleteAssign(Long assignId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "delete from ContactGroupAssign where id = :assignId";
		params.put("assignId", assignId);
		this.execByHql(hql, params);
	}

	@Override
	public void saveBatchAssign(List<Long> contactIds, Long groupId) {
		if (contactIds != null && !contactIds.isEmpty()) {
			List<Object[]> params = new ArrayList<Object[]>();
			String sql = "insert ignore into system_contact_group_assign (group_id,contact_id,is_delete,create_time) values (?,?,?,?)";
			Date now = new Date();
			for (Long id : contactIds) {
				params.add(new Object[] { groupId, id, 0, now });
			}
			this.getJdbcTemplate().batchUpdate(sql, params);
		}
	}

}