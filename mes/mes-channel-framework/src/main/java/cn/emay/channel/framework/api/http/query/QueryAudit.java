package cn.emay.channel.framework.api.http.query;
/**
* @author dejun
* @version 创建时间：2019年5月6日 下午3:28:21
* 类说明
*/

import java.util.List;

import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelResponseDto;

public interface QueryAudit {

	List<FmsTemplateChannelResponseDto> queryAudit();
}
