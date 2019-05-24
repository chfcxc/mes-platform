package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.auth.Token;
import cn.emay.eucp.common.cache.CenterBaseRedisCache;
import cn.emay.eucp.common.dto.user.RoleDTO;
import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.PassportService;
import cn.emay.eucp.data.service.system.ResourceService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.PasswordUtils;

@Service("passportService")
public class PassportServiceImpl implements PassportService {

	public final static String LOGIN_ERROR_PER = "LOGIN_ERROR_";

	public final static int ERROR_PASSWORD_COUNT_TIME = 30 * 60;

	public final static String TOKEN_PER = "WEB_TOKEN_";

	public final static int EXPIRY_TIME = 60 * 60 * 2;

	@javax.annotation.Resource
	private UserService userService;
	@javax.annotation.Resource
	private ResourceService resourceService;
	@javax.annotation.Resource
	private RedisClient redis;

	@Override
	public Result login(String username, String password, String fromUrl, String system) {
		User user = userService.findUserByUserName(username);
		if (user == null) {
			return Result.badResult(3, "用户不存在", null);
		}
		if (user.getState() == User.STATE_DELETE) {
			return Result.badResult(3, "用户不存在", null);
		}
		if (user.getState() == User.STATE_OFF) {
			return Result.badResult(4, "用户被停用", null);
		}
		if (user.getState() == User.STATE_LOCKING) {
			boolean isOpen = isOpenLocking(user);
			if (!isOpen) {
				return Result.badResult(5, "用户被锁定，请联系客服解锁或者一个小时后再试", null);
			}
		}
		String pass = PasswordUtils.dncrypt(user.getPassword());
		if (!password.equalsIgnoreCase(pass)) {
			return passwordErrorLogic(user);
		}
		redis.del(LOGIN_ERROR_PER + user.getUsername());
		redis.del(CenterBaseRedisCache.SYSTEM_USER_RESET_PASSWORD + user.getId());
		List<RoleDTO> roles = userService.findAllUserRoles(user.getId());
		boolean isCanAccess = false;
		for (RoleDTO role : roles) {
			if (role.getRoleType().equals(system)) {
				isCanAccess = true;
				break;
			}
		}
		if (!isCanAccess) {
			return Result.badResult(0, "无权登录此系统", null);
		}
		List<Resource> authResources = resourceService.findResourceByUserId(user.getId());
		List<Resource> systemResources = resourceService.findAll();
		Token token = new Token(user, systemResources, authResources);
		redis.set(TOKEN_PER + token.getId(), token, EXPIRY_TIME);
		return Result.rightResult(token.getId());
	}

	/**
	 * 锁定一小时后自动解锁
	 */
	private boolean isOpenLocking(User user) {
		Date lastUpdateTime = userService.findLastUpdateTime(user.getId());
		if (user.getState() == User.STATE_LOCKING && System.currentTimeMillis() - lastUpdateTime.getTime() >= 3600 * 1000l) {
			Result result = userService.modifyOn(user.getId(), user.getUserType());
			if (result.getSuccess() == true) {
				user.setState(User.STATE_ON);
			}
			return true;
		}
		return false;
	}

	/**
	 * 4次锁定
	 */
	private Result passwordErrorLogic(User user) {
		Integer times = redis.get(LOGIN_ERROR_PER + user.getUsername(), Integer.class);
		int total = times == null || times == 0 ? 1 : times + 1;
		String message = null;
		if (total >= 4) {
			redis.del(LOGIN_ERROR_PER + user.getUsername());
			userService.updateLockUserByUsername(user.getUsername());
			message = "您已经输错密码4次，账号被锁定";
		} else {
			redis.set(LOGIN_ERROR_PER + user.getUsername(), total, ERROR_PASSWORD_COUNT_TIME);
			message = "密码错误，半小时内您已经输错密码" + total + "次，输错4次将被锁定";
		}
		return Result.badResult(5, message, total);
	}

	/**
	 * 根据用户id判断所属客户（企业）是否是代理商
	 */
	// private boolean isAgentEnterprise(Long userId) {
	// boolean isAgent = false;
	// if (userId.longValue() == 1) {
	// return true;
	// }
	// Result result = enterpriseService.findEnterpriseByUserId(userId);
	// Enterprise enterprise = (Enterprise) result.getResult();
	// if (null != enterprise && Enterprise.TYPE_AGENT.equals(enterprise.getType())) {
	// isAgent = true;
	// }
	// return isAgent;
	// }

	@Override
	public void logout(String tokenId) {
		if (tokenId == null) {
			return;
		}
		redis.del(TOKEN_PER + tokenId);
	}

	@Override
	public String getCurrentTokenStr(String tokenId, boolean flush) {
		if (tokenId == null) {
			return null;
		}
		String str = redis.get(TOKEN_PER + tokenId);
		Token token = JsonHelper.fromJson(Token.class, str);
		if (flush && token != null) {
			redis.expire(TOKEN_PER + token.getId(), EXPIRY_TIME);
		}
		return str;
	}

	private Token getCurrentToken(String tokenId, boolean flush) {
		if (tokenId == null) {
			return null;
		}
		Token token = redis.get(TOKEN_PER + tokenId, Token.class);
		if (flush && token != null) {
			redis.expire(TOKEN_PER + token.getId(), EXPIRY_TIME);
		}
		return token;
	}

	@Override
	public boolean isCurrentLogin(String tokenId, boolean flush) {
		Token token = getCurrentToken(tokenId, flush);
		return token == null ? false : true;
	}

	@Override
	public User getCurrentLoginUser(String tokenId, boolean flush) {
		Token token = getCurrentToken(tokenId, flush);
		if (token == null) {
			return null;
		}
		return userService.findById(token.getUser().getId());
	}

//	@Override
//	public Enterprise getCurrentEnterpriseByLoginUser(String tokenId, boolean flush) {
//		Token token = getCurrentToken(tokenId, flush);
//		if (token == null) {
//			return null;
//		}
//		Result result = enterpriseService.findEnterpriseByUserId(token.getUser().getId());
//		if (null == result) {
//			return null;
//		}
//		return (Enterprise) result.getResult();
//	}

}