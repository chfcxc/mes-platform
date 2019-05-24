package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.enterprise.EnterpriseUserDTO;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.Enterprise Dao super
 * 
 * @author frank
 */
public interface EnterpriseDao extends BaseSuperDao<Enterprise> {

	List<Enterprise> findListByName(String enterpriseName);

	/**
	 * 分页查询企业信息
	 * 
	 * @param start
	 * @param limit
	 * @param entity
	 * @return
	 */
	Page<Enterprise> findPage(int start, int limit, Enterprise entity);

	/**
	 * 分页查询客户编号、客户名称
	 * 
	 * @param start
	 * @param limit
	 * @param entity
	 * @return
	 */
	Map<String, Object> findPage(int start, int limit, String clientName);

	Map<String, Object> findPageByServiceType(int start, int limit, String clientName, String serviceType);

	Map<String, Object> findPage(int start, int limit, String clientName, String type);

	List<Enterprise> findListUseChannel(Long channelId);

	Enterprise findByClientNumber(String clientNumber);

	Enterprise findeByName(String enterpriseName);

	/**
	 * 根据属性查询企业信息
	 * 
	 * @param fieldName
	 * @param value
	 * @return
	 */
	Enterprise findEnterpriseByProperty(String fieldName, Object value);

	/**
	 * 根据id查询企业信息
	 * 
	 * @param id
	 * @return
	 */
	Object findEnterpriseInfo(Long id);

	Enterprise findEnterpriseByName(String enterpriseName);

	/**
	 * 根据用户id获取企业
	 * 
	 * @param userId
	 * @return
	 */
	Enterprise findEnterpriseByUserId(Long userId);

	Enterprise findEmail(String email);

	public Enterprise findEnterpriseByServiceCode(String serviceCode);

	List<Enterprise> getEnterprises(Boolean isDelete);

	List<Enterprise> getEnterpriseByNameOrClientNumber(String NameOrClientNumber);

	public List<Enterprise> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize);

	List<Map<String, Object>> getEnterpriseByIds(String ids);

	/**
	 * 批量修改企业余额
	 */
	void batchUpdateBalance(List<Object[]> list);

	List<Enterprise> getEnterpriseByNameAndClientNumber(String Name, String clientNumber);

	List<Enterprise> getEnterprisesByType(String nameCn, String type);

	/**
	 * 更加企业id查询企业
	 * 
	 * @param idset
	 * @return
	 */
	List<Enterprise> findByids(Set<Long> idset);

	List<Enterprise> findListByNameAndClientNumber(String enterpriseName, String clientNumber);

	List<EnterpriseUserDTO> findListByNameAndRealName(String enterpriseName, String saleManager, Set<Long> enterpriseIdSet);
}
