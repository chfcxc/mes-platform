package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Industry;

public interface IndustryService {

	Page<Industry> findPage(String industry, int start, int limit);

	Boolean isExist(String industry, Long id);

	Result addIndustry(String industry);

	Result modifyIndustry(Long id, String industry);

	Result deleteIndustry(Long id);

	List<Industry> findAllIndustry();

	Industry findById(Long id);
	
	List<Industry> findList(int currentPage, int pageSize, Date lastUpdateTime);
}
