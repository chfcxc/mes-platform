package cn.emay.eucp.data.service.fms;

import java.util.List;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypePage;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:31:46 类说明 */
public interface FmsBusinessTypeService {

	public Page<FmsBusinesTypePage> findPage(Long busiId, int saveType, Long contentId, int start, int limit);

	List<FmsBusinessType> findBusiName(Long busiId);

	List<FmsBusinessType> findContent(Long contentId);

	public void save(FmsBusinessType fmsBusinessType);

	public void delete(Long id);

	public void update(FmsBusinessType fmsBusinessType);

	List<FmsBusinessType> findBusinessName();

	public List<FmsBusinesTypeDto> findIds(Set<Long> ids);

	List<FmsBusinessType> findList();

	public List<FmsBusinessType> findByid(Set<Long> ids);

	public List<FmsBusinessType> findByIdAndName(int saveType, String conntentName, String businessType);

	FmsBusinessType findbyId(Long id);

	FmsBusinessType findParentId(Long id);

	public List<FmsBusinessType> findContentByBusi(int saveType, Long busiId);

	FmsBusinesTypeDto findbyBusiness(Long id);

}
