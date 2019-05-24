package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.contact.ContactDTO;
import cn.emay.eucp.common.dto.contact.ContactParamsDTO;
import cn.emay.eucp.common.moudle.db.system.Contact;
import cn.emay.eucp.common.moudle.db.system.ContactGroup;
import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;
import cn.emay.eucp.data.dao.system.ContactDao;
import cn.emay.eucp.data.dao.system.ContactGroupAssignDao;
import cn.emay.eucp.data.dao.system.ContactGroupDao;
import cn.emay.eucp.data.service.system.ContactService;
import cn.emay.eucp.util.RegularCheckUtils;
import cn.emay.util.DateUtil;

@Service("contactService")
public class ContactServiceImpl implements ContactService {

	@Resource
	private ContactDao contactDao;
	@Resource
	private ContactGroupAssignDao contactGroupAssignDao;
	@Resource
	private ContactGroupDao contactGroupDao;
	@Resource
	private RedisClient redis;

	@Override
	public Page<ContactDTO> findPage(Integer groupType, Long groupId, String trueName, String mobile, Long userId, Long enterpriseId, Date startTime, Date endTime, int start, int limit) {
		List<Long> groupIds = new ArrayList<Long>();
		if (groupId.longValue() == 0l) {// 查询全部
			List<ContactGroup> list = contactGroupDao.findList(groupType, userId, enterpriseId,false);
			for (ContactGroup group : list) {
				groupIds.add(group.getId());
			}
		} else {
			groupIds.add(groupId);
		}
		if(groupIds.isEmpty()) {//数据不存在
			return new Page<ContactDTO>(1, limit, 0, new ArrayList<ContactDTO>());
		}
		return contactDao.findPage(groupType, groupIds, trueName, mobile, userId, enterpriseId, startTime, endTime, start, limit);
	}

	@Override
	public Page<ContactDTO> findPage(Integer groupType, Long groupId, String trueName, String mobile, Long userId, Long enterpriseId, int start, int limit) {
		return findPage(groupType, groupId, trueName, mobile, userId, enterpriseId, null, null, start, limit);
	}
	
	@Override
	public Boolean isExist(Long groupId, String mobile, Long id) {
		return contactDao.isExist(groupId, mobile, id);
	}

	@Override
	public Result addContact(ContactParamsDTO dto) {
		// 校验联系人（手机号）是否存在
		Contact entity = contactDao.getContact(dto.getUserId(),dto.getMobile());
		if (entity == null || entity.getIsDelete()) {// 不存在则联系人入库
			entity = new Contact();
			BeanUtils.copyProperties(dto, entity);
			entity.setCreateTime(new Date());
			entity.setIsDelete(false);
			contactDao.save(entity);
		}
		ContactGroupAssign assign = new ContactGroupAssign();
		assign.setContactId(entity.getId());
		assign.setGroupId(dto.getGroupId());
		assign.setCreateTime(new Date());
		assign.setIsDelete(false);
		contactGroupAssignDao.save(assign);
		return Result.rightResult();
	}

	@Override
	public Result modifyContact(ContactParamsDTO dto, ContactGroupAssign oldAssign) {
		// 校验联系人（手机号）是否存在
		Contact entity = contactDao.getContact(dto.getUserId(),dto.getMobile());
		if (entity == null || entity.getIsDelete()) {// 不存在，则信息直接覆盖
			entity = contactDao.findById(oldAssign.getContactId());
			BeanUtils.copyProperties(dto, entity);
			entity.setId(oldAssign.getContactId());
			contactDao.update(entity);
			if (!dto.getGroupId().equals(oldAssign.getGroupId())) {
				oldAssign.setGroupId(dto.getGroupId());
				contactGroupAssignDao.update(oldAssign);
			}
		} else {// 存在
			Long contactId = entity.getId();
			// 更新联系人信息
			BeanUtils.copyProperties(dto, entity);
			entity.setId(contactId);
			contactDao.update(entity);
			// 更新关联表信息
			if (!dto.getGroupId().equals(oldAssign.getGroupId())) {
				oldAssign.setGroupId(dto.getGroupId());
				contactGroupAssignDao.update(oldAssign);
			}
			contactGroupAssignDao.updateAssign(entity.getId(), oldAssign.getContactId());
		}
		return Result.rightResult();
	}

	@Override
	public ContactParamsDTO findByAssignId(Long assignId) {
		return contactDao.findByAssignId(assignId);
	}

	@Override
	public Result deleteContact(Long assignId) {
		contactGroupAssignDao.deleteAssign(assignId);
		return Result.rightResult();
	}

	@Override
	public void redisSaveDownloadError(String downloadKey, List<String[]> errors) {
		redis.set(downloadKey, errors, 60 * 60 * 5);
	}

	@Override
	public String redisGetDownloadErrorInfo(String downloadKey) {
		return redis.get(downloadKey);
	}

	@Override
	public List<Long> findIdsByMobiles(List<String> mobiles, Long userId) {
		return contactDao.findIdsByMobiles(mobiles, userId);
	}

	@Override
	public List<String[]> findExportData(Long groupId, String params, int currentPage, int pageSize) {
		String realName = "";
		String mobile = "";
		if (!StringUtils.isEmpty(params) && RegularCheckUtils.checkMobile(params)) {
			mobile = params;
		} else {
			realName = params;
		}
		List<String[]> result = new ArrayList<String[]>();
		ContactGroup group = contactGroupDao.findById(groupId);
		String groupType ="个人组";
		if(group != null) {
			if(group.getGroupType() != null && group.getGroupType().equals(ContactGroup.GROUP_TYPE_SHARE)) {
				groupType = "共享组";
			} 
		}
		List<ContactParamsDTO> list = contactDao.findByPage(groupId, realName, mobile, currentPage, pageSize);
		if (null != list && !list.isEmpty()) {
			for (ContactParamsDTO dto : list) {
				String birthday = dto.getBirthday() != null ? DateUtil.toString(dto.getBirthday(), "yyyy-MM-dd"):"";
				String[] tmp = new String[] {dto.getRealName(),dto.getMobile(),dto.getEmail(),dto.getQq(),birthday,dto.getCompany(),dto.getPosition(),dto.getCompanyAddress(),group.getGroupName(),groupType};
				result.add(tmp);
			}
		}
		return result;
	}
	
	@Override
	public List<String> findByGroupIds(List<Long> groupIds){
		return contactDao.findByGroupIds(groupIds);
	}

}