package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeInfoDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.GeneratorSerciceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsServiceCodeDao extends BaseSuperDao<FmsServiceCode> {

	List<FmsServiceCode> findByEnterId(Long enterpriseId, int state);

	List<FmsServiceCode> findByIds(Long enterpriseId, List<Long> ids);

	Page<FmsServiceCode> findlist(int start, int limit, String serviceCode, String appId, Long businessTypeId, Long enterpriseId);

	Page<FmsServiceCode> findPage(int start, int limit, String serviceCode, String appId, Long businessTypeId, Set<Long> enterpriseId, int state, Long serviceCodeId);

	public Set<String> findExitAppid(String appId);

	void saveBatchServiceCode(GeneratorSerciceCodeDTO serviceSourceCode);

	FmsServiceCodeInfoDTO findbyid(Long id);

	List<FmsServiceCode> findByUserId(Long userId);

	List<FmsServiceCode> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	FmsServiceCode findByserviceCode(String appId);

	List<FmsServiceCode> findByIds(Set<Long> ids);

}
