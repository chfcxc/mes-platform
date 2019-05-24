package cn.emay.eucp.data.dao.fms.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsAccount;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsAccountDao;

@Repository
public class FmsAccountDaoImpl extends PojoDaoImpl<FmsAccount> implements FmsAccountDao {

	@Override
	public Page<AccountDTO> findAccount(String appId, Long serviceCodeId, Long businessTypeId, int start, int limit, Long enterpriseId) {
		List<Object> list = new ArrayList<Object>();
		String sql = " select a.id,a.service_code_id,a.app_id,a.balance,b.service_code,b.business_type_id,b.remark from fms_account a,fms_service_code b where a.service_code_id=b.id ";
		if (!StringUtils.isEmpty(appId)) {
			sql += " and a.app_id =? ";
			list.add(appId);
		}
		if (serviceCodeId != 0L) {
			sql += " and a.service_code_id =? ";
			list.add(serviceCodeId);
		}
		if (enterpriseId != 0L) {
			sql += " and a.enterprise_id =? ";
			list.add(enterpriseId);
		}
		if (businessTypeId != 0L) {
			sql += " and b.business_type_id = '" + businessTypeId + "'";
		}
		sql += " order by a.id desc ";
		return this.findSqlForPageForMysql(AccountDTO.class, sql, list, start, limit);
	}

	@Override
	public Page<AccountDTO> findPage(String appId, Long serviceCodeId, Long businessTypeId, int start, int limit, List<Long> enterpriseId) {
		List<Object> list = new ArrayList<Object>();
		String sql = " select a.id,a.service_code_id,a.app_id,a.balance,a.enterprise_id,b.service_code,b.business_type_id ,b.remark from fms_account a,fms_service_code b where a.service_code_id=b.id ";
		if (!StringUtils.isEmpty(appId)) {
			sql += " and a.app_id =? ";
			list.add(appId);
		}
		if (serviceCodeId != 0L) {
			sql += " and a.service_code_id =? ";
			list.add(serviceCodeId);
		}
		if (businessTypeId != 0L) {
			sql += " and b.business_type_id = '" + businessTypeId + "'";
		}
		if (enterpriseId != null && enterpriseId.size() > 0) {
			sql += " and b.enterprise_id in ('" + StringUtils.join(enterpriseId.toArray(), ",") + "')";
		}
		sql += " order by a.id desc ";
		return this.findSqlForPageForMysql(AccountDTO.class, sql, list, start, limit);
	}

	@Override
	public void saveBatchBind(Long enterpriseId, String sn) {
		List<Object[]> paramsList = new ArrayList<Object[]>();
		String sql = "INSERT INTO fms_account (enterprise_id,service_code_id,app_id,balance) SELECT ?,id as service_code_id,app_id,0 FROM fms_service_code where app_id=?";
		paramsList.add(new Object[] { enterpriseId, sn });
		this.jdbcTemplate.batchUpdate(sql, paramsList);

	}

	@Override
	public void updateBalance(List<FmsAccount> list) {
		String sql = " update fms_account set balance=? where service_code_id=? and balance != ?";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				FmsAccount account = list.get(i);
				ps.setLong(1, account.getBalance());
				ps.setLong(2, account.getServiceCodeId());
				ps.setLong(3, account.getBalance());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});

	}

}
