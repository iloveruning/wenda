package com.cll.wenda.model.es;

import com.cll.wenda.model.Answer;
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
@Document(indexName = "wenda",type = "answer")
@XmlRootElement // MediaType 转为 XML
public class EsAnswer implements Serializable {

    @Id
    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    private String aid;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer ansId;

    @Field(type = FieldType.String,index = FieldIndex.not_analyzed)
    private String ansSummary;

    @Field(type = FieldType.String,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String ansContent;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Date)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ansPubtime;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    private String ansAuthor;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer ansDz;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer ansCollect;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.Integer)
    private Integer ansCmt;

    @Field(index = FieldIndex.not_analyzed,type = FieldType.String)
    private String ansPic;

    public EsAnswer(Answer answer){
        this.ansId=answer.getId();
        this.ansCmt=answer.getCmt();
        this.ansContent=answer.getContent();
        this.ansDz=answer.getDz();
        this.ansPic=answer.getPic();
        this.ansPubtime=answer.getPubtime();
        this.ansSummary=answer.getSummary();
        this.ansAuthor=answer.getUsername();
        this.aid=answer.getId().toString();
        this.ansCollect=answer.getSc();
    }

}
