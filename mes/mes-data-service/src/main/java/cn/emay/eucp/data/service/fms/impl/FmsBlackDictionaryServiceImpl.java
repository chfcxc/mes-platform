package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.fms.FmsBlackDictionary;
import cn.emay.eucp.data.dao.fms.FmsBlackDictionaryDao;
import cn.emay.eucp.data.service.fms.FmsBlackDictionaryService;

/** @author dejun
 * @version 创建时间：2019年4月26日 下午4:34:17 类说明 */
@Service("fmsBlackDictionaryService")
public class FmsBlackDictionaryServiceImpl implements FmsBlackDictionaryService {

	@Resource
	FmsBlackDictionaryDao fmsBlackDictionaryDao;

	@Override
	public Page<FmsBlackDictionary> findPage(String content, int start, int limit) {
		return fmsBlackDictionaryDao.findPage(content, start, limit);
	}

	@Override
	public Result save(FmsBlackDictionary fmsBlackDictionary) {
		fmsBlackDictionaryDao.save(fmsBlackDictionary);
		return Result.rightResult();
	}

	@Override
	public FmsBlackDictionary findbyName(String content) {
		return fmsBlackDictionaryDao.findbyName(content);
	}

	@Override
	public FmsBlackDictionary findid(Long id) {
		return fmsBlackDictionaryDao.findById(id);
	}

	@Override
	public List<FmsBlackDictionary> findFmsBlackDictionaryDTOForPageByIsDeleteAndLastUpdateTime(int currentPage, int pageSize, Date lastUpdateTime, Boolean isDelete) {
		return fmsBlackDictionaryDao.findFmsBlackDictionaryDTOForPageByIsDeleteAndLastUpdateTime(currentPage, pageSize, lastUpdateTime, isDelete);
	}

	@Override
	public void deletebyContent(String content) {
		fmsBlackDictionaryDao.deletebyContent(content);

	}

}
