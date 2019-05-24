package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.common.Result;


public interface EnterpriseAuthService {

	List<String> findEnterpriseAuth(Integer authLevel, String authType);

	Result modifyEnterpriseAuth(int authLevel, String menuParams, String columnParams);

}
