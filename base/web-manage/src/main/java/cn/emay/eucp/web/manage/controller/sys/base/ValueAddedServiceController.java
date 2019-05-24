package cn.emay.eucp.web.manage.controller.sys.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.servicemodule.ValueAddedService;
import cn.emay.eucp.common.dto.servicemodule.ValueAddedServiceDto;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 增值服务
 * 
 */
@PageAuth("SYS_VALUE_ADDED_SERVICE")
@RequestMapping("/sys/value/added/service")
@Controller
public class ValueAddedServiceController {

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "/sys/base/valueadded";
	}

	/**
	 * 配置列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String valueAddedService = RequestUtils.getParameter(request, "valueAddedService");

		List<ValueAddedServiceDto> list = ValueAddedService.toDtos();
		if (!StringUtils.isEmpty(valueAddedService)) {
			List<ValueAddedServiceDto> dtoList = new ArrayList<ValueAddedServiceDto>();
			for (ValueAddedServiceDto vas : list) {
				if (vas.getName().contains(valueAddedService)) {
					dtoList.add(vas);
				}
			}
			list = dtoList;
		}
		Page<ValueAddedServiceDto> page = new Page<ValueAddedServiceDto>(start / limit + 1, limit, list.size(), list);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

}
