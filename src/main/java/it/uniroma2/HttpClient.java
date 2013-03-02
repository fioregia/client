package it.uniroma2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClient {

	private final String FILTER = "1";
	// private String urlString =
	// "http://0.0.0.0:8080/imagetranscoder/uploadServlet";
	private String urlString = "http://pasquale-499086798.us-east-1.elb.amazonaws.com/imagetranscoder/uploadServlet";

	public void upload(File uploadFile) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		try {
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("filter", new StringBody(FILTER, "text/plain",
					Charset.forName("UTF-8")));

			entity.addPart("file", new FileBody(uploadFile));
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			File newf = new File(System.getenv("HOME")+"/"+ UUID.randomUUID().toString() + ".png");
			BufferedInputStream br = new BufferedInputStream(response
					.getEntity().getContent());
			BufferedOutputStream bw = new BufferedOutputStream(
					new FileOutputStream(newf));
			int r;
			while ((r = br.read()) != -1)
				bw.write(r);
			bw.close();
			br.close();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
