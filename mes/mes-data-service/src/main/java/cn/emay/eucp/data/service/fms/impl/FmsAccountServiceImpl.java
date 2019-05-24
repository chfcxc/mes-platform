package cn.emay.eucp.data.service.fms.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDTO;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsAccount;
import cn.emay.eucp.data.dao.fms.FmsAccountDao;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.service.fms.FmsAccountService;

@Service("fmsAccountService")
public class FmsAccountServiceImpl implements FmsAccountService {
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsAccountDao fmsAccountDao;

	@Override
	public Page<AccountDTO> findClientList(String appId, Long serviceCodeId, int start, int limit, Long businessTypeId, Long enterpriseId) {
		Page<AccountDTO> page = fmsAccountDao.findAccount(appId, serviceCodeId, businessTypeId, start, limit, enterpriseId);
		Set<Long> businessId = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDTO dto : page.getList()) {
				businessId.add(dto.getBusinessTypeId());
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
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDTO dto : page.getList()) {
				FmsBusinesTypeDto typeDto = map.get(dto.getBusinessTypeId());
				dto.setBusinessType(typeDto.getParentName());
				dto.setSaveType(typeDto.getSaveType());
				dto.setContentType(typeDto.getName());
			}
		}
		return page;
	}

	@Override
	public Page<AccountDTO> findList(String appId, Long serviceCodeId, int start, int limit, Long businessTypeId, List<Long> enterpriseId) {
		Page<AccountDTO> page = fmsAccountDao.findPage(appId, serviceCodeId, businessTypeId, start, limit, enterpriseId);
		Set<Long> businessId = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDTO dto : page.getList()) {
				businessId.add(dto.getBusinessTypeId());
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
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDTO dto : page.getList()) {
				FmsBusinesTypeDto typeDto = map.get(dto.getBusinessTypeId());
				dto.setBusinessType(typeDto.getParentName());
				dto.setSaveType(typeDto.getSaveType());
				dto.setContentType(typeDto.getName());
			}
		}
		return page;
	}

	@Override
	public FmsAccount findbyId(Long id) {
		return fmsAccountDao.findById(id);
	}

	@Override
	public void update(FmsAccount fmsAccount) {
		fmsAccountDao.update(fmsAccount);
	}

	@Override
	public void updateBalance(List<FmsAccount> updateList) {
		fmsAccountDao.updateBalance(updateList);
	}

}
