package cn.emay.eucp.common.dto.servicemodule;

import java.util.ArrayList;
import java.util.List;

/**
 * 增值服务
 *
 */
public enum ValueAddedService {

	短链接简易版("短链接简易版", "1"), //
	短链接精确版("短链接精确版", "2") //
	;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	private static List<ValueAddedServiceDto> dtos;

	private static Object syobj = new Object();

	private ValueAddedService(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static List<ValueAddedServiceDto> toDtos() {
		if (dtos == null) {
			synchronized (syobj) {
				if (dtos == null) {
					dtos = new ArrayList<ValueAddedServiceDto>();
					for (ValueAddedService oc : ValueAddedService.values()) {
						dtos.add(new ValueAddedServiceDto(oc.name, oc.code));
					}
				}
			}
		}
		return dtos;
	}

	public static String findNameByCode(String code) {
		for (ValueAddedService oc : ValueAddedService.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (ValueAddedService oc : ValueAddedService.values()) {
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
	}

}
