package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.moudle.db.system.EnterpriseAuth;
import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.data.dao.system.EnterpriseAuthDao;
import cn.emay.eucp.data.dao.system.ResourceDao;
import cn.emay.eucp.data.service.system.EnterpriseAuthService;

@Service("enterpriseAuthService")
public class EnterpriseAuthServiceImpl implements EnterpriseAuthService {

	@Autowired
	private EnterpriseAuthDao enterpriseAuthDao;
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public List<String> findEnterpriseAuth(Integer authLevel, String authType) {
		return enterpriseAuthDao.findEnterpriseAuth(authLevel, authType);
	}

	@Override
	public Result modifyEnterpriseAuth(int authLevel, String menuParams, String columnParams) {
		List<String> allAuthList = new ArrayList<String>();// 客户端所有页面菜单权限
		List<Resource> resourceList = resourceDao.findAuthByType(Resource.RESOURCE_TYPE_PAGE, EucpSystem.客户系统.getCode());
		for (Resource resource : resourceList) {
			allAuthList.add(resource.getResourceCode());
		}
		List<EnterpriseAuth> enterpriseAuthList=new ArrayList<EnterpriseAuth>();
		//菜单权限
		if (!StringUtils.isEmpty(menuParams)) {
			String[] menuArr = menuParams.split(",");
			//校验菜单
			Boolean isContains=allAuthList.containsAll(Arrays.asList(menuArr));
			if(!isContains){
				return Result.badResult("参数错误");
			}
			for(String menu:menuArr){
				if(menu.startsWith("SMS_")){//短信服务
					enterpriseAuthList.add(new EnterpriseAuth(menu, null, EnterpriseAuth.AUTH_TYPE_PAGE, EnterpriseAuth.FOR_SERVICE_SMS));
				}else if(menu.startsWith("SYS_")){//系统服务
					enterpriseAuthList.add(new EnterpriseAuth(menu, null, EnterpriseAuth.AUTH_TYPE_PAGE, EnterpriseAuth.FOR_SERVICE_SYS));
				}else if(menu.startsWith("FLOW_")){//流量服务
					enterpriseAuthList.add(new EnterpriseAuth(menu, null, EnterpriseAuth.AUTH_TYPE_PAGE, EnterpriseAuth.FOR_SERVICE_FLOW));
				}
			}
		}
		//列权限
		if(!StringUtils.isEmpty(columnParams)){//menu#column,menu1#column1,
			String[] columnArr = columnParams.split(",");
			for(String column:columnArr){
				String[] coArr=column.split("#");
				if(coArr.length>1 && allAuthList.contains(coArr[0])){
					if(coArr[0].startsWith("SMS_")){//短信服务
						enterpriseAuthList.add(new EnterpriseAuth(coArr[1], coArr[0], EnterpriseAuth.AUTH_TYPE_COLUMNS, EnterpriseAuth.FOR_SERVICE_SMS));
					}else if(coArr[0].startsWith("SYS_")){//系统服务
						enterpriseAuthList.add(new EnterpriseAuth(coArr[1], coArr[0], EnterpriseAuth.AUTH_TYPE_COLUMNS, EnterpriseAuth.FOR_SERVICE_SYS));
					}else if(coArr[0].startsWith("FLOW_")){//流量服务
						enterpriseAuthList.add(new EnterpriseAuth(coArr[1], coArr[0], EnterpriseAuth.AUTH_TYPE_COLUMNS, EnterpriseAuth.FOR_SERVICE_FLOW));
					}
				}else{
					return Result.badResult("参数错误");
				}
			}
		}
		enterpriseAuthDao.deleteEnterAuthByProperty("authLevel", authLevel);
		enterpriseAuthDao.saveBatchEnterpriseAuth(authLevel, enterpriseAuthList);
		return Result.rightResult();
	}
}
