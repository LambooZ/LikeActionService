package cn.edu.bjtu.weibo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bjtu.weibo.dao.CommentDAO;
import cn.edu.bjtu.weibo.dao.UserDAO;
import cn.edu.bjtu.weibo.dao.WeiboDAO;
import cn.edu.bjtu.weibo.service.LikeActionService;
import cn.edu.bjtu.weibo.service.MessageToMeService;

@Service("likeActionService")
public class LikeActionServiceImpl implements LikeActionService{
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CommentDAO commentDAO;
	@Autowired
	private WeiboDAO weiboDAO;	
	@Autowired
	private MessageToMeService messageToMeService ;

	public boolean LikeWeiboOrCommentAction(String userId, String weiboOrCommentId,int pageIndex, int numberPerPage) {
		//判断是给微博还是给评论点赞
		//给微博
		if(weiboOrCommentId.startsWith("w")){
			
			List<String> WeiboLikeList = weiboDAO.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);//得到所有赞过这条微博的用户id的List
			String Owner = weiboDAO.getOwner(weiboOrCommentId);//微博的作者
			if(WeiboLikeList.contains(userId)) {
				//WeiboLikeList如果含有点赞的人的userId,说明他点过赞，应该删除
				weiboDAO.deleteLikeList(weiboOrCommentId, userId);
				userDAO.deleteLikeWeibo(userId, weiboOrCommentId);
				userDAO.deleteWeiboLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				//没点赞过，就添加
				weiboDAO.insertLikeList(weiboOrCommentId, userId);
				userDAO.insertLikeWeibo(userId, weiboOrCommentId);
				userDAO.insertWeiboLikeMe(userId, Owner, weiboOrCommentId);
				messageToMeService.likeMyWeiboInform(userId, Owner, weiboOrCommentId);//发消息
			}
			return true;
			//给评论
		}else if(weiboOrCommentId.startsWith("c")) {
			List<String> CommentLikeList = commentDAO.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);//得到所有赞过这条评论的用户id的List
			String Owner = commentDAO.getOwner(weiboOrCommentId); //评论的作者
			if(CommentLikeList.contains(userId)) {
				//CommentLikeList如果含有点赞的人的userId,说明他点过赞，应该删除
				commentDAO.deleteLikeList(weiboOrCommentId, userId);
				userDAO.deleteCommentLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				//没点赞过，就添加
				commentDAO.insertLikeList(weiboOrCommentId, userId);
				userDAO.insertCommentLikeMe(userId, Owner, weiboOrCommentId);
				messageToMeService.likeMyCommentInform(userId, Owner, weiboOrCommentId);//发消息
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
