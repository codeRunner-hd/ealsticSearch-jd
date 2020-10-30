package com.hand.entity;

import org.springframework.stereotype.Component;

/**
 * 商品实体类
 *
 * @author hd
 * @date 2020-10-29 17:11
 */
@Component
public class Content {
    private String title;
    private String price;
    private String img;

    public Content() {
    }

    public Content(String title, String price, String img) {
        this.title = title;
        this.price = price;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Content{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
