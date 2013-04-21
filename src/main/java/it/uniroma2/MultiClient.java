package it.uniroma2;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Counter;
import com.yammer.metrics.MetricFilter;
import com.yammer.metrics.MetricRegistry;
import com.yammer.metrics.Timer;
import com.yammer.metrics.graphite.Graphite;
import com.yammer.metrics.graphite.GraphiteReporter;


public class MultiClient extends Thread{

	private String homeDir; 

	public MultiClient(String homeDir) {
		this.homeDir = homeDir;
	}
	
	@Override
	public void run() {
		super.run();
		String uuid = UUID.randomUUID().toString();
		final MetricRegistry mRegistry = new MetricRegistry("client-" + uuid);
		final Graphite graphite = new Graphite(new InetSocketAddress("ec2-54-234-175-54.compute-1.amazonaws.com", 2023));
		GraphiteReporter reporter;
		try {
			reporter = GraphiteReporter.forRegistry(mRegistry)
			        .prefixedWith(uuid + "-" + InetAddress.getLocalHost().getHostName() + "." + "client")
			        .convertRatesTo(TimeUnit.SECONDS)
			        .convertDurationsTo(TimeUnit.MILLISECONDS)
			        .filter(MetricFilter.ALL)
			        .build(graphite);
			reporter.start(10, TimeUnit.SECONDS);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
		HttpClient client = new HttpClient();	
		final Timer timer = mRegistry.timer("duration");
		final Counter localRequestCounter = mRegistry.counter(MetricRegistry.name("localRequestCounter"));
		WorkloadAnalisys workloadAnalisys = new WorkloadAnalisys(homeDir);
		File[] files = workloadAnalisys.list();

		for (File f : files) {
			final Timer.Context context = timer.time();
			client.upload(f);
			context.stop();
			localRequestCounter.inc();
		}
	}
	
	public static void main(String[] param) throws UnknownHostException, InterruptedException {
		int cont = 30;
		for(int i=0; i<cont; i++){
			String home = System.getenv("HOME");
			MultiClient multiClient = new MultiClient(home + "/imgs");
			multiClient.run();
			Thread.sleep(1000);
			
		}
	}

}
