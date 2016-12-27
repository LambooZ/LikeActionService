package cn.edu.bjtu.weibo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bjtu.weibo.dao.CommentDAO;
import cn.edu.bjtu.weibo.dao.UserDAO;
import cn.edu.bjtu.weibo.dao.WeiboDAO;
import cn.edu.bjtu.weibo.dao.Impl.UserDAOImpl;
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
		//判断是给微博还是给评论点赞
		//给微博
		if(weiboOrCommentId.startsWith("w")){
			
			List<String> WeiboLikeList = wdao.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);//得到所有赞过这条微博的用户id的List
			String Owner = wdao.getOwner(weiboOrCommentId);//微博的作者
			if(WeiboLikeList.contains(userId)) {
				//WeiboLikeList如果含有点赞的人的userId,说明他点过赞，应该删除
				wdao.deleteLikeList(weiboOrCommentId, userId);
				udao.deleteLikeWeibo(userId, weiboOrCommentId);
				udao.deleteWeiboLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				//没点赞过，就添加
				wdao.insertLikeList(weiboOrCommentId, userId);
				udao.insertLikeWeibo(userId, weiboOrCommentId);
				udao.insertWeiboLikeMe(userId, Owner, weiboOrCommentId);
				service.likeMyWeiboInform(userId, Owner, weiboOrCommentId);//发消息
			}
			return true;
			//给评论
		}else if(weiboOrCommentId.startsWith("c")) {
			List<String> CommentLikeList = cdao.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);//得到所有赞过这条评论的用户id的List
			String Owner = cdao.getOwner(weiboOrCommentId); //评论的作者
			if(CommentLikeList.contains(userId)) {
				//CommentLikeList如果含有点赞的人的userId,说明他点过赞，应该删除
				cdao.deleteLikeList(weiboOrCommentId, userId);
				udao.deleteCommentLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				//没点赞过，就添加
				cdao.insertLikeList(weiboOrCommentId, userId);
				udao.insertCommentLikeMe(userId, Owner, weiboOrCommentId);
				service.likeMyCommentInform(userId, Owner, weiboOrCommentId);//发消息
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
