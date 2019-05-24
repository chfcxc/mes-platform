package cn.emay.eucp.data.service.system;

import java.util.Map;

import cn.emay.common.Result;
import cn.emay.common.db.Page;

public interface FeedbackService {

	/**
	 * 保存反馈
	 * @param content
	 * @param email
	 * @param mobile
	 * @param qq
	 * @param userId
	 */
	void saveFeedback(String content, String email, String mobile, String qq, Long userId);

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
	Page<Map<String, Object>> findPage(String content, String email, String mobile, String qq, int state, int start, int limit);
	
	/**
	 * 处理反馈
	 * @param id
	 * @param userId
	 * @return
	 */
	Result modifyFeedback(Long id, Long userId);

}
