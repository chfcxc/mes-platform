package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDetailDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsAccountDetails;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsAccountDetailDao;

@Repository
public class FmsAccountDetailsDaoImpl extends PojoDaoImpl<FmsAccountDetails> implements FmsAccountDetailDao {

	@Override
	public Page<AccountDetailDTO> findlist(Long serviceCodeId, Long businessId, int start, int limit, Date startTime, Date endTime, int operationType) {
		String sql = " select a.id,a.enterprise_id,a.service_code_id,a.user_id,a.app_id,a.change_number,a.remaining_number,a.operation_type,a.remark, a.operation_time, "
				+ " s.business_type_id,s.service_code from fms_account_details a,fms_service_code s where a.service_code_id=s.id ";
		List<Object> list = new ArrayList<Object>();
		if (serviceCodeId != 0L) {
			sql += " and a.service_code_id =? ";
			list.add(serviceCodeId);
		}
		if (operationType != -1) {
			sql += " and a.operation_type =? ";
			list.add(operationType);
		}
		if (businessId != 0L) {
			sql += " and s.business_type_id = '" + businessId + "'";
		}
		if (null != startTime) {
			sql += " and a.operation_time >= ?";
			list.add(startTime);
		}
		if (null != endTime) {
			sql += " and a.operation_time <= ?";
			list.add(endTime);
		}
		sql += " order by a.id desc ";
		return this.findSqlForPageForMysql(AccountDetailDTO.class, sql, list, start, limit);
	}

}
