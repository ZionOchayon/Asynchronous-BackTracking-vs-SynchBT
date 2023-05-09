package MainPack;

public class CPA implements Message { 
	
	public static int Ass=0,CCs=0,BTs=0;
	public VarTuple[] assignments;
	
	public CPA(VarTuple agentAssignment,int agentsSize) {
		this.assignments = new VarTuple[agentsSize];
		assignments[0] = agentAssignment;
		Ass++;
	}
	
	public void addAssignment(int id,VarTuple agentAssignment) {
		assignments[id] = agentAssignment;
		Ass++;
	}
	
	// this function remove all the assignments to specific id we use this on backtracks because we perform back jumping algorithm
	public void removeAssignmentsToId(int id) {
		for(int i=assignments.length-1;i>=id;i--) {
			assignments[i] = null;
		}
	}
	
	public VarTuple getAssignment(int id) {
		for(VarTuple tmp : assignments) {
			if(tmp.getI() == id) {
				return tmp;
			}
		}
		return null;
	}
	
}
