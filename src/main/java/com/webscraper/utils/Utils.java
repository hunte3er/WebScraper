/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webscraper.utils;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.webscraper.Exchange;
import com.webscraper.StockIdentifier;
import java.util.List;

/**
 *
 * @author eiker
 */
public class Utils {
    public static boolean siContainsSymbol(final List<StockIdentifier> list, final String symbol){
        return list.stream().anyMatch(o -> o.getSymbol().equals(symbol));
    }
    
    
    public static boolean exContainsSymbol(final List<Exchange> list, final String symbol){
        return list.stream().anyMatch(o -> o.getSymbol().equals(symbol));
    }
    
    public static boolean containsUrl (final List<StockIdentifier> list, final String url){
        return list.stream().anyMatch(o -> o.getUrl().equals(url));
    }
    
    public static String getStringFromXPath(HtmlPage page, String xPath) throws Exception{
        List<DomText> dom = page.getByXPath(xPath);
        if(!dom.isEmpty()){
//        System.out.println("t--------------------------------"+dom.toString().replace(" ,, ", "").replace("[", "").replace("]", ""));
            return dom.toString().replace(" ,, ", "").replace("[", "").replace("]", "");
        } else{
//            throw new Exception(((HtmlElement) dom).asXml());
//            System.out.println("f--------------------------------"+dom.toString());
            return null;
        }
    }
}
