/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webscraper;

import org.json.simple.JSONObject;

/**
 *
 * @author eiker
 */
public class Exchange {
    private String symbol;
    private String desc;
    private String country;

    public Exchange() {};

    public Exchange(JSONObject o) {
        this.country = (String) o.get("country");
        this.desc = (String) o.get("desc");
        this.symbol = (String) o.get("symbol");
    };
    
    public Exchange(String symbol, String desc, String country) {
        this.symbol = symbol;
        this.desc = desc;
        this.country = country;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}