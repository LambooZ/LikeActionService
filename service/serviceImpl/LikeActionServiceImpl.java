package cn.edu.bjtu.weibo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bjtu.weibo.dao.CommentDAO;
import cn.edu.bjtu.weibo.dao.UserDAO;
import cn.edu.bjtu.weibo.dao.WeiboDAO;
import cn.edu.bjtu.weibo.service.LikeActionService;

@Service("LikeActionService")
public class LikeActionServiceImpl implements LikeActionService{
	@Autowired
	private UserDAO Udao;
	@Autowired
	private CommentDAO Cdao;
	@Autowired
	private WeiboDAO Wdao;	
	
	public boolean LikeWeiboOrCommentAction(String userId, String weiboOrCommentId) {
		
		if(weiboOrCommentId.startsWith("w")){
			Wdao.updateLikeList(weiboOrCommentId, userId);
			Wdao.updateLikeNmuber(weiboOrCommentId);
			Udao.insertLikeWeibo(userId, weiboOrCommentId);
			//String Owner = Wdao.getOwner(weiboOrCommentId);
			return true;
		}else if(weiboOrCommentId.startsWith("C")) {
			Cdao.updateLikeList(weiboOrCommentId, userId);
			Cdao.updateLikeNumber();
			//String Owner = Cdao.getOwner(weiboOrCommentId);
			return true;
		}else {
			return false;
		}
	}
}
