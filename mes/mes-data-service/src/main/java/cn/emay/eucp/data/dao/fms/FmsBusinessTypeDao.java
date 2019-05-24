package cn.emay.eucp.data.dao.fms;

import java.util.List;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypePage;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsBusinessTypeDao extends BaseSuperDao<FmsBusinessType> {

	List<FmsBusinessType> findByIds(Long... ids);

	List<FmsBusinessType> findByIdAndName(int saveType, String conntentName, String businessType);

	List<Object[]> findIds(Set<Long> ids);

	List<FmsBusinessType> findBusinessName();

	public Page<FmsBusinesTypePage> findPage(Long busiId, int saveType, Long contentId, int start, int limit);

	List<FmsBusinessType> findBusiName(Long busiId);

	List<FmsBusinessType> findContent(Long contentId);

	public void delete(Long id);

	List<Long> findIds(Long busiId, Integer saveType, Long contentId);

	List<FmsBusinessType> findById(Set<Long> ids);

	FmsBusinessType findParentId(Long id);

	public List<FmsBusinessType> findContentByBusi(int saveType, Long busiId);

	FmsBusinesTypeDto findbyBusiness(Long id);

}
