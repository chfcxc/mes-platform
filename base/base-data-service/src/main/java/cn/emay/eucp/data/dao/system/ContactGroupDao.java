package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.ContactGroup;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface ContactGroupDao extends BaseSuperDao<ContactGroup> {

	List<ContactGroup> findList(Integer groupType, Long userId, Long enterpriseId,Boolean isOwn);

	ContactGroup getContactGroup(Integer groupType, Long userId, Long enterpriseId, String groupName, Long id);

}
