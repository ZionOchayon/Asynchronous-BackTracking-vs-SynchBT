package MainPack;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

public class CSP {

	// required fields - the constraints tables and the domain size
	private HashMap<VarTuple, ConsTable> cons_tables;
	public HashMap<VarTuple, ConsTable> getCons_tables() {
		return cons_tables;
	}

	private int domainSize;
	int n;
	
	public CSP(HashMap<VarTuple, ConsTable> cons_tables, int domainSize, int agents) {
		this.cons_tables = cons_tables;
		this.domainSize = domainSize;
		this.n = agents;
	}

	public HashMap<Integer, ConsTable> tablesOf(int i) {		
		HashMap<Integer, ConsTable> tables = new HashMap<Integer, ConsTable>();
		SortedSet<Integer> neighbors = neighborsOf(i);
		for (int j: neighbors) {
			for (Entry<VarTuple, ConsTable> entry : cons_tables.entrySet()) {
				VarTuple vt = entry.getKey();
				if ((vt.getI() == i && vt.getJ() == j) || (vt.getJ() == i && vt.getI() == j)) {
					tables.put(j, cons_tables.get(vt));
				}
			}
		}
		return tables;
	}
	
	private SortedSet<Integer> neighborsOf(int i) {
		SortedSet<Integer> neighbors = new TreeSet<Integer>();
		for (Entry<VarTuple, ConsTable> entry : cons_tables.entrySet()) {
			if (entry.getKey().getI() == i) {
				neighbors.add(entry.getKey().getJ());
			}
			if (entry.getKey().getJ() == i) {
				neighbors.add(entry.getKey().getI());
			}
		}
		return neighbors;
	}
	
	// print a csp
	public void print() {
		for (Entry<VarTuple, ConsTable> entry : cons_tables.entrySet()) {
			System.out.println("table of " + entry.getKey().getI() + " and " + entry.getKey().getJ() + ":");
			entry.getValue().print(domainSize);
			System.out.println();
		}
	}
}
