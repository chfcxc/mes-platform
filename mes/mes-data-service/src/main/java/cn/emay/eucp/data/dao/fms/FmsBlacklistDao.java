package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.base.FmsBlacklistDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午5:36:36 类说明 */
public interface FmsBlacklistDao extends BaseSuperDao<FmsBlacklist> {
	Page<FmsBlacklistDto> findPage(String mobile, int start, int limit);

	public void deletebyMobiles(List<String> mobiles);

	public void deletebyMobile(String mobile);

	public FmsBlacklist findbymobile(String mobile, Long id);

	List<FmsBlacklist> findFmsBlacklistByLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime);

}
