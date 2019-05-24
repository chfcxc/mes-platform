package cn.emay.eucp.data.service.system;

import java.util.List;
import java.util.Map;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.enterprise.EnterpriseBindingSaleDTO;
import cn.emay.eucp.common.moudle.db.system.EnterpriseBindingSale;
import cn.emay.eucp.common.moudle.db.system.User;

/**
 * 企业与人员关系Service
 * 
 * @author ChenXiyao
 * 
 */
public interface EnterpriseBindingSaleService {

	/**
	 * 绑定关系查询
	 * 
	 * @param clientUserNameAndCode
	 * @param saleMan
	 * @param start
	 * @param limit
	 * @return
	 */
	Page<EnterpriseBindingSale> selectRelationship(String clientUserNameAndCode, String saleMan, Integer start, Integer limit);

	/**
	 * 绑定关系
	 * 
	 * @param clientUserNameAndCode
	 * @param saleMan
	 * @param userId
	 * @param enterpriseIds
	 * @param user
	 * @param isAll
	 * @return
	 */
	String relationship(String clientUserNameAndCode, String saleMan, String userId, String enterpriseIds, User user, String isAll);

	/**
	 * 保存
	 * 
	 * @param enterpriseBindingSale
	 */
	void saveEnterpriseBindingSale(EnterpriseBindingSale enterpriseBindingSale);

	/**
	 * 客户端查询
	 * 
	 * @param userName
	 * @param ids
	 * @param clientUserNameAndCode
	 * @param start
	 * @param limit
	 * @return
	 */
	Page<EnterpriseBindingSale> selectCientManage(String userName, String ids, String clientUserNameAndCode, Integer start, Integer limit);

	/**
	 * 置顶
	 * 
	 * @param enterpriseBindingSaleId
	 * @return
	 */
	String stickCientManage(String userName, String enterpriseBindingSaleId);

	/**
	 * 保存
	 * 
	 * @param params
	 * @return
	 */
	int[] saveBatchEnterpriseBindingSale(List<Object[]> params);

	List<Long> getEnterpriseIdsBySaleId(String userIds);

	List<Long> getServiceCodeIdsBySaleId(String userIds);

	List<Long> getPlatformIdsBySaleId(String userIds);

	Map<Long, String> selectAllRelationshipForflow();
	
	Map<Long,Long> findByEnterIds(List<Long> enterpriseId);

	void saveBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList);

	void updateBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList);

}
