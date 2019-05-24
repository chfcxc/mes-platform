package cn.emay.eucp.web.manage.controller.global.fms.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.reflect.TypeToken;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.common.excel.ExcelBean;
import cn.emay.common.excel.ExcelHelper;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.dto.fms.base.FmsBlacklistDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBlacklist;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.util.RegularCheckUtils;
import cn.emay.eucp.data.service.fms.FmsBlacklistService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.manage.controller.util.FmsBlacklistSchemaSheetDataHandler;
import cn.emay.excel.read.ExcelReader;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:37:54 类说明 */
@PageAuth("FMS_BASE_BLACKLISTGLOBAL")
@RequestMapping("/fms/base/blacklistglobal")
@Controller
public class FmsBlacklistController {

	@Resource
	private FmsBlacklistService fmsBlacklistService;
	@Resource
	private UserService userService;
	@Resource(name = "redis")
	RedisClient redis;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "fms/base/blacklist";
	}

	@RequestMapping("/ajax/list")
	public void findPage(HttpServletRequest request, HttpServletResponse response) {
		String mobile = RequestUtils.getParameter(request, "mobile");
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<FmsBlacklistDto> page = fmsBlacklistService.findPage(mobile, start, limit);
		Set<Long> setids = new HashSet<Long>();
		if (page.getList().size() > 0) {
			for (FmsBlacklistDto global : page.getList()) {
				setids.add(global.getUserId());
			}
			List<User> userByIds = userService.findUserByIds(setids);
			Map<Long, String> map = new HashMap<Long, String>();
			for (User user : userByIds) {
				map.put(user.getId(), user.getUsername());
			}
			for (FmsBlacklistDto global : page.getList()) {
				global.setUserName(map.get(global.getUserId()));
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	@RequestMapping("/ajax/deleteid")
	public void deleteid(HttpServletRequest request, HttpServletResponse response) {
		String mobile = RequestUtils.getParameter(request, "mobile");
		if (StringUtils.isEmpty(mobile)) {
			ResponseUtils.outputWithJson(response, Result.badResult("手机号为空"));
			return;
		}
		fmsBlacklistService.deletebyMobile(mobile);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	@RequestMapping("/ajax/deleteids")
	public void deleteids(HttpServletRequest request, HttpServletResponse response) {
		String mobiles = RequestUtils.getParameter(request, "mobiles");
		if (StringUtils.isEmpty(mobiles)) {
			ResponseUtils.outputWithJson(response, Result.badResult("手机号为空"));
			return;
		}
		List<String> list = new ArrayList<>();
		String[] mobileArr = mobiles.split(",");
		for (String mobile : mobileArr) {
			list.add(mobile);
		}
		fmsBlacklistService.deletebyMobiles(list);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	@RequestMapping("/ajax/save")
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String mobile = RequestUtils.getParameter(request, "mobile");
		String remark = RequestUtils.getParameter(request, "remark");
		String data = this.checkData(mobile, remark);
		if (!StringUtils.isEmpty(data)) {
			ResponseUtils.outputWithJson(response, Result.badResult(data));
			return;
		}
		FmsBlacklist fmsBlacklist = new FmsBlacklist();
		fmsBlacklist.setMobile(mobile);
		fmsBlacklist.setRemark(remark);
		fmsBlacklist.setCreateTime(new Date());
		fmsBlacklist.setIsDelete(0);
		User user = WebUtils.getCurrentUser(request, response);
		fmsBlacklist.setUserId(user.getId());
		Result result = fmsBlacklistService.save(fmsBlacklist);
		ResponseUtils.outputWithJson(response, Result.rightResult(result));
	}

	public String checkData(String mobile, String remark) {
		if (!RegularCheckUtils.checkMobile(mobile)) {
			return "手机号格式错误";
		}
		if (remark != null) {
			if (remark.length() > 50) {
				return "备注不能超过50字";
			}
		}
		FmsBlacklist global = fmsBlacklistService.findbymobile(mobile, 0L);
		if (global != null) {
			return "黑名单已存在";
		}
		return null;
	}

	@RequestMapping("/ajax/upload")
	public void importexcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = WebUtils.importExcel(request, 1024 * 1024 * 5);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		FmsBlacklistSchemaSheetDataHandler readData = new FmsBlacklistSchemaSheetDataHandler(currentUser.getId());
		String excelPath = (String) result.getResult();
		ExcelReader.readFirstSheet(excelPath, readData);
		readData.batchInsert();
		String downloadKey = currentUser.getId() + UUID.randomUUID().toString();
		Map<String, Object> map = readData.getState();
		redis.set(downloadKey, map.get("errors"), 60 * 60 * 5);
		map.put("downloadKey", downloadKey);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}

	@RequestMapping("/ajax/exportexcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String downloadKey = request.getParameter("downloadKey");
		if (!redis.exists(downloadKey)) {
			ResponseUtils.outputWithJson(response, Result.badResult("信息不存在！"));
			return;
		}
		String value = redis.get(downloadKey);
		// json解析
		List<String[]> errors = new ArrayList<String[]>();
		errors = JsonHelper.fromJson(new TypeToken<ArrayList<String[]>>() {
		}, value);
		ExcelBean bean = new ExcelBean();
		bean.addSheet(0, "黑名单");
		for (String[] str : errors) {
			bean.setRowContentBySheet(0, str);
		}
		String headStr = "attachment;filename=blacklistglobal.xlsx";
		response.setHeader("Content-Disposition", headStr);
		response.setCharacterEncoding("utf-8");
		ExcelHelper.writeExcelInOutputStream(bean, false, true, response.getOutputStream());
	}
}
