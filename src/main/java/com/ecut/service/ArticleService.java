package com.ecut.service;

import com.ecut.model.Article;

import java.io.IOException;
import java.util.List;

/**
 * @author zhouwei
 */
public interface ArticleService {
    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    int saveArticle(Article article);

    /**
     * 根据Es查询文章
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    List<Article> searchArticleByEs(String keyword) throws IOException;

}
