package cn.emay.eucp.common.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 省份
 * 
 * @author Frank
 *
 */
public enum Province {

	北京("北京市", "11"), //
	天津("天津市", "12"), //
	河北("河北省", "13"), //
	山西("山西省", "14"), //
	内蒙古("内蒙古自治区", "15"), //
	辽宁("辽宁省", "21"), //
	吉林("吉林省", "22"), //
	黑龙江("黑龙江省", "23"), //
	上海("上海市", "31"), //
	江苏("江苏省", "32"), //
	浙江("浙江省", "33"), //
	安徽("安徽省", "34"), //
	福建("福建省", "35"), //
	江西("江西省", "36"), //
	山东("山东省", "37"), //
	河南("河南省", "41"), //
	湖北("湖北省", "42"), //
	湖南("湖南省", "43"), //
	广东("广东省", "44"), //
	广西("广西壮族自治区", "45"),// 
	海南("海南省", "46"), //
	重庆("重庆市", "50"), //
	四川("四川省", "51"), //
	贵州("贵州省", "52"), //
	云南("云南省", "53"), //
	西藏("西藏自治区", "54"),// 
	陕西("陕西省", "61"), //
	甘肃("甘肃省", "62"), //
	青海("青海省", "63"), //
	宁夏("宁夏回族自治区", "64"),// 
	新疆("新疆维吾尔自治区", "65"),// 
	台湾("台湾省", "71")//

	;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;
	
	private static List<ProvinceDTO> dtos;
	
	private static Object syobj = new Object();

	private Province(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public static List<ProvinceDTO> toDtos() {
		if(dtos == null) {
			synchronized (syobj) {
				if(dtos == null) {
					dtos = new ArrayList<ProvinceDTO>();
					for (Province oc : Province.values()) {
						dtos.add(new ProvinceDTO(oc.name, oc.code));
					}
				}
			}
		}
		return dtos;
	}

	public static String findNameByCode(String code) {
		for (Province oc : Province.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (Province oc : Province.values()) {
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
