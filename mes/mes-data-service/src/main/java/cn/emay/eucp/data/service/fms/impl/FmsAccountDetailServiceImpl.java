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
import cn.emay.eucp.common.dto.fms.account.AccountDetailDTO;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsAccountDetails;
import cn.emay.eucp.data.dao.fms.FmsAccountDetailDao;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.service.fms.FmsAccountDetailService;

@Service("fmsAccountDetailService")
public class FmsAccountDetailServiceImpl implements FmsAccountDetailService {
	@Resource
	private FmsAccountDetailDao fmsAccountDetailDao;
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;

	@Override
	public void save(FmsAccountDetails fmsAccountDetails) {
		fmsAccountDetailDao.save(fmsAccountDetails);
	}

	@Override
	public Page<AccountDetailDTO> findlist(Long serviceCodeId, Long businessId, int start, int limit, Date startTime, Date endTime, int operationType) {
		Page<AccountDetailDTO> page = fmsAccountDetailDao.findlist(serviceCodeId, businessId, start, limit, startTime, endTime, operationType);
		Set<Long> businessTypeId = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDetailDTO dto : page.getList()) {
				businessTypeId.add(dto.getBusinessTypeId());
			}
		}
		List<Object[]> findIds = fmsBusinessTypeDao.findIds(businessTypeId);
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
			for (AccountDetailDTO dto : page.getList()) {
				FmsBusinesTypeDto typeDto = map.get(dto.getBusinessTypeId());
				dto.setBusinessType(typeDto.getParentName());
				dto.setSaveType(typeDto.getSaveType());
				dto.setContentType(typeDto.getName());
			}
		}
		return page;
	}

}
