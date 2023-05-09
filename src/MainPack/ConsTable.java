package MainPack;

/*
 * represents a constraints table between two agents
 */
public class ConsTable {

	private boolean[][] table;
	
	public ConsTable(int d) {
		table = new boolean[d][d];
	}
	
	public void set(int k, int l, boolean b) {
		table[k][l] = b;
	}
	
	// create a constraint table with corresponding domain size and p2
	public ConsTable(int d, double p2) {
		
		table = new boolean[d][d];
		
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < d; j++) {
				table[i][j] = true;
				if (Math.random() < p2) {
					table[i][j] = false;
				}
			}
		}
	}
	
	// print a constraint table
	public void print(int d) {
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < d; j++) {
				System.out.print(table[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public boolean check(int i, int j) {
		return table[i][j];
	}
	
	public boolean[][] getTable() {
		return table;
	}
}
