package MainPack;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		// extract parameters
		int NAss=0,NCCs=0;
		int n = Integer.valueOf(args[0]).intValue();
		int d = Integer.valueOf(args[1]).intValue();
		double p1 = Double.valueOf(args[2]).doubleValue();

		for (double p2 = 0.1; p2 <= 0.9; p2 += 0.1) {
			//double p2 = 0.6;
			Generator gen = new Generator(n, d, p1, p2);
			
			for (int N = 0; N < 50; N++) {
				
				// generate and print CSP
				CSP csp = gen.generateDCSP();
				//csp.print();
				
				// solve by two algorithms
				Solution solution1 = ABT.search(n, d, csp);
				Solution solution2 = DisBT.search(n, d, csp);
				
				
				// verify that the solutions are identical
				System.out.println(check(solution1, solution2,csp));
				PerformanceMeasures PMs1 = solution1.getPMs();
				NAss+=PMs1.getNAss();
				NCCs+=PMs1.getNCCs();
			}
			
			// Performance Measures
			System.out.println();
			System.out.println("Average Performance Measures ABT : ");
			System.out.println("NAss = " + NAss/50 + ", NCCs = " + NCCs/50);
			System.out.println("Average Performance Measures BT : ");
			System.out.println("Ass = " + CPA.Ass/50 +", CCs = " + CPA.CCs/50);
			System.out.println();
			NAss=0;
			NCCs=0;
			CPA.Ass =0;
			CPA.CCs = 0;
			
		}
		System.out.println();
		
		
	}
	
	private static boolean check(Solution s1, Solution s2,CSP csp) {
		// validate ABT solution
		if(s1.isSolveable()) {
			for(VarTuple constrain : csp.getCons_tables().keySet()) {
				if(!csp.getCons_tables().get(constrain).getTable()[s1.getSolution()[constrain.getI()]][s1.getSolution()[constrain.getJ()]]) {
					s1.notSolveable();
				}
					
			}
		}
		return s1.isSolveable() == s2.isSolveable();
	}
}
