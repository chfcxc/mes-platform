package cn.emay.eucp.data.dao.fms;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsAccount;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsAccountDao extends BaseSuperDao<FmsAccount> {

	Page<AccountDTO> findAccount(String appId, Long serviceCodeId, Long businessTypeId, int start, int limit, Long enterpriseId);

	Page<AccountDTO> findPage(String appId, Long serviceCodeId, Long businessTypeId, int start, int limit, List<Long> enterpriseId);

	void saveBatchBind(Long enterpriseId, String sn);

	void updateBalance(List<FmsAccount> list);
}
