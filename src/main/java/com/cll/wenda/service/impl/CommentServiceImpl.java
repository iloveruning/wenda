package com.cll.wenda.service.impl;

import com.cll.wenda.exception.TransactionalException;
import com.cll.wenda.mapper.AnswerMapper;
import com.cll.wenda.mapper.CommentMapper;
import com.cll.wenda.mapper.ReplyMapper;
import com.cll.wenda.mapper.UserMapping;
import com.cll.wenda.model.Answer;
import com.cll.wenda.model.Comment;
import com.cll.wenda.model.es.EsAnswer;
import com.cll.wenda.service.AnswerService;
import com.cll.wenda.service.CommentService;
import com.cll.wenda.service.EsAnswerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
@Service
public class CommentServiceImpl implements CommentService {


    private CommentMapper commentMapper;

    private AnswerMapper answerMapper;

    private ThreadPoolTaskExecutor executor;

    private EsAnswerService esAnswerService;

    private ReplyMapper replyMapper;

    private UserMapping userMapping;

    private AnswerService answerService;

    @Autowired
    protected CommentServiceImpl(CommentMapper commentMapper, AnswerMapper answerMapper,
                                 EsAnswerService esAnswerService,ThreadPoolTaskExecutor executor,
                                 ReplyMapper replyMapper,UserMapping userMapping,AnswerService answerService){
        this.commentMapper=commentMapper;
        this.answerMapper=answerMapper;
        this.esAnswerService=esAnswerService;
        this.executor=executor;
        this.replyMapper=replyMapper;
        this.userMapping=userMapping;
        this.answerService=answerService;
    }


    /**
     * 发表评论
     * @param comment
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pubComment(Comment comment,int uid) throws RuntimeException {

        String img=userMapping.findHeadImgByUid(uid);
        if (img==null){
            throw new RuntimeException("该用户不存在");
        }
        comment.setPic(img);
        int aid = comment.getAid();
        int res1 = commentMapper.save(comment);
        if (res1 != 1) {
            throw new RuntimeException("保存评论失败");
        }
        int res2 = answerMapper.updateComment(aid,1);
        if (res2 != 1) {
            throw new RuntimeException("跟新回答的评论数失败");
        }
        executor.execute(()->{
            Answer answer1 = answerMapper.findById(aid);
            EsAnswer esAnswer=new EsAnswer(answer1);
            esAnswerService.update(esAnswer);
        });
    }

    /**
     * 评论点赞
     * @param uid
     * @param cid
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    public void thumbsUp(int uid, int cid) throws RuntimeException {

        try {
            commentMapper.commentThumbsUp(uid, cid);
        }catch (Exception e){
            throw new TransactionalException("保存评论点赞失败");
        }
      //更新评论点赞数
        int res2=commentMapper.updateDz(cid,1);
        if (res2!=1){
            throw new TransactionalException("跟新评论点赞数失败");
        }

    }

    /**
     * 阅读评论
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public Comment readComment(int uid, int cid) {

        Comment comment=commentMapper.findById(cid);
        //我是否点过赞
        int zg=commentMapper.countThumbsUp(uid, cid);
        comment.setZg(zg);
        return comment;
    }

    /**
     * 删除评论
     * @param uid
     * @param cid
     * @throws RuntimeException
     */
    @Override
    public void deleteComment(int uid,int cid) throws RuntimeException {
        Comment comment=commentMapper.findById(cid);
        if (comment==null){
            throw new RuntimeException("该评论不存在");
        }
        if (comment.getPuber()!=uid){
            throw new RuntimeException("您无权限删除此评论");
        }
        executor.execute(()->{
            commentMapper.deleteById(cid);
            //跟新回答的评论数
            answerMapper.updateComment(comment.getAid(),-1);
            //删除该评论所有的回复
            replyMapper.deleteByCommentId(cid);
        });

    }

    /**
     * 我的评论
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Comment> getMyComment(int uid,int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Comment> comments=commentMapper.findByPuber(uid);
        return new PageInfo<>(comments);
    }

    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    public void cancelThumsbUp(int uid, int cid) throws RuntimeException {

        try {
            commentMapper.deleteThumbsUp(uid,cid);
        }catch (Exception e){
            throw new TransactionalException("用户没有点赞");
        }
        //更新评论点赞数
        int res=commentMapper.updateDz(cid,-1);
        if (res!=1){
            throw new TransactionalException("跟新评论点赞数失败");
        }

    }

    @Override
    public PageInfo<Comment> getCommentByAid(int uid,int aid,int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Comment> comments= commentMapper.findByAnswerId(aid);
        comments.forEach(it->{
            int zg=commentMapper.countThumbsUp(uid,it.getId());
            it.setZg(zg);
        });
        return new PageInfo<>(comments);
    }


}
