package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsBatchPageDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBatch;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsBatchDao;
import cn.emay.util.DateUtil;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:02:05 类说明 */
@Repository
public class FmsBatchDaoImpl extends PojoDaoImpl<FmsBatch> implements FmsBatchDao {

	@Override
	public Page<FmsBatchPageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, List<Long> contentIds, int start, int limit, Date startTime,
			Date endTime, Set<Long> set) {
		String sql = "";
		List<Object> list = new ArrayList<Object>();
		if (contentIds != null && contentIds.size() > 0) {
			sql += "select m.*,fbt.save_type from fms_batch m,fms_business_type fbt where m.content_type_id=fbt.id and fbt.id in ("
					+ org.apache.commons.lang3.StringUtils.join(contentIds.toArray(), ",") + ")";
		} else {
			sql += "select m.*,fbt.save_type from fms_batch m,fms_business_type fbt where 1=1 ";
		}
		if (!StringUtils.isEmpty(batchNumber)) {
			sql += " and m.batch_number =? ";
			list.add(batchNumber);
		}
		if (!StringUtils.isEmpty(title)) {
			sql += " and m.title =? ";
			list.add(title);
		}
		if (serviceCodeId != 0L) {
			sql += " and m.service_code_id =? ";
			list.add(serviceCodeId);
		}
		if (state != -1) {
			sql += " and m.state =? ";
			list.add(state);
		}
		if (infoType != -1) {
			sql += " and m.template_type =? ";
			list.add(infoType);
		}
		if (null != startTime) {
			String starttime = DateUtil.toString(startTime, "yyyy-MM-dd HH:mm:ss");
			sql += " and m.submit_time >= ?";
			list.add(starttime);
		}
		if (null != endTime) {
			String endtime = DateUtil.toString(endTime, "yyyy-MM-dd HH:mm:ss");
			sql += " and m.submit_time <= ?";
			list.add(endtime);
		}
		if (!set.isEmpty()) {
			sql += " and m.service_code_id in ( " + org.apache.commons.lang3.StringUtils.join(set.toArray(), ",") + " )";
		}
		sql += " order by m.id desc ";
		return this.findSqlForPageForMysql(FmsBatchPageDto.class, sql, list, start, limit);
	}

	@Override
	public FmsBatch findBySerialNumber(String batchNumber) {
		return this.findByProperty("batchNumber", batchNumber);
	}

}
