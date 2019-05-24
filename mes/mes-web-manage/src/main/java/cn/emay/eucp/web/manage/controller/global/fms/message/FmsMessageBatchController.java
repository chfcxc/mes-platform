package cn.emay.eucp.web.manage.controller.global.fms.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.emay.eucp.common.dto.fms.message.FmsBatchPageDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.data.service.fms.FmsBatchService;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_MESSAGE_BATCH")
@RequestMapping("/fms/message/batch")
@Controller
public class FmsMessageBatchController {
	@Resource
	private FmsBatchService fmsBatchService;

	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;

	@Resource
	private FmsServiceCodeService fmsServiceCodeService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinessType> busiList = fmsBusinessTypeService.findBusiName(-1L);
		model.addAttribute("busiList", busiList);
		return "fms/message/batch";
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
		String batchNumber = RequestUtils.getParameter(request, "batchNumber");
		String title = RequestUtils.getParameter(request, "title");
		String serviceCodeIdString = request.getParameter("serviceCodeId");
		Long serviceCodeId = 0L;
		int state = RequestUtils.getIntParameter(request, "state", -1);
		int sendType = RequestUtils.getIntParameter(request, "sendType", -1);// 提交方式
		int infoType = RequestUtils.getIntParameter(request, "infoType", -1);// 信息类型
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", -1);// 业务类型
		int saveType = RequestUtils.getIntParameter(request, "saveType", -1);// 保存类型
		Long contentTypeId = RequestUtils.getLongParameter(request, "contentTypeId", -1);// 内容类型
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		List<Long> userIds = new ArrayList<Long>();
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		if (!StringUtils.isEmpty(serviceCodeIdString)) {
			serviceCodeIdString = serviceCodeIdString.trim();
			if (serviceCodeIdString.contains("-")) {
				FmsServiceCode fmsServiceCode = fmsServiceCodeService.findByserviceCode(serviceCodeIdString);
				if (null != fmsServiceCode) {
					serviceCodeId = fmsServiceCode.getId();
				} else {
					serviceCodeId = -1L;
				}
			} else {
				try {
					serviceCodeId = Long.parseLong(serviceCodeIdString);
				} catch (Exception e) {
					serviceCodeId = -1L;
				}
			}
		}
		Page<FmsBatchPageDto> page = fmsBatchService.findPage(batchNumber, title, serviceCodeId, state, sendType, infoType, businessTypeId, saveType, contentTypeId, start, limit, startTime, endTime,
				userIds);
		if (page.getList() != null && page.getList().size() > 0) {
			List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
			Map<Long, FmsBusinesTypeDto> map = new HashMap<>();
			if (list != null && list.size() > 0) {
				for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
					map.put(fmsBusinesTypeDto.getId(), fmsBusinesTypeDto);
				}
			}
			for (FmsBatchPageDto fmsBatchPageDto : page.getList()) {
				fmsBatchPageDto.setBusinessType(map.get(fmsBatchPageDto.getContentTypeId()).getParentName());
				fmsBatchPageDto.setContentType(map.get(fmsBatchPageDto.getContentTypeId()).getName());
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

}
