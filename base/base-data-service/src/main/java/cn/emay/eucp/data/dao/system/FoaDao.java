package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Foa;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * 
 * @author gh
 */
public interface FoaDao extends BaseSuperDao<Foa> {

	
	/**
	 * 获取分页数据信息
	 * @param desc
	 * @param start
	 * @param limit
	 * @return
	 */
	public Page<Foa> selectFoa(String desc, Integer start, Integer limit);
	
	
	/**
	 * 获取相应系统端信息
	 * @param subSystem
	 */
	List<Foa> findBySystemType(Integer  subSystem,Integer businessType); 
	
	/**
	 * 获取所有端信息
	 */
	List<Foa> findAllList();
	
	
	/**
	 * 保存信息
	 */
	public int[] saveBatchFoa(List<Object[]> params);
	
	/**
	 * 模糊查询foa 通过问题描述
	 */
	public List<Foa> findListByDescProblem(String desc);
	
	public int updateFoa(Long id, Integer subSystem, Integer businessType,String descProblem,String reply);	
}
