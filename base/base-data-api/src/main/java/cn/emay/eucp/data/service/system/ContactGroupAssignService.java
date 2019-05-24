package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;

public interface ContactGroupAssignService {

	Boolean isContactExist(Long groupId);

	ContactGroupAssign findById(Long id);

	void saveBatchAssign(List<Long> contactIds, Long groupId);


}