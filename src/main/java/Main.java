import com.webscraper.InteractiveBrokers;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException {

        //java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
        //java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
        //System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        //System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        //System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        //System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        //System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");

        InteractiveBrokers ibkr = new InteractiveBrokers();
        //System.out.println(ibkr.getBtns());
        //System.out.println(ibkr.getNumberOfPages());
        ibkr.get3();
//        ibkr.getIsin("https://contract.ibkr.info/v3.10/index.php?action=Details&site=GEN&conid=455616324");
       
    }
}