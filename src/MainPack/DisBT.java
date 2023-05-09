package MainPack;
import java.util.ArrayList;
import java.util.HashMap;

public class DisBT {

	public static Solution search(int n, int d, CSP csp) throws InterruptedException {
		
		Solution solution = new Solution(n);
		
		// initialize mailer
		Mailer mailer = new Mailer(n);
		for (int i = 0; i < n; i++) {
			mailer.put(i);
		}
		
		// create agents
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < n; i++) {
			// use the csp to extract the private information of each agent
			HashMap<Integer, ConsTable> private_information = csp.tablesOf(i);
			Thread t = new Thread(new DisBTAgent(i, mailer, private_information, solution, n, d));
			threads.add(t);
		}

		// run agents as threads
		for (Thread t : threads) {
			t.start();
		}
		
		// wait for all agents to terminate
		for (Thread t : threads) {
			t.join();
		}

		return solution;
	}
}
