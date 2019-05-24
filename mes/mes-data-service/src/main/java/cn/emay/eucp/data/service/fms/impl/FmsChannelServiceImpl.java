package cn.emay.eucp.data.service.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.channel.FmsChannelDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.dao.fms.FmsChannelDao;
import cn.emay.eucp.data.dao.fms.FmsChannelInfoDao;
import cn.emay.eucp.data.service.fms.FmsChannelService;

/**
 * @author dejun
 * @version 创建时间：2019年4月26日 下午3:53:51 类说明
 */
@Service("fmsChannelService")
public class FmsChannelServiceImpl implements FmsChannelService {

	@Resource
	private FmsChannelDao fmsChannelDao;
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsChannelInfoDao fmsChannelInfoDao;

	@Override
	public FmsChannel findById(Long id) {
		return fmsChannelDao.findById(id);
	}

	@Override
	public Page<FmsChannelDTO> findPage(String channeName, String channelNumber, int state, int start, int limit, Long businessId) {
		Page<FmsChannelDTO> page = fmsChannelDao.findPage(channeName, channelNumber, state, start, limit, businessId);
		Set<Long> businessTypeId = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsChannelDTO dto : page.getList()) {
				businessTypeId.add(dto.getBusinessTypeId());
			}
		}
		List<Object[]> findIds = fmsBusinessTypeDao.findIds(businessTypeId);
		Map<Long, FmsBusinesTypeDto> map = new HashMap<Long, FmsBusinesTypeDto>();
		if (null != findIds) {
			for (Object[] objects : findIds) {
				FmsBusinesTypeDto dto = new FmsBusinesTypeDto();
				dto.setId(Long.valueOf(objects[0].toString()));
				dto.setName(objects[1].toString());
				dto.setParentId(Long.valueOf(objects[2].toString()));
				dto.setSaveType(Integer.valueOf(objects[3].toString()));
				dto.setParentName(objects[4].toString());
				map.put(dto.getId(), dto);
			}
		}
		if (null != page.getList() && page.getList().size() > 0) {
			for (FmsChannelDTO dto : page.getList()) {
				FmsBusinesTypeDto typeDto = map.get(dto.getBusinessTypeId());
				dto.setBusinessType(typeDto.getParentName());
				dto.setSaveType(typeDto.getSaveType());
				dto.setContentType(typeDto.getName());
			}
		}
		return page;
	}

	@Override
	public List<FmsChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsChannelDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

	@Override
	public FmsChannel findIdandState(Long id, String controlType) {
		return fmsChannelDao.findIdandState(id, controlType);
	}

	@Override
	public void updateChannelState(Long id, int state) {
		fmsChannelDao.updateChannelState(id, state);
	}

	@Override
	public Long isExist(Long id, String channeName) {
		return fmsChannelDao.isExist(id, channeName);
	}

	@Override
	public Result addChannnel(FmsChannel fmsChannel, String channelParams) {
		fmsChannel.setState(FmsChannel.CHANNEL_STATE_OFF);
		if (null == fmsChannel.getCmccLimit()) {
			fmsChannel.setCmccLimit(70);
		}
		if (null == fmsChannel.getCuccLimit()) {
			fmsChannel.setCuccLimit(70);
		}
		if (null == fmsChannel.getCtccLimit()) {
			fmsChannel.setCtccLimit(70);
		}
		if (fmsChannel.getIsNeedReport() == FmsChannel.IS_NEED_REPORT) {
			if (null == fmsChannel.getReportType()) {
				fmsChannel.setReportType(0);
			}
		}
		fmsChannel.setCreateTime(new Date());
		fmsChannelDao.save(fmsChannel);
		if (!StringUtils.isEmpty(channelParams)) {
			List<FmsChannelInfo> mmsChannelInfoList = new ArrayList<FmsChannelInfo>();
			String[] channelParamsArr = channelParams.split(",");
			Set<String> channelParamsSet = new HashSet<String>();
			for (String params : channelParamsArr) {
				channelParamsSet.add(params);
			}
			for (String params : channelParamsSet) {
				String[] paramsArr = params.split("#");
				if (null != paramsArr && paramsArr.length != 0) {
					FmsChannelInfo mmsChannelInfo = new FmsChannelInfo();
					mmsChannelInfo.setPropertieyName(paramsArr[0]);
					mmsChannelInfo.setPropertyKey(paramsArr[1]);
					if (paramsArr.length > 2) {
						mmsChannelInfo.setPropertieyValue(paramsArr[2]);
					}
					mmsChannelInfo.setChannelId(fmsChannel.getId());
					mmsChannelInfo.setUserId(fmsChannel.getUserId());
					mmsChannelInfoList.add(mmsChannelInfo);
				}
			}
			fmsChannelInfoDao.saveBatch(mmsChannelInfoList);
		}
		return Result.rightResult();
	}

	@Override
	public Result update(FmsChannel fmsChannel, String channelParams) {
		FmsChannel channel = fmsChannelDao.findById(fmsChannel.getId());
		if (null == channel) {
			return Result.badResult("数据不存在");
		}
		if (channel.getState() == FmsChannel.CHANNEL_STATE_ON) {
			return Result.badResult("当前通道状态为启用，不能修改，请先停用");
		}
		channel.setBusinessTypeId(fmsChannel.getBusinessTypeId());
		channel.setChannelName(fmsChannel.getChannelName());
		channel.setChannelNumber(fmsChannel.getChannelNumber());
		channel.setCmccLimit(fmsChannel.getCmccLimit());
		channel.setCuccLimit(fmsChannel.getCuccLimit());
		channel.setCtccLimit(fmsChannel.getCtccLimit());
		channel.setIsNeedReport(fmsChannel.getIsNeedReport());
		channel.setProviders(fmsChannel.getProviders());
		channel.setReportType(fmsChannel.getReportType());
		channel.setSendSpeed(fmsChannel.getSendSpeed());
		channel.setTemplateType(fmsChannel.getTemplateType());
		fmsChannelDao.update(channel);
		fmsChannelInfoDao.deleteByChannelId(fmsChannel.getId());
		if (!StringUtils.isEmpty(channelParams)) {
			List<FmsChannelInfo> mmsChannelInfoList = new ArrayList<FmsChannelInfo>();
			String[] channelParamsArr = channelParams.split(",");
			Set<String> channelParamsSet = new HashSet<String>();
			for (String params : channelParamsArr) {
				channelParamsSet.add(params);
			}
			for (String params : channelParamsSet) {
				String[] paramsArr = params.split("#");
				if (null != paramsArr && paramsArr.length != 0) {
					FmsChannelInfo mmsChannelInfo = new FmsChannelInfo();
					mmsChannelInfo.setPropertieyName(paramsArr[0]);
					mmsChannelInfo.setPropertyKey(paramsArr[1]);
					if (paramsArr.length > 2) {
						mmsChannelInfo.setPropertieyValue(paramsArr[2]);
					}
					mmsChannelInfo.setChannelId(channel.getId());
					mmsChannelInfo.setUserId(channel.getUserId());
					mmsChannelInfoList.add(mmsChannelInfo);
				}
			}
			fmsChannelInfoDao.saveBatch(mmsChannelInfoList);
		}
		return Result.rightResult();
	}

	@Override
	public List<FmsChannel> findAll(int state) {
		return fmsChannelDao.findAll(state);
	}

}
