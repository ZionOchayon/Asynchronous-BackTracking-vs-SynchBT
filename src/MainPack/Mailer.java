package MainPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mailer {

	private int count = 0,agents;
	private int[] endCheck;
	
	public Mailer(int agents) {
		this.agents = agents;
		this.endCheck = new int[agents];
		for(int i = 0;i<agents;i++) {
			endCheck[i] = 0;
		}
	}
	// maps between agents and their mailboxes
	private HashMap<Integer, List<Message>> map = new HashMap<>();
	
	// send message @m to agent @receiver
	public void send(int receiver, Message m) {
	
		//System.out.println("MESSAGE");
		
		count = getCount() + 1;
		
		List<Message> l = map.get(receiver);
		
		synchronized (l) {
			l.add(m);
		}
	}

	// agent @receiver reads the first message from its mail box
	public Message readOne(int receiver) {
		
		List<Message> l = map.get(receiver);
		if (l.isEmpty()) {
				endCheck[receiver]++;
				if(echeck()) {
					for(int i=0;i<agents;i++) {
						send(i, (Message) new Stop(true));
					}
				}
			return null;
		}
		
		synchronized (l) {
			endCheck[receiver] = 0;
			Message m = l.get(0);
			l.remove(0);
			return m;
		}
	}
	
	public boolean echeck() {
		for(int i : endCheck) {
			if(i < 10000) {
				return false;
			}
		}
		return true;
	}
	
	// only used for initialization
	public void put(int i) {
		List<Message> l= new ArrayList<Message>();
		this.map.put(i, l);
	}

	public int getCount() {
		return count;
	}
}
