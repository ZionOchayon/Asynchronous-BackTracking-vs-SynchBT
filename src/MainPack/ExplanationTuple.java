package MainPack;

public class ExplanationTuple {

	private int agent;
	private int assignment;
	
	public ExplanationTuple(int agent, int assignment) {
		this.agent = agent;
		this.assignment = assignment;
	}
	
	public int getAgent() {
		return agent;
	}
	
	public int getAssignment() {
		return assignment;
	}
}
