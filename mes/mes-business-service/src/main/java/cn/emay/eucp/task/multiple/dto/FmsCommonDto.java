package cn.emay.eucp.task.multiple.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map 作为value的实体封装
 *
 */
public class FmsCommonDto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
