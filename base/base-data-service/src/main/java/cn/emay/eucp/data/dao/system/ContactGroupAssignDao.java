package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface ContactGroupAssignDao extends BaseSuperDao<ContactGroupAssign> {

	ContactGroupAssign findByGroupId(Long groupId);

	void updateAssign(Long contactId, Long oldContactId);

	void deleteAssign(Long assignId);

	void saveBatchAssign(List<Long> contactIds, Long groupId);

}
