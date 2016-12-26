package cn.edu.bjtu.weibo.service.Impl;

import java.util.List;

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
	private UserDAO udao;
	@Autowired
	private CommentDAO cdao;
	@Autowired
	private WeiboDAO wdao;	
	@Autowired
	private MessageToMeService service ;
	
	public boolean LikeWeiboOrCommentAction(String userId, String weiboOrCommentId,int pageIndex, int numberPerPage) {
		
		if(weiboOrCommentId.startsWith("w")){
			List<String> WeiboLikeList = wdao.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);
			String Owner = wdao.getOwner(weiboOrCommentId);
			if(WeiboLikeList.contains(userId)) {
				wdao.deleteLikeList(weiboOrCommentId, userId);
				udao.deleteLikeWeibo(userId, weiboOrCommentId);
				udao.deleteWeiboLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				wdao.insertLikeList(weiboOrCommentId, userId);
				udao.insertLikeWeibo(userId, weiboOrCommentId);
				udao.insertWeiboLikeMe(userId, Owner, weiboOrCommentId);
				service.likeMyWeiboInform(userId, Owner, weiboOrCommentId);//·¢ÏûÏ¢
			}
			return true;
		}else if(weiboOrCommentId.startsWith("c")) {
			List<String> CommentLikeList = cdao.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);
			String Owner = cdao.getOwner(weiboOrCommentId);
			if(CommentLikeList.contains(userId)) {
				cdao.deleteLikeList(weiboOrCommentId, userId);
				udao.deleteCommentLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				cdao.insertLikeList(weiboOrCommentId, userId);
				udao.insertCommentLikeMe(userId, Owner, weiboOrCommentId);
				service.likeMyCommentInform(userId, Owner, weiboOrCommentId);
			}
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean LikeWeiboOrCommentAction(String userId, String weiboOrCommentId) {
		// TODO Auto-generated method stub
		return false;
	}
}
