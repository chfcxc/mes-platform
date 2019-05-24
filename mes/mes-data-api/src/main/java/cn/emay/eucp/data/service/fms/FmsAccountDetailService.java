package cn.emay.eucp.data.service.fms;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDetailDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsAccountDetails;

public interface FmsAccountDetailService {

	void save(FmsAccountDetails fmsAccountDetails);

	Page<AccountDetailDTO> findlist(Long serviceCodeId, Long businessTypeId, int start, int limit, Date startTime, Date endTime, int operationType);

}
