package com.cll.wenda.service.impl;

import com.cll.wenda.exception.AppException;
import com.cll.wenda.exception.TransactionalException;
import com.cll.wenda.mapper.*;
import com.cll.wenda.model.Answer;
import com.cll.wenda.model.Comment;
import com.cll.wenda.model.es.EsAnswer;
import com.cll.wenda.service.AnswerService;
import com.cll.wenda.service.EsAnswerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hankcs.hanlp.HanLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
@Service
public class AnswerServiceImpl implements AnswerService {

    private AnswerMapper answerMapper;

    private QuestionMapper questionMapper;

    private KeywordMapper keywordMapper;

    private ThreadPoolTaskExecutor executor;

    private EsAnswerService esAnswerService;

    private CommentMapper commentMapper;

    private UserMapping userMapping;

    @Autowired
    protected AnswerServiceImpl(AnswerMapper answerMapper, QuestionMapper questionMapper,
                                KeywordMapper keywordMapper, ThreadPoolTaskExecutor executor,
                                EsAnswerService esAnswerService, CommentMapper commentMapper,UserMapping userMapping) {
        this.answerMapper = answerMapper;
        this.questionMapper = questionMapper;
        this.keywordMapper = keywordMapper;
        this.executor = executor;
        this.esAnswerService = esAnswerService;
        this.commentMapper = commentMapper;
        this.userMapping=userMapping;
    }

    /**
     * 发表回答
     *
     * @param answer
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    @CacheEvict(value = "read", key = "'readQuestion_uid_'+#answer.puber+'_qid_'+#answer.qid")
    public void pubAnswer(Answer answer,int uid) throws RuntimeException {

        if(answer.getIsanony()==0){
            String img=userMapping.findHeadImgByUid(uid);
            if (img==null){
                throw new RuntimeException("该用户不存在");
            }
            answer.setPic(img);
        }else {
            answer.setPic("https://t1.picb.cc/uploads/2017/11/28/bFEIc.png");
        }

        int qid = answer.getQid();
        List<String> sentenceList = HanLP.extractSummary(answer.getContent(), 4);
        answer.setSummary(sentenceList.toString());

        System.out.println("qid---"+qid);
        int res1 = questionMapper.updateAns(qid, 1);
        if (res1 != 1) {
            throw new TransactionalException("跟新问题回答数失败");
        }
        try {
            answerMapper.save(answer);
        } catch (Exception e) {
            throw new TransactionalException("保存回答失败");
        }

        executor.execute(() -> {
            Answer answer1 = answerMapper.findById(answer.getId());
            EsAnswer esAnswer = new EsAnswer(answer1);
            esAnswerService.update(esAnswer);
            HanLP.extractPhrase(answer.getContent(), 4)
                    .forEach(it -> keywordMapper.save(it));

        });
    }

    /**
     * 回答点赞
     *
     * @param uid
     * @param aid
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    @CacheEvict(value = "read", key = "'readAnswer__aid_'+#aid")
    public void thumbsUp(int uid, int aid) throws RuntimeException {

        try {
            answerMapper.saveThumbsUp(uid, aid);
        } catch (Exception e) {
            throw new TransactionalException("保存用户对回答的点赞失败");
        }

        int res2 = answerMapper.updateDz(aid, 1);
        if (res2 != 1) {
            throw new RuntimeException("跟新回答的点赞数失败");
        }

        executor.execute(() -> {
            Answer answer1 = answerMapper.findById(aid);
            EsAnswer esAnswer = new EsAnswer(answer1);
            esAnswerService.update(esAnswer);
        });
    }

    /**
     * 获取问题对应的回答
     *
     * @param qid
     * @param pageNum
     * @return
     * @throws RuntimeException
     */
    @Cacheable(value = "test", key = "'getQuestionAnswers_'+#qid+'pageNum_'+#pageNum")
    @Override
    public PageInfo<Answer> getQuestionAnswers(int qid, int pageNum) throws RuntimeException {
        PageHelper.startPage(pageNum, 4);
        try {
            List<Answer> answers = answerMapper.findAnswersByQuestionId(qid);
            answers.forEach(it -> {
                Comment comment = commentMapper.findHotComment(it.getId());
                it.setHotComment(comment);
            });
            return new PageInfo<>(answers);
        } catch (Exception e) {
            throw new AppException("获取问题对应的回答失败");
        }


    }


    /**
     * 获取用户uid收藏的回答
     *
     * @param uid
     * @param pageNum
     * @return
     * @throws RuntimeException
     */
    @Cacheable(value = "test", key = "'getCollectAnswers_'+#uid+'pageNum_'+#pageNum")
    @Override
    public PageInfo<Answer> getCollectAnswers(int uid, int pageNum) throws RuntimeException {
        PageHelper.startPage(pageNum, 12);
        List<Answer> answers = answerMapper.findCollectAnswersByUserId(uid);
        return new PageInfo<>(answers);
    }

    /**
     * 收藏回答
     *
     * @param uid
     * @param aid
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    public void collectAnswer(int uid, int aid) throws RuntimeException {

        try {
            answerMapper.saveCollection(uid, aid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("您已经收藏过了！");
        }
        //更新收藏数
        int res = answerMapper.updateCollection(aid, 1);
        if (res != 1) {
            throw new TransactionalException("该id对应的回答不存在");
        }
    }

    /**
     * 获取我的回答
     *
     * @param uid
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<Answer> getMyAnswers(int uid, int pageNum) {
        PageHelper.startPage(pageNum, 12);
        List<Answer> answers = answerMapper.findMyAnswers(uid);
        return  new PageInfo<>(answers);
    }

    @Override
    @CacheEvict(value = "read", key = "'readAnswer__aid_'+#aid")
    public void deleteAnswer(int aid) throws RuntimeException {

        int res = answerMapper.deleteById(aid);
        if (res != 1) {
            throw new AppException("删除回答失败");
        }

    }

    /**
     * 取消点赞
     *
     * @param uid
     * @param aid
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    @CacheEvict(value = "read", key = "'readAnswer__aid_'+#aid")
    public void deleteThumbsUp(int uid, int aid) throws RuntimeException {

        try {
            answerMapper.removeThumbsUp(uid, aid);
        } catch (Exception e) {
            throw new TransactionalException("您还没有给这个回答点赞");
        }

        int res = answerMapper.updateDz(aid, -1);
        if (res != 1) {
            throw new TransactionalException("跟新点赞数失败");
        }

        executor.execute(() -> {
            Answer answer1 = answerMapper.findById(aid);
            EsAnswer esAnswer = new EsAnswer(answer1);
            esAnswerService.update(esAnswer);
        });
    }

    /**
     * 获取回答
     *
     * @param aid
     * @return
     */
    @Override
    @Cacheable(value = "read", key = "'readAnswer__aid_'+#aid")
    public Answer getAnswerById(int aid) {

        return answerMapper.findById(aid);
    }

    /**
     * 阅读回答（个性化）
     *
     * @param uid
     * @param aid
     * @return
     */
    @Override
    public Answer readAnswer(int uid, int aid) {
        Answer answer = answerMapper.findById(aid);
        //我是否收藏了这个回答
        int collect = answerMapper.countCollection(uid, aid);
        answer.setCollect(collect);
        //我是否给这个回答点赞了
        int zg = answerMapper.countThumbsUp(uid, aid);
        answer.setZg(zg);
        return answer;
    }

    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    public void cancelCollection(int uid, int aid) throws RuntimeException {
        try {
            answerMapper.removeCollection(uid, aid);
        }catch (Exception e){
            throw new TransactionalException("您还没有收藏这个回答");
        }
        //更新收藏数
        int res=answerMapper.updateCollection(aid, -1);
        if (res!=1){
            throw new TransactionalException("更新回答的收藏数失败");
        }
    }

    @Override
    @CacheEvict(value = "read", key = "'readAnswer__aid_'+#aid")
    @Transactional(rollbackFor = TransactionalException.class)
    public void removeMyAnswer(int uid, int aid) throws RuntimeException {

        Answer answer=answerMapper.findById(aid);
        System.out.println("removeMyAnswer---"+answer);
        if (answer==null){
            throw new AppException("要删除的这个问题不存在");
        }

        int res1=answerMapper.deleteMyAnswer(uid,aid);
        if (res1!=1){
            throw new TransactionalException("删除我的回答失败");
        }
        //跟新问题的回答数
        int qid=answer.getQid();
        int res2=questionMapper.updateAns(qid,-1);
        if(res2!=1){
            throw new TransactionalException("跟新问题的回答数失败");
        }

        executor.execute(()->{
            //删除ESAnswer
            EsAnswer esAnswer=new EsAnswer(answer);
            esAnswerService.remove(esAnswer);
            //更新收藏表
            answerMapper.deleteCollectionByAid(aid);
            //更新点赞表
            answerMapper.deleteThumbsUpByAid(aid);
        });

    }
}
