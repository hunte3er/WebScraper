package com.webscraper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

public class StockIdentifier {

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getCins() {
        return cins;
    }

    public void setCins(String cins) {
        this.cins = cins;
    }

    public String getWarscode() {
        return warscode;
    }

    public void setWarscode(String warscode) {
        this.warscode = warscode;
    }

    public String getYhSymbol() {
        return YhSymbol;
    }

    public void setYhSymbol(String YhSymbol) {
        this.YhSymbol = YhSymbol;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public String getConid() {
        return conid;
    }

    public void setConid(String conid) {
        this.conid = conid;
    }

    public List<String> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<String> exchanges) {
        this.exchanges = exchanges;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String symbol;
    private String description;
    private String isin;
    private String cins;
    private String warscode;
    private String YhSymbol;
//    private String ibSymbol;
    private String assetID;
    private String conid;
    private List<String> exchanges;
    private String currency;
    private String contractType;
    private String country;
    private String url;


    public StockIdentifier() {};

//    public StockIdentifier(Map<String, String> args) {
//    }
    
    public StockIdentifier(JSONObject o){
        this.symbol = (String) o.get("symbol");
        this.description = (String) o.get("description");
        this.isin = (String) o.get("isin");
        this.cins = (String) o.get("cins");
        this.warscode = (String) o.get("warscode");
        this.YhSymbol = (String) o.get("YhSymbol");
//        this.ibSymbol = (String) o.get("ibSymbol");
        this.assetID = (String) o.get("assetID");
        this.conid = (String) o.get("conid");
            String temp = ((String)o.get("exchanges"));
            String str[] = temp.split(",");
        this.exchanges = Arrays.asList(str);
        this.currency = (String) o.get("currency");
        this.contractType = (String) o.get("contractType");
        this.country = (String) o.get("country");
        this.url = (String) o.get("url");
    }
    
//    public StockIdentifier(Map<String, String> o){
//        this.symbol = o.get("symbol");
//        this.description = o.get("description");
//        this.isin = o.get("isin");
//        this.cins = o.get("cins");
//        this.warscode = o.get("warscode");
//        this.YhSymbol = o.get("YhSymbol");
////        this.ibSymbol = o.get("ibSymbol");
//        this.assetID = o.get("assetID");
//        this.conid = o.get("conid");
//        this.exchanges = Arrays.asList(o.get("exchanges").replace("[", "").replace("]", "").split(","));
//        this.currency = (String) o.get("currency");
//        this.contractType = (String) o.get("contractType");
//        this.country = (String) o.get("country");
//        this.url = (String) o.get("url");
//    }

    public StockIdentifier(String ibSymbol, String link, String currency, String symbol, String error, String...args) {
//        this.ibSymbol = ibSymbol;
        this.url = link;
        this.currency = currency;
        this.symbol = symbol;
        this.isin = args[0];
    }
}