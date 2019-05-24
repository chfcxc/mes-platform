package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.contact.ContactDTO;
import cn.emay.eucp.common.dto.contact.ContactParamsDTO;
import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;

public interface ContactService {

	Page<ContactDTO> findPage(Integer groupType, Long groupId, String trueName, String mobile, Long userId, Long enterpriseId, Date startTime, Date endTime, int start, int limit);

	Boolean isExist(Long groupId, String mobile, Long id);

	Result addContact(ContactParamsDTO dto);

	Result modifyContact(ContactParamsDTO dto,ContactGroupAssign assign);

	ContactParamsDTO findByAssignId(Long assignId);

	Result deleteContact(Long assignId);

	void redisSaveDownloadError(String downloadKey, List<String[]> errors);

	String redisGetDownloadErrorInfo(String downloadKey);

	List<Long> findIdsByMobiles(List<String> mobiles, Long userId);

	List<String[]> findExportData(Long groupId, String params, int currentPage, int pageSize);

	List<String> findByGroupIds(List<Long> groupIds);

	/**
	 * @param groupType
	 * @param groupId
	 * @param trueName
	 * @param mobile
	 * @param userId
	 * @param enterpriseId
	 * @param start
	 * @param limit
	 * @return
	 */
	Page<ContactDTO> findPage(Integer groupType, Long groupId, String trueName, String mobile, Long userId, Long enterpriseId, int start, int limit);

}