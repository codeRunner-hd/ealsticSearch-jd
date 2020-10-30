package com.hand.controller;

import com.hand.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 商品Controller
 *
 * @author hd
 * @date 2020-10-29 19:28
 */
@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception {
        return contentService.parseContent(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNumber}/{pageSize}")
    public List<Map<String, Object>> search(@PathVariable("keyword") String keyword, @PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) throws IOException {
        // 普通查询结果
        // return contentService.searchContentPage(keyword, pageNumber, pageSize);
        // 带有高亮显示的查询结果
        return contentService.searchContentHighlighter(keyword, pageNumber, pageSize);
    }
}
