package MainPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ABTAgent implements Runnable {

	private Integer domainSize,agents,assignment,id;
	private Mailer mailer;
	private Solution solution;
	private ArrayList<Integer> currentDomain;
	private VarTuple[] agentView;
	private HashMap<Integer, ConsTable> constraints;
	private Map<Integer, Explanation> explanations = new HashMap<Integer, Explanation>(); // change to list
	private PerformanceMeasures PMs = new PerformanceMeasures();
	
	public ABTAgent(int id, Mailer mailer, HashMap<Integer, ConsTable> constraints, Solution solution, int n, int d) {
		this.id = id;
		this.mailer = mailer;
		this.constraints = constraints;
		this.solution = solution;
		this.domainSize = d;
		this.agents = n;
		this.currentDomain = resetDomain();
		this.agentView = new VarTuple[agents];
	}
	
	@Override
	public void run() {
		Boolean end = false;
		CheckAgentView();
		while(!end) {
			Message inboxMsg = mailer.readOne(id);
			if(inboxMsg instanceof Info) {
				if(((Info)inboxMsg).getPM().getTimeStep() > PMs.getTimeStep()) {
					PMs = ((Info)inboxMsg).getPM();
				}
				ProcessInfo(inboxMsg);
			}else if(inboxMsg instanceof Nogood) {	
				ResolveConflict(inboxMsg);	
			}else if(inboxMsg instanceof Stop) {
				if(!((Stop)inboxMsg).getSolution()) {
					solution.notSolveable();
				}
				solution.set(id, assignment);
				solution.setPms(id, PMs);
				end = true;
			}
		}
		
	}
	
	public void ProcessInfo(Message inboxMsg) {
		VarTuple msgInfo = ((Info)inboxMsg).getAssignment();
		Update(msgInfo.getI(),msgInfo.getJ());
		currentDomain = resetDomain();
		CheckAgentView();	
	}
	
	public void CheckAgentView() {
			boolean myValue = ChooseValue();
			if(myValue) {
				PMs.updateNAss();
				for(int agendId : constraints.keySet()) {
					if(agendId>id) {
						mailer.send(agendId, (Message) new Info(id,assignment,new PerformanceMeasures(PMs)));
					}	
				}
			}else {
				Backtrack();
			}
	}
		
	public void Update(int id,int newAssign) {
		if(newAssign == -1) {
			agentView[id] = null;
			explanations.clear();
		}else {
			agentView[id] = new VarTuple(id,newAssign);
		}
		if(!Coherent(explanations,agentView)) {
			explanations.clear();
		}
	}
	
	public void ResolveConflict(Message inboxMsg) {
		if(Coherent(((Nogood)inboxMsg).getExplanation(),agentView) && assignment == ((Nogood)inboxMsg).getBadAssignment()) {
			if(((Nogood)inboxMsg).getPM().getTimeStep() > PMs.getTimeStep()) {
				PMs = ((Nogood)inboxMsg).getPM();
			}
			for (Integer key : ((Nogood)inboxMsg).getExplanation().keySet()) {
				for(ExplanationTuple ng : ((Nogood)inboxMsg).getExplanation().get(key).getTuples()) {
						Update(ng.getAgent(),ng.getAssignment());		
						if(!explanations.containsKey(key)) {
							explanations.put(key, new Explanation());
						}
						explanations.get(key).add(ng);
				}		
			}
			CheckAgentView();
		}else if(constraints.containsKey(((Nogood)inboxMsg).getAgentId()) && assignment == ((Nogood)inboxMsg).getBadAssignment()) {
			mailer.send(((Nogood)inboxMsg).getAgentId(), (Message) new Info(id,assignment,((Nogood)inboxMsg).getPM()));
		}
	}
	
	private void Backtrack() {
		ExplanationTuple rhs = findRhs();
		if (rhs == null ) {
			for(int i=0;i<agents;i++) {
				mailer.send(i, (Message) new Stop(false));
			}
		} else {
			Map<Integer, Explanation> newExplanation = findLhs(rhs.getAgent());
			int sendTo = rhs.getAgent();
			int badAssignment = rhs.getAssignment();
			Nogood nogood = new Nogood(id,badAssignment, newExplanation,new PerformanceMeasures(PMs));
			mailer.send(sendTo, nogood);
			Update(sendTo,-1);
			currentDomain = resetDomain();
			CheckAgentView();
		}
		
	}
	
	public Boolean ChooseValue() {
		Boolean conflict = false;
		while(!currentDomain.isEmpty()) {
			conflict = false;
			assignment = currentDomain.remove(0);
			for(int i : constraints.keySet()) { 
				if(i<id && agentView[i] != null) { 
					PMs.updateNCCs();
					if(!constraints.get(i).getTable()[agentView[i].getJ()][assignment]) {
						if(!explanations.containsKey(assignment)) {
							explanations.put(assignment, new Explanation());
						}
						explanations.get(assignment).add(new ExplanationTuple(agentView[i].getI(),agentView[i].getJ()));
						conflict = true;
					}
				}
			}
			if(!conflict) {
				return true;
			}
		}
		return false;
	}
	
	public boolean Coherent(Map<Integer, Explanation> explanations,VarTuple[] agentView) {
		for (VarTuple agentInfo : agentView) {
			if(agentInfo != null && constraints.containsKey(agentInfo.getI())) {
				for (Integer key : explanations.keySet()) {
					for(ExplanationTuple ng : explanations.get(key).getTuples()) {
						if(ng.getAgent() == agentInfo.getI() && ng.getAssignment() != agentInfo.getJ()) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private ExplanationTuple findRhs() {
		ExplanationTuple rhs = null;
		for (Integer key: explanations.keySet()) {
			for(ExplanationTuple ng : explanations.get(key).getTuples()) {
				if (rhs == null || ng.getAgent() > rhs.getAgent()) {

						rhs = ng;
					
				}
			}
		}	
		return rhs;
	}
	
	private Map<Integer, Explanation> findLhs(int rhsAgent) {
		Map<Integer, Explanation> newExplanations = new HashMap<Integer, Explanation>();
		for (Integer key : explanations.keySet()) {
			for(ExplanationTuple ng : explanations.get(key).getTuples()) {
				if(ng.getAgent() < rhsAgent) {
					if(!newExplanations.containsKey(key)) {
						newExplanations.put(key, new Explanation());
					}
					newExplanations.get(key).add(ng);
				}
			}
		}
		return newExplanations;
	}
	
	public ArrayList<Integer> resetDomain() {
		ArrayList<Integer> domain = new ArrayList<Integer>();
		for(int i=0;i<domainSize;i++) {
			domain.add(i);
		}
		return domain;	
	}
}
