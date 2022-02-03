/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webscraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import static com.webscraper.utils.Utils.exContainsSymbol;
import static com.webscraper.utils.Utils.getStringFromXPath;
import static com.webscraper.utils.Utils.siContainsSymbol;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author eiker
 */
public class InteractiveBrokers {

    private final String baseUrl;
    private final WebClient client = new WebClient();
    private List<Exchange> exchanges;
    private List<String> categories;
    private List<StockIdentifier> StockIdentifiers;
    private List<String> countries;
    private List<String> urls;
    private Map<String, List<String>> exCatMap;

    private ObjectMapper mapper;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(InteractiveBrokers.class);

    public InteractiveBrokers() {
        this.baseUrl = "https://www1.interactivebrokers.com/en/index.php?";
        this.client.getOptions().setCssEnabled(false);
        this.client.getOptions().setJavaScriptEnabled(false);
        this.client.getOptions().setThrowExceptionOnScriptError(false);
        this.client.getOptions().setPrintContentOnFailingStatusCode(false);
        this.client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.mapper = new ObjectMapper();
        this.exchanges = new ArrayList();
        this.StockIdentifiers = new ArrayList();
        this.countries = new ArrayList();
        this.urls = new ArrayList();
        this.exCatMap = new HashMap();
    }

    public void get1() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray as = (JSONArray) parser.parse(new FileReader("c:\\sids.json"));
            as.forEach(o -> {
                StockIdentifiers.add(new StockIdentifier((JSONObject) o));
            });
            
            JSONArray a = (JSONArray) parser.parse(new FileReader("c:\\exids.json"));
            a.forEach(o -> {
                exchanges.add(new Exchange((JSONObject) o));
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InteractiveBrokers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(InteractiveBrokers.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        fetchCountries();

        countries.forEach((String country) -> {
            this.exchanges.addAll(fetchExchanges(country));
        });

        this.exchanges.forEach((Exchange exchange) -> {
            try {
                this.exCatMap.put(exchange.getSymbol(), fetchCategories(exchange.getSymbol()));
            } catch (IOException ex) {
                logger.fatal(ex);
            }
        });

    }
    
        public void get2(){
//        try {
//            JSONParser parser = new JSONParser();
//            JSONArray as = (JSONArray) parser.parse(new FileReader("c:\\sids.json"));
//            as.forEach(o -> {
//                items.add(new StockIdentifier((JSONObject) o));
//            });
//            
////            JSONArray a = (JSONArray) parser.parse(new FileReader("c:\\exids.json"));
////            a.forEach(o -> {
////                exchanges.add(new Exchange((JSONObject) o));
////            });
//        } catch (FileNotFoundException e) {
//            logger.fatal(e.getMessage());
//        } catch (IOException | ParseException e) {
//            logger.fatal(e.getMessage());
//        }
            
        fetchCountries();
        this.countries.forEach((String country) -> {
            List<Exchange> ex = fetchExchanges(country);
            List<String> exSymList = ex.stream().map(Exchange::getSymbol).collect(Collectors.toList());
            this.exchanges.addAll(ex);
            logger.info("Country/Region: " + country + " - Exchanges: " + exSymList.toString());
        });
        
        this.exchanges.forEach((Exchange exchange) -> {
            try {
                this.exCatMap.put(exchange.getSymbol(), fetchCategories(exchange.getSymbol()));
            } catch (IOException ex) {
                logger.fatal(ex);
            }
        });
        
        logger.info(new JSONObject(this.exCatMap));

        for (Map.Entry<String, List<String>> entry : this.exCatMap.entrySet()) {
            String exchange = entry.getKey();
            for(String category : entry.getValue()){
                if(!"IND".equals(category) && !"BOND".equals(category) && !"BILL".equals(category)){
                    try {
                        int maxPages = fetchNumberOfPages(exchange, category);
                        
                        for(int pageNr = 1; pageNr <= maxPages; pageNr++){
                            collectUrls(exchange, category, pageNr);
                            logger.info(pageNr + "/" + maxPages + " - " + exchange + "@" + category);
                        }
                    } catch (IOException ex) {
                        logger.fatal(ex);
                    }
                }
            }
        }         
    }

        public void get3(){
            try {
               JSONParser parser = new JSONParser();
               JSONArray as = (JSONArray) parser.parse(new FileReader("c:\\conids.json"));
               as.forEach(o -> {
                   try {
                        fetchStockData("https://contract.ibkr.info/index.php?action=Details&site=GEN&conid=" + o.toString());
                   } catch (Exception ex) {
                        logger.fatal(ex.getMessage());
                   }
               });
               logger.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.StockIdentifiers));

   ////            JSONArray a = (JSONArray) parser.parse(new FileReader("c:\\exids.json"));
   ////            a.forEach(o -> {
   ////                exchanges.add(new Exchange((JSONObject) o));
   ////            });
           } catch (FileNotFoundException e) {
               logger.fatal(e.getMessage());
           } catch (IOException | ParseException e) {
               logger.fatal(e.getMessage());
           }
        }
        
    public List<String> fetchCategories(String exchangeSymbol) throws IOException {

        HttpArgsGET args = new HttpArgsGET();
        args.setExchange(exchangeSymbol);

        String queryUrl = this.baseUrl + URLEncodedUtils.format(args.getArgs(), Charset.forName("utf-8"));
        HtmlPage page = this.client.getPage(queryUrl);

        List<String> categoriesL;
        categoriesL = new ArrayList();

        List<HtmlElement> btnSelectors = page.getByXPath(".//div[@class='btn-selectors']/p");

        btnSelectors.forEach(p -> {
            String id = ((HtmlElement) p.getFirstChild()).getAttribute("id");
            if(id == null)
                id = "";
            categoriesL.add(id);
        });

        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(categoriesL);
            logger.info(exchangeSymbol + " " +jsonString);

        } catch (JsonProcessingException e) {
            logger.fatal(e.getMessage());
        }

        return categoriesL;
    }

    public Integer fetchNumberOfPages(String exchangeSymbol, String category) throws IOException {
        
        HttpArgsGET args = new HttpArgsGET();
        args.setExchange(exchangeSymbol);
        args.setCategory(category);

        String queryUrl = this.baseUrl + URLEncodedUtils.format(args.getArgs(), Charset.forName("utf-8"));
        HtmlPage page = this.client.getPage(queryUrl);

        int numberOfPages = 1;

        List<HtmlElement> pagination = page.getByXPath(".//ul[@class='pagination']/li[not(@class='disabled')]");

        if (!pagination.isEmpty()) {
            List<Integer> pageNumbers = new ArrayList<>();

            pagination.stream().filter(li -> (li.getTextContent().matches("[0-9]*"))).forEachOrdered(li -> {
                pageNumbers.add(Integer.parseInt(li.getTextContent()));
            });

            if(!pageNumbers.isEmpty())
                numberOfPages = Collections.max(pageNumbers);
            logger.info(numberOfPages + " pages.");
        }

        return numberOfPages;
    }

    public void collectUrls(String exchangeSymbol, String category, int pageNr) {

        HttpArgsGET args = new HttpArgsGET();
        args.setExchange(exchangeSymbol);
        args.setCategory(category);
        args.setPage(pageNr);

        String queryUrl = this.baseUrl + URLEncodedUtils.format(args.getArgs(), Charset.forName("utf-8"));
        try{
            HtmlPage page = this.client.getPage(queryUrl);

            List<HtmlElement> tableRows = page.getByXPath(".//*[@id='exchange-products']//table/tbody/tr");
            if (tableRows.isEmpty()) {
                logger.trace("No scrapable items.");
            } else {
                            //                        tableRows.stream().map(tableRow -> tableRow.getByXPath(".//td")).map(tableCell -> {
                for (HtmlElement tableRow: tableRows) {

                    List<HtmlElement> tableCell = tableRow.getByXPath(".//td");

                    String symbol = ((HtmlElement) tableCell.get(2)).getFirstChild().toString();

                    if(!siContainsSymbol(this.StockIdentifiers, symbol)){
                        String itemUrl = ((HtmlAnchor)((HtmlElement) tableCell.get(1)).getFirstChild()).getHrefAttribute().replaceAll(".*\\(\\'", "").replaceAll("\\s\\',\\'.*", "");
                        this.urls.add(itemUrl);
//                        logger.info(itemUrl);
System.out.println(itemUrl);
                    }
                }
            }
        } catch (IOException | FailingHttpStatusCodeException e) {
            logger.fatal(e.getMessage());
        }
    }
    
    public void fetchStockData(String url) throws Exception{
        StockIdentifier sid = new StockIdentifier(getData(url));
        logger.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sid));
        this.StockIdentifiers.add(sid);
    }

    public JSONObject getData(String url) throws IOException, Exception {            
            Map<String, String > data = new HashMap();
            
            HtmlPage page = solveCaptcha(this.client.getPage(url));
            
            data.put("symbol", getStringFromXPath(page, ".//th[text() = 'Symbol']/../td/text()[1]"));
            data.put("isin", getStringFromXPath(page, ".//th[text() = 'ISIN']/../td/text()[1]"));
            data.put("currency", getStringFromXPath(page, ".//th[text() = 'Currency']/../td/text()[1]").replaceFirst(".*\\(", "").replaceFirst("\\)", ""));
            data.put("cins", getStringFromXPath(page, ".//th[text() = 'CINS']/../td/text()[1]"));                    
            data.put("warscode", getStringFromXPath(page, ".//th[text() = 'WARSCODE']/../td/text()[1]"));
            data.put("description", getStringFromXPath(page, ".//th[text()='Contract Information']/../../tr/th[text() = 'Description/Name']/../td/descendant-or-self::*/text()"));
            data.put("contractType", getStringFromXPath(page, ".//th[text() = 'Contract Type']/../td/text()[1]"));
            data.put("country", getStringFromXPath(page, ".//th[text() = 'Country/Region']/../td/text()[1]"));
            data.put("exchanges", getStringFromXPath(page, ".//th[text() = 'Exchange']/../td/descendant-or-self::*/text()"));
            data.put("assetID", getStringFromXPath(page, ".//th[text() = 'ASSETID']/../td/text()[1]"));
            data.put("conid", getStringFromXPath(page, ".//th[normalize-space(text()) = 'Conid']/../td/text()[1]"));
            data.put("type", getStringFromXPath(page, ".//th[text() = 'Stock Type']/../td/text()[1]"));
            data.put("url", url);
            data.put("YhSymbol", null);

//            String temp = data.get("exchanges").replace(" ", "").replace("[", "").replace("]", "");
//            String str[] = temp.split(",");
//            data.put("exchanges", Arrays.toString(str));
//            data.put("exchanges", Arrays.toString(str));
            
            JSONObject jobj = new JSONObject(data); 
            return jobj;
        }

    public HtmlPage solveCaptcha(HtmlPage page) {

        if (!page.getByXPath(".//form[normalize-space(text()) = 'To continue please enter the text from the image below']").isEmpty()) {
            try {
                String captcha = ((HtmlElement) page.getByXPath(".//img").get(0)).getAttribute("src").replace("image.php?str=", "");

                HtmlForm form = page.getForms().get(0);
                HtmlTextInput input = form.getInputByName("filter");
                input.type(captcha);
                page = (HtmlPage) input.type('\n');

                logger.info("CAPTCHA found and solved with \"" + captcha + "\".");
            } catch (IOException e) {
                logger.fatal(e.getMessage());
            }
        } else {
            logger.info("No CAPTCHA found.");
        }
        return page;
    }

    public void fetchCountries() {

        HttpArgsGET args = new HttpArgsGET();

        args.setP("");
        args.setFeature(1562);
        
        try {
            String queryUrl = this.baseUrl + URLEncodedUtils.format(args.getArgs(), Charset.forName("utf-8"));
            HtmlPage page = this.client.getPage(queryUrl);

            List<HtmlAnchor> btnSelectors = page.getByXPath(".//div[@id='toptabs']/ul/li/a");

            btnSelectors.forEach(a -> {
                this.countries.add(a.getHrefAttribute().replace("/en/index.php?f=1562&p=", ""));
            });
            
        } catch (FailingHttpStatusCodeException | IOException e) {
            logger.fatal(e.getMessage());
        }
    }

    public List<Exchange> fetchExchanges(String country) {

        List<Exchange> exchangesL = new ArrayList();

        HttpArgsGET args = new HttpArgsGET();

        args.setP(country);
        args.setFeature(1562);

        String queryUrl = this.baseUrl + URLEncodedUtils.format(args.getArgs(), Charset.forName("utf-8"));

        try {
            HtmlPage page = this.client.getPage(queryUrl);

            List<HtmlElement> tableRows = page.getByXPath(".//*[@id='exchange-listings']//table/tbody/tr/td/a");
            if (tableRows.isEmpty()) {
                logger.trace("No scrapable items.");
            } else {
                tableRows.stream().forEach(tableCell -> {
                    String symbol = ((HtmlAnchor) tableCell).getHrefAttribute().replace("index.php?f=2222&exch=", "").replace("&showcategories=", "");
                    if(!exContainsSymbol(this.exchanges, symbol)){
                        String desc = ((HtmlElement) tableCell).getTextContent();
                        
                        try {
                            logger.trace(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Exchange(symbol, desc, country)));
                        } catch (JsonProcessingException e) {
                            logger.fatal(e.getMessage());
                        }

                        exchangesL.add(new Exchange(symbol, desc, country));
                    }
                });
            }
        } catch (FailingHttpStatusCodeException | JsonProcessingException e) {
            logger.fatal(e.getMessage());
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }
        return exchangesL;
    }
}