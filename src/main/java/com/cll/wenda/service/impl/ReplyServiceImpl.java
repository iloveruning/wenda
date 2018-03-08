package com.cll.wenda.service.impl;

import com.cll.wenda.exception.TransactionalException;
import com.cll.wenda.mapper.CommentMapper;
import com.cll.wenda.mapper.ReplyMapper;
import com.cll.wenda.mapper.UserMapping;
import com.cll.wenda.model.Reply;
import com.cll.wenda.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/29
 */
@Service
public class ReplyServiceImpl implements ReplyService {


    private ReplyMapper replyMapper;

    private CommentMapper commentMapper;

    private UserMapping userMapping;

    @Autowired
    protected ReplyServiceImpl(ReplyMapper replyMapper, CommentMapper commentMapper,
                               UserMapping userMapping) {
        this.replyMapper = replyMapper;
        this.commentMapper = commentMapper;
        this.userMapping = userMapping;
    }


    /**
     * 发表回复
     * @param reply
     * @param uid
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    public void pubReply(Reply reply, int uid) throws RuntimeException {

        String img=userMapping.findHeadImgByUid(uid);
        if (img==null){
            throw new RuntimeException("该用户不存在");
        }
        reply.setPic(img);
        int res1 = replyMapper.save(reply);

        if (res1 != 1) {
            throw new TransactionalException("发表回复失败！");
        }
        int res2 = commentMapper.updateReply(reply.getCid(), 1);

        if (res2 != 1) {
            throw new TransactionalException("跟新评论回复数失败");
        }
    }

    @Override
    public List<Reply> getCommentReply(int cid) {
        List<Reply> replies = replyMapper.findReplyByCommentId(cid);
        if (replies == null) {
            return new ArrayList<>(0);
        }
        return replies;
    }
}
