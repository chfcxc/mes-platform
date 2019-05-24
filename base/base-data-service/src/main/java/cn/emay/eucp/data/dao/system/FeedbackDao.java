package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.Feedback;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FeedbackDao extends BaseSuperDao<Feedback> {

	/**
	 * 分页查询
	 * @param content
	 * @param email
	 * @param mobile
	 * @param qq
	 * @param state
	 * @param start
	 * @param limit
	 * @return
	 */
	List<?> findPage(String content, String email, String mobile, String qq, int state, int start, int limit);
	
	/**
	 * 查询总数
	 * @param content
	 * @param email
	 * @param mobile
	 * @param qq
	 * @param state
	 * @return
	 */
	Long getTotal(String content, String email, String mobile, String qq, int state);

}
