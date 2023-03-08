package com.ecut.controller;

import com.ecut.model.Article;
import com.ecut.service.ArticleService;
import com.ecut.util.CommonResult;
import com.ecut.vo.ResultVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author zhouwei
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @PostMapping
    public ResultVo writeArticle(@RequestBody Article article) {
        int res = articleService.saveArticle(article);
        if (res > 0) {
            return CommonResult.success(article);
        }
        return CommonResult.fail();
    }

    @GetMapping
    public ResultVo findArticle(@RequestParam("keyword") String keyword) throws IOException {
        List<Article> list = articleService.searchArticleByEs(keyword);
        return CommonResult.success(list);
    }
}
