package com.hand.service;

import com.alibaba.fastjson.JSON;
import com.hand.entity.Goods;
import com.hand.utils.ParseUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品业务类
 *
 * @author hd
 * @date 2020-10-29 17:39
 */
@Service
public class GoodsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 解析商品数据存入es中
     *
     * @param keywords 关键字
     * @return 返回结果
     * @throws Exception 异常
     */
    public Boolean parseGoods(String keywords) throws Exception {
        List<Goods> goods = new ParseUtils().parseJd(keywords);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(2));
        for (int i = 0; i < goods.size(); i++) {
            bulkRequest.add(new IndexRequest("jd_goods").source(JSON.toJSONString(goods.get(i)), XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }

    /**
     * 实现搜索功能，带分页处理
     *
     * @param keyword    关键字
     * @param pageNumber 页码
     * @param pageSize   每页个数
     * @return 集合
     * @throws IOException IO异常
     */
    public List<Map<String, Object>> searchContentPage(String keyword, Integer pageNumber, Integer pageSize) throws IOException {
        if (pageNumber <= 1) {
            pageNumber = 1;
        }
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 分页
        searchSourceBuilder.from(pageNumber);
        searchSourceBuilder.size(pageSize);
        // 根据关键字精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        // 设置最大构建时间60s
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(60));

        // 搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析结果
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            arrayList.add(documentFields.getSourceAsMap());
        }
        return arrayList;
    }

    /**
     * 搜索内容高亮显示
     *
     * @param keyword    关键字
     * @param pageNumber 页码
     * @param pageSize   每页个数
     * @return 集合
     * @throws IOException IO异常
     */
    public List<Map<String, Object>> searchContentHighlighter(String keyword, int pageNumber, int pageSize) throws IOException {
        if (pageNumber <= 1) {
            pageNumber = 1;
        }
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 分页
        searchSourceBuilder.from(pageNumber);
        searchSourceBuilder.size(pageSize);
        // 根据关键字精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);

        // 高亮构建！生成高亮查询器
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 高亮查询字段
        highlightBuilder.field("title");
        // 如果要多个字段高亮,这项要为false
        highlightBuilder.requireFieldMatch(false);
        // 高亮设置
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        // 设置最大构建时间60s
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(60));

        // 搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析结果
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            // 获取高亮字段
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField highlightField = highlightFields.get("title");
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            // 千万记得要记得判断是不是为空,不然你匹配的第一个结果没有高亮内容,那么就会报空指针异常, 这个错误一开始真的搞了很久
            if (highlightField != null) {
                Text[] fragments = highlightField.fragments();
                StringBuilder name = new StringBuilder();
                for (Text text : fragments) {
                    name.append(text);
                }
                // 高亮字段替换掉原本的内容
                sourceAsMap.put("title", name.toString());
            }
            arrayList.add(documentFields.getSourceAsMap());
        }
        return arrayList;
    }
}
