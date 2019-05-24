package cn.emay.eucp.common.constant;

/** 需要导出的模块 */
public enum ExportModule {
/*
	国际短信明细查询导出("国际短信明细查询导出", "IMSMESSAGE"), 历史消费明细导出("历史消费明细导出", "HISTORICALCONSUMPTIONDETAILS"), 管理端服务号报表导出("管理端服务号报表导出", "IMSSERVICODEREPORT"), 管理端服务号国家发送报表导出("管理端服务号国家发送报表导出",
			"IMSSERVICODECOUNTRYREPORT"), 管理端账单统计报表导出("管理端账单统计报表导出", "IMSACCOUNTREPORT"), 管理端通道报表导出("管理端通道报表导出", "IMSCHANNELREPORT"), 客户端服务号报表导出("客户端服务号报表导出",
					"IMSSERVICODEREPORT"), 客户端服务号国家发送报表导出("客户端服务号国家发送报表导出", "IMSSERVICODECOUNTRYREPORT"), 客户端账单统计报表导出("客户端账单统计报表导出", "IMSACCOUNTREPORT"), 充值扣费明细导出("充值扣费明细导出", "CHARGERECORD");

	*//** 名称 *//*
	private String name;
	*//** 编码 *//*
	private String code;

	private ExportModule(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (ExportModule oc : ExportModule.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (ExportModule oc : ExportModule.values()) {
			if (oc.getName().equals(name)) {
				return oc.getCode();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}*/

}
