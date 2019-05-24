package cn.emay.eucp.data.service.fms;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsAccount;

public interface FmsAccountService {
	Page<AccountDTO> findClientList(String appId, Long serviceCodeId, int start, int limit, Long businessTypeId, Long enterpriseId);

	Page<AccountDTO> findList(String appId, Long serviceCodeId, int start, int limit, Long businessTypeId, List<Long> enterpriseId);

	FmsAccount findbyId(Long id);

	void update(FmsAccount fmsAccount);

	void updateBalance(List<FmsAccount> updateList);
}
