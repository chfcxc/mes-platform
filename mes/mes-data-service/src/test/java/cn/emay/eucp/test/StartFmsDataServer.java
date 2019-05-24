package cn.emay.eucp.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.emay.eucp.data.service.fms.BatchInsertEntityService;

public class StartFmsDataServer {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/spring-context.xml" });
		context.start();
		System.out.println("1111111111111111");
		BatchInsertEntityService b = context.getBean(BatchInsertEntityService.class);
		List<MmsTmp> beanList = new ArrayList<MmsTmp>();
		for (int i = 0; i < 1000; i++) {
			MmsTmp model = new MmsTmp();
			if (i > 10) {
				model.setTitle("title" + i);
				model.setContent("content" + i);
			}
			beanList.add(model);
		}
		b.saveBatchList(beanList, "mms_tmp", false, false);
		System.out.println("完成");
		System.in.read();
	}
}
