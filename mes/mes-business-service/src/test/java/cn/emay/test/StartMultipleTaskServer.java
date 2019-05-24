package cn.emay.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartMultipleTaskServer {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/spring-context.xml" });
		context.start();
		System.in.read();
	}
}
