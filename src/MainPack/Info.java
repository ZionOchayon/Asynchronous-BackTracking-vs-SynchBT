package MainPack;

public class Info implements Message {
	private VarTuple assignment;
	private int id;
	private PerformanceMeasures PM;
	
	public Info(int id,int agentAssignment,PerformanceMeasures PM) {
		this.assignment = new VarTuple(id,agentAssignment);
		this.id = id;
		this.PM = PM;
	}

	public VarTuple getAssignment() {
		return assignment;
	}

	public int getId() {
		return id;
	}

	public PerformanceMeasures getPM() {
		return PM;
	}
}
