package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.ContactGroup;

public interface ContactGroupService {

	List<ContactGroup> findList(Integer groupType, Long userId, Long enterpriseId,Boolean isOwn);

	Boolean isExist(Integer groupType, Long userId, Long enterpriseId, String groupName, Long id);

	Result addGroup(Integer groupType, Long userId, Long enterpriseId, String groupName);

	ContactGroup findById(Long id);

	Result modifyGroup(ContactGroup entity);

}