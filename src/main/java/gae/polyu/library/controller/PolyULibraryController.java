package gae.polyu.library.controller;

import gae.polyu.library.model.Record;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.esxx.js.protocol.GAEConnectionManager;
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

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

@Component
@Controller
public class PolyULibraryController {
    private static final Logger logger = Logger.getLogger(PolyULibraryController.class.getName());
    
    @Value("${gae.polyu.library.controller.PolyULibraryController.username}")
    private String username;
    @Value("${gae.polyu.library.controller.PolyULibraryController.password}")
    private String password;
    @Value("${gae.polyu.library.controller.PolyULibraryController.usernameValue}")
    private String usernameValue;
    @Value("${gae.polyu.library.controller.PolyULibraryController.passwordValue}")
    private String passwordValue;
    @Value("${gae.polyu.library.controller.PolyULibraryController.infoPath}")
    private String infoPath;
    
    
    
    @RequestMapping(value = "/lib", method = RequestMethod.GET)
    public ModelAndView anyMethodName_1() throws ClientProtocolException, IOException, ServiceException {

        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter(HttpProtocolParams.HTTP_CONTENT_CHARSET, "UTF-8");
        ClientConnectionManager connectionManager = new GAEConnectionManager();
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
        //HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);   
        //HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BEST_MATCH);
        HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.RFC_2109);

        this.login(httpClient);

        //List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
        //List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            logger.info("Cookie None");
        } 
        else {
            for (Cookie cookie : cookies) {
                logger.info("- " + cookie.toString());
            }
        }
        
        String responseBody = this.redirect(httpClient);
        List<Record> records = this.getRecord(responseBody);
        //boolean isOK = this.saveRecord(records, httpClient);
        boolean isOK = this.saveRecordBySpreadSheet(records);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/polyu/library/lib");
        mav.addObject("lib", this.format(responseBody));
        return mav;
    }
    
    public void login(DefaultHttpClient httpClient) throws IOException {
        String loginPath = "https://library.polyu.edu.hk/patroninfo";
        String usernameKey = "code";
        String passwordKey = "pin";
        String submitKey = "pat_submit";
        String submitValue = "xxx";
        
        HttpPost httppost = new HttpPost(loginPath);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(usernameKey, usernameValue));
        nvps.add(new BasicNameValuePair(passwordKey, passwordValue));
        nvps.add(new BasicNameValuePair(submitKey, submitValue));
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        //httppost.setHeader("Content-Type", "text/xml; charset=utf-8");
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        logger.info("login status code: " + statusCode);
        EntityUtils.consume(entity);
        
    }
    
    public String  redirect(DefaultHttpClient httpClient) throws ClientProtocolException, IOException {       
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        HttpGet httpget = new HttpGet(infoPath);
        String responseBody = httpClient.execute(httpget, responseHandler);
        
        return responseBody;
    }
    
    public List<Record> getRecord(String s) {
        Record r;
        String title;
        String barcode;
        String duedate;
        String callnumber;
        List<Record> records = new ArrayList<Record>();
        
        Document doc = Jsoup.parse(s);
        Elements elements = doc.select(".patFuncEntry");
        for (Element e : elements) {
            r = new Record();
            title = e.select(".patFuncTitle").text();
            barcode = e.select(".patFuncBarcode").text();
            duedate = e.select(".patFuncStatus").get(0).ownText();
            callnumber = e.select(".patFuncCallNo").text();

            duedate = duedate.substring(0, 12).trim();
            int end;
            if ((end=callnumber.indexOf("c."))!=-1) {
                callnumber = callnumber.substring(0, end).trim();
            } 
            
            r.setTitle(title);
            r.setBarcode(barcode);
            r.setDuedate(duedate);
            r.setCallnumber(callnumber);
            
            records.add(r);
        }
        
        return records;
    }
    
    public boolean saveRecordBySpreadSheet(List<Record> records) throws ClientProtocolException, IOException, ServiceException {
        String title;
        String barcode;
        String duedate;
        String callnumber;

        for (Record r : records) {
            title = r.getTitle();
            barcode = r.getBarcode();
            duedate = r.getDuedate();
            callnumber = r.getCallnumber();
            
            if (!this.isExistRecord(callnumber, duedate)) {
                this.insertRecord(barcode, callnumber, duedate, title);
            }
        }
        
        return true;
    }
    
    
    public boolean isExistRecord(String callnumber, String duedate) throws IOException, ServiceException {
        SpreadsheetService service = new SpreadsheetService("POLYU_LIBRARY_RECORD"); 
        service.setUserCredentials(username, password);
        
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed spreadSheetFeed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = spreadSheetFeed.getEntries();
        
        Iterator<SpreadsheetEntry> iterator = spreadsheets.iterator();
        
        while (iterator.hasNext()) {
            SpreadsheetEntry spreadSheetEntry = iterator.next();
            logger.info("spreadsheet name:" + spreadSheetEntry.getTitle().getPlainText());            
            
            if (spreadSheetEntry.getTitle().getPlainText().equals("POLYU_LIBRARY_RECORD")) {                       
                List<WorksheetEntry> worksheets = spreadSheetEntry.getWorksheets();
                WorksheetEntry worksheet = worksheets.get(0);
                
                URL listFeedUrl = worksheet.getListFeedUrl();
                logger.info(listFeedUrl.toString());
                ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);
                for (ListEntry entry : feed.getEntries()) {
                    if(entry.getCustomElements().getValue("callnumber").equals(callnumber) && entry.getCustomElements().getValue("duedate").equals(duedate)) {
                        logger.info("callnumber:"+callnumber);
                        logger.info("duedate:"+duedate);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void insertRecord(String barcode, String callnumber, String duedate, String title) throws IOException, ServiceException {
        SpreadsheetService service = new SpreadsheetService("POLYU_LIBRARY_RECORD"); 
        service.setUserCredentials(username, password);
        
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed spreadSheetFeed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = spreadSheetFeed.getEntries();
        
        Iterator<SpreadsheetEntry> iterator = spreadsheets.iterator();
        
        while (iterator.hasNext()) {
            SpreadsheetEntry spreadSheetEntry = iterator.next();
            logger.info("spreadsheet name:" + spreadSheetEntry.getTitle().getPlainText());            
            
            if (spreadSheetEntry.getTitle().getPlainText().equals("POLYU_LIBRARY_RECORD")) {                       
                List<WorksheetEntry> worksheets = spreadSheetEntry.getWorksheets();
                WorksheetEntry worksheet = worksheets.get(0);
                
                URL listFeedUrl = worksheet.getListFeedUrl();

                ListEntry newEntry = new ListEntry();
                newEntry.getCustomElements().setValueLocal("barcode", barcode);
                newEntry.getCustomElements().setValueLocal("callnumber", callnumber);
                newEntry.getCustomElements().setValueLocal("duedate", duedate);
                newEntry.getCustomElements().setValueLocal("link", "<null>");                
                newEntry.getCustomElements().setValueLocal("title", title);
                service.insert(listFeedUrl, newEntry);
                
                logger.info("addded");
            }
        }
    }
    
    public boolean saveRecord(List<Record> records, DefaultHttpClient httpClient) throws ClientProtocolException, IOException {
        String title;
        String barcode;
        String duedate;
        String callnumber;
        String urlStr;
        ResponseHandler<String> responseHandler;
        HttpGet httpget;
        String responseBody;
        
        for (Record r : records) {
            title = r.getTitle();
            barcode = r.getBarcode();
            duedate = r.getDuedate();
            callnumber = r.getCallnumber();
            
            urlStr =    "http://url-services.appspot.com/checkRecordServlet" 
                        + "?callnumber="+URLEncoder.encode(callnumber, "UTF-8")
                        + "&duedate="+URLEncoder.encode(duedate, "UTF-8");
            
            responseHandler = new BasicResponseHandler();
            httpget = new HttpGet(urlStr);
            responseBody = httpClient.execute(httpget, responseHandler);
            logger.info(responseBody + "\t" + urlStr);
            
            if (responseBody.trim().startsWith("N")) {
            //if (responseBody.equals("N")) {            
                urlStr = "http://url-services.appspot.com/insertRecordServlet"  
                           + "?title=" + URLEncoder.encode(title, "UTF-8")
                           + "&barcode=" + URLEncoder.encode(barcode, "UTF-8") 
                           + "&duedate=" +  URLEncoder.encode(duedate, "UTF-8") 
                           + "&callnumber=" +  URLEncoder.encode(callnumber, "UTF-8") ;
                
                httpget = new HttpGet(urlStr);
                HttpResponse response = httpClient.execute(httpget);
                
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                logger.info(statusCode + "\t" + urlStr);
            }
        }
        
        return true;
    }
    
    public String format(String s) {
        Document doc = Jsoup.parse(s);
        //Elements elements = doc.select("#checkout_form");
        //return elements.html();
        Elements elements = doc.select(".patFunc");
        return elements.outerHtml();
    }
}
