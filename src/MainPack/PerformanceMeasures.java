package MainPack;

public class PerformanceMeasures {
	
	private int NCCs=0,NAss=0,timeStep=0;

	public PerformanceMeasures() {
	}
	
	public PerformanceMeasures(PerformanceMeasures copy) {
		this.NAss += copy.getNAss();
		this.NCCs += copy.getNCCs();
		this.timeStep = copy.timeStep;
		this.timeStep++;
	}
	
	public void updateNAss() {
		this.NAss++;
	}
	
	public void updateNCCs() {
		this.NCCs++;
	}
	
	public int getNCCs() {
		return NCCs;
	}
	
	public int getNAss() {
		return NAss;
	}
	
	public int getTimeStep() {
		return timeStep;
	}
}
