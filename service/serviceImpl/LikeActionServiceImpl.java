package cn.edu.bjtu.weibo.service.Impl;

import java.util.List;

import cn.edu.bjtu.weibo.dao.CommentDAO;
import cn.edu.bjtu.weibo.dao.UserDAO;
import cn.edu.bjtu.weibo.dao.WeiboDAO;
import cn.edu.bjtu.weibo.dao.Impl.CommentDAOImpl;
import cn.edu.bjtu.weibo.dao.Impl.UserDAOImpl;
import cn.edu.bjtu.weibo.dao.Impl.WeiboDAOImpl;
import cn.edu.bjtu.weibo.service.LikeActionService;

public class LikeActionServiceImpl implements LikeActionService{
	
	@Override
	public boolean LikeWeiboOrCommentAction(String userId, String weiboOrCommentId) {
		UserDAO Udao = new UserDAOImpl();
		CommentDAO Cdao = new CommentDAOImpl();
		WeiboDAO Wdao = new WeiboDAOImpl();	
		int pageIndex = 0;
		int pagePerNumber = 0;
		
		if (Cdao.getLikeList(weiboOrCommentId) != null) {
			List<String> Clist = Cdao.getLikeList(weiboOrCommentId);
			if(Clist.contains(userId)) {
				Cdao.deleteLikeUser(weiboOrCommentId, userId);
			}else {
				Cdao.insertLikeUser(weiboOrCommentId, userId);
			}
		}
		if (Wdao.getLikeList(weiboOrCommentId, pageIndex, pagePerNumber) != null) {
			List<String> Wlist = Wdao.getLikeList(weiboOrCommentId, pageIndex, pagePerNumber);
			if(Wlist.contains(userId)) {
				Wdao.deleteLikeUser(weiboOrCommentId, userId);
				Udao.deleteLikeWeibo(userId,weiboOrCommentId);//从我赞过的删除
			}else {
				Wdao.insertLikeUser(weiboOrCommentId, userId);
				Udao.insertLikeWeibo(userId,weiboOrCommentId);//加入我赞过的
			}
		}
		
		return true;
	}
}
