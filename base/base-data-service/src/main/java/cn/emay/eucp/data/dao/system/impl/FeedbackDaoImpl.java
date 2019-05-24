package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.eucp.common.moudle.db.system.Feedback;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.FeedbackDao;
import cn.emay.eucp.util.RegularUtils;

@Repository
public class FeedbackDaoImpl extends PojoDaoImpl<Feedback> implements FeedbackDao {

	@Override
	public List<?> findPage(String content, String email, String mobile, String qq, int state, int start, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select f.id,f.feedback_content,f.email,f.mobile,f.qq,f.create_time,f.handle_state,f.handle_user_id,f.handle_time,u.username from system_feedback f left join system_user u on f.handle_user_id=u.id where 1=1";
		if (!StringUtils.isEmpty(content)) {
			sql += " and f.feedback_content like :content";
			params.put("content", "%" + RegularUtils.specialCodeEscape(content) + "%");
		}
		if (!StringUtils.isEmpty(email)) {
			sql += " and f.email = :email";
			params.put("email", email);
		}
		if (!StringUtils.isEmpty(mobile)) {
			sql += " and f.mobile = :mobile";
			params.put("mobile", mobile);
		}
		if (!StringUtils.isEmpty(qq)) {
			sql += " and f.qq = :qq";
			params.put("qq", qq);
		}
		if (state > -1) {
			sql += " and f.handle_state = :state";
			params.put("state", state);
		}
		sql += " order by f.create_time desc";
		return this.getPageListResultBySqlByHibernate(sql, start, limit, params);
	}

	@Override
	public Long getTotal(String content, String email, String mobile, String qq, int state) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select count(f.id) from system_feedback f left join system_user u on f.handle_user_id=u.id where 1=1";
		if (!StringUtils.isEmpty(content)) {
			sql += " and f.feedback_content like ?";
			params.add("%" + RegularUtils.specialCodeEscape(content) + "%");
		}
		if (!StringUtils.isEmpty(email)) {
			sql += " and f.email = ?";
			params.add(email);
		}
		if (!StringUtils.isEmpty(mobile)) {
			sql += " and f.mobile = ?";
			params.add(mobile);
		}
		if (!StringUtils.isEmpty(qq)) {
			sql += " and f.qq = ?";
			params.add(qq);
		}
		if (state > -1) {
			sql += " and f.handle_state = ?";
			params.add(state);
		}
		return Long.parseLong(this.getUniqueResultBySql(sql.toString(), params.toArray()).toString());
	}

}
