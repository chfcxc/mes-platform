package cn.emay.channel.framework.api.http.query;

import java.util.List;

import cn.emay.channel.framework.dto.FmsChannelReport;

/** 主动查询状态报告
 * 
 * @author dejun */
public interface QueryReport {

	List<FmsChannelReport> query();
}
