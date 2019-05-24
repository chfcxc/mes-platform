package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.dto.enterprise.EnterpriseBindingSaleDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.EnterpriseBindingSale;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.dao.system.EnterpriseBindingSaleDao;
import cn.emay.eucp.data.dao.system.EnterpriseDao;
import cn.emay.eucp.data.dao.system.UserDao;
import cn.emay.eucp.data.service.system.EnterpriseBindingSaleService;

@Service("enterpriseBindingSaleService")
public class EnterpriseBindingSaleServiceImpl implements EnterpriseBindingSaleService {

	@Resource
	private EnterpriseBindingSaleDao enterpriseBindingSaleDao;
	@Resource
	private EnterpriseDao enterpriseDao;
	@Resource
	private UserDao userDao;

	@Override
	public Page<EnterpriseBindingSale> selectRelationship(String clientUserNameAndCode, String saleMan, Integer start, Integer limit) {
		Enterprise enterprise = null;
		UserDTO userDTO = null;
		List<String> systemEnterpriseIdList = new ArrayList<String>();
		if (null != clientUserNameAndCode && !"".equals(clientUserNameAndCode)) {
			List<Enterprise> enterpriseList = enterpriseDao.getEnterpriseByNameOrClientNumber(clientUserNameAndCode);
			if (null != enterpriseList && enterpriseList.size() != 0) {
				for (Enterprise retEnterprise : enterpriseList) {
					if (null != retEnterprise.getId()) {
						systemEnterpriseIdList.add(retEnterprise.getId().toString());
					}
				}
			} else {
				return new Page<EnterpriseBindingSale>();
			}
		}
		List<String> systemUserIdList = new ArrayList<String>();
		if (null != saleMan && !"".equals(saleMan)) {
			List<Map<String, Object>> userList = userDao.findUserIdByUsernameOrRealname(saleMan);
			if (null != userList && userList.size() != 0) {
				for (Map<String, Object> map : userList) {
					if (null != map.get("id")) {
						systemUserIdList.add(map.get("id").toString());
					}
				}
			} else {
				return new Page<EnterpriseBindingSale>();
			}
		}
		Page<EnterpriseBindingSale> ret = enterpriseBindingSaleDao.selectRelationship(systemEnterpriseIdList, systemUserIdList, start, limit);
		List<EnterpriseBindingSale> list = ret.getList();
		for (EnterpriseBindingSale EnterpriseBindingSale : list) {
			enterprise = enterpriseDao.findById(EnterpriseBindingSale.getSystemEnterpriseId());
			userDTO = userDao.findUserDTOById(Long.valueOf(EnterpriseBindingSale.getSystemUserId()));
			if (null != enterprise) {
				EnterpriseBindingSale.setSystemEnterpriseName(enterprise.getNameCn());
				EnterpriseBindingSale.setSystemEnterpriseCode(enterprise.getClientNumber());
			}
			if (null != userDTO) {
				EnterpriseBindingSale.setSystemUsername(userDTO.getUsername());
				EnterpriseBindingSale.setSystemRealname(userDTO.getRealname());
			}
		}
		ret.setList(list);
		return ret;
	}

	@Override
	public String relationship(String clientUserNameAndCode, String saleMan, String userId, String enterpriseIds, User user, String isAll) {
		String retMsg = "";
		String delIds = "";
		List<EnterpriseBindingSale> entities = null;
		EnterpriseBindingSale EnterpriseBindingSale = null;
		// 是否调整所有isAll：0：所有
		try {
			if (null != isAll && !"".equals(isAll) && (isAll.equals("0"))) {
				String systemEnterpriseId = "";
				String systemUserId = "";
				List<Enterprise> enterpriseList = enterpriseDao.getEnterpriseByNameOrClientNumber(clientUserNameAndCode);
				if (null != enterpriseList && enterpriseList.size() != 0) {
					for (Enterprise retEnterprise : enterpriseList) {
						if (null == systemEnterpriseId || "".equals(systemEnterpriseId)) {
							systemEnterpriseId += retEnterprise.getId().toString();
						} else if (null != systemEnterpriseId && !"".equals(systemEnterpriseId)) {
							systemEnterpriseId += "," + retEnterprise.getId().toString();
						}
					}
				}
				List<UserDTO> userList = userDao.findUserPageByRoleType(EucpSystem.销售系统.getCode(), User.USER_TYPE_EMAY, saleMan, 0, 10000).getList();
				if (null != userList && userList.size() != 0) {
					for (UserDTO retUserDTO : userList) {
						if (null == systemUserId || "".equals(systemUserId)) {
							systemUserId += retUserDTO.getId().toString();
						} else if (null != systemUserId && !"".equals(systemUserId)) {
							systemUserId += "," + retUserDTO.getId().toString();
						}
					}
				}
				List<Map<String, Object>> findAllRelationshipList = enterpriseBindingSaleDao.findAllRelationship(systemEnterpriseId, systemUserId);
				enterpriseIds = "";
				for (Map<String, Object> map : findAllRelationshipList) {
					if (null != enterpriseIds && !"".equals(enterpriseIds)) {
						enterpriseIds += "," + map.get("system_enterprise_id");
					} else {
						enterpriseIds += map.get("system_enterprise_id");
					}
				}
			}
			List<Map<String, Object>> retList = enterpriseBindingSaleDao.findRelationships(enterpriseIds);
			for (Map<String, Object> map : retList) {
				if (null != delIds && !"".equals(delIds)) {
					delIds += "," + map.get("id").toString();
				} else {
					delIds += map.get("id").toString();
				}
			}
			if (null != delIds && !"".equals(delIds)) {
				enterpriseBindingSaleDao.deleteRelationships(delIds);
			}
			String arrayEnterpriseIds[] = enterpriseIds.split(",");
			entities = new ArrayList<EnterpriseBindingSale>();
			for (int i = 0; i < arrayEnterpriseIds.length; i++) {
				EnterpriseBindingSale = new EnterpriseBindingSale();
				EnterpriseBindingSale.setSystemEnterpriseId(Long.valueOf(arrayEnterpriseIds[i].toString()));
				EnterpriseBindingSale.setSystemUserId(Long.valueOf(userId));
				EnterpriseBindingSale.setCreateMan(user.getUsername());
				EnterpriseBindingSale.setLastUpdateMan(user.getUsername());
				entities.add(EnterpriseBindingSale);
			}
			enterpriseBindingSaleDao.saveBatchSystemEnterpriseBindingSaleTop(entities);
			userDao.updateLastUpdateTimeById(userId);
			enterpriseBindingSaleDao.deleteSystemEnterpriseBindingSaleTop(delIds);
			List<Map<String, Object>> enterpriseList = enterpriseDao.getEnterpriseByIds(enterpriseIds);
			if (null != enterpriseList && enterpriseList.size() != 0) {
				for (Map<String, Object> map : enterpriseList) {
					if (null != retMsg && !"".equals(retMsg)) {
						retMsg += "," + map.get("client_number").toString();
					} else {
						retMsg += map.get("client_number").toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMsg;
	}

	@Override
	public void saveEnterpriseBindingSale(EnterpriseBindingSale enterpriseBindingSale) {
		enterpriseBindingSaleDao.save(enterpriseBindingSale);
	}

	/************************ 销售系统 ************************/
	@Override
	public Page<EnterpriseBindingSale> selectCientManage(String userName, String systemEnterpriseId, String clientUserNameAndCode, Integer start, Integer limit) {
		Enterprise enterprise = null;
		UserDTO userDTO = null;
		Page<EnterpriseBindingSale> ret = null;
		if (null != clientUserNameAndCode && !"".equals(clientUserNameAndCode)) {
			if (null != systemEnterpriseId && !"".equals(systemEnterpriseId) && !"null".equals(systemEnterpriseId)) {
				systemEnterpriseId = getSystemEnterpriseId(clientUserNameAndCode, systemEnterpriseId);
			}
		}
		if (null == systemEnterpriseId || "".equals(systemEnterpriseId)) {
			ret = new Page<EnterpriseBindingSale>();
		} else {
			ret = enterpriseBindingSaleDao.selectCientManage(userName, systemEnterpriseId, start, limit);
		}
		List<EnterpriseBindingSale> list = ret.getList();
		if (null != list && list.size() != 0) {
			for (EnterpriseBindingSale EnterpriseBindingSale : list) {
				enterprise = enterpriseDao.findById(EnterpriseBindingSale.getSystemEnterpriseId());
				userDTO = userDao.findUserDTOById(Long.valueOf(EnterpriseBindingSale.getSystemUserId()));
				if (null != enterprise) {
					EnterpriseBindingSale.setSystemEnterpriseName(enterprise.getNameCn());
					EnterpriseBindingSale.setSystemEnterpriseCode(enterprise.getClientNumber());
					EnterpriseBindingSale.setLinkman(enterprise.getLinkman());
					EnterpriseBindingSale.setMobile(enterprise.getMobile());
					EnterpriseBindingSale.setType(enterprise.getType());
				}
				if (null != userDTO) {
					EnterpriseBindingSale.setSystemRealname(userDTO.getRealname());
				}
			}
		}
		ret.setList(list);
		return ret;
	}

	@Override
	public String stickCientManage(String userName, String enterpriseBindingSaleId) {
		String retMsg = "";
		int maxNum = enterpriseBindingSaleDao.stickCientManageFindMaxNum(userName) + Integer.valueOf(1);
		List<Map<String, Object>> retList = enterpriseBindingSaleDao.stickCientManageSelect(userName, enterpriseBindingSaleId);
		if (null != retList && retList.size() != 0) {
			enterpriseBindingSaleDao.stickCientManageUpdate(userName, enterpriseBindingSaleId, String.valueOf(maxNum));
		} else {
			enterpriseBindingSaleDao.stickCientManageInsert(userName, enterpriseBindingSaleId, String.valueOf(maxNum));
		}
		Map<String, Object> enterpriseBindingSale = enterpriseBindingSaleDao.findByEnterpriseBindingSaleId(Long.valueOf(enterpriseBindingSaleId));
		List<Map<String, Object>> enterpriseList = enterpriseDao.getEnterpriseByIds(enterpriseBindingSale.get("system_enterprise_id").toString());
		if (null != enterpriseList && enterpriseList.size() != 0) {
			for (Map<String, Object> map : enterpriseList) {
				if (null != retMsg && !"".equals(retMsg)) {
					retMsg += "," + map.get("client_number").toString();
				} else {
					retMsg += map.get("client_number").toString();
				}
			}
		}
		return retMsg;
	}

	public String getSystemEnterpriseId(String clientUserNameAndCode, String retIds) {
		String systemEnterpriseId = "";
		List<Enterprise> enterpriseList = enterpriseDao.getEnterpriseByNameOrClientNumber(clientUserNameAndCode);
		if (null != enterpriseList && enterpriseList.size() != 0) {
			for (Enterprise retEnterprise : enterpriseList) {
				if (retIds.contains(retEnterprise.getId().toString())) {
					if (null == systemEnterpriseId || "".equals(systemEnterpriseId)) {
						systemEnterpriseId += retEnterprise.getId().toString();
					} else if (null != systemEnterpriseId && !"".equals(systemEnterpriseId)) {
						systemEnterpriseId += "," + retEnterprise.getId().toString();
					}
				}
			}
		}
		return systemEnterpriseId;
	}

	@Override
	public int[] saveBatchEnterpriseBindingSale(List<Object[]> params) {
		return enterpriseBindingSaleDao.saveBatchEnterpriseBindingSale(params);
	}

	@Override
	public List<Long> getEnterpriseIdsBySaleId(String userIds) {
		return enterpriseBindingSaleDao.getEnterpriseIdsBySaleId(userIds);
	}

	@Override
	public List<Long> getServiceCodeIdsBySaleId(String userIds) {
		return enterpriseBindingSaleDao.getServiceCodeIdsBySaleId(userIds);
	}

	@Override
	public List<Long> getPlatformIdsBySaleId(String userIds) {
		return enterpriseBindingSaleDao.getPlatformIdsBySaleId(userIds);
	}
	
	@Override
	public Map<Long,Long> findByEnterIds(List<Long> enterpriseId){
		Map<Long,Long> map = new HashMap<Long,Long>();
		List<EnterpriseBindingSaleDTO> dtoList= enterpriseBindingSaleDao.findByEnterIds(enterpriseId);
		if(dtoList!=null && dtoList.size()> 0){
			for(EnterpriseBindingSaleDTO dto :dtoList){
				map.put(dto.getSystemEnterpriseId(), dto.getSystemUserId());
			}
		}
		return map;
	}
	
	@Override
	public void saveBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList){
		enterpriseBindingSaleDao.saveBatchEnterSale(dtoList);
	}

	@Override
	public void updateBatchEnterSale(List<EnterpriseBindingSaleDTO> dtoList){
		enterpriseBindingSaleDao.updateBatchEnterSale(dtoList);
	}
	
	@Override
	public Map<Long, String> selectAllRelationshipForflow() {
		Map<Long, String> result = new HashMap<>();
		List<Map<String, Object>> list = enterpriseBindingSaleDao.selectAllRelationshipForflow();
		if (null != list && list.size() != 0) {
			for (Map<String, Object> map : list) {
				result.put((Long) map.get("eid"), (String) map.get("sname"));
			}
		}
		return result;
	}
}
