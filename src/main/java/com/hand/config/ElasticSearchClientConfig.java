package com.hand.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elasticSearch客户端配置
 *
 * @author hd
 * @date 2020-10-29 17:36
 */
@Configuration
public class ElasticSearchClientConfig {

    /**
     * 构建高级客户端对象
     * @return 返回客户端对象
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "HTTP")));
    }
}
