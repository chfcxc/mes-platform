package cn.emay.eucp.data.dao.fms;

import java.util.List;

import cn.emay.eucp.common.dto.fms.serviceCode.GeneratorSerciceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsUserServiceCodeAssign;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsUserServiceCodeAssignDao extends BaseSuperDao<FmsUserServiceCodeAssign> {
	public List<FmsServiceCode> findByAssignUserId(Long userId);

	public void deleteUserSCAssignByProperty(String string, Long userId);

	public void saveBatchUserSCAssign(List<Long> serviceCodeIdList, Long userId);

	public List<FmsUserServiceCodeAssign> findUserSCAssignByProperty(String string, Long userId);

	public FmsUserServiceCodeAssign getMmsUserSCAssign(Long userId, Long serviceCodeId);

	List<FmsServiceCode> findByAssignUserId(Long userId, int start, int end);

	public void saveBatchBind(List<GeneratorSerciceCodeDTO> listServiceSourceCodes, Long userId);

}
