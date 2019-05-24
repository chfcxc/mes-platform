package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.contact.ContactDTO;
import cn.emay.eucp.common.dto.contact.ContactParamsDTO;
import cn.emay.eucp.common.moudle.db.system.Contact;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface ContactDao extends BaseSuperDao<Contact> {

	Boolean isExist(Long groupId, String mobile, Long id);

	Contact getContact(Long userId,String mobile);

	Page<ContactDTO> findPage(Integer groupType, List<Long> groupIds, String trueName, String mobile, Long userId, Long enterpriseId, Date startTime, Date endTime, int start, int limit);

	ContactParamsDTO findByAssignId(Long assignId);

	List<Long> findIdsByMobiles(List<String> mobiles,Long userId);

	List<ContactParamsDTO> findByPage(Long groupId, String trueName, String mobile, int currentPage, int pageSize);

	List<String> findByGroupIds(List<Long> groupIds);

	
	

}
