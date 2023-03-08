package com.ecut.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecut.kafka.KafkaProducer;
import com.ecut.mapper.ArticleMapper;
import com.ecut.model.Article;
import com.ecut.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouwei
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    KafkaProducer kafkaProducer;


    @Override
    public int saveArticle(Article article) {
        article.setCreateTime(new Date());
        int res = articleMapper.saveArticle(article);
        if (res > 0) {
            kafkaProducer.sendMessage("article", JSON.toJSONString(article));
            log.info("article信息:{}", article);
        }
        return res;
    }

    @Override
    public List<Article> searchArticleByEs(String keyword) throws IOException {
        List<Article> list = new ArrayList<>();

        return list;
    }
}
