package gae.jobsdb.controller;

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
public class JobCountController {
    private static final Logger logger = Logger.getLogger(JobCountController.class.getName());
    
    private String username;        

    private String password;
    
    @RequestMapping(value = "/jobsdbjobcount", method = RequestMethod.GET)
    public ModelAndView anyMethodName_2() throws ClientProtocolException, IOException, ServiceException, SVNException {        
        StringBuffer sb = new StringBuffer(); 
        String url = "http://www.jobsdb.com/HK/";
        
        //Document doc = Jsoup.parse(new URL(url), 120000);
        Document doc = Jsoup.connect(url)
		        	      	.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
		        	      	.referrer("http://www.jobsdb.com")
		        	      	.get();
        //Elements elements = doc.select(".searchbar-tagline").select("span");
        Elements elements = doc.select(".searchbar-total-job-count");
        Element e = elements.get(0);
        String jobCount = e.text();
        logger.info("today job count = " + jobCount);
                            
        sb.append("url = " + url + "<br/>");
        sb.append("today job count = " + jobCount + "<br/>");
        
        this.upload(url, String.valueOf(jobCount), username, password);   
                
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/jobsdb/jobsdbjobcount");
        mav.addObject("output", sb.toString());
        return mav;
    }
    

    
    public void upload(String url, String value1, String username, String password) throws IOException, ServiceException {        
        Calendar cal = new GregorianCalendar();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(timeZone);
        cal.setTimeZone(timeZone);
        
        String DATE = formatter.format(cal.getTime());
        String JOBS = value1;
        
        logger.info("DATE=" + DATE);
        logger.info("JOBS=" + JOBS);
        
        SpreadsheetService service = new SpreadsheetService("JOBSDB-COUNT"); 
        service.setUserCredentials(username, password);
        
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed spreadSheetFeed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = spreadSheetFeed.getEntries();
        
        Iterator<SpreadsheetEntry> iterator = spreadsheets.iterator();
        
        while (iterator.hasNext()) {
            SpreadsheetEntry spreadSheetEntry = iterator.next();
            logger.info("spreadsheet name:" + spreadSheetEntry.getTitle().getPlainText());            
            
            if (spreadSheetEntry.getTitle().getPlainText().equals("JOBSDB_JOB_NO_WORKSHEET")) {            
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
                newEntry.getCustomElements().setValueLocal("JOBS", JOBS);
                service.insert(listFeedUrl, newEntry);
                logger.info("addded");
            }
        }
    }
}
