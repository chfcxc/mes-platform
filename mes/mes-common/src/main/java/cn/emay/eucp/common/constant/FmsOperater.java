package cn.emay.eucp.common.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @项目名称：eucp-fms-common 
 * @类描述：  
 * @创建人：Cbb  
 * @创建时间：2019年5月10日 下午5:15:06  
 * @修改人：Cbb  
 * @修改时间：2019年5月10日 下午5:15:06  
 * @修改备注：
 */
public enum FmsOperater {
	/**
	 * 移动
	 */
	CMCC("移动", "1"), //
	/**
	 * 联通
	 */
	CUCC("联通", "2"), //
	/**
	 * 电信
	 */
	CTCC("电信", "3");

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	/**
	 * enum lookup map
	 */
	private static final Map<String, String> LOOK_UP = new HashMap<String, String>();

	static {
		for (FmsOperater s : EnumSet.allOf(FmsOperater.class)) {
			LOOK_UP.put(s.getCode(), s.getName());
		}
	}

	private FmsOperater(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (FmsOperater oc : FmsOperater.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (FmsOperater oc : FmsOperater.values()) {
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

	public static Map<String, String> lookup() {
		return LOOK_UP;
	}

	public static void main(String[] args) {
		System.out.println(findNameByCode("1"));
	}
}
