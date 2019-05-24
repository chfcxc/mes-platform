package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Feedback;
import cn.emay.eucp.data.dao.system.FeedbackDao;
import cn.emay.eucp.data.service.system.FeedbackService;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {

	@Resource
	private FeedbackDao feedbackDao;

	@Override
	public void saveFeedback(String content, String email, String mobile, String qq, Long userId) {
		Feedback entity = new Feedback();
		entity.setFeedbackContent(content);
		entity.setEmail(email);
		entity.setMobile(mobile);
		entity.setQq(qq);
		entity.setOperatorId(userId);
		entity.setCreateTime(new Date());
		entity.setHandleState(Feedback.STATE_NO_HANDLE);
		feedbackDao.save(entity);
	}

	@Override
	public Page<Map<String, Object>> findPage(String content, String email, String mobile, String qq, int state, int start, int limit) {
		List<?> list = feedbackDao.findPage(content, email, mobile, qq, state, start, limit);
		Long totalCount = feedbackDao.getTotal(content, email, mobile, qq, state);
		List<Map<String, Object>> feedbackList = new ArrayList<Map<String, Object>>();
		for (Object object : list) {
			Object[] obj = (Object[]) object;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", obj[0]);
			map.put("feedbackContent", obj[1]);
			map.put("email", obj[2] == null ? "" : obj[2]);
			map.put("mobile", obj[3] == null ? "" : obj[3]);
			map.put("qq", obj[4] == null ? "" : obj[4]);
			map.put("createTime", obj[5]);
			map.put("handleState", obj[6]);
			map.put("handleUserId", obj[7]);
			map.put("handleTime", obj[8] == null ? "" : obj[8]);
			map.put("userName", obj[9] == null ? "" : obj[9]);
			feedbackList.add(map);
		}
		// 分页信息
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		page.setNumByStartAndLimit(start, limit, Integer.parseInt(String.valueOf(totalCount)));
		page.setList(feedbackList);
		return page;
	}
	
	@Override
	public Result modifyFeedback(Long id,Long userId){
		Feedback entity=feedbackDao.findById(id);
		if(null==entity){
			return Result.badResult("数据不存在");
		}
		if(entity.getHandleState().intValue()!=Feedback.STATE_NO_HANDLE){
			return Result.badResult("该数据已处理");
		}
		entity.setHandleState(Feedback.STATE_HANDLE);
		entity.setHandleUserId(userId);
		entity.setHandleTime(new Date());
		feedbackDao.update(entity);
		return Result.rightResult(entity.getFeedbackContent());
	}

}
