package cn.emay.eucp.data.dao.system;

import java.util.List;
import java.util.Map;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.enterprise.EnterpriseBindingSaleDTO;
import cn.emay.eucp.common.moudle.db.system.EnterpriseBindingSale;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * 企业与人员关系Dao
 * 
 * @author ChenXiyao
 * 
 */
public interface EnterpriseBindingSaleDao extends BaseSuperDao<EnterpriseBindingSale> {

	Page<EnterpriseBindingSale> selectRelationship(List<String> systemEnterpriseIdList, List<String> systemUserIdList, Integer start, Integer limit);

	List<Map<String, Object>> findRelationships(String enterpriseIds);

	boolean deleteRelationships(String enterpriseIds);

	boolean deleteSystemEnterpriseBindingSaleTop(String enterpriseIds);

	List<Map<String, Object>> findAllRelationship(String systemEnterpriseId, String systemUserId);

	Page<EnterpriseBindingSale> selectCientManage(String userName, String systemEnterpriseId, Integer start, Integer limit);

	int stickCientManageFindMaxNum(String userName);

	boolean stickCientManageUpdate(String userName, String enterpriseBindingSaleId, String newMaxNum);

	boolean stickCientManageInsert(String userName, String enterpriseBindingSaleId, String newMaxNum);

	List<Map<String, Object>> stickCientManageSelect(String userName, String enterpriseBindingSaleId);

	int[] saveBatchEnterpriseBindingSale(List<Object[]> params);

	List<Map<String, Object>> findByEnterpriseId(Long id);

	Map<String, Object> findByEnterpriseBindingSaleId(Long id);

	EnterpriseBindingSale findByfieldName(String fieldName, Object value);

	List<Long> getEnterpriseIdsBySaleId(String userIds);

	List<Long> getServiceCodeIdsBySaleId(String userIds);

	List<Long> getPlatformIdsBySaleId(String userIds);

	void deleteByfieldName(String fieldName, Object value);

	List<Long> getIdsBySaleId(String userIds);

	void saveBatchSystemEnterpriseBindingSaleTop(List<EnterpriseBindingSale> entities);

	List<Map<String, Object>> selectAllRelationshipForflow();
	
		List<EnterpriseBindingSaleDTO> findByEnterIds(List<Long> enterpriseId);

	void saveBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList);

	void updateBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList);
}
