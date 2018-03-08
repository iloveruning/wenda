package com.cll.wenda.service.impl;

import com.cll.wenda.exception.AppException;
import com.cll.wenda.exception.TransactionalException;
import com.cll.wenda.mapper.AnswerMapper;
import com.cll.wenda.mapper.KeywordMapper;
import com.cll.wenda.mapper.QuestionMapper;
import com.cll.wenda.mapper.UserMapping;
import com.cll.wenda.model.Answer;
import com.cll.wenda.model.Question;
import com.cll.wenda.model.es.EsQuestion;
import com.cll.wenda.service.EsQuestionService;
import com.cll.wenda.service.QuestionService;
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
 * @date: 2017/10/26
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    private QuestionMapper questionMapper;

    private ThreadPoolTaskExecutor executor;

    private EsQuestionService esQuestionService;

    private AnswerMapper answerMapper;

    private KeywordMapper keywordMapper;

    private UserMapping userMapping;

    @Autowired
    protected QuestionServiceImpl(QuestionMapper questionMapper, EsQuestionService esQuestionService,
                                  AnswerMapper answerMapper, ThreadPoolTaskExecutor executor,
                                  KeywordMapper keywordMapper,UserMapping userMapping) {
        this.questionMapper = questionMapper;
        this.esQuestionService = esQuestionService;
        this.answerMapper = answerMapper;
        this.executor = executor;
        this.keywordMapper = keywordMapper;
        this.userMapping=userMapping;
    }

    /**
     * 用户uid关注问题qid
     *
     * @param uid
     * @param qid
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = TransactionalException.class)
    @CacheEvict(value = "read", key = "'readQuestion_uid_'+#uid+'_qid_'+#qid")
    public void attention(int uid, int qid) throws RuntimeException {
        Question question = questionMapper.findById(qid);
        if (question == null) {
            throw new AppException("您关注的问题不存在");
        }
        try {
           questionMapper.attention(uid, qid);
        }catch (Exception e){
            throw new TransactionalException("关注表跟新失败！");
        }

        int res2 = questionMapper.updateAtt(qid, 1);
        if (res2 != 1) {
            throw new TransactionalException("问题表跟新失败！");
        }

        executor.execute(() -> {
                    EsQuestion esQuestion = new EsQuestion(question);
                    esQuestionService.update(esQuestion);
                }
        );


    }

    /**
     * 获取用户uid关注的问题
     *
     * @param uid
     * @return
     */
    @Override
    public PageInfo<Question> getAttQuestion(int uid,int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Question> questions = questionMapper.findAttQuestionByUserId(uid);

        return new PageInfo<>(questions);
    }

    /**
     * 获取我的问题
     *
     * @param uid
     * @return
     */
    @Override
    public PageInfo<Question> getMyQuestion(int uid,int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Question> questions = questionMapper.findMyQuestions(uid);

        return new PageInfo<>(questions);
    }

    /**
     * @param uid
     * @param qid
     * @return
     */
    @Override
    @Cacheable(value = "read", key = "'readQuestion_uid_'+#uid+'_qid_'+#qid")
    public Question readQuestion(int uid, int qid) {

        Question question = questionMapper.findById(qid);

        if (question != null) {
            List<Answer> answers = answerMapper.findAnswersByQuestionId(qid);
            question.setAnswers(answers);
            questionMapper.increaseReadSize(qid);
            int isMyAtt = questionMapper.isAttentted(uid, qid);
            System.out.println("isMyAtt: " + isMyAtt);
            question.setIsMyAtt(isMyAtt);
        }
        executor.execute(() -> {
                    Question question1 = questionMapper.findById(qid);
                    EsQuestion esQuestion = new EsQuestion(question1);
                    esQuestionService.update(esQuestion);
                }
        );
        return question;
    }

    /**
     * 发表问题
     * @param question
     * @throws RuntimeException
     */
    @Override
    public void saveQuestion(Question question,int uid) throws RuntimeException {

        if (question.getIsanony()==0){
            String img=userMapping.findHeadImgByUid(uid);
            if (img!=null){
                question.setPic(img);
            }else {
                throw new RuntimeException("该用户不存在");
            }
        }else {
            question.setPic("https://t1.picb.cc/uploads/2017/11/28/bFEIc.png");
        }


        int res = questionMapper.save(question);
        if (res != 1) {
            throw new AppException("保存问题失败");
        }
        executor.execute(() -> {
                    Question question1 = questionMapper.findById(question.getId());
                    EsQuestion esQuestion = new EsQuestion(question1);
                    esQuestionService.update(esQuestion);
                    HanLP.extractPhrase(question.getContent(), 3)
                            .forEach(it -> keywordMapper.save(it));
                    keywordMapper.save(question.getTitle());
                }
        );
    }

    /**
     * 获取热门问题
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Question> getHotQuestion(int uid,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.findHotestQuestion();
        /*questions.forEach(it -> {
            Answer answer = answerMapper.findQuestionHotAnswer(it.getId());
            it.setHotAnswer(answer);
        });*/

        //个性化
        for (Question question : questions) {
            int isMyAtt = questionMapper.isAttentted(uid, question.getId());
            question.setIsMyAtt(isMyAtt);
        }
        return new PageInfo<>(questions);
    }

    @Override
    @CacheEvict(value = "read", key = "'readQuestion_uid_'+#uid+'_qid_'+#qid")
    public void cancelAttention(int uid, int qid) throws RuntimeException {
        try {
            questionMapper.deleteAtt(uid, qid);
            questionMapper.updateAtt(qid, -1);
        } catch (Exception e) {
            throw new AppException("您还没有关注这个问题");
        }
    }

    /**
     * 获取热门话题
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Question> getHotTopic(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> topics = questionMapper.findHotTopic();
        return new PageInfo<>(topics);
    }

    @Override
    public PageInfo<Question> getTopicQuestion(String topic, int uid,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionMapper.findByTopic(topic);
        //个性化
        for (Question question : questions) {
            int isMyAtt = questionMapper.isAttentted(uid, question.getId());
            question.setIsMyAtt(isMyAtt);
        }
        return new PageInfo<>(questions);
    }


    @Override
    public void removeMyQuestion(int uid, int qid) throws RuntimeException {

    }


}
