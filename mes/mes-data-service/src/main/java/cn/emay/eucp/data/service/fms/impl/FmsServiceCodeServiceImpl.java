package cn.emay.eucp.data.service.fms.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.encryption.Md5;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeInfoDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.GeneratorSerciceCodeDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.SimpleFmsServiceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.common.util.RegeditUtil;
import cn.emay.eucp.data.dao.fms.FmsAccountDao;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeChannelDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeParamDao;
import cn.emay.eucp.data.dao.fms.FmsUserServiceCodeAssignDao;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;

@Service("fmsServiceCodeService")
public class FmsServiceCodeServiceImpl implements FmsServiceCodeService {
	@Resource
	private FmsServiceCodeDao fmsServiceCodeDao;
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsAccountDao fmsAccountDao;
	@Resource
	private FmsUserServiceCodeAssignDao fmsUserServiceCodeAssignDao;
	@Resource
	private FmsServiceCodeParamDao fmsServiceCodeParamDao;
	@Resource
	private FmsServiceCodeChannelDao fmsServiceCodeChannelDao;

	@Override
	public List<FmsServiceCode> findByEnterId(Long enterpriseId, int state) {
		return fmsServiceCodeDao.findByEnterId(enterpriseId, state);
	}

	@Override
	public FmsServiceCode findById(Long serviceCodeId) {
		return fmsServiceCodeDao.findById(serviceCodeId);
	}

	@Override
	public Page<FmsServiceCodeDto> findClientList(String appId, String service, Long businessTypeId, int start, int limit, Long enterpriseId) {
		// List<FmsBusinessType> andName = fmsBusinessTypeDao.findByIdAndName(saveType, contenttype, businessType);
		// List<Long> list = new ArrayList<Long>();
		// if (andName != null & andName.size() > 0) {
		// for (FmsBusinessType type : andName) {
		// list.add(type.getId());
		// }
		// }
		Page<FmsServiceCode> page = fmsServiceCodeDao.findlist(start, limit, service, appId, businessTypeId, enterpriseId);
		Set<Long> businessId = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsServiceCode servicecode : page.getList()) {
				businessId.add(servicecode.getBusinessTypeId());
			}
		}
		List<Object[]> findIds = fmsBusinessTypeDao.findIds(businessId);
		Map<Long, FmsBusinesTypeDto> map = new HashMap<Long, FmsBusinesTypeDto>();
		if (null != findIds) {
			for (Object[] objects : findIds) {
				FmsBusinesTypeDto dto = new FmsBusinesTypeDto();
				dto.setId(Long.valueOf(objects[0].toString()));
				dto.setName(objects[1].toString());
				dto.setParentId(Long.valueOf(objects[2].toString()));
				dto.setSaveType(Integer.valueOf(objects[3].toString()));
				dto.setParentName(objects[4].toString());
				map.put(dto.getId(), dto);
			}
		}
		Page<FmsServiceCodeDto> page2 = new Page<FmsServiceCodeDto>();
		List<FmsServiceCodeDto> list2 = new ArrayList<FmsServiceCodeDto>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsServiceCode servicecode : page.getList()) {
				FmsServiceCodeDto dto = new FmsServiceCodeDto();
				dto.setId(servicecode.getId());
				dto.setAppId(servicecode.getAppId());
				dto.setServiceCode(servicecode.getServiceCode());
				dto.setSecretKey(servicecode.getSecretKey());
				dto.setState(servicecode.getState());
				FmsBusinesTypeDto businesTypeDto = map.get(servicecode.getBusinessTypeId());
				dto.setSaveType(businesTypeDto.getSaveType());
				dto.setBusinessType(businesTypeDto.getParentName());
				dto.setContentType(businesTypeDto.getName());
				list2.add(dto);
			}
		}
		page2.setLimit(page.getLimit());
		page2.setStart(page.getStart());
		page2.setCurrentPageNum(page.getCurrentPageNum());
		page2.setTotalCount(page.getTotalCount());
		page2.setTotalPage(page.getTotalPage());
		page2.setList(list2);
		return page2;
	}

	@Override
	public void update(FmsServiceCode fmsServiceCode) {
		fmsServiceCodeDao.update(fmsServiceCode);
	}

	@Override
	public Page<FmsServiceCodeDto> findList(String appId, String service, int start, int limit, Set<Long> enterpriseId, Long serviceCodeId, int state, Long businessTypeId) {

		Page<FmsServiceCode> page = fmsServiceCodeDao.findPage(start, limit, service, appId, businessTypeId, enterpriseId, state, serviceCodeId);
		Set<Long> businessId = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsServiceCode servicecode : page.getList()) {
				businessId.add(servicecode.getBusinessTypeId());
			}
		}
		List<Object[]> findIds = fmsBusinessTypeDao.findIds(businessId);
		Map<Long, FmsBusinesTypeDto> map = new HashMap<Long, FmsBusinesTypeDto>();
		if (null != findIds) {
			for (Object[] objects : findIds) {
				FmsBusinesTypeDto dto = new FmsBusinesTypeDto();
				dto.setId(Long.valueOf(objects[0].toString()));
				dto.setName(objects[1].toString());
				dto.setParentId(Long.valueOf(objects[2].toString()));
				dto.setSaveType(Integer.valueOf(objects[3].toString()));
				dto.setParentName(objects[4].toString());
				map.put(dto.getId(), dto);
			}
		}
		Page<FmsServiceCodeDto> page2 = new Page<FmsServiceCodeDto>();
		List<FmsServiceCodeDto> list2 = new ArrayList<FmsServiceCodeDto>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsServiceCode servicecode : page.getList()) {
				FmsServiceCodeDto dto = new FmsServiceCodeDto();
				dto.setId(servicecode.getId());
				dto.setAppId(servicecode.getAppId());
				dto.setServiceCode(servicecode.getServiceCode());
				dto.setSecretKey(servicecode.getSecretKey());
				dto.setState(servicecode.getState());
				dto.setEnterpriseId(servicecode.getEnterpriseId());
				dto.setRemark(servicecode.getRemark());
				FmsBusinesTypeDto businesTypeDto = map.get(servicecode.getBusinessTypeId());
				if (null != businesTypeDto) {
					dto.setSaveType(businesTypeDto.getSaveType());
					dto.setBusinessType(businesTypeDto.getParentName());
					dto.setContentType(businesTypeDto.getName());
				}
				list2.add(dto);
			}
		}
		page2.setLimit(page.getLimit());
		page2.setStart(page.getStart());
		page2.setCurrentPageNum(page.getCurrentPageNum());
		page2.setTotalCount(page.getTotalCount());
		page2.setTotalPage(page.getTotalPage());
		page2.setList(list2);
		return page2;
	}

	@Override
	public Result saveCreateSericeCodeNew(SimpleFmsServiceCodeDTO scDTO) {
		Result result = this.checkServiceCode(scDTO);
		if (!result.getSuccess()) {
			return result;
		}
		Long userId = scDTO.getUserId();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) result.getResult();
		GeneratorSerciceCodeDTO serviceSourceCode = (GeneratorSerciceCodeDTO) map.get("listServiceSourceCodes");
		if (serviceSourceCode == null) {
			return Result.badResult("生成服务号失败");
		}
		// 保存生成的特服号
		fmsServiceCodeDao.saveBatchServiceCode(serviceSourceCode);

		// 生成特服号余额
		fmsAccountDao.saveBatchBind(scDTO.getEnterpriseId(), serviceSourceCode.getSn());
		FmsServiceCodeParam serviceCodeParam = new FmsServiceCodeParam();
		serviceCodeParam.setAppId(serviceSourceCode.getSn());
		serviceCodeParam.setCreateTime(new Date());
		serviceCodeParam.setRemark(scDTO.getRemark());
		serviceCodeParam.setGetReportType(FmsServiceCodeParam.GET_REPORT_TYPE);
		fmsServiceCodeParamDao.save(serviceCodeParam);
		// 企业管理账号绑定特服号
		List<GeneratorSerciceCodeDTO> listServiceSourceCodes = new ArrayList<GeneratorSerciceCodeDTO>();
		listServiceSourceCodes.add(serviceSourceCode);
		fmsUserServiceCodeAssignDao.saveBatchBind(listServiceSourceCodes, userId);

		return Result.rightResult(serviceSourceCode);
	}

	public Result checkServiceCode(SimpleFmsServiceCodeDTO scDTO) {
		// 生成SN
		String generAppid = RegeditUtil.generAppid(scDTO);
		// 返回未使用的Sn
		Set<String> findExitAppid = fmsServiceCodeDao.findExitAppid(generAppid);
		int snSize = findExitAppid.size();
		if (snSize > 0) {
			GeneratorSerciceCodeDTO listServiceSourceCode = fillServiceCode(scDTO, findExitAppid);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("listServiceSourceCodes", listServiceSourceCode);
			return Result.rightResult(map);
		}
		return Result.badResult("没有可用服务号");
	}

	private GeneratorSerciceCodeDTO fillServiceCode(SimpleFmsServiceCodeDTO scDTO, Set<String> findExitAppid) {
		GeneratorSerciceCodeDTO gcDto = null;
		if (findExitAppid.size() > 0) {
			String[] sns = findExitAppid.toArray(new String[] {});
			Arrays.sort(sns);
			gcDto = new GeneratorSerciceCodeDTO();
			String secretKey = Md5.md5For16(UUID.randomUUID().toString().getBytes()).toUpperCase();
			gcDto.setSecretKey(secretKey);
			gcDto.setSn(sns[0]);
			gcDto.setServiceCode(scDTO.getServiceCode());
			gcDto.setEnterpriseId(scDTO.getEnterpriseId());
			gcDto.setBusinessTypeId(scDTO.getBusinessTypeId());
			gcDto.setRemark(scDTO.getRemark());
		}
		return gcDto;
	}

	@Override
	public FmsServiceCodeInfoDTO findbyid(Long id) {
		return fmsServiceCodeDao.findbyid(id);
	}

	@Override
	public Result updateServiceCode(Long serviceCodeId, Integer isNeedReport, String ipConfiguration, String serviceCodeChannel, String psersonServiceCodeChannel) {
		FmsServiceCode serviceCode = fmsServiceCodeDao.findById(serviceCodeId);
		if (null == serviceCode) {
			return Result.badResult("此服务号不存在");
		}
		List<FmsServiceCodeChannel> serviceCodeChannels = new ArrayList<FmsServiceCodeChannel>();
		if (!serviceCodeChannel.isEmpty()) {
			String[] channels = serviceCodeChannel.split("#");
			for (String chanel : channels) {
				FmsServiceCodeChannel code = new FmsServiceCodeChannel();
				String[] single = chanel.split(",");
				code.setChannelId(Long.valueOf(single[1]));
				code.setCreateTime(new Date());
				code.setOperatorCode(single[0].toUpperCase());
				code.setServiceCodeId(serviceCodeId);
				code.setTemplateType(0);
				code.setAppId(serviceCode.getAppId());
				serviceCodeChannels.add(code);
			}
		}
		if (!psersonServiceCodeChannel.isEmpty()) {
			String[] personchannels = psersonServiceCodeChannel.split("#");
			for (String chanel : personchannels) {
				FmsServiceCodeChannel code = new FmsServiceCodeChannel();
				String[] single = chanel.split(",");
				code.setChannelId(Long.valueOf(single[1]));
				code.setCreateTime(new Date());
				code.setOperatorCode(single[0].toUpperCase());
				code.setServiceCodeId(serviceCodeId);
				code.setTemplateType(1);
				code.setAppId(serviceCode.getAppId());
				serviceCodeChannels.add(code);
			}
		}
		FmsServiceCodeParam param = new FmsServiceCodeParam();
		param.setAppId(serviceCode.getAppId());
		param.setIpConfiguration(ipConfiguration);
		param.setGetReportType(isNeedReport);
		param.setCreateTime(new Date());
		fmsServiceCodeParamDao.deletebyAppid(serviceCode.getAppId());
		fmsServiceCodeParamDao.save(param);
		fmsServiceCodeChannelDao.deletebyServiceId(serviceCodeId);
		fmsServiceCodeChannelDao.saveServiceCodeChannel(serviceCodeChannels);
		return Result.rightResult();
	}

	@Override
	public Result updateSecretKey(Long id) {
		FmsServiceCode entity = fmsServiceCodeDao.findById(id);
		if (null == entity) {
			return Result.badResult("服务号不存在");
		}
		String secretKey = Md5.md5For16(UUID.randomUUID().toString().getBytes()).toUpperCase();
		entity.setSecretKey(secretKey);
		fmsServiceCodeDao.update(entity);
		String log = "为服务号：" + entity.getServiceCode() + "重新生成密钥:" + secretKey;
		return Result.rightResult(log);
	}

	@Override
	public List<FmsServiceCode> findByUserId(Long userId) {
		return fmsServiceCodeDao.findByUserId(userId);
	}

	@Override
	public List<FmsServiceCode> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsServiceCodeDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

	@Override
	public FmsServiceCode findByserviceCode(String appId) {
		return fmsServiceCodeDao.findByserviceCode(appId);
	}

	@Override
	public Map<Long, FmsServiceCode> findbyIds(Set<Long> id) {
		Map<Long, FmsServiceCode> map = new HashMap<Long, FmsServiceCode>();
		List<FmsServiceCode> list = fmsServiceCodeDao.findByIds(id);
		if (null != list && list.size() > 0) {
			for (FmsServiceCode fmsServiceCode : list) {
				map.put(fmsServiceCode.getId(), fmsServiceCode);
			}
		}
		return map;
	}

	@Override
	public List<FmsServiceCodeDto> findFmsServiceCodeDtoByUserId(Long userId) {
		List<FmsServiceCode> list = fmsServiceCodeDao.findByUserId(userId);
		Set<Long> businessId = new HashSet<Long>();
		if (null != list && list.size() > 0) {
			for (FmsServiceCode servicecode : list) {
				businessId.add(servicecode.getBusinessTypeId());
			}
		}
		List<Object[]> findIds = fmsBusinessTypeDao.findIds(businessId);
		Map<Long, FmsBusinesTypeDto> map = new HashMap<Long, FmsBusinesTypeDto>();
		if (null != findIds) {
			for (Object[] objects : findIds) {
				FmsBusinesTypeDto dto = new FmsBusinesTypeDto();
				dto.setId(Long.valueOf(objects[0].toString()));
				dto.setName(objects[1].toString());
				dto.setParentId(Long.valueOf(objects[2].toString()));
				dto.setSaveType(Integer.valueOf(objects[3].toString()));
				dto.setParentName(objects[4].toString());
				map.put(dto.getId(), dto);
			}
		}
		List<FmsServiceCodeDto> list2 = new ArrayList<FmsServiceCodeDto>();
		if (null != list && list.size() > 0) {
			for (FmsServiceCode servicecode : list) {
				if (servicecode.getState() == FmsServiceCode.STATE_DISABLE) {
					continue;
				}
				FmsServiceCodeDto dto = new FmsServiceCodeDto();
				dto.setId(servicecode.getId());
				dto.setAppId(servicecode.getAppId());
				dto.setServiceCode(servicecode.getServiceCode());
				dto.setSecretKey(servicecode.getSecretKey());
				dto.setState(servicecode.getState());
				FmsBusinesTypeDto businesTypeDto = map.get(servicecode.getBusinessTypeId());
				dto.setSaveType(businesTypeDto.getSaveType());
				dto.setBusinessType(businesTypeDto.getParentName());
				dto.setContentType(businesTypeDto.getName());
				dto.setRemark(servicecode.getRemark());
				list2.add(dto);
			}
		}
		return list2;
	}

	@Override
	public Result modifyOn(Long id) {
		FmsServiceCode entity = fmsServiceCodeDao.findById(id);
		if (null == entity) {
			return Result.badResult("数据不存在");
		}
		List<FmsServiceCodeChannel> channel = fmsServiceCodeChannelDao.getListByServiceCodeId(id);
		if (null == channel || channel.size() == 0) {
			return Result.badResult("服务号未绑定通道");
		}
		entity.setState(FmsServiceCode.STATE_ENABLE);
		fmsServiceCodeDao.update(entity);
		return Result.rightResult(entity.getServiceCode());
	}

	@Override
	public Result modifyOff(Long id) {
		FmsServiceCode entity = fmsServiceCodeDao.findById(id);
		if (null == entity) {
			return Result.badResult("数据不存在");
		}
		entity.setState(FmsServiceCode.STATE_DISABLE);
		fmsServiceCodeDao.update(entity);
		return Result.rightResult(entity.getServiceCode());
	}
}
