package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeInfoDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.SimpleFmsServiceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;

public interface FmsServiceCodeService {
	List<FmsServiceCode> findByEnterId(Long enterpriseId, int state);

	FmsServiceCode findById(Long serviceCodeId);

	Page<FmsServiceCodeDto> findClientList(String appId, String service, Long businessTypeId, int start, int limit, Long enterpriseId);

	void update(FmsServiceCode fmsServiceCode);

	Page<FmsServiceCodeDto> findList(String appId, String service, int start, int limit, Set<Long> enterpriseId, Long serviceCodeId, int state, Long businessTypeId);

	Result saveCreateSericeCodeNew(SimpleFmsServiceCodeDTO scDTO);

	Result updateServiceCode(Long serviceCodeId, Integer isNeedReport, String ipConfiguration, String serviceCodeChannel, String psersonServiceCodeChannel);

	FmsServiceCodeInfoDTO findbyid(Long id);

	Result updateSecretKey(Long id);

	List<FmsServiceCode> findByUserId(Long userId);

	List<FmsServiceCode> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	FmsServiceCode findByserviceCode(String appId);

	Map<Long, FmsServiceCode> findbyIds(Set<Long> id);

	List<FmsServiceCodeDto> findFmsServiceCodeDtoByUserId(Long userId);

	Result modifyOn(Long id);

	Result modifyOff(Long id);

}
