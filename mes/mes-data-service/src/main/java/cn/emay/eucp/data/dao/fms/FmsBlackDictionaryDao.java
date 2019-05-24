package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午5:36:36 类说明 */
public interface FmsBlackDictionaryDao extends BaseSuperDao<FmsBlackDictionary> {

	public Page<FmsBlackDictionary> findPage(String content, int start, int limit);

	public FmsBlackDictionary findbyName(String content);

	public void deletebyContent(String content);

	List<FmsBlackDictionary> findFmsBlackDictionaryDTOForPageByIsDeleteAndLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime, Boolean isDelete);

}
