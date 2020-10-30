package com.hand.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

/**
 * 爬虫工具类测试
 *
 * @author hd
 * @date 2020-10-29 20:15
 */
public class TestParseUtils {

    /**
     * 测试爬虫工具类
     * @throws Exception 异常
     */
    @Test
    void testParseContent() throws Exception {
        ParseUtils parseUtils = new ParseUtils();
        parseUtils.parseJd("vue").forEach(System.out::println);
    }

    @Test
    void testParseContentUtils() throws IOException {
        String url = "https://search.jd.com/Search?keyword=java";
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            System.out.println(img);
            System.out.println(price);
            System.out.println(title);
            System.out.println("================================");
        }
    }
}
