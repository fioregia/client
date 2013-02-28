package it.uniroma2;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import com.yammer.metrics.reporting.GraphiteReporter;

public class MultiClient extends Thread{

	private String homeDir; 
	
	public MultiClient(String homeDir) {
		this.homeDir = homeDir;
	}
	
	@Override
	public void run() {
		super.run();
		HttpClient client = new HttpClient();	
		// per graphite usare new e prefix
		try {
			GraphiteReporter.enable(1, TimeUnit.SECONDS, "23.22.114.15", 2023, "Client-" + InetAddress.getLocalHost().getHostName().replace('.', '-'));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		final Timer timer = Metrics.newTimer(HttpClient.class, "duration",
				TimeUnit.SECONDS, TimeUnit.SECONDS);
		WorkloadAnalisys workloadAnalisys = new WorkloadAnalisys(homeDir);
		File[] files = workloadAnalisys.list();

		for (File f : files) {
			final TimerContext context = timer.time();
			client.upload(f);
			context.stop();
		}
	}
	
	public static void main(String[] param) throws UnknownHostException {
		int cont = 5;
		for(int i=0; i<cont; i++){
			String home = System.getenv("HOME");
			MultiClient multiClient = new MultiClient(home + "/imgs");
			multiClient.run();
			
		}
	}

}
