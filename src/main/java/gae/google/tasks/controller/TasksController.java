package gae.google.tasks.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.esxx.js.protocol.GAEConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

@Component
@Controller
public class TasksController {
    private static final Logger logger = Logger.getLogger(TasksController.class.getName());
    
    @Value("${gae.google.tasks.controller.TasksController.client_id}")  
    private String client_id;
    
    @Value("${gae.google.tasks.controller.TasksController.client_secret}")  
    private String client_secret;
    
    @Value("${gae.google.tasks.controller.TasksController.refresh_token}")  
    private String refresh_token;
    
    @Value("${gae.google.tasks.controller.TasksController.username}")  
    private String username;
    
    @Value("${gae.google.tasks.controller.TasksController.password}")  
    private String password;
    
    
    @RequestMapping(value = "/taskscount", method = RequestMethod.GET)
    public ModelAndView anyMethodName_1() throws ClientProtocolException, IOException, ServiceException {
        String url = "https://accounts.google.com/o/oauth2/token";
        String grant_type = "refresh_token";

        
        List<BasicNameValuePair> data = new ArrayList<BasicNameValuePair>();  
        data.add(new BasicNameValuePair("client_id", client_id));  
        data.add(new BasicNameValuePair("client_secret", client_secret));  
        data.add(new BasicNameValuePair("refresh_token", refresh_token));
        data.add(new BasicNameValuePair("grant_type", grant_type));
        
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter(HttpProtocolParams.HTTP_CONTENT_CHARSET, "UTF-8");
        ClientConnectionManager connectionManager = new GAEConnectionManager();
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, httpParams);
          
        HttpPost httpPost = new HttpPost(url);  
        httpPost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8)); 
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String respondBody = httpClient.execute(httpPost, responseHandler);
        
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(respondBody);  
        String accessToken = jsonObject.getString("access_token");
        
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();                
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
 
        Tasks service = new Tasks(httpTransport, jsonFactory, credential);

        String taskID = "MDkwNjk0OTkzMzIzMzY2OTk1MjY6MDow";
        com.google.api.services.tasks.model.Tasks tasks = service.tasks().list(taskID).execute();

        int totalCount = 0;
        int completedCount = 0;;
        while (true) {
            for (Task task : tasks.getItems()) {
                totalCount++;
                if (task.getCompleted()!= null) {
                    completedCount++;
                }
            }

            String pageToken = tasks.getNextPageToken();
            if (pageToken != null && !pageToken.equalsIgnoreCase("")) {
                tasks = service.tasks().list(taskID).setPageToken(pageToken).execute();
            }
            else {
                break;
            }
        }
        logger.info("total count = " + totalCount);
        logger.info("completed count = " + completedCount);
        
        System.out.println("total count = " + totalCount);
        System.out.println("completed count = " + completedCount);
        
        StringBuffer sb = new StringBuffer(); 
        sb.append("total count = " + totalCount + "<br>");
        sb.append("completed count = " + completedCount + "<br>");
        
        this.upload(String.valueOf(totalCount), String.valueOf(completedCount), username, password);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("gae/google/tasks/taskscount");
        mav.addObject("output", sb.toString());
        return mav;
    }
    
    public void upload(String value1, String value2, String username, String password) throws IOException, ServiceException {
        
        Calendar cal = new GregorianCalendar();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(timeZone);
        cal.setTimeZone(timeZone);
        
        String DATE = formatter.format(cal.getTime());
        String TOTAL = value1;
        String COMPELTED = value2;

        logger.info("DATE=" + DATE);
        logger.info("TOTAL=" + TOTAL);
        logger.info("COMPELTED=" + COMPELTED);
        
        SpreadsheetService service = new SpreadsheetService("GOOGLE-TASKS"); 
        service.setUserCredentials(username, password);
        
        FeedURLFactory factory = FeedURLFactory.getDefault();
        SpreadsheetFeed spreadSheetFeed = service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = spreadSheetFeed.getEntries();
        
        Iterator<SpreadsheetEntry> iterator = spreadsheets.iterator();
        
        while (iterator.hasNext()) {
            SpreadsheetEntry spreadSheetEntry = iterator.next();
            
            if (spreadSheetEntry.getTitle().getPlainText().equals("GOOGLE_TASKS_COUNT")) {            
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
                newEntry.getCustomElements().setValueLocal("TOTAL", TOTAL);
                newEntry.getCustomElements().setValueLocal("COMPLETED", COMPELTED);
                service.insert(listFeedUrl, newEntry);
                logger.info("addded");
            }
        }
    }
}
