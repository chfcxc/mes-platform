package cn.emay.eucp.web.manage.controller.global.fms.base;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypePage;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:37:54 类说明 */
@PageAuth("FMS_BASE_BUSINESS")
@RequestMapping("/fms/base/business")
@Controller
public class FmsBusinessTypeController {

	@Resource
	FmsBusinessTypeService fmsBusinessTypeService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinessType> busiList = fmsBusinessTypeService.findBusiName(-1L);
		model.addAttribute("busiList", busiList);
		return "fms/base/business";
	}

	@RequestMapping("/ajax/getcontent")
	public void busiTocontent(HttpServletRequest request, HttpServletResponse response, Model model) {
		int saveType = RequestUtils.getIntParameter(request, "saveType", -1);
		Long busiId = RequestUtils.getLongParameter(request, "busiId", -1);
		List<FmsBusinessType> contentList = fmsBusinessTypeService.findContentByBusi(saveType, busiId);
		ResponseUtils.outputWithJson(response, Result.rightResult(contentList));
	}

	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		Long busiId = RequestUtils.getLongParameter(request, "businessTypeName", -1);
		Long contentId = RequestUtils.getLongParameter(request, "content", -1);
		int saveType = RequestUtils.getIntParameter(request, "saveType", -1);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<FmsBusinesTypePage> page = fmsBusinessTypeService.findPage(busiId, saveType, contentId, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	@RequestMapping("/ajax/savebusi")
	public void saveBusi(HttpServletRequest request, HttpServletResponse response) {
		String businessTypeName = RequestUtils.getParameter(request, "businessTypeName");
		if (StringUtils.isEmpty(businessTypeName)) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能为空"));
			return;
		}
		List<FmsBusinessType> list = fmsBusinessTypeService.findByIdAndName(-1, "", businessTypeName);
		if (list != null && list.size() > 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("业务类型重复"));
			return;
		}
		FmsBusinessType fmsBusinessType = new FmsBusinessType();
		User user = WebUtils.getCurrentUser(request, response);
		fmsBusinessType.setCreateTime(new Date());
		fmsBusinessType.setFullPath("1");
		fmsBusinessType.setLevel(1);
		fmsBusinessType.setName(businessTypeName);
		fmsBusinessType.setParentId(0L);
		fmsBusinessType.setUserId(user.getId());
		fmsBusinessTypeService.save(fmsBusinessType);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	@RequestMapping("/ajax/savecontent")
	public void saveContent(HttpServletRequest request, HttpServletResponse response) {
		Long busiId = RequestUtils.getLongParameter(request, "busiId", -1);
		String businessTypeName = RequestUtils.getParameter(request, "businessTypeName");
		String contentName = RequestUtils.getParameter(request, "contentName");
		int saveType = RequestUtils.getIntParameter(request, "saveType", -1);
		List<FmsBusinessType> listFms = fmsBusinessTypeService.findByIdAndName(saveType, contentName, businessTypeName);
		if (listFms != null && listFms.size() > 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("内容类型重复"));
			return;
		}
		FmsBusinessType fmsBusinessType = new FmsBusinessType();
		User user = WebUtils.getCurrentUser(request, response);
		fmsBusinessType.setCreateTime(new Date());
		fmsBusinessType.setFullPath("1,2");
		fmsBusinessType.setLevel(2);
		fmsBusinessType.setName(contentName);
		List<FmsBusinessType> list = fmsBusinessTypeService.findBusiName(busiId);
		Long parentId = list.get(0).getId();
		fmsBusinessType.setParentId(parentId);
		fmsBusinessType.setUserId(user.getId());
		fmsBusinessType.setSaveType(saveType);
		fmsBusinessTypeService.save(fmsBusinessType);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	@RequestMapping("/ajax/findid")
	public void find(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "contentId", -1);
		Set<Long> ids = new HashSet<>();
		ids.add(id);
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(ids);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	@RequestMapping("/ajax/update")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		Long contentId = RequestUtils.getLongParameter(request, "contentId", -1);
		String contentName = RequestUtils.getParameter(request, "contentName");
		int saveType = RequestUtils.getIntParameter(request, "saveType", -1);
		String businessTypeName = RequestUtils.getParameter(request, "businessTypeName");
		List<FmsBusinessType> listFms = fmsBusinessTypeService.findByIdAndName(saveType, contentName, businessTypeName);
		if (listFms != null && listFms.size() > 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("内容类型重复"));
			return;
		}
		FmsBusinessType fmsBusinessType = new FmsBusinessType();
		User user = WebUtils.getCurrentUser(request, response);
		fmsBusinessType.setCreateTime(new Date());
		fmsBusinessType.setFullPath("1,2");
		fmsBusinessType.setLevel(2);
		fmsBusinessType.setName(contentName);
		List<FmsBusinessType> list = fmsBusinessTypeService.findContent(contentId);
		fmsBusinessType.setId(list.get(0).getId());
		fmsBusinessType.setParentId(list.get(0).getParentId());
		fmsBusinessType.setUserId(user.getId());
		fmsBusinessType.setSaveType(saveType);
		fmsBusinessTypeService.update(fmsBusinessType);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	@RequestMapping("/ajax/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "contentId", -1);
		List<FmsBusinessType> list = fmsBusinessTypeService.findContent(id);
		if (list == null || list.size() == 0) {
			ResponseUtils.outputWithJson(response, Result.badResult("内容类型不存在"));
			return;
		}
		fmsBusinessTypeService.delete(id);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}
}
