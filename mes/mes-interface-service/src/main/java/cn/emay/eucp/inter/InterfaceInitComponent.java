package cn.emay.eucp.inter;

import org.dom4j.Element;

import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.mis.core.Command;
import cn.emay.mis.core.Component;
import cn.emay.mis.support.MicroServiceSupport;
import cn.emay.store.redis.RedisClient;

public class InterfaceInitComponent implements Component<Object> {

	private static final long serialVersionUID = 1L;

	private String name;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void init(String name, Element infoElement) {
		this.name = name;

	}

	@Override
	public void start() {
		// 初始化redis以及接口框架参数
		RedisClient redis = MicroServiceSupport.getRedisClient("Redis");
		FmsInterfaceFrameWork.init(redis);
	}

	@Override
	public void stop() {

	}

	@Override
	public void destroy() {
		FmsInterfaceFrameWork.destroy();
	}

	@Override
	public <R> R execute(Command<R, Object> command) {
		return null;
	}

}
