package cn.emay.eucp.data.dao.common;

import java.io.Serializable;
import java.util.List;

/**
 * 通用dao方法定义
 * 
 * @author Frank
 *
 * @param <E>
 */
public interface BaseSuperDao<E extends Serializable> {

	/**
	 * 增
	 */
	public void save(E entity);

	/**
	 * 改
	 */
	public void update(E entity);

	/**
	 * 删
	 */
	public void delete(E entity);

	/**
	 * 根据ID查
	 */
	public E findById(Serializable id);

	/**
	 * 查询所有
	 */
	public List<E> findAll();

	/**
	 * 批量增 hibernate 逐条插入，会有性能瓶颈，有性能要求时，不能使用
	 */
	public void saveBatch(List<E> entities);

	/**
	 * 批量改 hibernate 逐条更新，会有性能瓶颈，有性能要求时，不能使用
	 */
	public void updateBatch(List<E> entities);

	/**
	 * 根据ID批量删 推荐使用
	 * 
	 * @param ids
	 */
	public void deleteBatchByPKid(List<E> entities);

	/**
	 * 根据ID批量删 推荐使用
	 * 
	 * @param ids
	 */
	public void deleteBatchByPKids(List<? extends Serializable> ids);
	
	/**
	 * 批量清除对象
	 */
	public void evictBatch(List<E> entities);

}
