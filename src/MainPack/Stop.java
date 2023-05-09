package MainPack;


public class Stop implements Message{

	private Boolean Solution;
	
	public Stop(Boolean Solution) {
		this.Solution = Solution;
	}

	public Boolean getSolution() {
		return Solution;
	}
	
	
}
