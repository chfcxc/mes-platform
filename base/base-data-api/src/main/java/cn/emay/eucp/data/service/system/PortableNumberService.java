package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;

/**
 * 携号转网管理服务
 * 
 * @author cbb
 * 
 */
public interface PortableNumberService {
	// 携号转网分页查询
	List<PortableNumber> findLimit(int currentPage, int pageSize);

	// 携号转网按更新时间查询
	List<PortableNumber> findByLastUpdateTime(Date date);

	List<PortableNumber> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	// 分页查询
	Page<PortableNumber> findall(int start, int limit, String mobile);

	// 插入
	void save(PortableNumber entity);

	// 批量插入
	void addBatch(List<PortableNumber> entities);

	// 批量更新
	void updateBatch(List<PortableNumber> entities);

	// 批量删除
	void deleteBatch(List<String> mobiles);

	// 根据手机号查询
	Set<String> findByMobiles(List<String> mobiles);

	// 按照更新时间删除
	int deleteByLastUpdateTime(Date date);

	// 携号转网分页查询
	List<PortableNumber> findPageById(long startId, long endId);

	// 根据手机号查询对象
	Map<String, PortableNumber> findPortableNumberByMobiles(List<String> mobiles);

}
