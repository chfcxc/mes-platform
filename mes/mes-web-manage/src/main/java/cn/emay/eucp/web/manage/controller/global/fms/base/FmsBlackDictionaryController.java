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
import cn.emay.eucp.common.dto.fms.base.BlackwordsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.fms.FmsBlackDictionaryService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.manage.controller.util.FmsBlackDictionarySchemaSheetDataHandler;
import cn.emay.excel.read.ExcelReader;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:37:54 类说明 */
@PageAuth("FMS_BASE_BLACKDICTIONARY")
@RequestMapping("/fms/base/blackdictionary")
@Controller
public class FmsBlackDictionaryController {

	@Resource
	FmsBlackDictionaryService fmsBlackDictionaryService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "redis")
	RedisClient redis;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "fms/base/blackdictionary";
	}

	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		String content = RequestUtils.getParameter(request, "content");
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<FmsBlackDictionary> page = fmsBlackDictionaryService.findPage(content, start, limit);
		List<BlackwordsDTO> arrayList = new ArrayList<BlackwordsDTO>();
		if (page.getList().size() > 0) {
			Set<Long> set = new HashSet<Long>();
			for (FmsBlackDictionary blackDictionary : page.getList()) {
				set.add(blackDictionary.getUserId());
			}
			List<User> findUserByIds = userService.findUserByIds(set);
			Map<Long, String> map = new HashMap<Long, String>();
			for (User user : findUserByIds) {
				map.put(user.getId(), user.getUsername());
			}
			for (FmsBlackDictionary blackDictionary : page.getList()) {
				BlackwordsDTO dto = new BlackwordsDTO();
				dto.setId(blackDictionary.getId());
				dto.setContent(blackDictionary.getContent());
				dto.setCreateTime(blackDictionary.getCreateTime());
				dto.setRemark(blackDictionary.getRemark());
				dto.setUserName(map.get(blackDictionary.getUserId()));
				arrayList.add(dto);
			}
		}
		Page<BlackwordsDTO> page2 = new Page<BlackwordsDTO>();
		page2.setList(arrayList);
		page2.setStart(page.getStart());
		page2.setLimit(page.getLimit());
		page2.setTotalCount(page.getTotalCount());
		page2.setTotalPage(page.getTotalPage());
		page2.setCurrentPageNum(page.getCurrentPageNum());
		ResponseUtils.outputWithJson(response, Result.rightResult(page2));
	}

	@RequestMapping("ajax/save")
	public void save(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String content = RequestUtils.getParameter(request, "content");
		String remark = RequestUtils.getParameter(request, "remark");
		String checkData = this.checkData(content);
		if (!RegularUtils.isEmpty(checkData)) {
			ResponseUtils.outputWithJson(response, Result.badResult(checkData));
			return;
		}
		FmsBlackDictionary fmsBlackDictionary = new FmsBlackDictionary();
		fmsBlackDictionary.setContent(content);
		fmsBlackDictionary.setCreateTime(new Date());
		fmsBlackDictionary.setRemark(remark);
		Long id = user.getId();
		fmsBlackDictionary.setUserId(id);
		fmsBlackDictionary.setIsDelete(0);
		Result result = fmsBlackDictionaryService.save(fmsBlackDictionary);
		ResponseUtils.outputWithJson(response, Result.rightResult(result));
	}

	@RequestMapping("ajax/detele")
	public void detele(HttpServletRequest request, HttpServletResponse response) {
		String content = RequestUtils.getParameter(request, "content");
		fmsBlackDictionaryService.deletebyContent(content);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	private String checkData(String content) {
		if (RegularUtils.isEmpty(content)) {
			return "内容不能为空";
		}
		if (content.length() > 50) {
			return "内容输入过长，不能大于50字";
		}
		FmsBlackDictionary name = fmsBlackDictionaryService.findbyName(content);
		if (name != null) {
			return "黑字典内容已存在";
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
		FmsBlackDictionarySchemaSheetDataHandler readData = new FmsBlackDictionarySchemaSheetDataHandler(currentUser.getId());
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
		String headStr = "attachment;filename=blackdictionary.xlsx";
		response.setHeader("Content-Disposition", headStr);
		response.setCharacterEncoding("utf-8");
		ExcelHelper.writeExcelInOutputStream(bean, false, true, response.getOutputStream());
	}
}
