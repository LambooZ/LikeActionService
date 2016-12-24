package cn.edu.bjtu.weibo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bjtu.weibo.dao.CommentDAO;
import cn.edu.bjtu.weibo.dao.UserDAO;
import cn.edu.bjtu.weibo.dao.WeiboDAO;
import cn.edu.bjtu.weibo.service.LikeActionService;
import cn.edu.bjtu.weibo.service.MessageToMeService;

@Service("LikeActionService")
public class LikeActionServiceImpl implements LikeActionService{
	@Autowired
	private UserDAO Udao;
	@Autowired
	private CommentDAO Cdao;
	@Autowired
	private WeiboDAO Wdao;	
	@Autowired
	private MessageToMeService service ;
	
	public boolean LikeWeiboOrCommentAction(String userId, String weiboOrCommentId) {
		
		if(weiboOrCommentId.startsWith("w")){
			Wdao.updateLikeList(weiboOrCommentId, userId);//更新赞的列表
			Wdao.updateLikeNmuber(weiboOrCommentId);//更新赞的数目
			Udao.insertLikeWeibo(userId, weiboOrCommentId);//insert和delete应该合并成update？
			//Udao.deleteLikeWeibo(userId, weiboOrCommentId);
			
			String Owner = Wdao.getOwner(weiboOrCommentId);
			service.likeMyWeiboInform(userId, Owner, weiboOrCommentId);//发消息
			return true;
		}else if(weiboOrCommentId.startsWith("c")) {
			Cdao.updateLikeList(weiboOrCommentId, userId);//更新赞的列表
			Cdao.updateLikeNumber();//更新赞的数目
			
			String Owner = Cdao.getOwner(weiboOrCommentId);
			service.likeMyCommentInform(userId, Owner, weiboOrCommentId);//发消息
			return true;
		}else {
			return false;
		}
	}
}
