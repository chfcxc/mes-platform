package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.enterprise.EnterpriseUserDTO;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;

/**
 * cn.emay.eucp.common.moudle.db.system.Enterprise Service Super
 * 
 * @author frank
 */
public interface EnterpriseService {

	public List<Enterprise> getEnterpriseForList();

	public Enterprise getEnterpriseById(Long id);

	public List<Enterprise> findListByName(String enterpriseName);

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
	 * @param clientName
	 * @return
	 */
	Page<Map<String, Object>> findPage(int start, int limit, String clientName);

	Page<Map<String, Object>> findPage(int start, int limit, String clientName, String type);

	public List<Enterprise> findListUseChannel(Long channelId);

	public Enterprise findByClientNumber(String clientNumber);

	public Result findEnterpriseByClientNumber(String clientNumber);

	public Enterprise findById(Long enterpriseId);

	/**
	 * 判断客户编号是否存在
	 * 
	 * @param clientNumber
	 * @return
	 */
	Boolean isExist(String clientNumber);

	/**
	 * 新增客户
	 * 
	 * @param valueAddedService
	 *            TODO
	 * @param serviceType
	 */
	Result add(String type, String clientNumber, String clientName, String userName, String linkman, String mobile, User user, String email, int isVip, String telephone, String address, Long salesId,
			int authority, String valueAddedService, int viplevel, String serviceType,Date startTime,Date endTime);

	/**
	 * 修改客户
	 * 
	 * @param valueAddedService
	 *            TODO
	 * @param viplevel
	 * @param serviceType
	 * 
	 */
	Result modify(String type, Long id, String clientName, String linkman, String mobile, User user, String email, int isVip, String telephone, String address, Long salesId, int authority,
			String valueAddedService, int viplevel, String serviceType,Date startTime,Date endTime);

	/**
	 * 查看客户账号详情
	 * 
	 * @param id
	 * @return
	 */
	Map<String, Object> findClientInfo(Long id);

	/**
	 * 根据企业名称判断企业是否存在
	 * 
	 * @param enterpriseName
	 * @return
	 */
	public Enterprise isExistByName(String enterpriseName);

	/**
	 * 根据企业属性判断企业是否存在
	 * 
	 * @param fieldName
	 * @param value
	 * @return
	 */
	Boolean isExistByProperty(String fieldName, Object value);

	/**
	 * 根据id查询企业信息
	 * 
	 * @param id
	 * @return
	 */
	Map<String, Object> findEnterpriseInfo(Long id);

	public Enterprise findEnterpriseByName(String enterpriseName);

	/**
	 * 根据用户查询企业id
	 * 
	 * @param userId
	 * @return
	 */
	public Result findEnterpriseByUserId(Long userId);

	Enterprise findByUserId(Long userId);

	public boolean findemail(String email, Long id);

	public Enterprise findEnterpriseByServiceCode(String serviceCode);

	public List<Enterprise> getEnterprises(Boolean isDelete);

	List<Enterprise> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize);

	/**
	 * 批量修改企业余额
	 */
	void batchUpdateBalance(List<Object[]> list);

	List<Enterprise> getEnterpriseByNameAndClientNumber(String Name, String clientNumber);

	List<Enterprise> getEnterprisesByType(String nameCn, String type);

	/**
	 * 根据ids 查询企业
	 * 
	 * @param idset
	 * @return
	 */
	public List<Enterprise> findByIds(Set<Long> idset);

	/**
	 * 根据企业名称和 clientNumber 查询
	 */
	public List<Enterprise> findListByNameAndClientNumber(String enterpriseName, String clientNumber);

	public List<Long> findEnterpriseIdListByName(String enterpriseName);

	public List<EnterpriseUserDTO> findListByNameAndRealName(String enterpriseName, String saleManager, Set<Long> enterpriseIdSet);

	/**
	 * 根据企业名称和开通服务 查询
	 */
	public Page<Map<String, Object>> findPageByServiceType(int start, int limit, String enterpriseName, String serviceType);

}
