package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.base.FmsBlacklistDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsBlacklistDao;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:02:05 类说明 */
@Repository
public class FmsBlacklistDaoImpl extends PojoDaoImpl<FmsBlacklist> implements FmsBlacklistDao {

	@Override
	public Page<FmsBlacklistDto> findPage(String mobile, int start, int limit) {
		String sql = "select * from fms_blacklist b where b.is_delete=0 ";
		List<Object> parameters = new ArrayList<Object>();
		if (!StringUtils.isEmpty(mobile)) {
			parameters.add(mobile);
			sql += " and mobile=? ";
		}
		sql += " order by b.id desc";
		return this.findSqlForPageForMysql(FmsBlacklistDto.class, sql, parameters, start, limit);
	}

	@Override
	public void deletebyMobiles(List<String> mobiles) {
		String sqlHql = "delete from FmsBlacklist where mobile in(:mobiles) and isDelete = 1";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobiles", mobiles);
		this.execByHql(sqlHql, params);
		String updateHql = "update FmsBlacklist set isDelete=:isDelete where mobile in(:mobiles) and isDelete = 0";
		Map<String, Object> updateParams = new HashMap<String, Object>();
		updateParams.put("isDelete", 1);
		updateParams.put("mobiles", mobiles);
		this.execByHql(updateHql, updateParams);
	}

	@Override
	public void deletebyMobile(String mobile) {
		String delSql = " delete from fms_blacklist where is_delete =1 and mobile= ? ";
		this.jdbcTemplate.update(delSql, new Object[] { mobile });
		String sql = " update fms_blacklist set is_delete =1 where mobile=?";
		this.jdbcTemplate.update(sql, new Object[] { mobile });
	}

	@Override
	public FmsBlacklist findbymobile(String mobile, Long id) {
		String hql = "select b from FmsBlacklist b where b.isDelete=0 ";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(mobile)) {
			hql += " and b.mobile =:mobile ";
			map.put("mobile", mobile);
		}
		if (id != 0L) {
			hql += " and b.id !=:id ";
			map.put("id", id);
		}
		return this.getUniqueResult(FmsBlacklist.class, hql, map);
	}

	@Override
	public List<FmsBlacklist> findFmsBlacklistByLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime) {
		String sql = "select id,mobile,is_delete from fms_blacklist where 1 = 1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != lastUpdateTime) {
			sql += " and last_update_time >= ? ";
			parameters.add(lastUpdateTime);
		}
		return this.findSqlForListForMysql(FmsBlacklist.class, sql, parameters, currentPage, pageSize);
	}
}
