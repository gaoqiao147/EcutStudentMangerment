package com.ecut.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhouwei
 */
@Data
@Document(indexName = "article",shards = 3,replicas = 1)
public class Article implements Serializable {
    @Id
    private Integer id;

    @NotNull(message = "学生学号不能为空")
    @Field(type = FieldType.Integer)
    private Integer usernumber;

    @NotBlank(message = "标题不能为空")
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String content;

    @Field(type = FieldType.Date)
    private Date createTime;
}
