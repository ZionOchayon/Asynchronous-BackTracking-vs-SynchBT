package MainPack;

import java.util.HashMap;
import java.util.Scanner;

public class Generator {
	
	private int n, d;
	private double p1, p2;

	public Generator(int n, int d, double p1, double p2) {
		this.n = n;
		this.d = d;
		this.p1 = p1;
		this.p2 = p2;
	}

	public CSP generateGameManually(Scanner scan) {
		
		HashMap<VarTuple, ConsTable> game = new HashMap<VarTuple, ConsTable>();
		
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				System.out.println("agents are " + i + " and " + j + ". skip? (y / n)");
				String s = scan.next();
				if (s.equals("y")) {
					continue;
				} else {
					VarTuple at = new VarTuple(i, j);
					ConsTable ct = new ConsTable(d);
					for (int k = 0; k < d; k++) {
						System.out.println(k + "'s line");
						for (int l = 0; l < d; l++) {
							int b = scan.nextInt();
							ct.set(k, l, (b == 1));
						}
					}
					game.put(at, ct);
				}
			}
		}
		
		return new CSP(game, d, n);
	}
	
	// genetate csp
	public CSP generateDCSP() {
		
		HashMap<VarTuple, ConsTable> cons_tables = new HashMap<VarTuple, ConsTable>();
		// return that game
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (Math.random() < p1) {
					VarTuple at = new VarTuple(i, j);
					ConsTable ct = new ConsTable(d, p2);
					cons_tables.put(at, ct);
				}
			}
		}
		
		return new CSP(cons_tables, d, n);
	}
}
