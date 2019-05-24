package cn.emay.eucp.data.service.system;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.User;

/**
 * 登录服务
 * 
 * @author Frank
 * 
 */
public interface PassportService {

	/**
	 * 登录
	 * 
	 * @return
	 */
	public Result login(String username, String password, String fromUrl, String system);

	/**
	 * 登出
	 */
	public void logout(String tokenId);

	/**
	 * 获取当前的TOKEN【包含登录时的用户信息】
	 */
	public String getCurrentTokenStr(String tokenId, boolean flush);

	/**
	 * 是否登录
	 */
	public boolean isCurrentLogin(String tokenId, boolean flush);

	/**
	 * 获取当前登录的用户【实时用户信息】
	 */
	public User getCurrentLoginUser(String tokenId, boolean flush);

	/**
	 * 获取当前登录的用户所属企业
	 */
//	public Enterprise getCurrentEnterpriseByLoginUser(String tokenId, boolean flush);

}
