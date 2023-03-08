package com.ecut.mapper;

import com.ecut.model.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouwei
 */
public interface ArticleMapper {
    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    int saveArticle(@Param("article") Article article);

    /**
     * 查询文章
     * @param usernumber
     * @return
     */
    List<Article> searchArticle(@Param("usernumber") Integer usernumber);
}
