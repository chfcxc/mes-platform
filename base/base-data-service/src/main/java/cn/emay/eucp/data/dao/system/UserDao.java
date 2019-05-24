package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.user.RoleDTO;
import cn.emay.eucp.common.dto.user.SimpleUserDTO;
import cn.emay.eucp.common.dto.user.TurnOperatorUserDTO;
import cn.emay.eucp.common.dto.user.UserBindingDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.dto.user.UserRoleDTO;
import cn.emay.eucp.common.dto.user.WarningUserDTO;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.User Dao super
 * 
 * @author frank
 */
public interface UserDao extends BaseSuperDao<User> {

	/**
	 * 根据用户名查找用户
	 */
	User findUserByUserName(String username);

	/**
	 * 查找用户最后更新时间
	 */
	Date findLastUpdateTime(Long userId);

	/**
	 * 查找用户所有的角色
	 */
	List<RoleDTO> findAllUserRoles(Long userId);

	/**
	 * 查找企业管理员用户
	 */
	User findUserManage(Long enterpriseId);

	/**
	 * 查找用户所属企业ID
	 */
	Long findenterpriseId(Long userId);

	/**
	 * 根据用户ID查找用户DTO
	 */
	UserDTO findUserDTOById(Long userId);

	/**
	 * 查询企业用户信息
	 */
	List<?> findEnterpriseUser(Long enterpriseId);

	/**
	 * 批量更新用户状态
	 */
	void udpateUdateState(List<Long> userIds, int state);

	/**
	 * 查询用户名是否重复
	 */
	Long countByUserName(Long userId, String username);

	/**
	 * 查询手机号是否重复
	 */
	Long countByMobile(Long userId, String mobile);

	/**
	 * 查询邮箱是否重复
	 */
	Long countByEmail(Long userId, String email);

	/**
	 * 查询Emay用户列表
	 */
	Page<UserDTO> findUserPageByManager(String username, String realname, String mobile, int start, int limit);

	/*-----------------------------------*/

	Page<User> findBycondition(String variableName, Long departmentId, int start, int limit);

	List<User> countByUserNames(List<String> list, String roleType);
	
	List<User> countByRealNames(List<String> list, String roleType);
	
	List<User> countByUserIds(List<Long> list, String roleType);

	List<?> findUserSByIds(Set<Long> idsSet);

	/**
	 * 查询企业管理账户信息
	 * 
	 * @param enterpriseId
	 * @return
	 */
	Object findMangerAccount(Long enterpriseId);

	/**
	 * 更新用户信息
	 * 
	 * @param id
	 * @param linkman
	 * @param mobile
	 * @param operatorUserId
	 */
	void updateUser(Long id, String linkman, String mobile, Long operatorUserId, String email);

	/**
	 * 
	 * 客户端查询全部
	 * 
	 * @param username
	 * @param realname
	 * @param mobile
	 * @return
	 */

	Page<UserDTO> findall(int start, int limit, String username, String realname, String mobile, Long enterpriseId, Long userId);

	/**
	 * 停用 启用 删除
	 * 
	 */
	void updatestate(Long id, int state);

	/**
	 * 
	 * 手机号 去重
	 */
	Boolean findmobile(Long id, String mobile);

	/**
	 * 
	 * 邮箱去重
	 * 
	 * @param id
	 * @param email
	 * @return
	 */
	Boolean findemail(Long id, String email);

	Boolean finduserName(Long id, String username);

	User findBymobile(String mobile);

	void updatepassword(String password, Long id, Date lastChangePasswordTime);

	List<UserRoleDTO> finduserAndRole(Long id);

	/**
	 * 客户端首页查询子账号信息
	 * 
	 * @param enterpriseId
	 * @return
	 */
	List<UserDTO> findEnterpriseChild(Long enterpriseId);

	/**
	 * 客户端根据当前登录用户ID 查找同企业下的所有用户
	 * 
	 * @param currentUserId
	 * @return
	 */
	List<String> findEnterpriseUsers(Long currentUserId);

	/**
	 * 根据用户id 查询所在企业下的用户总数
	 * 
	 * @param id
	 * @return
	 */
	long findCilentCount(Long id);

	Page<UserDTO> findUserPage(String userName, String realName, String mobile, Integer accountType, Integer state, String userType, String clientName, String clientNumber, int start, int limit);

	List<WarningUserDTO> findUserSByIdsIgnoreState(Set<Long> idsSet);

	/**
	 * 
	 * 查看客户信息详情
	 * 
	 * @param id
	 * @return
	 */
	UserDTO findClientUserById(Long id);

	List<TurnOperatorUserDTO> findAllTurnOperatorUser(int currentPage, int pageSize, Date date, Boolean isDelete);

	/**
	 * 查询用户名是以某字符串结尾的用户
	 * 
	 * @param endStr
	 * @return
	 */
	List<String> findUserEndWithStr(String endStr);

	/**
	 * 根据角色类型查找用户
	 */
	List<User> findUserByRoleType(String roleType, String userType, Date date, String username);

	/**
	 * 根据用户ID更新最后修改时间
	 * 
	 * @param id
	 * @return
	 */
	boolean updateLastUpdateTimeById(String id);

	/**
	 * 根据角色类型查找用户
	 */
	Page<UserDTO> findUserPageByRoleType(String roleType, String userType, String username, int start, int limit);

	/**
	 * 根据用户名或者真实姓名查询userid
	 * 
	 * @param UsernameOrRealname
	 * @return
	 */
	List<Map<String, Object>> findUserIdByUsernameOrRealname(String UsernameOrRealname);

	List<User> findUserByUserType(String userType);

	List<User> findUserByLastUpdateTime(Date lastUpdateTime, String userType, int currentPage, int pageSize);

	/**
	 * 根据用户名 模糊查询用户
	 * 
	 * @param userName
	 * @return
	 */
	List<User> findByUserName(String userName);

	/**
	 * 根据用户ids 查询用户
	 * 
	 * @param userIds
	 * @return
	 */
	List<User> findUserByIds(Set<Long> idsSet);

	Page<Map<String, Object>> findServiceCodeBindingPageClient(int start, int limit, String userName, Long userId, Long enterpriseId);

//	List<Long> findUserBindServiceCodeClient(Long userId);

	List<Long> findEnterpriseBindServiceCode(Long userId);
	
	Page<UserBindingDTO> findUserInfoPage(int start, int limit, String userName, List<Long> userIdList);

	List<SimpleUserDTO> findByUserType(String userType);

	List<User> findByRealNames(List<String> list, String userType);
	List<Long> findUserIdbyEnterpriseId(Long enterpriseId);
}
