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
		//�ж��Ǹ�΢�����Ǹ����۵���
		//��΢��
		if(weiboOrCommentId.startsWith("w")){
			
			List<String> WeiboLikeList = weiboDAO.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);//�õ������޹�����΢�����û�id��List
			String Owner = weiboDAO.getOwner(weiboOrCommentId);//΢��������
			if(WeiboLikeList.contains(userId)) {
				//WeiboLikeList������е��޵��˵�userId,˵��������ޣ�Ӧ��ɾ��
				weiboDAO.deleteLikeList(weiboOrCommentId, userId);
				userDAO.deleteLikeWeibo(userId, weiboOrCommentId);
				userDAO.deleteWeiboLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				//û���޹��������
				weiboDAO.insertLikeList(weiboOrCommentId, userId);
				userDAO.insertLikeWeibo(userId, weiboOrCommentId);
				userDAO.insertWeiboLikeMe(userId, Owner, weiboOrCommentId);
				messageToMeService.likeMyWeiboInform(userId, Owner, weiboOrCommentId);//����Ϣ
			}
			return true;
			//������
		}else if(weiboOrCommentId.startsWith("c")) {
			List<String> CommentLikeList = commentDAO.getLikeList(weiboOrCommentId, pageIndex, numberPerPage);//�õ������޹��������۵��û�id��List
			String Owner = commentDAO.getOwner(weiboOrCommentId); //���۵�����
			if(CommentLikeList.contains(userId)) {
				//CommentLikeList������е��޵��˵�userId,˵��������ޣ�Ӧ��ɾ��
				commentDAO.deleteLikeList(weiboOrCommentId, userId);
				userDAO.deleteCommentLikeMe(userId, Owner, weiboOrCommentId);
			}else {
				//û���޹��������
				commentDAO.insertLikeList(weiboOrCommentId, userId);
				userDAO.insertCommentLikeMe(userId, Owner, weiboOrCommentId);
				messageToMeService.likeMyCommentInform(userId, Owner, weiboOrCommentId);//����Ϣ
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
