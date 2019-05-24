package cn.emay.eucp.data.service.system;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.User;

public interface AuthInterceptorService {
	
	Result authErrorHandle(String tokenId, String passportPath, String localSystem, String fromUrl);

	Result authHandle(Boolean isAjaxRequest, String tokenStr, User user, String[] pageAuthCode, String[] operationAuthCode, String contextPath, String requestUri,String localSystem,String businessType, String domainPath);
}
