/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webscraper;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author eiker
 */
public class HttpArgsGET {

    private int feature;
    private String exchange;
    private String category;
    private String p;
    private String cc;
    private int limit;
    private int page;

    public int getFeature() {
        return feature;
    }

    public void setFeature(int feature) {
        this.feature = feature;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public HttpArgsGET() {
        this.feature = 2222;
        this.exchange = "ibis";
        this.category = "";
        this.p = "";
        this.cc = "";
        this.limit = 100;
        this.page = 2;
    }

    public List<NameValuePair> getArgs() {

        List<NameValuePair> webArgs = new ArrayList<>();

        webArgs.add(new BasicNameValuePair("f", String.valueOf(this.feature)));
        webArgs.add(new BasicNameValuePair("exch", this.exchange));
        webArgs.add(new BasicNameValuePair("showcategories", this.category));
        webArgs.add(new BasicNameValuePair("p", this.p));
        webArgs.add(new BasicNameValuePair("cc", this.cc));
        webArgs.add(new BasicNameValuePair("limit", String.valueOf(this.limit)));
        webArgs.add(new BasicNameValuePair("page", String.valueOf(this.page)));

        return webArgs;
    }

}