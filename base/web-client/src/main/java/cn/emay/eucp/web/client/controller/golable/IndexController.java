package cn.emay.eucp.web.client.controller.golable;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.auth.Token;
import cn.emay.eucp.common.dto.user.ClientUserIndexDTO;
import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.BusinessService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.PassportService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.ResponseUtils;
/**
 * 首页
 *
 * @author Frank
 *
 */
@Controller
public class IndexController {

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "passportService")
	private PassportService passportService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "businessService")
	private BusinessService businessService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!WebUtils.isLogin(request, response)) {
			return "redirect:" + CommonConstants.PASSPORT_PATH + "/toLogin";
		}
		String tokenId = WebUtils.getCurrentTokenId(request);
		String tokenStr = passportService.getCurrentTokenStr(tokenId, true);
		Token token = JsonHelper.fromJson(Token.class, tokenStr);
		User user = WebUtils.getCurrentUser(request, response);
		ClientUserIndexDTO clientUserIndexDTO = userService.findByUserId(user);
		Enterprise enterprise = enterpriseService.findByUserId(user.getId());
		List<Business> businessList = businessService.findUserOpenBusinessList(enterprise.getServiceType());
		model.addAttribute("clientUserIndexDTO", clientUserIndexDTO);
		model.addAttribute("TOKEN", token);
		model.addAttribute("businessList", businessList);
		return "pub/index";
	}

	@RequestMapping("/")
	public String indexline(HttpServletRequest request, HttpServletResponse response, Model model) {
		return index(request, response, model);
	}

	/**
	 * 查看客户账号详情--已开通的服务信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/getBusinessList")
	public void getBusinessList(HttpServletRequest request, HttpServletResponse response) {
		if (!WebUtils.isLogin(request, response)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请重新登录"));
		}
		User user = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = enterpriseService.findByUserId(user.getId());
		List<Business> businessList = businessService.findUserOpenBusinessList(enterprise.getServiceType());
		ResponseUtils.outputWithJson(response, Result.rightResult(businessList));
	}

	/*
	@RequestMapping("/getFlowInfo")
	public void getFlowInfo(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		Long enterpriseId = userService.findenterpriseIdbyUser(user.getId());
		Long flowScCount = flowServiceCodeService.countByEnterpriseId(enterpriseId);// 统计企业流量服务号个数
		ClientUserIndexDTO clientUserIndexDTO = new ClientUserIndexDTO();
		clientUserIndexDTO.setServiceModules("1");// 1--开通流量服务
		if (flowScCount != null && flowScCount.longValue() > 0l) {
			clientUserIndexDTO.setServiceCount(1);// 开通服务个数
		} else {
			clientUserIndexDTO.setServiceCount(0);// 开通服务个数
		}
		// 流量服务号--首页展示3条
		List<SimpleFlowServiceCodeDTO> flowServiceCodeList = flowServiceCodeService.findByAssignUserId(user.getId(), 1, 3);
		// 统计流量当月发送量
		BigDecimal flowCount = new BigDecimal(0);
		Date startTime = DateUtil.getNowMonthFirstDay();
		Date endTime = DateUtil.getNowMonthLastDay();
		List<SimpleFlowServiceCodeDTO> dtoList = null;
		int currentPage = 1;
		int limit = 1000;
		do {
			dtoList = flowServiceCodeService.findByAssignUserId(user.getId(), currentPage, currentPage * limit);
			if (dtoList != null && dtoList.size() > 0) {
				List<String> scList = new ArrayList<String>();// 服务号
				for (SimpleFlowServiceCodeDTO dto : dtoList) {
					scList.add(dto.getServiceCode());
				}
				BigDecimal countPrice = flowTaskService.countByServiceCodes(scList, startTime, endTime);
				flowCount = flowCount.add(countPrice == null ? new BigDecimal(0) : countPrice);
			}
			currentPage++;
		} while (dtoList != null && dtoList.size() == limit);
		clientUserIndexDTO.setFlowCount(flowCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flowServiceCodeList", flowServiceCodeList);
		map.put("clientUserIndexDTO", clientUserIndexDTO);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}

	@RequestMapping("/getImsInfo")
	public void getImsInfo(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		ClientUserIndexDTO clientUserIndexDTO = new ClientUserIndexDTO();
		clientUserIndexDTO.setServiceModules("2");// 1--开通国际短信
		// 开通服务个数
		if (!StringUtils.isEmpty(enterprise.getServiceType()) && enterprise.getServiceType().contains(Enterprise.IMS_SERVICE)) {
			clientUserIndexDTO.setServiceCount(1);
		} else {
			clientUserIndexDTO.setServiceCount(0);
		}

		// 国际短信服务号--首页展示3条
		List<ImsServiceCode> imsServiceCodeList = imsUserServiceCodeAssignService.findByAssignUserId(user.getId(), 1, 3);
		// 统计国际短信当月发送量
		Long imsCount = 0l;
		Date startTime = DateUtil.getNowMonthFirstDay();
		Date endTime = DateUtil.getNowMonthLastDay();
		List<ImsServiceCode> dtoList = null;
		int currentPage = 1;
		int limit = 1000;
		do {
			dtoList = imsUserServiceCodeAssignService.findByAssignUserId(user.getId(), currentPage, currentPage * limit);
			if (dtoList != null && dtoList.size() > 0) {
				List<String> scList = new ArrayList<String>();// 服务号
				for (ImsServiceCode dto : dtoList) {
					scList.add(dto.getAppId());
				}
				// 目前查ImsMessage后期报表完成 可优化为查服务号报表
				Long countNumber = imsServiceCodeReportService.countByServiceCode(scList, startTime, endTime);
				if (null == countNumber) {
					countNumber = 0l;
				}
				imsCount += countNumber;
			}
			currentPage++;
		} while (dtoList != null && dtoList.size() == limit);
		clientUserIndexDTO.setImsCount(imsCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("imsServiceCodeList", imsServiceCodeList);
		map.put("clientUserIndexDTO", clientUserIndexDTO);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}
	@RequestMapping("/getVoiceInfo")
	public void getVoiceInfo(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		ClientUserIndexDTO clientUserIndexDTO = new ClientUserIndexDTO();
		clientUserIndexDTO.setServiceModules("3");// 1--开通语音短信
		// 开通服务个数
		if (!StringUtils.isEmpty(enterprise.getServiceType()) && enterprise.getServiceType().contains(Enterprise.VOICE_SERVICE)) {
			clientUserIndexDTO.setServiceCount(1);
		} else {
			clientUserIndexDTO.setServiceCount(0);
		}

		// 语音短信服务号--首页展示3条
		List<VoiceServiceCode> voiceServiceCodeList = voiceServiceCodeService.findByAssignUserId(user.getId(), 1, 3);
		// 统计语音短信当月发送量
		Long voiceCount = 0l;
		Date startTime = DateUtil.getNowMonthFirstDay();
		Date endTime = DateUtil.getNowMonthLastDay();
		List<VoiceServiceCode> dtoList = null;
		int currentPage = 1;
		int limit = 1000;
		do {
			dtoList = voiceServiceCodeService.findByAssignUserId(user.getId(), currentPage, currentPage * limit);
			if (dtoList != null && dtoList.size() > 0) {
				List<String> scList = new ArrayList<String>();// 服务号
				for (VoiceServiceCode dto : dtoList) {
					scList.add(dto.getAppId());
				}
				// 目前查voiceMessage后期报表完成 可优化为查服务号报表
				Long countNumber = voiceMessageService.countByAppIds(scList, startTime, endTime);
				voiceCount += countNumber;
			}
			currentPage++;
		} while (dtoList != null && dtoList.size() == limit);
		clientUserIndexDTO.setVoiceCount(voiceCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("voiceServiceCodeList", voiceServiceCodeList);
		map.put("clientUserIndexDTO", clientUserIndexDTO);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}
*/
}
