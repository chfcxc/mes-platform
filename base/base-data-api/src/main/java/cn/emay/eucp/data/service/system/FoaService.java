package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Foa;


public interface FoaService {

	public Page<Foa> selectFoa(String desc, Integer start, Integer limit);
	
	/**
	 * 获取相应系统端信息
	 * @param subSystem
	 */
	public List<Foa> findBySystemType(Integer  subSystem,Integer businessType); 
	
	/**
	 * 获取所有端信息
	 */
	public List<Foa> findAllList();
	
	
	/**
	 * 保存信息
	 */
	public int[] saveBatchFoa(List<Object[]> params);
	
	/**
	 * 模糊查询foa 通过问题描述
	 */
	public List<Foa> findListByDescProblem(String desc);
	
	
	public void saveFoa(Integer subSystem,Integer businessType,String  descProblem,String  reply);

	
	public Foa findById(Long id);
	
	public void updateFoa(Long id, Integer subSystem, Integer businessType, String descProblem,String reply);
}
