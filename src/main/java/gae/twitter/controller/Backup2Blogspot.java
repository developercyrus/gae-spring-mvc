package gae.twitter.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

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

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.model.Post;
import com.google.api.services.blogger.model.PostList;
import com.google.gdata.client.GoogleService;
import com.google.gdata.data.Category;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

@Component
@Controller
public class Backup2Blogspot {
	private static final Logger logger = Logger.getLogger(Backup2Blogspot.class.getName());
	    
    @Value("${gae.twitter.controller.Backup2Blogspot.client_id}")    
    private String client_id;
    
    @Value("${gae.twitter.controller.Backup2Blogspot.client_secret}")
    private String client_secret;
    
    @Value("${gae.twitter.controller.Backup2Blogspot.refresh_token}")
    private String refresh_token;  
    
    
    @Value("${gae.twitter.controller.Backup2Blogspot.tUsername}")
    private String tUsername;
    
    @Value("${gae.twitter.controller.Backup2Blogspot.tConsumerKey}")
    private String tConsumerKey;
    
    @Value("${gae.twitter.controller.Backup2Blogspot.tConsumerSecret}")
    private String tConsumerSecret;
    
    @Value("${gae.twitter.controller.Backup2Blogspot.tAccessToken}")
    private String tAccessToken;
    
    @Value("${gae.twitter.controller.Backup2Blogspot.tAccessTokenSecret}")
    private String tAccessTokenSecret;
    
		
    @RequestMapping(value = "/backup2blogspot", method = RequestMethod.GET)
    public ModelAndView anyMethodName() throws IOException {
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
    	
    	Blogger blogger = new Blogger.Builder(httpTransport, jsonFactory, credential).setApplicationName("blogger").build();
        
    	
        int posted = 0;
        try {
            PostList posts = blogger.posts().list("7654023").setLabels("tweet").execute();
            long startPostID = Long.parseLong(posts.getItems().get(0).getTitle());
            
            
            //startPostID = 14596788878L;
            logger.info("startPostID="+String.valueOf(startPostID));
            logger.info("==== start posting ====");

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true).setOAuthConsumerKey(tConsumerKey)
                                       .setOAuthConsumerSecret(tConsumerSecret)
                                       .setOAuthAccessToken(tAccessToken)
                                       .setOAuthAccessTokenSecret(tAccessTokenSecret);
            
            TwitterFactory factory = new TwitterFactory(cb.build());
            Twitter twitter = factory.getInstance();
            
            List<Status> statuses = twitter.getUserTimeline(tUsername);
            logger.info("size=" + statuses.size());
            for (int i = statuses.size()-1; i>=0; i--) {
                Status status = statuses.get(i);
                if (status.getId() > startPostID) {
                    posted++;
                    //System.out.println(status.getUser().getName() + "@" + status.getId() + "\t"+ status.getCreatedAt() + "\t" + status.getText() );
                    
                    String geoLocation = (status.getGeoLocation() != null) ? status.getGeoLocation().toString() : "";
                    logger.info(status.getUser().getName() + "@" + status.getId() + "\t"+ status.getCreatedAt() + "\t" + status.getText() + "\t" + geoLocation);
                    
                    Post p = new Post();
                    p.setTitle(String.valueOf(status.getId()));
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    p.setContent(
                            "<a href='http://twitter.com/bansiudou/status/" + status.getId() + "'>" + format.format(status.getCreatedAt()) + "</a>"
                            + " " + change2Href(status.getText())
                            + " " + geoLocation
                    );
                    List<String> labels = new ArrayList<String>();
                    labels.add("tweet");
                    p.setLabels(labels);
                    blogger.posts().insert("7654023", p).execute();
                }
            }
        } catch (TwitterException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        
        //send(String.valueOf(posted), email);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("output", String.valueOf(posted));
        return mav;
    }
    
    public String change2Href(String text) {
        String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while (m.find()) {
            String find = m.group();
            text = text.replace(find, "<a href=\"" + find + "\">" + find + "</a>");
        }
        return text;
    }
    
    
}
