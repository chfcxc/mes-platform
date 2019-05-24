package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.base.FmsBlacklistDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:29:54 类说明 */
public interface FmsBlacklistService {

	Page<FmsBlacklistDto> findPage(String mobile, int start, int limit);

	public void deletebyMobiles(List<String> mobiles);

	public void deletebyMobile(String mobile);

	public Result save(FmsBlacklist fmsBlacklist);

	public FmsBlacklist findbymobile(String mobile, Long id);

	public List<FmsBlacklist> findFmsBlacklistByLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime);

}
