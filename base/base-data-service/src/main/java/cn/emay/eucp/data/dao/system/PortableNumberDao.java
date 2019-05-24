package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.PortableNumber;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface PortableNumberDao extends BaseSuperDao<PortableNumber> {

	public List<PortableNumber> findLimit(int currentPage, int pageSize);

	public List<PortableNumber> findByLastUpdateTime(Date date);

	public List<PortableNumber> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	public Page<PortableNumber> findall(int start, int limit, String mobile);

	public void saveBatchBySql(List<PortableNumber> entities);

	public void updateBatchBySql(List<PortableNumber> entities);

	public void deleteBatchBySql(List<String> mobiles);

	public Set<String> findByMobiles(List<String> mobiles);

	public int deleteByLastUpdateTime(Date date);

	public List<PortableNumber> findPageById(long startId, long endId);

	public Map<String, PortableNumber> findPortableNumberByMobiles(List<String> mobiles);
}
