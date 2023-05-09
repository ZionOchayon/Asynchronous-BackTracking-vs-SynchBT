package MainPack;

public class Solution {

	private boolean solveable = true;
	private int[] solution;
	private PerformanceMeasures[] PMs;
	
	public Solution(int n) {
		solution = new int[n];
		PMs = new PerformanceMeasures[n];
	}

	public boolean isSolveable() {
		return solveable;
	}

	public int[] getSolution() {
		return solution;
	}

	public void setPms(int id, PerformanceMeasures p) {
		PMs[id] = p;
	}
	
	public PerformanceMeasures getPMs() {
		PerformanceMeasures tmp = null;
		for(PerformanceMeasures pm : PMs) {
			if(pm != null) {
				if( tmp == null || tmp.getTimeStep()<pm.getTimeStep()) {
					tmp = pm;
				}
			}

		}
		return tmp;
	}

	public void set(int id, int assignment) {
		solution[id] = assignment;
	}
	
	public void notSolveable() {
		solveable = false;
	}
}
