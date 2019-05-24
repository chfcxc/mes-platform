package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.user.ClientUserIndexDTO;
import cn.emay.eucp.common.dto.user.RoleDTO;
import cn.emay.eucp.common.dto.user.SimpleUserDTO;
import cn.emay.eucp.common.dto.user.TurnOperatorUserDTO;
import cn.emay.eucp.common.dto.user.UserBindingDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.dto.user.UserRoleDTO;
import cn.emay.eucp.common.dto.user.WarningUserDTO;
import cn.emay.eucp.common.moudle.db.system.User;

/**
 * cn.emay.eucp.common.moudle.db.system.User Service Super
 * 
 * @author frank
 */
public interface UserService {

	/**
	 * 根据用户名查找用户
	 */
	User findUserByUserName(String username);

	/**
	 * 查询用户的所有角色
	 */
	List<RoleDTO> findAllUserRoles(Long userId);

	/**
	 * 查询最后更新时间
	 */
	Date findLastUpdateTime(Long id);

	/**
	 * 启用用户
	 */
	Result modifyOn(Long userId, String operUserType);

	/**
	 * 停用用户
	 */
	Result modifyOff(Long userId, String operUserType);

	/**
	 * 删除用户
	 */
	Result deleteUserByUserId(Long userId, String operUserType);

	/**
	 * 用户名是否重复
	 */
	Long countByUserName(Long userId, String username);

	/**
	 * 根据用户ID查找用户
	 */
	User findUserById(Long userId);

	/**
	 * 手机号码是否重复
	 */
	Long countByMobile(Long userId, String mobile);

	/**
	 * 邮箱是否重复
	 */
	Long countByEmail(Long userId, String email);

	/**
	 * 添加用户
	 */
	Result addUser(String username, String realname, String password, String email, String mobile, String roles, Long department, User currentUser, String userType, int identity);

	/**
	 * 修改用户
	 */
	Result modifyUser(String username, String realname, String email, String mobile, String roles, Long userId, Long departmentId, int identity);

	/**
	 * 重置用户密码
	 */
	Result updateResetUserPassword(Long userId);

	/**
	 * 根据条件查找用户(系统用户)
	 */
	Page<UserDTO> findUserPageByManager(String username, String realname, String mobile, int start, int limit);

	/*-----------------------------------*/

	/**
	 * 更新用户密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	Result updateUserPassword(Long userId, String oldPassword, String newPassword);

	/**
	 * 锁定用户
	 * 
	 * @param username
	 */
	void updateLockUserByUsername(String username);

	/**
	 * 根据条件查找用户
	 * 
	 * @param username
	 * @param realname
	 * @param mobile
	 * @param start
	 * @param limit
	 * @return
	 */

	Page<UserDTO> findUserPage(String userName, String realName, String mobile, Integer accountType, Integer state, String userType, String clientName, String clientNumber, int start, int limit);

	/**
	 * 根据条件查询此部门下的用户
	 * 
	 * @param username
	 * @param realname
	 * @param mobile
	 * @param departmentId
	 * @return
	 */
	Page<UserDTO> findBycondition(String variableName, Long departmentId, int start, int limit);

	/**
	 * 批量查询用户
	 * 
	 * @param idsList
	 * @return
	 */
	public List<WarningUserDTO> findUserSByIds(Set<Long> idsSet);

	/**
	 * 批量查询所有状态的用户
	 * 
	 * @param idsList
	 * @return
	 */
	public List<WarningUserDTO> findUserSByIdsIgnoreState(Set<Long> idsSet);

	/**
	 * 按用户名查找用户集合
	 * 
	 * @param list
	 * @return
	 */
	List<User> countByUserNames(List<String> list, String roleType);

	/**
	 * 判断账号是否是管理账号
	 * 
	 * @param userId
	 * @return
	 */
	Boolean isMangerAccount(Long userId);

	/**
	 * 判断用户是否是客户端用户
	 * 
	 * @param userId
	 * @return
	 */
	Boolean isClientUser(Long userId);

	/**
	 * 修改客户账号密码
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	Result updateClientPassword(Long userId, String password);

	/**
	 * 
	 * 客户端查询全部
	 * 
	 * @param username
	 * @param realname
	 * @param mobile
	 * @return
	 */

	Page<UserDTO> findall(int start, int limit, String username, String realname, String mobile, Long userId);

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

	Boolean findusername(Long id, String username);

	void add(User user);

	User findBymobile(String mobile);

	void update(User user);

	void updatepassword(String password, Long id);

	List<UserRoleDTO> finduserAllRole(Long id);

	/**
	 * 个人信息修改密码
	 * 
	 * @param user
	 * @param password
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	Result updatePassword(User user, String password, String newPassword, String verifyPassword);

	/**
	 * 个人信息修改名称
	 * 
	 * @param user
	 * @param realname
	 * @return
	 */
	Result updateRealName(User user, String realName);

	/**
	 * 个人信息修改手机号
	 * 
	 * @param user
	 * @param realname
	 * @return
	 */
	Result updateMoblie(User user, String mobile);

	/**
	 * 个人信息修改邮箱
	 * 
	 * @param user
	 * @param email
	 * @return
	 */
	Result updateEmail(User user, String email);

	/**
	 * 客户端首页信息查询
	 * 
	 * @param id
	 * @return
	 */
	ClientUserIndexDTO findByUserId(User user);

	/**
	 * 短信概览页面信息查询
	 * 
	 * @param user
	 * @return
	 */
//	List<Map<String, Object>> ClientSmsSendStatistics(User user);

	Long findenterpriseIdbyUser(Long userId);

	String findenterpriseTypebyUserId(Long userId);

	/**
	 * 
	 * 校验id属于当前登录用户所在的企业
	 * 
	 * @param userId
	 * @param currentUserId
	 * @return
	 */
	Result findenterpriseId(Long userId, Long currentUserId);

	/**
	 * 根据用户id 查询企业下总用户个数
	 * 
	 * @param id
	 * @return
	 */

	Result findCilentCount(Long id);

	public Result addverify(String username, String realname, String mobile, String email, Long usernumber, String roleids);

	Result updateverify(String realname, String mobile, String email, Long id, String roleids);

	/**
	 * 
	 * 查看客户信息详情
	 * 
	 * @param id
	 * @return
	 */
	UserDTO findClientUserById(Long id);

	/**
	 * 查看所有的用户 dto 调整用
	 * 
	 * @param @param currentPage
	 * @param @param pageSize
	 */
	List<TurnOperatorUserDTO> findAllTurnOperatorUser(int currentPage, int pageSize, Date date, Boolean isDelete);

	/**
	 * 重置客户账号密码
	 * 
	 * @param userId
	 * @return
	 */
	Result updateClientPassword(Long userId);

	/**
	 * 新增客户用户
	 * 
	 * @param username
	 * @param realname
	 * @param mobile
	 * @param email
	 * @param roleids
	 * @param operatorId
	 */
	Result addClientUser(String username, String realname, String mobile, String email, String roleids, Long operatorId);

	/**
	 * 根据角色类型查找用户Id
	 */
	List<User> findUserByRoleType(String roleType, String userType, Date date, String username);

	/**
	 * 分页查询用户
	 * 
	 * @param roleType
	 * @param userType
	 * @param date
	 * @param username
	 * @return
	 */
	Page<UserDTO> findUserPageByRoleType(String roleType, String userType, String username, int start, int limit);

	List<User> findUserByUserType(String userType);

	List<User> findUserByLastUpdateTime(Date lastUpdateTime, String userType, int currentPage, int pageSize);

	Object findMangerAccount(Long enterpriseId);

	/**
	 * 通过用户名 模糊查询用户
	 * 
	 * @param userName
	 * @return
	 */
	List<User> findByUserName(String userName);

	/**
	 * 
	 * @param idsSet
	 */
	List<User> findUserByIds(Set<Long> idsSet);

	/**
	 * 根据用户ID更新最后修改时间
	 * 
	 * @param id
	 * @return
	 */
	boolean updateLastUpdateTimeById(String id);

	/**
	 * 查找用户所属企业ID
	 */
//	Long findenterpriseId(Long userId);

	Page<Map<String, Object>> findServiceCodeBindingPageClient(int start, int limit, String userName, Long userId, Long enterpriseId);

	List<String> findEnterpriseUsers(Long currentUserId);

//	List<Long> findUserBindServiceCodeClient(Long userId);

	List<Long> findEnterpriseBindServiceCode(Long userId);

	User findById(Long id);

	Page<UserBindingDTO> findUserInfoPage(int start, int limit, String userName, List<Long> userIdList);

	List<User> findall();
	
	Result checkUserResetPassword(Long userId);

	List<SimpleUserDTO> findByUserType(String userType);

	List<User> findByRealNames(List<String> list, String userType);

	List<User> countByUserIds(List<Long> list, String roleType);

	List<User> countByRealNames(List<String> list, String roleType);
	
	List<Long> findUserIdbyEnterpriseId(Long enterpriseId);
}
