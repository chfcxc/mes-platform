package cn.emay.eucp.web.manage.controller.global.fms.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.FmsGlobalConstants;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.channel.FmsChannelDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.util.RegularCheckUtils;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsChannelInfoService;
import cn.emay.eucp.data.service.fms.FmsChannelService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.eucp.web.manage.controller.util.StringUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CHANNEL_MANAGE")
@RequestMapping("/fms/channel/manage")
@Controller
public class FmsChannelController {
	@Resource
	private FmsChannelService fmsChannelService;
	@Resource
	private FmsChannelInfoService fmsChannelInfoService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;

	@Resource(name = "redis")
	private RedisClient redis;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinessType> name = fmsBusinessTypeService.findBusiName(-1L);
		model.addAttribute("list", name);
		return "fms/channel/manage";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		int state = RequestUtils.getIntParameter(request, "state", -1);
		String channelName = RequestUtils.getParameter(request, "channelName");
		String channelNumber = RequestUtils.getParameter(request, "channelNumber");
		Long businessId = RequestUtils.getLongParameter(request, "businessId", 0L);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<FmsChannelDTO> page = fmsChannelService.findPage(channelName, channelNumber, state, start, limit, businessId);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));

	}

	@RequestMapping("/to/newchannel")
	public String newchannel(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
		Map<String, List<FmsBusinesTypeDto>> map = new HashMap<String, List<FmsBusinesTypeDto>>();
		for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
			if (map.isEmpty()) {
				List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
				list2.add(fmsBusinesTypeDto);
				map.put(fmsBusinesTypeDto.getParentName(), list2);
			} else {
				if (!map.containsKey(fmsBusinesTypeDto.getParentName())) {
					List<FmsBusinesTypeDto> list1 = new ArrayList<FmsBusinesTypeDto>();
					list1.add(fmsBusinesTypeDto);
					map.put(fmsBusinesTypeDto.getParentName(), list1);
				} else {
					String parentName = fmsBusinesTypeDto.getParentName();
					List<FmsBusinesTypeDto> list3 = map.get(parentName);
					list3.add(fmsBusinesTypeDto);
					map.put(parentName, list3);
				}

			}
		}
		model.addAttribute("map", map);
		return "fms/channel/add";
	}

	/**
	 * 通道新增-三方 跳转
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/to/addthree")
	public String addthree(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "fms/channel/addthree";
	}

	@RequestMapping("/ajax/on")
	public void on(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		if (id <= 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("通道id错误"));
			return;
		}

		FmsChannel fmsChannel = fmsChannelService.findIdandState(id, FmsGlobalConstants.CHANNEL_COMMAND_START);
		if (null == fmsChannel) {
			ResponseUtils.outputWithJson(response, Result.badResult("没有查到通道信息，请检查。"));
			return;
		}
		String message = channelControl(id.toString(), FmsGlobalConstants.CHANNEL_COMMAND_START, fmsChannel);
		if (!StringUtils.isEmpty(message)) {
			ResponseUtils.outputWithJson(response, Result.badResult(message));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());

	}

	@RequestMapping("/ajax/off")
	public void off(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		if (id <= 0L) {
			ResponseUtils.outputWithJson(response, Result.badResult("通道id错误"));
			return;
		}

		FmsChannel fmsChannel = fmsChannelService.findIdandState(id, FmsGlobalConstants.CHANNEL_COMMAND_STOP);
		if (null == fmsChannel) {
			ResponseUtils.outputWithJson(response, Result.badResult("没有查到通道信息，请检查。"));
			return;
		}
		String message = channelControl(id.toString(), FmsGlobalConstants.CHANNEL_COMMAND_STOP, fmsChannel);
		if (!StringUtils.isEmpty(message)) {
			ResponseUtils.outputWithJson(response, Result.badResult(message));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());

	}

	public String channelControl(String channelId, String command, FmsChannel channel) {
		String message = "";
		if (FmsGlobalConstants.CHANNEL_COMMAND_START.equals(command)) {
			if (FmsChannel.CHANNEL_STATE_ON == channel.getState()) {
				message = "通道已启动";
			} else {
				fmsChannelService.updateChannelState(Long.parseLong(channelId), FmsChannel.CHANNEL_STATE_ON);
				reloadChannel(channelId);
				redis.hset(RedisConstants.FMS_CHANNEL_COMMAND_HASH, channelId, FmsChannel.CHANNEL_STATE_ON, -1);
			}
		} else if (FmsGlobalConstants.CHANNEL_COMMAND_STOP.equals(command)) {
			if (FmsChannel.CHANNEL_STATE_OFF == channel.getState()) {
				message = "通道已停止";
			} else {
				fmsChannelService.updateChannelState(Long.parseLong(channelId), FmsChannel.CHANNEL_STATE_OFF);
				reloadChannel(channelId);
				redis.hset(RedisConstants.FMS_CHANNEL_COMMAND_HASH, channelId, FmsChannel.CHANNEL_STATE_OFF, -1);
			}
		}
		return message;
	}

	private void reloadChannel(String channelId) {
		FmsChannel entity = fmsChannelService.findById(Long.parseLong(channelId));
		List<FmsChannelInfo> list = fmsChannelInfoService.findByFmsChannelId(Long.parseLong(channelId));
		redis.hset(RedisConstants.FMS_CHANNEL_HASH, channelId, entity, -1);
		redis.hset(RedisConstants.FMS_CHANNEL_PARAM_HASH, channelId, list, -1);
	}

	@RequestMapping("ajax/addChannel")
	public void addChannel(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String params = RequestUtils.getParameter(request, "params");
		if (StringUtils.isEmpty(params)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不能为空"));
			return;
		}
		params = params.replaceAll("&quot;", "\"");
		FmsChannel fmsChannel = JsonHelper.fromJson(FmsChannel.class, params);
		fmsChannel.setId(0L);
		Result result = this.checkCommon(fmsChannel);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		String channelParams = RequestUtils.getParameter(request, "channelParams");
		if (StringUtils.isEmpty(channelParams)) {
			ResponseUtils.outputWithJson(response, Result.badResult("协议参数配置不能为空"));
			return;
		}
		fmsChannel.setUserId(user.getId());
		Result result2 = fmsChannelService.addChannnel(fmsChannel, channelParams);
		ResponseUtils.outputWithJson(response, result2);
	}

	@RequestMapping("/to/modify")
	public String modify(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0L);
		FmsChannel mmsChannel = fmsChannelService.findById(id);
		FmsBusinessType findbyId = fmsBusinessTypeService.findbyId(mmsChannel.getBusinessTypeId());
		mmsChannel.setSaveType(findbyId.getSaveType());
		mmsChannel.setContentType(findbyId.getName());
		FmsBusinessType parentId = fmsBusinessTypeService.findParentId(findbyId.getParentId());
		mmsChannel.setBusinessType(parentId.getName());
		model.addAttribute("mmsChannel", mmsChannel);
		List<FmsChannelInfo> mmsChannelInfoList = fmsChannelInfoService.findByFmsChannelId(id);
		model.addAttribute("mmsChannelInfoList", mmsChannelInfoList);
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
		Map<String, List<FmsBusinesTypeDto>> map = new HashMap<String, List<FmsBusinesTypeDto>>();
		for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
			if (!map.isEmpty()) {
				if (!map.containsKey(fmsBusinesTypeDto.getParentName())) {
					List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
					list2.add(fmsBusinesTypeDto);
					map.put(fmsBusinesTypeDto.getParentName(), list2);
				} else {
					String parentName = fmsBusinesTypeDto.getParentName();
					List<FmsBusinesTypeDto> list3 = map.get(parentName);
					list3.add(fmsBusinesTypeDto);
					map.put(parentName, list3);
				}
			} else {
				List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
				list2.add(fmsBusinesTypeDto);
				map.put(fmsBusinesTypeDto.getParentName(), list2);
			}
		}
		model.addAttribute("map", map);
		return "fms/channel/modify";
	}

	@RequestMapping("/ajax/modify")
	public void updateChannel(HttpServletRequest request, HttpServletResponse response) {
		String params = RequestUtils.getParameter(request, "params");
		if (StringUtils.isEmpty(params)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不能为空"));
			return;
		}
		params = params.replaceAll("&quot;", "\"");
		FmsChannel fmsChannel = JsonHelper.fromJson(FmsChannel.class, params);

		Result result = this.checkCommon(fmsChannel);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		String channelParams = RequestUtils.getParameter(request, "channelParams");
		if (StringUtils.isEmpty(channelParams)) {
			ResponseUtils.outputWithJson(response, Result.badResult("协议参数配置不能为空"));
			return;
		}
		Result result2 = fmsChannelService.update(fmsChannel, channelParams);
		ResponseUtils.outputWithJson(response, result2);
	}

	public Result checkCommon(FmsChannel mmsChannel) {
		int sendSpeed = mmsChannel.getSendSpeed();
		if (sendSpeed == -1 || sendSpeed > 10000) {
			return Result.badResult("发送速度不能超过10000");
		}
		if (StringUtil.isEmpty(mmsChannel.getChannelName())) {
			return Result.badResult("通道名称不可为空");
		}
		if (mmsChannel.getChannelName().length() > 30) {
			return Result.badResult("通道名称长度不可超过30个字符");
		}
		if (!RegularCheckUtils.notExistSpecial(mmsChannel.getChannelName())) {
			return Result.badResult("通道名称不能包含特殊字符");
		}

		if (StringUtil.isEmpty(mmsChannel.getProviders())) {
			return Result.badResult("请选择允许发送运营商");
		}
		// 校验通道名称唯一
		Long exist = fmsChannelService.isExist(mmsChannel.getId(), mmsChannel.getChannelName().trim());
		if (exist > 0) {
			return Result.badResult("通道名称已存在");
		}
		return Result.rightResult();
	}

}
