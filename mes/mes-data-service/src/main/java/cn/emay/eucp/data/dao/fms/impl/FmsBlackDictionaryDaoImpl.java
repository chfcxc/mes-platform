package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;
import cn.emay.eucp.common.util.RegularCheckUtils;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsBlackDictionaryDao;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:02:05 类说明 */
@Repository
public class FmsBlackDictionaryDaoImpl extends PojoDaoImpl<FmsBlackDictionary> implements FmsBlackDictionaryDao {

	@Override
	public Page<FmsBlackDictionary> findPage(String content, int start, int limit) {
		String hql = "select b from FmsBlackDictionary b where b.isDelete=0 ";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(content)) {
			hql += " and b.content like:content ";
			map.put("content", "%" + RegularCheckUtils.specialCodeEscape(content) + "%");
		}
		hql += " order by b.id desc";
		return this.getPageResult(hql, start, limit, map, FmsBlackDictionary.class);
	}

	@Override
	public FmsBlackDictionary findbyName(String content) {
		String hql = "select b from FmsBlackDictionary b where b.isDelete=0 and b.content =:content ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", content);
		return this.getUniqueResult(FmsBlackDictionary.class, hql, map);
	}

	@Override
	public List<FmsBlackDictionary> findFmsBlackDictionaryDTOForPageByIsDeleteAndLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime, Boolean isDelete) {
		String sql = "select id,content,is_delete from fms_black_dictionary  where 1 = 1  ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != isDelete) {
			sql += " and is_delete = ? ";
			parameters.add(isDelete);
		}
		if (null != lastUpdateTime) {
			sql += " and last_update_time >= ? ";
			parameters.add(lastUpdateTime);
		}
		List<FmsBlackDictionary> smsBlackDictionaryDTOList = this.findSqlForListForMysql(FmsBlackDictionary.class, sql, parameters, currentPage, pageSize);
		return smsBlackDictionaryDTOList;
	}

	@Override
	public void deletebyContent(String content) {
		String delSql = " delete from fms_balck_dictionary where is_delete =1 and content= ? ";
		this.jdbcTemplate.update(delSql, new Object[] { content });
		String sql = " update fms_balck_dictionary set is_delete =1 where content=?";
		this.jdbcTemplate.update(sql, new Object[] { content });
	}

}
