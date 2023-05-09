package MainPack;
import java.util.Map;

public class Nogood implements Message {

	private int agentId;
	private int badAssignment;
	private PerformanceMeasures PM;
	private Map<Integer, Explanation> explanation;
	
	Nogood(int agentId,int badAssignment , Map<Integer, Explanation> explanation,PerformanceMeasures PM) {
		this.agentId = agentId;
		this.explanation = explanation;
		this.badAssignment = badAssignment;
		this.PM = PM;
	}

	public int getAgentId() {
		return agentId;
	}

	public Map<Integer, Explanation> getExplanation() {
		return explanation;
	}

	public int getBadAssignment() {
		return badAssignment;
	}

	public PerformanceMeasures getPM() {
		return PM;
	}
}
 