package MainPack;

public class BackTrack implements Message {

	private CPA message;
	
	public BackTrack(Message message) {
		CPA.BTs++;
		this.message = ((CPA)message);
	}
	
	public CPA getCPA() {
		return message;
	}
}
