package cn.emay.eucp.data.dao.fms;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDetailDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsAccountDetails;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsAccountDetailDao extends BaseSuperDao<FmsAccountDetails> {

	Page<AccountDetailDTO> findlist(Long serviceCodeId, Long businessId, int start, int limit, Date startTime, Date endTime, int operationType);

}
