package cn.emay.eucp.data.service.fms;

import java.util.List;
import java.util.Map;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;

public interface FmsUserServiceCodeAssignService {

	List<FmsServiceCode> findByAssignUserId(Long userId);

	Result addFmsUserSCAssign(Long userId, Long enterpriseId, String serviceCodeIds);

	Map<String, Object> findBinding(Long userId, Long enterpriseId);

	Result modifyfmsUserSCAssign(Long userId, Long enterpriseId, String imsServiceCodeIds);

	Result deleteImsBind(Long userId, Long serviceCodeId);

	List<FmsServiceCode> findByAssignUserId(Long userId, int start, int end);

}
