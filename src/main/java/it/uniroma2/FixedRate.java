package it.uniroma2;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FixedRate {

	private Timer timer;
	private Executor executor;
	
	public FixedRate(double requestPerSecond, Executor executor) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new ExecuteRequestTask(), 0L, // initial delay
				new Double((1D / requestPerSecond) * 1000).longValue()); // subsequent rate
		this.executor = executor;
		
	}

	class ExecuteRequestTask extends TimerTask {
		public void run() {
			new Thread(executor).start();
		}
	}

}
