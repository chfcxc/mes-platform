package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsTemplateDao extends BaseSuperDao<FmsTemplate> {

	List<FmsTemplate> findByLastUpdateTime(Date date, int currentPage, int pageSize);

}
