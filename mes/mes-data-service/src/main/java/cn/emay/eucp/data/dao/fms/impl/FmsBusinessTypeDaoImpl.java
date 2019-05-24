package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypePage;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;

@Repository
public class FmsBusinessTypeDaoImpl extends PojoDaoImpl<FmsBusinessType> implements FmsBusinessTypeDao {

	@Override
	public List<FmsBusinessType> findByIds(Long... ids) {
		String hql = "from FmsBusinessType where 1 = 1 ";
		if (null != ids && ids.length > 0) {
			hql += " and id in (:ids)";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			return this.getListResult(FmsBusinessType.class, hql, params);
		} else {
			return new ArrayList<FmsBusinessType>();
		}
	}

	@Override
	public List<FmsBusinessType> findByIdAndName(int saveType, String conntentName, String businessType) {
		String sql = "select *  from fms_business_type where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (saveType != -1) {
			sql += " and save_type =? ";
			params.add(saveType);
		}
		if (!StringUtils.isEmpty(businessType) && !StringUtils.isEmpty(conntentName)) {
			sql += " and name='" + conntentName + "' and parent_id = (select id from fms_business_type where name='" + businessType + "' and  parent_id=0) ";
		} else {
			if (!StringUtils.isEmpty(businessType)) {
				sql += " and name =? and parent_id =0 ";
				params.add(businessType);
			}
			if (!StringUtils.isEmpty(conntentName)) {
				sql += " and name =? ";
				params.add(conntentName);
			}
		}
		return this.findSqlForListObj(FmsBusinessType.class, sql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findIds(Set<Long> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select b.id,b.name,b.parent_id,b.save_type,parent.name as parenName "
				+ " from fms_business_type b,(select id,name from fms_business_type where parent_id=0) parent where parent.id= b.parent_id ";
		if (ids != null && ids.size() > 0) {
			sql += " and b.id in (:ids)";
			map.put("ids", ids);
		}
		return (List<Object[]>) this.getPageListResultBySqlByHibernate(sql, 0, 0, map);
	}

	@Override
	public List<FmsBusinessType> findBusinessName() {
		String sql = " select * from fms_business_type where parent_id !=0 ";
		return this.findSqlForListObj(FmsBusinessType.class, sql, null);
	}

	@Override
	public Page<FmsBusinesTypePage> findPage(Long busiId, int saveType, Long contentId, int start, int limit) {
		String sql = " select a.name contentName ,b.name busiName,a.save_type saveType,b.id busiId,a.id contentId from fms_business_type a , ";
		List<Object> params = new ArrayList<Object>();
		if (busiId != -1) {
			sql += " (select * from fms_business_type where level = 1 and id=? ) b ";
			params.add(busiId);
		} else {
			sql += " (select * from fms_business_type where level = 1 ) b ";
		}
		sql += " where a.parent_id= b.id and a.level = 2";
		if (contentId != -1) {
			sql += " and a.id=? ";
			params.add(contentId);
		}
		if (saveType != -1) {
			sql += " and a.save_type=? ";
			params.add(saveType);
		}
		return this.findSqlForPageForMysql(FmsBusinesTypePage.class, sql, params, start, limit);
	}

	@Override
	public List<FmsBusinessType> findBusiName(Long busiId) {
		String sql = "select * from fms_business_type where level=1 ";
		List<Object> params = new ArrayList<>();
		if (busiId != -1) {
			sql += " and id=? ";
			params.add(busiId);
		}
		return this.findSqlForListObj(FmsBusinessType.class, sql, params);
	}

	@Override
	public List<FmsBusinessType> findContent(Long contentId) {
		String sql = "select * from fms_business_type where level=2 ";
		List<Object> params = new ArrayList<>();

		if (contentId != -1) {
			sql += " and id=? ";
			params.add(contentId);
		}
		return this.findSqlForListObj(FmsBusinessType.class, sql, params);
	}

	@Override
	public void delete(Long id) {
		String sql = "delete from fms_business_type where id = " + id;
		this.execSql(sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findIds(Long businessTypeId, Integer saveType, Long contentTypeId) {
		String sql = "select id from fms_business_type where 1=1 ";
		List<Object> list = new ArrayList<Object>();
		if (saveType != -1) {
			sql += " and save_type=? ";
			list.add(saveType);
		}
		if (businessTypeId != -1 && contentTypeId != -1) {
			sql += " and id = " + contentTypeId + " and parent_id=" + businessTypeId;
		} else {
			if (businessTypeId != -1) {
				sql += " and id=? and parent_id=0 ";
				list.add(businessTypeId);
			}
			if (contentTypeId != -1) {
				sql += " and id=?  ";
				list.add(contentTypeId);
			}
		}
		return (List<Long>) this.getListResultBySql(sql, list.toArray());
	}

	@Override
	public List<FmsBusinessType> findById(Set<Long> ids) {
		String hql = "from FmsBusinessType where 1 = 1 ";
		if (null != ids && ids.size() > 0) {
			hql += " and id in (:ids)";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			return this.getListResult(FmsBusinessType.class, hql, params);
		} else {
			return new ArrayList<FmsBusinessType>();
		}
	}

	@Override
	public FmsBusinessType findParentId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from FmsBusinessType where level=1 and id=:id ";
		params.put("id", id);
		return this.getUniqueResult(FmsBusinessType.class, hql, params);
	}

	@Override
	public List<FmsBusinessType> findContentByBusi(int saveType, Long busiId) {
		String sql = "select * from fms_business_type where 1=1 ";
		List<Object> list = new ArrayList<Object>();
		if (saveType != -1) {
			sql += " and save_type=? ";
			list.add(saveType);
		}
		if (busiId != -1) {
			sql += "  and parent_id=? ";
			list.add(busiId);
		}
		return this.findSqlForListObj(FmsBusinessType.class, sql, list);
	}

	@Override
	public FmsBusinesTypeDto findbyBusiness(Long id) {
		String sql = "select b.id,b.name,b.parent_id,b.save_type,parent.name as parentName " + " from fms_business_type b,(select id,name from fms_business_type where parent_id=0) parent "
				+ " where parent.id= b.parent_id and  b.id='" + id + "'  ";
		FmsBusinesTypeDto findSqlForObj = this.findSqlForObj(FmsBusinesTypeDto.class, sql, null);
		return findSqlForObj;
	}

}
