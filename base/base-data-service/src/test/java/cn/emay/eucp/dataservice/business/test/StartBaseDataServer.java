package cn.emay.eucp.dataservice.business.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartBaseDataServer {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/spring-context.xml" });
		context.start();
		System.in.read();
	}
}
