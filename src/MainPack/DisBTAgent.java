package MainPack;

import java.util.ArrayList;
import java.util.HashMap;

public class DisBTAgent implements Runnable {

	private Solution solution;
	private int domainSize,agents,id,assignment;
	private Mailer mailer;
	private HashMap<Integer, ConsTable> constraints;
	private ArrayList<Integer> currentDomain;
	private ArrayList<VarTuple> explanations;
	
	public DisBTAgent(int id, Mailer mailer, HashMap<Integer, ConsTable> constraints, Solution solution, int n, int d) {
		this.id = id;
		this.mailer = mailer;
		this.constraints = constraints;
		this.solution = solution;
		this.domainSize = d;
		this.agents = n;
		this.currentDomain = resetDomain();
		this.explanations = new ArrayList<VarTuple>();
	}

	@Override
	public void run() {
		if(id == 0) {
			assignment = currentDomain.remove(0);
			Message message = new CPA(new VarTuple(id,assignment),agents);
			mailer.send(id+1, message);
		}
		
		while(true) {
			Message inboxMsg = mailer.readOne(id);
			if(inboxMsg != null) {
				if(inboxMsg instanceof CPA) {
					currentDomain = resetDomain();
					findAssignmentAndSendMassage(inboxMsg);				
				} else if(inboxMsg instanceof BackTrack) {
					if(currentDomain.size() > 0){				
						findAssignmentAndSendMassage(inboxMsg);
					} else if(id != 0) {
						((BackTrack) inboxMsg).getCPA().removeAssignmentsToId(id-1);
						mailer.send(id-1, (Message) new BackTrack(((BackTrack) inboxMsg).getCPA()));
					} else {
						for(int i=0;i<agents;i++) {
							mailer.send(i, (Message) new Message.NoSolution());
						}
						solution.notSolveable();
					}	
				} else if(inboxMsg instanceof Message.Solution || inboxMsg instanceof Message.NoSolution) {
					solution.set(id, assignment);

					break;
				}
				
			}
		}	
	}
	
	public ArrayList<Integer> resetDomain() {
		ArrayList<Integer> domain = new ArrayList<Integer>();
		for(int i=0;i<domainSize;i++) {
			domain.add(i);
		}
		return domain;	
	}
	
	private void findAssignmentAndSendMassage(Message inboxMsg) {
		
		Boolean conflict = true;	
		VarTuple agentView = null;

		if(inboxMsg instanceof BackTrack) {
			inboxMsg = ((BackTrack) inboxMsg).getCPA();
		}
		
		while(!currentDomain.isEmpty()) {
			conflict = false;
			assignment = currentDomain.remove(0);
			for(Integer i : constraints.keySet()) {
				if(i<id) {
					CPA.CCs++;	
					agentView = ((CPA) inboxMsg).getAssignment(i);
					if(!constraints.get(i).getTable()[agentView.getJ()][assignment]) {
						explanations.add(agentView);
						conflict = true;
					}
				}
			}
			
			if(!conflict) {
				((CPA) inboxMsg).addAssignment(id,new VarTuple(id,assignment));
				if(id != agents-1) {
					mailer.send(id+1, inboxMsg);
				}else {
					for(int i=0;i<agents;i++) {
						mailer.send(i, (Message) new Message.Solution());
					}
				}
				break;
			}
		}

		if(currentDomain.isEmpty() && conflict) {
			((CPA) inboxMsg).removeAssignmentsToId(id-1);
			mailer.send(id-1, (Message) new BackTrack(inboxMsg));
		}
	}
		
}


