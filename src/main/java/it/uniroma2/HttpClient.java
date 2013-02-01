package it.uniroma2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClient {
	private final String FILTER = "1";
    private String urlString = "http://0.0.0.0:8080/imagetranscoder/uploadServlet";
    HttpURLConnection conn;
    String uploadedFile = "/Users/giampaolo/Desktop/car.png";
    String downloadFile="/Users/giampaolo/Desktop/giampaolo.png";
    String boundary = "--";
    
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    
   
    private void upload(){
    	 DefaultHttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost(urlString);
    	try {
    	MultipartEntity entity = new MultipartEntity();
    	File fileUp = new File(uploadedFile);
    	entity.addPart("filter", new StringBody( FILTER, "text/plain",
                Charset.forName( "UTF-8" )));

    	entity.addPart("file", new FileBody(fileUp));
    	post.setEntity(entity);

    
			HttpResponse response = client.execute(post);
			
		
			
			File newf = new File(downloadFile);
			BufferedInputStream br = new BufferedInputStream(response.getEntity().getContent());
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(newf));
			int r;
			while ((r=br.read()) != -1) bw.write(r);
			bw.close();
			br.close(); 
			
    	} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    
    }
    
    public static void main(String[] param){
    	HttpClient client = new HttpClient();
    	client.upload();
    }
    
   
    
}
