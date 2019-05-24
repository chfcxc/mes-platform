package cn.emay.eucp.data.dao.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Industry;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface IndustryDao extends BaseSuperDao<Industry> {

	Page<Industry> findPage(String industry, int start, int limit);

	Industry getIndustry(String industry, Long id);

	List<Industry> findAllIndustry();

	List<Industry> findList(int currentPage, int pageSize, Date lastUpdateTime);
}
