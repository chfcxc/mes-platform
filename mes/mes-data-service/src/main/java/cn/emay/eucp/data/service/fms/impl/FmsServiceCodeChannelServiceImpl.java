package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeChannelDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeChannelDao;
import cn.emay.eucp.data.service.fms.FmsServiceCodeChannelService;

@Service("fmsServiceCodeChannelService")
public class FmsServiceCodeChannelServiceImpl implements FmsServiceCodeChannelService {
	@Resource
	private FmsServiceCodeChannelDao fmsServiceCodeChannelDao;

	@Override
	public void add(FmsServiceCodeChannel entity) {
		fmsServiceCodeChannelDao.save(entity);
	}

	@Override
	public void update(FmsServiceCodeChannel entity) {
		fmsServiceCodeChannelDao.update(entity);
	}

	@Override
	public Map<String, String> getListByServiceCodeId(Long serviceCodeId) {
		List<FmsServiceCodeChannel> codeId = fmsServiceCodeChannelDao.getListByServiceCodeId(serviceCodeId);
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer personCmcc = new StringBuffer();
		StringBuffer personCucc = new StringBuffer();
		StringBuffer personCtcc = new StringBuffer();
		StringBuffer ordinaryCmcc = new StringBuffer();
		StringBuffer ordinaryCucc = new StringBuffer();
		StringBuffer ordinaryCtcc = new StringBuffer();
		if (null != codeId) {
			for (FmsServiceCodeChannel fmsServiceCodeChannel : codeId) {
				String operatorCode = fmsServiceCodeChannel.getOperatorCode();
				Long channelId = fmsServiceCodeChannel.getChannelId();
				if (fmsServiceCodeChannel.getTemplateType() == 0) {
					if (operatorCode.equalsIgnoreCase("CMCC")) {
						ordinaryCmcc.append(operatorCode).append(",").append(channelId + "");
					}
					if (operatorCode.equalsIgnoreCase("CUCC")) {
						ordinaryCucc.append(operatorCode).append(",").append(channelId + "");
					}
					if (operatorCode.equalsIgnoreCase("CTCC")) {
						ordinaryCtcc.append(operatorCode).append(",").append(channelId + "");
					}
				} else {
					if (operatorCode.equalsIgnoreCase("CMCC")) {
						personCmcc.append(operatorCode).append(",").append(channelId + "");
					}
					if (operatorCode.equalsIgnoreCase("CUCC")) {
						personCucc.append(operatorCode).append(",").append(channelId + "");
					}
					if (operatorCode.equalsIgnoreCase("CTCC")) {
						personCtcc.append(operatorCode).append(",").append(channelId + "");
					}
				}
			}

			map.put("ordinary", ordinaryCmcc + "#" + ordinaryCucc + "#" + ordinaryCtcc);
			map.put("person", personCmcc + "#" + personCucc + "#" + personCtcc);
		}

		return map;
	}

	@Override
	public List<FmsServiceCodeChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsServiceCodeChannelDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

	@Override
	public List<FmsServiceCodeChannelDto> findBindingChannel(Long serviceCodeId) {
		return fmsServiceCodeChannelDao.findBindingChannel(serviceCodeId);
	}

}
