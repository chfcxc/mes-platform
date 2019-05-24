package cn.emay.eucp.web.client.controller.sys;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.reflect.TypeToken;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.excel.BigDataGetter;
import cn.emay.common.excel.ExcelBean;
import cn.emay.common.excel.ExcelHelper;
import cn.emay.common.excel.db.support.imports.ExcelReaderUtil;
import cn.emay.common.excel.db.support.imports.ExcelRowReaderDB;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.GlobalConstants;
import cn.emay.eucp.common.dto.contact.ContactDTO;
import cn.emay.eucp.common.dto.contact.ContactParamsDTO;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.common.moudle.db.system.ContactGroup;
import cn.emay.eucp.common.moudle.db.system.ContactGroupAssign;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;
import cn.emay.eucp.data.service.system.ContactGroupAssignService;
import cn.emay.eucp.data.service.system.ContactGroupService;
import cn.emay.eucp.data.service.system.ContactService;
import cn.emay.eucp.data.service.system.ExcelReaderService;
import cn.emay.eucp.util.RegularCheckUtils;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.DateUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 联系人
 *
 */
@PageAuth("SYS_CLIENT_CONTACT")
@RequestMapping("/sys/client/contact")
@Controller
public class SysClientContactController {

	private Logger log = Logger.getLogger(SysClientContactController.class);

	@Resource
	private ClientUserOperLogService clientUserOperLogService;
	@Resource
	private ContactGroupService contactGroupService;
	@Resource
	private ContactService contactService;
	@Resource
	private ContactGroupAssignService contactGroupAssignService;
	@Resource(name = "baseExcelReaderService")
	private ExcelReaderService excelReaderService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/sys/contact/contact";
	}

	/**
	 * 查询所有组
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/ajax/listGroup")
	public void listGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer groupType = RequestUtils.getIntParameter(request, "groupType", 0);// 组类型[0-个人组,1-共享组]
		User currentUser = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		List<ContactGroup> list = contactGroupService.findList(groupType, currentUser.getId(), enterprise.getId(), false);
		//用户权限
		Boolean isShow = true;
		for(ContactGroup group: list) {
			if(group.getGroupType().intValue() == ContactGroup.GROUP_TYPE_SHARE && !currentUser.getId().equals(group.getUserId())) {
				isShow = false;
			} else {
				isShow = true;
			}
			group.setIsShow(isShow);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 查询联系人组：个人组；共享组（仅当前用户创建的组）
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/ajax/listOwnGroup")
	public void listOwnGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer groupType = RequestUtils.getIntParameter(request, "groupType", 0);// 组类型[0-个人组,1-共享组]
		User currentUser = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		List<ContactGroup> list = contactGroupService.findList(groupType, currentUser.getId(), enterprise.getId(), true);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	@RequestMapping("/ajax/addGroup")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_GROUP_ADD")
	public void addGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer groupType = RequestUtils.getIntParameter(request, "groupType", 0);// 组类型[0-个人组,1-共享组]
		String groupName = RequestUtils.getParameter(request, "groupName");
		User currentUser = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		Result result = checkGroup(groupType, currentUser.getId(), enterprise.getId(), groupName, null);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		result = contactGroupService.addGroup(groupType, currentUser.getId(), enterprise.getId(), groupName);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "联系人管理";
			String content = "新增";
			if (groupType.intValue() == ContactGroup.GROUP_TYPE_PERSONAL) {
				content += "个人组，";
			} else {
				content += "共享组，";
			}
			content += "组名称：{0}";
			log.info("【系统服务>联系人管理】-->用户:" + currentUser.getUsername() + "," + MessageFormat.format(content, new Object[] { groupName }));
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(content, new Object[] { groupName }), ClientUserOperLog.OPERATE_ADD);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	@RequestMapping("/ajax/modifyGroup")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_GROUP_MODIFY")
	public void modifyGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String groupName = RequestUtils.getParameter(request, "groupName");

		User currentUser = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		ContactGroup entity = contactGroupService.findById(id);
		if (entity == null || entity.getIsDelete()) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Integer groupType = entity.getGroupType();
		Result result = checkGroup(groupType, currentUser.getId(), enterprise.getId(), groupName, id);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		// 校验数据权限
		if (!entity.getUserId().equals(currentUser.getId())) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		entity.setGroupName(groupName);
		result = contactGroupService.modifyGroup(entity);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "联系人管理";
			String content = "修改";
			if (groupType.intValue() == ContactGroup.GROUP_TYPE_PERSONAL) {
				content += "个人组，";
			} else {
				content += "共享组，";
			}
			content += "组名称：{0}修改为：{1}";

			log.info("【系统服务>联系人管理】-->用户:" + currentUser.getUsername() + "," + MessageFormat.format(content, new Object[] { entity.getGroupName(), groupName }));
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(content, new Object[] { entity.getGroupName(), groupName }), ClientUserOperLog.OPERATE_MODIFY);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	public Result checkGroup(Integer groupType, Long userId, Long enterpriseId, String groupName, Long id) {
		if (groupType.intValue() != ContactGroup.GROUP_TYPE_PERSONAL && groupType.intValue() != ContactGroup.GROUP_TYPE_SHARE) {
			return Result.badResult("参数不正确");
		}
		if (StringUtils.isEmpty(groupName)) {
			return Result.badResult("组名称不能为空");
		}
		Boolean isExist = contactGroupService.isExist(groupType, userId, enterpriseId, groupName, id);
		if (isExist) {
			return Result.badResult("组名称已存在");
		}
		return Result.rightResult();
	}

	@RequestMapping("/ajax/deleteGroup")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_GROUP_DELETE")
	public void deleteGroup(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);

		ContactGroup entity = contactGroupService.findById(id);
		if (entity == null || entity.getIsDelete()) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		// 校验数据权限
		if (!entity.getUserId().equals(currentUser.getId())) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Boolean isContactExist = contactGroupAssignService.isContactExist(id);
		if (isContactExist) {
			ResponseUtils.outputWithJson(response, Result.badResult("该组下存在联系人，请先删除联系人"));
			return;
		}
		entity.setIsDelete(true);
		Result result = contactGroupService.modifyGroup(entity);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "联系人管理";
			String content = "删除";
			if (entity.getGroupType().intValue() == ContactGroup.GROUP_TYPE_PERSONAL) {
				content += "个人组，";
			} else {
				content += "共享组，";
			}
			content += "组名称：{0}";

			log.info("【系统服务>联系人管理】-->用户:" + currentUser.getUsername() + "," + MessageFormat.format(content, new Object[] { entity.getGroupName() }));
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(content, new Object[] { entity.getGroupName() }), ClientUserOperLog.OPERATE_DELETE);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	@RequestMapping("/ajax/listContact")
	public void listContact(HttpServletRequest request, HttpServletResponse response, Model model) {
		Integer groupType = RequestUtils.getIntParameter(request, "groupType", 0);// 组类型[0-个人组,1-共享组]
		Long groupId = RequestUtils.getLongParameter(request, "groupId", 0l);// 0-全部
		String params = RequestUtils.getParameter(request, "params");// 联系人 or 手机号
		Integer start = RequestUtils.getIntParameter(request, "start", 0);
		Integer limit = RequestUtils.getIntParameter(request, "limit", 20);

		User currentUser = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		String trueName = "";
		String mobile = "";
		if (!StringUtils.isEmpty(params) && RegularCheckUtils.checkMobile(params)) {
			mobile = params;
		} else {
			trueName = params;
		}
		Page<ContactDTO> page = contactService.findPage(groupType, groupId, trueName, mobile, currentUser.getId(), enterprise.getId(), start, limit);
		Map<Long,String> groupMap = new HashMap<Long,String>();
		if(groupId.longValue() == 0l) {
			List<ContactGroup> groupList = contactGroupService.findList(groupType, currentUser.getId(), enterprise.getId(), false);
			for(ContactGroup group : groupList) {
				groupMap.put(group.getId(), group.getGroupName());
			}
		}
		Boolean isShow = true;// 页面操作按钮是否展示
		for(ContactDTO dto :page.getList()) {
			if (!dto.getUserId().equals(currentUser.getId())) {
				isShow = false;
			} else {
				isShow = true;
			}
			dto.setIsShow(isShow);
			dto.setGroupName(groupMap.get(dto.getGroupId()));
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	@RequestMapping("/ajax/addContact")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_ADD")
	public void addContact(HttpServletRequest request, HttpServletResponse response, Model model) {
		String realName = RequestUtils.getParameter(request, "realName");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		String qq = RequestUtils.getParameter(request, "qq");
		Integer groupType = RequestUtils.getIntParameter(request, "groupType", 0);// 组类型[0-个人组,1-共享组]
		Long groupId = RequestUtils.getLongParameter(request, "groupId", 0l);
		Date birthday = RequestUtils.getDateParameter(request, "birthday", "yyyy-MM-dd", null);
		String company = RequestUtils.getParameter(request, "company");
		String position = RequestUtils.getParameter(request, "position");
		String companyAddress = RequestUtils.getParameter(request, "companyAddress");
		Boolean isAddMember = RequestUtils.getBooleanParameter(request, "isAddMember", false);// 是否添加成员操作

		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result = checkContact(realName, mobile, groupType, groupId, null, isAddMember);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		ContactParamsDTO dto = new ContactParamsDTO(realName, mobile, email, qq, groupId, birthday, company, position, companyAddress, currentUser.getId(), null);
		result = contactService.addContact(dto);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "联系人管理";
			String content = "新增联系人，手机号：{0}，groupId:{1}";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(content, new Object[] { mobile, groupId }), ClientUserOperLog.OPERATE_ADD);
			log.info("【系统服务>联系人管理】-->用户:" + currentUser.getUsername() + ",新增联系人，手机号：" + mobile + "，groupId:" + groupId);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	@RequestMapping("/ajax/info")
	public void info(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long assignId = RequestUtils.getLongParameter(request, "assignId", 0l);// 关联表id
		ContactParamsDTO dto = contactService.findByAssignId(assignId);
		ContactGroup group = contactGroupService.findById(dto.getGroupId());
		dto.setGroupName(group.getGroupName());
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	@RequestMapping("/ajax/modifyContact")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_MODIFY")
	public void modifyContact(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long assignId = RequestUtils.getLongParameter(request, "assignId", 0l);// 关联表id
		String realName = RequestUtils.getParameter(request, "realName");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		String qq = RequestUtils.getParameter(request, "qq");
//		Integer groupType = RequestUtils.getIntParameter(request, "groupType", 0);// 组类型[0-个人组,1-共享组]
		Long groupId = RequestUtils.getLongParameter(request, "groupId", 0l);
		Date birthday = RequestUtils.getDateParameter(request, "birthday", "yyyy-MM-dd", null);
		String company = RequestUtils.getParameter(request, "company");
		String position = RequestUtils.getParameter(request, "position");
		String companyAddress = RequestUtils.getParameter(request, "companyAddress");

		User currentUser = WebUtils.getCurrentUser(request, response);
		ContactGroupAssign assign = contactGroupAssignService.findById(assignId);
		if (assign == null || assign.getIsDelete()) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		ContactGroup contactGroup = contactGroupService.findById(assign.getGroupId());
		// 校验数据权限
		if (contactGroup == null || !contactGroup.getUserId().equals(currentUser.getId())) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Result result = checkContact(realName, mobile, contactGroup.getGroupType(), groupId, assignId, false);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		ContactParamsDTO dto = new ContactParamsDTO(realName, mobile, email, qq, groupId, birthday, company, position, companyAddress, currentUser.getId(), assignId);
		result = contactService.modifyContact(dto, assign);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "联系人管理";
			String content = "修改联系人，手机号：{0}";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(content, new Object[] { mobile }), ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>联系人管理】-->用户:" + currentUser.getUsername() + ",修改联系人，手机号：" + mobile);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	public Result checkContact(String realName, String mobile, Integer groupType, Long groupId, Long id, Boolean isAddMember) {
		if (StringUtils.isEmpty(realName)) {
			return Result.badResult("姓名不能为空");
		}
		if(realName.length() > 50) {
			return Result.badResult("姓名不能超过50个字符");
		}
		if (StringUtils.isEmpty(mobile) || !RegularCheckUtils.checkMobile(mobile)) {
			return Result.badResult("手机号不正确");
		}
		ContactGroup contactGroup = contactGroupService.findById(groupId);
		if (isAddMember) {// 添加成员操作: 不需要选择组类别、所属组
			if (contactGroup == null || contactGroup.getIsDelete()) {
				return Result.badResult("所属组不正确");
			}
		} else {
			if (groupType.intValue() != ContactGroup.GROUP_TYPE_PERSONAL && groupType.intValue() != ContactGroup.GROUP_TYPE_SHARE) {
				return Result.badResult("组类别不正确");
			}
			if (contactGroup == null || contactGroup.getIsDelete() || !contactGroup.getGroupType().equals(groupType)) {
				return Result.badResult("所属组不正确");
			}
		}
		Boolean isExist = contactService.isExist(groupId, mobile, id);
		if (isExist) {
			return Result.badResult("当前所选组已包含该手机号，请重新填写手机号");
		}
		return Result.rightResult();
	}

	@RequestMapping("/ajax/deleteContact")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_DELETE")
	public void deleteContact(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long assignId = RequestUtils.getLongParameter(request, "assignId", 0l);// 关联表id
		User currentUser = WebUtils.getCurrentUser(request, response);

		ContactParamsDTO dto = contactService.findByAssignId(assignId);
		// 校验数据权限
		if (dto == null || !dto.getUserId().equals(currentUser.getId())) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Result result = contactService.deleteContact(assignId);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "联系人管理";
			String content = "删除联系人，手机号：{0}，groupId :{1}";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(content, new Object[] { dto.getMobile(), dto.getGroupId() }), ClientUserOperLog.OPERATE_DELETE);
			log.info("【系统服务>联系人管理】-->用户:" + currentUser.getUsername() + ",删除联系人，手机号：" + dto.getMobile() + "，groupId:" + dto.getGroupId());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	@RequestMapping("/importContact")
	@OperationAuth("OPER_SYS_CLIENT_CONTACT_ADD")
	public void importContact(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = WebUtils.importExcel(request, 1024 * 1024 * 5);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}

		Long groupId = RequestUtils.getLongParameter(request, "groupId", 0l);
		if (groupId == null || groupId.longValue() <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("请先选择组再导入联系人"));
			return;
		}
		final ContactGroup group = contactGroupService.findById(groupId);
		if (group == null || group.getIsDelete()) {
			ResponseUtils.outputWithJson(response, Result.badResult("请先选择组再导入联系人"));
			return;
		}
		final Map<String, Object> params = new HashMap<String, Object>();
		final List<String[]> errors = new ArrayList<String[]>();
		final List<Integer> sum = new ArrayList<Integer>();
		final List<String> mobiles = new ArrayList<String>();
		User user = WebUtils.getCurrentUser(request, response);
		String[] columns = new String[] { "real_name", "mobile", "email", "qq", "birthday", "company", "position", "company_address", "is_delete", "create_time", "user_id" };
		String tableName = "system_contact";
		ExcelRowReaderDB readerDBx = new ExcelRowReaderDB(tableName, columns, 1000) {
			@Override
			public boolean checkRow(int sheetIndex, int curRow, List<String> rowlist) {
				if (curRow == 0) { // 标题行放过
					return false;
				}
				if (null == rowlist || rowlist.isEmpty()) {
					return false;
				}
				sum.add(curRow);
				String[] array = new String[rowlist.size()];
				rowlist.toArray(array);
				if (rowlist.size() < 8) {
					errors.add(RegularUtils.copyOfRange(array, "列数不正确"));
					return false;
				}

				String realName = rowlist.get(0);
				String mobile = rowlist.get(1);
				String email = rowlist.get(2);
				String qq = rowlist.get(3);
				String birthday = rowlist.get(4);
				String company = rowlist.get(5);
				String position = rowlist.get(6);
				String companyAddress = rowlist.get(7);
				String[] error = { realName, mobile, email, qq, birthday, company, position, companyAddress };
				if (RegularCheckUtils.isEmpty(realName)) {
					errors.add(RegularUtils.copyOfRange(error, "姓名不能为空"));
					return false;
				}
				String regExp = "^[\\u4E00-\\u9FA5A-Za-z\\s.]{0,50}$";
				Pattern p = Pattern.compile(regExp);
				Matcher m = p.matcher(realName);
				if(!m.matches()) {
					errors.add(RegularUtils.copyOfRange(error, "姓名支持中文、英文、空格 .最多50个字符"));
					return false;
				}
				if (!RegularUtils.checkMobile(mobile.trim())) {
					errors.add(RegularUtils.copyOfRange(error, "手机号不正确"));
					return false;
				}
				if(!RegularUtils.isEmpty(email) && !RegularUtils.checkEmail(email)) {
					errors.add(RegularUtils.copyOfRange(error, "邮箱格式不正确"));
					return false;
				}
				if(!RegularUtils.isEmpty(qq) && !RegularCheckUtils.checkQQ(qq)) {
					errors.add(RegularUtils.copyOfRange(error, "QQ格式不正确"));
					return false;			
				}
				if(StringUtils.isEmpty(birthday) || "".equals(birthday.trim())) {
					rowlist.set(4, null);
				} else {
					Date birth = DateUtil.parseDate(birthday, "yyyy-MM-dd");
					if(birth == null) {
						errors.add(RegularUtils.copyOfRange(error, "生日格式不正确"));
						return false;	
					}
				}
				if(!RegularCheckUtils.isEmpty(company)) {
					if(!RegularCheckUtils.notExistSpecial(company)) {
						errors.add(RegularUtils.copyOfRange(error, "单位不能包含特殊字符"));
						return false;
					}
					if(company.length() > 20) {
						errors.add(RegularUtils.copyOfRange(error, "单位不能超过20个字符"));
						return false;
					}
				
				}
				if(!RegularCheckUtils.isEmpty(position)) {
					if(!RegularCheckUtils.notExistSpecial(position)) {
						errors.add(RegularUtils.copyOfRange(error, "职位不能包含特殊字符"));
						return false;
					}
					if(position.length() > 50) {
						errors.add(RegularUtils.copyOfRange(error, "职位不能超过50个字符"));
						return false;
					}
				}
				if(!RegularCheckUtils.isEmpty(companyAddress)) {
					if(companyAddress.length() > 100) {
						errors.add(RegularUtils.copyOfRange(error, "公司地址不能超过100个字符"));
						return false;
					}
				}
				rowlist.set(2, email.trim());
				rowlist.set(3, qq.trim());
				rowlist.set(5, company.trim());
				rowlist.set(6, position.trim());
				rowlist.add(7, companyAddress.trim());
				rowlist.add(8, "false");
				rowlist.add(9, DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				rowlist.add(10, group.getUserId().toString());
				mobiles.add(mobile);
				return true;
			}

			@Override
			public int saveDB(String sql, List<Object[]> setters) {
				return excelReaderService.saveBatchPreparedStatement(sql, setters);

			}
		};

		String excelPath = (String) result.getResult();
		readerDBx.setShowPrint(false); // 是否同步输出数据
		ExcelReaderUtil.readExcel(readerDBx, excelPath);
		List<String> batchList = new ArrayList<String>();
		for (int i = 0; i < mobiles.size(); i++) {
			batchList.add(mobiles.get(i));
			if (i % 1000 == 0) {
				List<Long> contactIds = contactService.findIdsByMobiles(batchList, group.getUserId());
				contactGroupAssignService.saveBatchAssign(contactIds, groupId);
				batchList.clear();
			}
		}
		if (!batchList.isEmpty()) {
			List<Long> contactIds = contactService.findIdsByMobiles(batchList, group.getUserId());
			contactGroupAssignService.saveBatchAssign(contactIds, groupId);
			batchList.clear();
		}

		String downloadKey = user.getId() + UUID.randomUUID().toString();
		contactService.redisSaveDownloadError(downloadKey, errors);
		int success = sum.size() - errors.size();
		params.put("sum", sum.size());
		params.put("fail", errors.size());
		params.put("downloadKey", downloadKey);
		params.put("success", success);
		log.info("user : " + user.getUsername() + " import contact");
		clientUserOperLogService.save("系统服务", "联系人", user, "导入联系人", "新增");
		ResponseUtils.outputWithJson(response, Result.rightResult(params));
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String downloadKey = request.getParameter("downloadKey");
		String value = contactService.redisGetDownloadErrorInfo(downloadKey);
		if (null == value) {
			ResponseUtils.outputWithJson(response, Result.badResult("信息不存在！"));
			return;
		}
		// json解析
		List<String[]> errors = JsonHelper.fromJson(new TypeToken<ArrayList<String[]>>() {
		}, value);
		ExcelBean bean = new ExcelBean();
		bean.addSheet(0, "联系人");
		for (String[] str : errors) {
			bean.setRowContentBySheet(0, str);
		}
		String headStr = "attachment;filename=errorcontact.xlsx";
		response.setHeader("Content-Disposition", headStr);
		response.setCharacterEncoding("utf-8");
		ExcelHelper.writeExcelInOutputStream(bean, false, true, response.getOutputStream());
	}

	@RequestMapping("/exportContact")
	public void exportContact(final HttpServletRequest request, HttpServletResponse response) throws IOException {
		int cacheSize = 1000;
		final User currentUser = WebUtils.getCurrentUser(request, response);
		String exportFileName = "联系人.xlsx";
		String sheetName = "联系人";
		response.setContentType("application/vnd.ms-excel;charset=gb2312");
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(exportFileName.getBytes("gb2312"), "iso8859-1"));
//		ExcelHelper.integerColumnIndex = Arrays.asList(2, 3, 4, 5);
		ExcelHelper.write07BigDataExcelInOutputStream(sheetName, new BigDataGetter() {
			int currentPage = GlobalConstants.DEFAULT_PAGE_HB_START;
			int pageSize = 1000;
			Long groupId = RequestUtils.getLongParameter(request, "groupId", 0l);
			String params = RequestUtils.getParameter(request, "params");// 姓名 or 手机号

			String[] title = { "姓名", "手机号", "邮箱", "QQ", "生日", "单位", "职位", "公司地址", "所属组", "组类别" };
			int i = 0;

			Queue<String[]> q = new LinkedList<String[]>();
			String[] tmp = null;

			@Override
			public String[] next() {
				return tmp;
			}

			@Override
			public boolean hasNext() {
				if (i == 0) {
					q.offer(title);
				}
				i++;
				tmp = q.poll();
				if (tmp == null) {
					List<String[]> tmps = contactService.findExportData(groupId, params, currentPage, pageSize);
					q.addAll(tmps);
					tmp = q.poll();
					currentPage++;
				}
				if (tmp == null) {
					return false;
				} else {
					return true;
				}
			}
		}, cacheSize, response.getOutputStream());
		String service = "系统管理";
		String module = "联系人管理";
		String context = "导出联系人";
		clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(context, new Object[] {}), ClientUserOperLog.OPERATE_DOWNLOAD);
		log.info("【系统管理>联系人管理】-->用户:" + currentUser.getUsername() + "导出联系人");
	}

}
