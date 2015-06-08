package gae.google.code.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.tmatesoft.svn.core.SVNException;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

@Component
@Controller
public class CheckInCountController {
    private static final Logger logger = Logger.getLogger(CheckInCountController.class.getName());
    
    @Value("${gae.google.code.controller.CheckInCountController.username}")       
    private String username;        
    @Value("${gae.google.code.controller.CheckInCountController.password}")
    private String password;
    
    @RequestMapping(value = "/checkincount", method = RequestMethod.GET)
    public ModelAndView anyMethodName_2() throws ClientProtocolException, IOException, ServiceException, SVNException {
        String[] urls = {
                            "http://code.google.com/p/app-snippets/source/list"
                            , "http://code.google.com/p/nexus-snippets/source/list"
                            , "https://code.google.com/p/gae-jax-rs/source/list"
                            , "https://code.google.com/p/webapp-snippets/source/list"                            
                        };
        StringBuffer sb = new StringBuffer(); 
        
        for (String url : urls) {
            Document doc = Jsoup.parse(new URL(url), 60000);
            Elements elements = doc.select(".id");
            Element e = elements.get(0);
            String dailyCount = e.text().substring(1, e.text().length());
            logger.info("today count = " + dailyCount);
                                
            sb.append("url = " + url + "<br/>");
            sb.append("today count = " + dailyCount + "<br/>");
            
            this.upload(url, String.valueOf(dailyCount), username, password);   
        }
                
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/google/code/checkincount");
        mav.addObject("output", sb.toString());
        return mav;
    }
    
    /*
    @RequestMapping(value = "/checkincountbysvn", method = RequestMethod.GET)
    public ModelAndView anyMethodName_1() throws ClientProtocolException, IOException, ServiceException, SVNException {
        String urlStr = "https://app-snippets.googlecode.com/svn/trunk/";
        String username = "";
        String password = "";
        
        DAVRepositoryFactory.setup();
        SVNURL url = SVNURL.parseURIEncoded(urlStr);
        SVNRepository repos = SVNRepositoryFactory.create(url);
        //ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
        //repos.setAuthenticationManager(authManager);

        logger.info("repos.getLocation() = " + repos.getLocation());
        
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays(1);

        logger.info("today revision = " + repos.getDatedRevision(today.toDate()));
        logger.info("yesterday revision = " + repos.getDatedRevision(yesterday.toDate())); 
        long dailyCount = repos.getDatedRevision(today.toDate()) - repos.getDatedRevision(yesterday.toDate());
        logger.info("today count = " + dailyCount);
                
        StringBuffer sb = new StringBuffer(); 
        sb.append("today count = " + dailyCount);
        
        this.upload(String.valueOf(dailyCount), username, password);        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/google/code/checkincountbysvn");
        mav.addObject("output", sb.toString());
        return mav;
    }
    */
    
    
    public void upload(String url, String value1, String username, String password) throws IOException, ServiceException {        
        Calendar cal = new GregorianCalendar();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(timeZone);
        cal.setTimeZone(timeZone);
        
        String DATE = formatter.format(cal.getTime());
        String REPOSITORY = url;
        String TOTAL = value1;
        

        logger.info("DATE=" + DATE);
        logger.info("REPOSITORY=" + REPOSITORY);
        logger.info("TOTAL=" + TOTAL);
        
        SpreadsheetService service = new SpreadsheetService("GOOGLE-CODE"); 
        service.setUserCredentials(username, password);
        
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed spreadSheetFeed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = spreadSheetFeed.getEntries();
        
        Iterator<SpreadsheetEntry> iterator = spreadsheets.iterator();
        
        while (iterator.hasNext()) {
            SpreadsheetEntry spreadSheetEntry = iterator.next();
            logger.info("spreadsheet name:" + spreadSheetEntry.getTitle().getPlainText());            
            
            if (spreadSheetEntry.getTitle().getPlainText().equals("GOOGLE_CODE_COUNT")) {            
                logger.info(spreadSheetEntry.getTitle().getPlainText());
                
                List<WorksheetEntry> worksheets = spreadSheetEntry.getWorksheets();
                WorksheetEntry worksheet = worksheets.get(0);
                
                String title = worksheet.getTitle().getPlainText();
                int rowCount = worksheet.getRowCount();
                int colCount = worksheet.getColCount();
                logger.info(title + "\trows:" + rowCount + "\tcols: " + colCount);
                    
                
                URL listFeedUrl = worksheet.getListFeedUrl();
                logger.info(listFeedUrl.toString());
    
                ListEntry newEntry = new ListEntry();
                newEntry.getCustomElements().setValueLocal("DATE", DATE);
                newEntry.getCustomElements().setValueLocal("REPOSITORY", REPOSITORY);
                newEntry.getCustomElements().setValueLocal("TOTAL", TOTAL);
                service.insert(listFeedUrl, newEntry);
                logger.info("addded");
            }
        }
    }
}
