package MainPack;
import java.util.ArrayList;
import java.util.List;

public class Explanation {

	private List<ExplanationTuple> tuples = new ArrayList<ExplanationTuple>();

	public List<ExplanationTuple> getTuples() {
		return tuples;
	}
	
	public void add(ExplanationTuple et) {
		tuples.add(et);
	}
}
