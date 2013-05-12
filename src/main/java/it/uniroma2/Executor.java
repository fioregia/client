package it.uniroma2;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Counter;
import com.yammer.metrics.Meter;
import com.yammer.metrics.MetricFilter;
import com.yammer.metrics.MetricRegistry;
import com.yammer.metrics.Timer;
import com.yammer.metrics.graphite.Graphite;
import com.yammer.metrics.graphite.GraphiteReporter;

public class Executor implements Runnable{

	private Workload workload;
	private static final MetricRegistry mRegistry = new MetricRegistry(Utils.getIP()+ "-client");
	private final Timer timer = mRegistry.timer("duration");
	private final Counter localRequestCounter = mRegistry
			.counter(MetricRegistry.name("localRequestCounter"));
	private final Meter requestRate = mRegistry.meter("requestRate-client");

	public Executor(String homeDir) {
		this.workload = new Workload(homeDir);
	}

	public void execRequest() {
		HttpClient client = new HttpClient();
		final Timer.Context context = timer.time();
		File f = workload.randomPick();
		requestRate.mark();
		client.upload(f);
		context.stop();
		localRequestCounter.inc();
	}
	
	
	public void run() {
		execRequest();
	}

	public static void main(String[] param) throws UnknownHostException,
			InterruptedException {
		final Graphite graphite = new Graphite(new InetSocketAddress(
				"ec2-184-72-70-243.compute-1.amazonaws.com", 2023));
		GraphiteReporter reporter = GraphiteReporter.forRegistry(mRegistry)
				.prefixedWith(Utils.getIP() + "." + "client")
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.filter(MetricFilter.ALL).build(graphite);
		reporter.start(10, TimeUnit.SECONDS);

		String home = System.getenv("HOME");
		FixedRate fixedRate = new FixedRate(Double.parseDouble(param[0]), new Executor(home + "/imgs"));
		System.out.println(new Double((1D / Double.parseDouble(param[0])) * 1000).longValue());
	}
}
