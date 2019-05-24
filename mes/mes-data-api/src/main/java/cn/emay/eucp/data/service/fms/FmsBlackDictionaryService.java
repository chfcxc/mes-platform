package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:30:24 类说明 */
public interface FmsBlackDictionaryService {

	Page<FmsBlackDictionary> findPage(String content, int start, int limit);

	public Result save(FmsBlackDictionary fmsBlackDictionary);

	public FmsBlackDictionary findbyName(String content);

	public FmsBlackDictionary findid(Long id);

	public void deletebyContent(String content);

	List<FmsBlackDictionary> findFmsBlackDictionaryDTOForPageByIsDeleteAndLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime, Boolean isDelete);
}
