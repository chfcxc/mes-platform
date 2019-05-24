package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.dto.fms.channel.ChannelSimpleDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeChannelDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeInfoDTO;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.common.moudle.db.fms.FmsUserServiceCodeAssign;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeChannelDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeDao;
import cn.emay.eucp.data.dao.fms.FmsTemplateChannelReportDao;
import cn.emay.eucp.data.dao.fms.FmsTemplateDao;
import cn.emay.eucp.data.dao.fms.FmsTemplateServiceCodeAssignDao;
import cn.emay.eucp.data.dao.fms.FmsUserServiceCodeAssignDao;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;

@Service("fmsTemplateServiceCodeAssignService")
public class FmsTemplateServiceCodeAssignServiceImpl implements FmsTemplateServiceCodeAssignService {

	@Resource
	private FmsTemplateServiceCodeAssignDao fmsTemplateServiceCodeAssignDao;
	@Resource
	private FmsServiceCodeChannelDao fmsServiceCodeChannelDao;
	@Resource
	private FmsTemplateChannelReportDao fmsTemplateChannelReportDao;
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsTemplateDao fmsTemplateDao;
	@Resource
	private FmsUserServiceCodeAssignDao fmsUserServiceCodeAssignDao;
	@Resource
	private FmsServiceCodeDao fmsServiceCodeDao;

	@Override
	public void addTemplateServiceCodeAssign(FmsTemplateServiceCodeAssign entity) {
		fmsTemplateServiceCodeAssignDao.save(entity);
	}

	@Override
	public void updateTemplateServiceCodeAssign(FmsTemplateServiceCodeAssign entity) {
		fmsTemplateServiceCodeAssignDao.update(entity);
	}

	@Override
	public Page<FmsTemplateDto> findTempLetPage(String title, String content, List<String> appId, Long businessTypeId, Integer saveType, Long contentTypeId, Integer messageType, Integer submitType,
			Integer auditState, Date startTime, Date endTime, int start, int limit, Integer templateType) {
		Page<FmsTemplateDto> page = fmsTemplateServiceCodeAssignDao.findTempLetPage(title, content, appId, businessTypeId, saveType, contentTypeId, messageType, submitType, auditState, startTime,
				endTime, start, limit, templateType);
		List<FmsTemplateDto> list = page.getList();
		Map<String, Map<String, ChannelSimpleDTO>> channelMap = new HashMap<String, Map<String, ChannelSimpleDTO>>();
		Map<String, Integer> auditStateMap = new HashMap<String, Integer>();
		Map<String, String> auditIdMap = new HashMap<String, String>();
		Map<String, Set<Long>> queryMap = new HashMap<String, Set<Long>>();
		Map<Long, FmsBusinessType> typeMap = new HashMap<Long, FmsBusinessType>();
		if (null != list && list.size() > 0) {
			Set<String> set = new HashSet<String>();
			Set<Long> typeIdSet = new HashSet<Long>();
			for (FmsTemplateDto fmsTemplateDto : list) {
				set.add(fmsTemplateDto.getAppId());
				typeIdSet.add(fmsTemplateDto.getBusinessTypeId());
				typeIdSet.add(fmsTemplateDto.getContentTypeId());
			}
			// 查询关联通道以及通道名
			List<FmsServiceCodeChannelDto> fmsServiceCodeChannels = fmsServiceCodeChannelDao.findChannelByAppIds(set.toArray(new String[set.size()]));
			if (null != fmsServiceCodeChannels && fmsServiceCodeChannels.size() > 0) {
				for (FmsServiceCodeChannelDto fmsServiceCodeChannel : fmsServiceCodeChannels) {
					if (channelMap.containsKey(fmsServiceCodeChannel.getAppId())) {
						Map<String, ChannelSimpleDTO> map = channelMap.get(fmsServiceCodeChannel.getAppId());
						map.put(fmsServiceCodeChannel.getOperatorCode() + "," + fmsServiceCodeChannel.getTemplateType(),
								new ChannelSimpleDTO(fmsServiceCodeChannel.getChannelId(), fmsServiceCodeChannel.getChannelName(), fmsServiceCodeChannel.getReportType()));
					} else {
						Map<String, ChannelSimpleDTO> map = new HashMap<String, ChannelSimpleDTO>();
						map.put(fmsServiceCodeChannel.getOperatorCode() + "," + fmsServiceCodeChannel.getTemplateType(),
								new ChannelSimpleDTO(fmsServiceCodeChannel.getChannelId(), fmsServiceCodeChannel.getChannelName(), fmsServiceCodeChannel.getReportType()));
						channelMap.put(fmsServiceCodeChannel.getAppId(), map);
					}
				}
				// 为模板设置当前服务号关联通道信息，以及查询条件
				setAuditChannel(list, channelMap, queryMap);
				if (queryMap.size() > 0) {
					// 查询当前服务号通道模板报备状态
					List<FmsTemplateChannelReport> fmsTempLetChannels = fmsTemplateChannelReportDao.findByTempLetIdAndChannelId(queryMap);
					for (FmsTemplateChannelReport tmp : fmsTempLetChannels) {
						auditStateMap.put(tmp.getTemplateId() + "," + tmp.getChannelId() + "," + tmp.getOperatorCode(), tmp.getState());
						auditIdMap.put(tmp.getTemplateId() + "," + tmp.getChannelId() + "," + tmp.getOperatorCode(), tmp.getChannelTemplateId());
					}
				}
			} else {
				setAuditChannel(list, channelMap, queryMap);
			}
			List<FmsBusinessType> fmsBusinessTypes = fmsBusinessTypeDao.findByIds(typeIdSet.toArray(new Long[typeIdSet.size()]));
			for (FmsBusinessType fmsBusinessType : fmsBusinessTypes) {
				typeMap.put(fmsBusinessType.getId(), fmsBusinessType);
			}
			for (FmsTemplateDto fmsTemplateDto : list) {
				setAuditState(auditStateMap, auditIdMap, fmsTemplateDto);
				setBusinessType(typeMap, fmsTemplateDto);
			}
		}
		return page;
	}

	private void setBusinessType(Map<Long, FmsBusinessType> typeMap, FmsTemplateDto fmsTemplateDto) {
		Long tmpBusinessTypeId = fmsTemplateDto.getBusinessTypeId();
		if (typeMap.containsKey(tmpBusinessTypeId)) {
			fmsTemplateDto.setBusinessType(typeMap.get(tmpBusinessTypeId).getName());
		}
		Long tmpContentTypeId = fmsTemplateDto.getContentTypeId();
		if (typeMap.containsKey(tmpContentTypeId)) {
			FmsBusinessType tmpBusinessType = typeMap.get(tmpContentTypeId);
			fmsTemplateDto.setContentType(tmpBusinessType.getName());
			fmsTemplateDto.setSaveType(tmpBusinessType.getSaveType());
		}
	}

	private void setAuditChannel(List<FmsTemplateDto> list, Map<String, Map<String, ChannelSimpleDTO>> channelMap, Map<String, Set<Long>> queryMap) {
		for (FmsTemplateDto fmsTemplateDto : list) {
			if (null != channelMap && channelMap.containsKey(fmsTemplateDto.getAppId())) {
				Map<String, ChannelSimpleDTO> channelNameMap = channelMap.get(fmsTemplateDto.getAppId());
				setTmpState(fmsTemplateDto, FmsTemplate.NOT_REPORT);
				if (channelNameMap.containsKey("CMCC" + "," + fmsTemplateDto.getTemplateType())) {
					ChannelSimpleDTO channelSimpleDTO = channelNameMap.get("CMCC" + "," + fmsTemplateDto.getTemplateType());
					fillQueryMap(queryMap, fmsTemplateDto, channelSimpleDTO);
					fmsTemplateDto.setCmccChannelId(channelSimpleDTO.getChannelId());
					fmsTemplateDto.setCmccChannelName(channelSimpleDTO.getChannelName());
					fmsTemplateDto.setCmccReportType(channelSimpleDTO.getAuditType());
				}
				if (channelNameMap.containsKey("CUCC" + "," + fmsTemplateDto.getTemplateType())) {
					ChannelSimpleDTO channelSimpleDTO = channelNameMap.get("CUCC" + "," + fmsTemplateDto.getTemplateType());
					fillQueryMap(queryMap, fmsTemplateDto, channelSimpleDTO);
					fmsTemplateDto.setCuccChannelId(channelSimpleDTO.getChannelId());
					fmsTemplateDto.setCuccChannelName(channelSimpleDTO.getChannelName());
					fmsTemplateDto.setCuccReportType(channelSimpleDTO.getAuditType());
				}
				if (channelNameMap.containsKey("CTCC" + "," + fmsTemplateDto.getTemplateType())) {
					ChannelSimpleDTO channelSimpleDTO = channelNameMap.get("CTCC" + "," + fmsTemplateDto.getTemplateType());
					fillQueryMap(queryMap, fmsTemplateDto, channelSimpleDTO);
					fmsTemplateDto.setCtccChannelId(channelSimpleDTO.getChannelId());
					fmsTemplateDto.setCtccChannelName(channelSimpleDTO.getChannelName());
					fmsTemplateDto.setCtccReportType(channelSimpleDTO.getAuditType());
				}
				setNoChannelTmpState(fmsTemplateDto);
			} else {
				setTmpState(fmsTemplateDto, FmsTemplate.NOT_SET_CHANNEL);
			}
		}
	}

	private void setAuditState(Map<String, Integer> auditStateMap, Map<String, String> auditIdMap, FmsTemplateDto fmsTemplateDto) {
		if (null != fmsTemplateDto.getCmccChannelId() && auditStateMap.containsKey(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCmccChannelId() + ",CMCC")) {
			fmsTemplateDto.setCmccAuditState(auditStateMap.get(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCmccChannelId() + ",CMCC"));
			fmsTemplateDto.setCmccTemplateId(auditIdMap.get(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCmccChannelId() + ",CMCC"));
		}
		fmsTemplateDto.setCmccAuditStateDesc(GlobalConstant.auditMap.get(fmsTemplateDto.getCmccAuditState()));
		if (null != fmsTemplateDto.getCuccChannelId() && auditStateMap.containsKey(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCuccChannelId() + ",CUCC")) {
			fmsTemplateDto.setCuccAuditState(auditStateMap.get(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCuccChannelId() + ",CUCC"));
			fmsTemplateDto.setCuccTemplateId(auditIdMap.get(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCuccChannelId() + ",CUCC"));
		}
		fmsTemplateDto.setCuccAuditStateDesc(GlobalConstant.auditMap.get(fmsTemplateDto.getCuccAuditState()));
		if (null != fmsTemplateDto.getCtccChannelId() && auditStateMap.containsKey(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCtccChannelId() + ",CTCC")) {
			fmsTemplateDto.setCtccAuditState(auditStateMap.get(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCtccChannelId() + ",CTCC"));
			fmsTemplateDto.setCtccTemplateId(auditIdMap.get(fmsTemplateDto.getTemplateId() + "," + fmsTemplateDto.getCtccChannelId() + ",CTCC"));
		}
		fmsTemplateDto.setCtccAuditStateDesc(GlobalConstant.auditMap.get(fmsTemplateDto.getCtccAuditState()));
	}

	private void fillQueryMap(Map<String, Set<Long>> queryMap, FmsTemplateDto fmsTemplateDto, ChannelSimpleDTO channelSimpleDTO) {
		if (queryMap.containsKey(fmsTemplateDto.getTemplateId())) {
			queryMap.get(fmsTemplateDto.getTemplateId()).add(channelSimpleDTO.getChannelId());
		} else {
			Set<Long> querySet = new HashSet<Long>();
			querySet.add(channelSimpleDTO.getChannelId());
			queryMap.put(fmsTemplateDto.getTemplateId(), querySet);
		}
	}

	private void setTmpState(FmsTemplateDto fmsTemplateDto, int state) {
		fmsTemplateDto.setCmccAuditState(state);
		fmsTemplateDto.setCuccAuditState(state);
		fmsTemplateDto.setCtccAuditState(state);
	}

	private void setNoChannelTmpState(FmsTemplateDto fmsTemplateDto) {
		if (null == fmsTemplateDto.getCmccChannelId()) {
			fmsTemplateDto.setCmccAuditState(FmsTemplate.NOT_SET_CHANNEL);
		}
		if (null == fmsTemplateDto.getCuccChannelId()) {
			fmsTemplateDto.setCuccAuditState(FmsTemplate.NOT_SET_CHANNEL);
		}
		if (null == fmsTemplateDto.getCtccChannelId()) {
			fmsTemplateDto.setCtccAuditState(FmsTemplate.NOT_SET_CHANNEL);
		}
	}

	@Override
	public void addTemplateServiceCodeAssignAndReport(FmsTemplate fmsTemplate, Boolean save, FmsTemplateServiceCodeAssign entity) {
		if (save) {
			fmsTemplateDao.save(fmsTemplate);
		}
		fmsTemplateServiceCodeAssignDao.save(entity);
	}

	@Override
	public void save(FmsTemplateServiceCodeAssign entity) {
		fmsTemplateServiceCodeAssignDao.save(entity);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findByAudiState(Integer auditState, int start, int limit) {
		return fmsTemplateServiceCodeAssignDao.findByAudiState(auditState, start, limit);
	}

	@Override
	public void updateBatch(List<FmsTemplateServiceCodeAssign> updateList) {
		fmsTemplateServiceCodeAssignDao.updateBatch(updateList);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsTemplateServiceCodeAssignDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findNeedRepeatReoportList() {
		return fmsTemplateServiceCodeAssignDao.findNeedRepeatReoportList();
	}

	@Override
	public List<FmsTemplateServiceCodeAssign> findByAppIdAndTemplateId(Long userId, Long serviceCodeId, String templateId) {
		Set<Long> serviceCodeIdSet = new HashSet<Long>();
		;
		if (userId != null) {
			List<FmsUserServiceCodeAssign> lists = fmsUserServiceCodeAssignDao.findUserSCAssignByProperty("userId", userId);
			for (FmsUserServiceCodeAssign serviceCodeUser : lists) {
				serviceCodeIdSet.add(serviceCodeUser.getServiceCodeId());
			}
			if (serviceCodeIdSet.contains(serviceCodeId)) {
				FmsServiceCodeInfoDTO fmsServiceCode = fmsServiceCodeDao.findbyid(serviceCodeId);
				return fmsTemplateServiceCodeAssignDao.findByAppIdAndTemplate(fmsServiceCode.getAppId(), templateId);
			}
		}
		return null;
	}
}
