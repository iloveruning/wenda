package com.cll.wenda.model.es;

import com.cll.wenda.model.Question;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author chenliangliang
 * @date: 2017/11/18
 */
@Data
@NoArgsConstructor
@Document(indexName = "wenda",type = "question")
@XmlRootElement // MediaType 转为 XML
public class EsQuestion implements Serializable {


    @Id
    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    private String qid;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer queId;

    @Field(type = FieldType.String,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String queTitle;


    @Field(type = FieldType.String,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String queContent;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Date)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quePubtime;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    private String queAuthor;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer queAns;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer queAtt;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer queReadSize;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    private String quePic;


    public EsQuestion(Question question){
        this.queAns=question.getAns();
        this.queAtt=question.getAtt();
        this.queContent=question.getContent();
        this.quePic=question.getPic();
        this.quePubtime=question.getPubtime();
        this.queId=question.getId();
        this.queTitle=question.getTitle();
        this.queReadSize=question.getReadSize();
        this.queAuthor=question.getUsername();
        this.qid=question.getId().toString();
    }

}
