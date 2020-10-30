package com.hand.utils;

import com.hand.entity.Goods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * JD爬虫工具类
 *
 * @author hd
 * @date 2020-10-29 17:25
 */
public class ParseUtils {

    /**
     * 京东商城的关键字内容爬虫
     *
     * @param keywords 关键字
     * @return 结果集合
     * @throws Exception 异常
     */
    public List<Goods> parseJd(String keywords) throws Exception {
        // 爬取地址
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        // 解析内容
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        ArrayList<Goods> goodsList = new ArrayList<>();
        // 遍历获取京东的商品信息
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            Goods goods = new Goods();
            goods.setImg(img);
            goods.setPrice(price);
            goods.setTitle(title);
            goodsList.add(goods);
        }
        return goodsList;
    }

}
