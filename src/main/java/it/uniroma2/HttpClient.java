package it.uniroma2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.httpclient.InstrumentedClientConnManager;
import com.yammer.metrics.httpclient.InstrumentedHttpClient;
import com.yammer.metrics.reporting.ConsoleReporter;
import com.yammer.metrics.reporting.GraphiteReporter;

public class HttpClient {

	private static final MetricsRegistry mRegistry = new MetricsRegistry();
	private final String FILTER = "1";
//	 private String urlString =
//	 "http://0.0.0.0:8080/imagetranscoder/uploadServlet";
	private String urlString = "http://testandriani-473276648.us-east-1.elb.amazonaws.com/imagetranscoder/uploadServlet";

	private String downloadFile = "/Users/pandriani/Desktop/giampaolo.png";

	public void upload(File uploadFile) {

		DefaultHttpClient client = new DefaultHttpClient();
//		InstrumentedHttpClient client = new InstrumentedHttpClient(mRegistry, new InstrumentedClientConnManager(), null);
		HttpPost post = new HttpPost(urlString);
		try {
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("filter", new StringBody(FILTER, "text/plain",
					Charset.forName("UTF-8")));

			entity.addPart("file", new FileBody(uploadFile));
			post.setEntity(entity);

			HttpResponse response = client.execute(post);

			File newf = new File(downloadFile);
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

	public static void main(String[] param) {
		HttpClient client = new HttpClient();	
//		ConsoleReporter.enable(mRegistry, 1, TimeUnit.SECONDS);
		// per graphite usare new e prefix
//		GraphiteReporter.enable(mRegistry, 1, TimeUnit.SECONDS,
//				"ec2-23-20-108-80.compute-1.amazonaws.com", 2003);
//		final Timer timer = mRegistry.newTimer(HttpClient.class, "duration",
//				TimeUnit.SECONDS, TimeUnit.SECONDS);
		WorkloadAnalisys workloadAnalisys = new WorkloadAnalisys();
		File[] files = workloadAnalisys.list();

		for (File f : files) {
//			final TimerContext context = timer.time();
			client.upload(f);
//			context.stop();
		}

	}
}
