import java.util.*;
import java.lang.Math;

class State {
    char[] board;
   

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
     
    }
    
 
    private ArrayList<char []> getSuccessors(char[] b) {
    	
    	ArrayList<char[]> succ = new ArrayList<char[]>();
    	
    	char [] newB;   	
    	for (int i = 0; i < b.length; i++) {
    		for (int k = 0; k < b.length; k++) {
    			
    			newB = b.clone();
    			int c = Character.getNumericValue(newB[i]);
    			
    			if (k != c) {
    				newB[i] = (char) ((char) k + '0');
    				succ.add(newB);
    			}
    		}
    		
    	}
    	
    	return succ;
    }
    
 private ArrayList<char []> getBestSuccessors(char [] b) {
    	
    	ArrayList<char []> bestSucc = new ArrayList<char []>();
    	int h = getHueristic(b);
    	int hSucc;
    	
    	ArrayList<char []> succ = getSuccessors(b);
    	for (int i = 0; i < succ.size(); i++) {
    		hSucc = getHueristic(succ.get(i));
    		if (hSucc == h) {
    			bestSucc.add(succ.get(i));
    		} else if (hSucc < h) {
    			h = hSucc;
    			bestSucc.clear();
    			bestSucc.add(succ.get(i));
    		}
    	}
    	
    	return bestSucc;
    }
    
    
    private int checkDiagonal(int column, char[] b) {
    	int c = 0;
    	for (int i = 1; i < b.length - column; i++) {
    		if ((b[column + i] == b[column] + i) || (b[column + i] == b[column] - i)) {
    			c++;
    		}
    	}
    	
    	return c;
    }
    
    private int getHueristic(char[] b) {
    	
    	int h = 0;
    	char check;
    
    	
    	//Check for queens in same row 
    	for (int i = 0; i < b.length; i++) {
    		check = b[i]; 
    		for (int j = i + 1; j < b.length; j++) {
    			if (check == b[j]) {
    				h++;
    			}
    		}
    	}
    	
    	//Check diagonal
    	for (int i = 0; i < b.length - 1; i++) {
    		h += checkDiagonal(i, b);
    	}
    	
    	return h;
    	
    }
    
    private void simulatedAnnealing(int option, int iteration, int seed) {
		char [] b;
		char [] succ;
		int ctr = 0;
		double temp = 100;
		double aRate = 0.95;
		int delta;
		Random rng = new Random();
		if (seed != -1) {
			rng.setSeed(seed);
		} 
		
		b = this.board;
		if (iteration == 0) {
			System.out.println(ctr + ":" + String.valueOf(b) + " " + getHueristic(b));
			if (getHueristic(b) == 0) {
    			System.out.println("Solved");
    			return;
    		}
			return;
		}

		while (ctr <= iteration) {
			
			System.out.println(ctr + ":" + String.valueOf(b) + " " + getHueristic(b));
			if (getHueristic(b) == 0) {
    			System.out.println("Solved");
    			return;
    		}
			
			int index = rng.nextInt(7); //Used to find new random column to adjust
			int value = rng.nextInt(7); //Used to find new random queen position
			double prob = rng.nextDouble(); //Probability of acceptance
			
			//Find new random close successor 
			succ = b.clone();
			succ[index] = (char) ((char)value + '0'); 
			
			delta = getHueristic(succ) - getHueristic(b);
			
			
			//Always accept succ with better h score
			if (delta < 0) {
				b = succ;
			} else {
				//Accept bad moves based on prob
				if (prob < Math.pow(temp, delta) ) {
					b = succ;
				} 
				
			}
			
			ctr++;
			temp *= aRate;
			
		}
		
    	
    	return;
    }
    
    public void printState(int option, int iteration, int seed) {

    	
    	//Print Heuristic cost of a given board.
    	if (option == 1) {
    		System.out.println(getHueristic(this.board));
    		return;
    	}
    	
    	//FIND BEST SUCCESSORS
    	if (option == 2) {
    		
    		ArrayList<char []> bestSuccessors = getBestSuccessors(this.board);
    		
    		if (!bestSuccessors.isEmpty()) {
    			
    			for (int i = 0; i < bestSuccessors.size(); i++) {
    				System.out.println(String.valueOf(bestSuccessors.get(i)));
    			}
    			//Print H of successors
    			System.out.println(getHueristic(bestSuccessors.get(0)));
    		}
    		
    		return;

    	}
    	
    	//HILL CLIMB
    	if (option == 3) {
    		ArrayList<char []> succ = new ArrayList<char []>();
    		char [] b;
    		int ctr = 0;
    		Random rng = new Random();
    		if (seed != -1) {
    			rng.setSeed(seed);
    		} 
    		
    		b = this.board;
    		if (iteration == 0) {
    			System.out.println(ctr + ":" + String.valueOf(b) + " " + getHueristic(b));
    			if (getHueristic(b) == 0) {
        			System.out.println("Solved");
        			return;
        		}
    			return;
    		}
    
    		
    		//Else Iterate
    		while (ctr <= iteration) {
    			
    			//Print Current State
    			System.out.println(ctr + ":" + String.valueOf(b) + " " + getHueristic(b));
    			if (getHueristic(b) == 0) {
        			System.out.println("Solved");
        			return;
        		}
    			
    			//Get successors, set new b
    			succ = getBestSuccessors(b);
    			int r = rng.nextInt(succ.size());
    			b = succ.get(r);
    			ctr++;
    		}
    		
    		
    		
    	}
    	
    	//PERFORM HILLCLIMB BUT WITH FIRST SUCCESSOR WITH LOWER H score
    	if (option == 4) {
    		ArrayList<char []> succ = new ArrayList<char []>();
    		char [] b;
    		int ctr = 0;
    		int lastH;
    	
    		b = this.board;
    		if (iteration == 0) {
    			System.out.println(ctr + ":" + String.valueOf(b) + " " + getHueristic(b));
    			if (getHueristic(b) == 0) {
        			System.out.println("Solved");
        			return;
        		}
    			
    			return;
    		}
    	
    		lastH = getHueristic(b); //To track local opt
    		
    		//Else Iterate
    		while (ctr <= iteration) {
    			lastH = getHueristic(b);
    			
    			//Print Current State
    			System.out.println(ctr + ":" + String.valueOf(b) + " " + getHueristic(b));
    			if (getHueristic(b) == 0) {
        			System.out.println("Solved");
        			return;
        		}
    			
    			//Get successors, set new b
    			succ = getSuccessors(b);
    			for (int i = 0; i < succ.size(); i++) {
    				if (getHueristic(succ.get(i)) < getHueristic(b)) {
    					
    					b = succ.get(i);
    					break;
    				}
    			}
    			
    			if (lastH == getHueristic(b)) {
    				System.out.println("Local optimum");
    				return;
    			}
    			
    			ctr++;
    		}
    		
    	}
    	
    	
    	//SIMULATED ANNEALLING
    	if (option == 5) {
    		simulatedAnnealing(option, iteration, seed);
    		
    	}
    	
    	
    }


    
}

public class EightQueen {
    public static void main(String args[]) {
        if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int option = flag / 100;
        int iteration = flag % 100;
        char[] board = new char[8];
        int seed = -1;
        int board_index = -1;

        if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
            board_index = 1;
        } else if (args.length == 3 && (option == 3 || option == 5)) {
            seed = Integer.valueOf(args[1]);
            board_index = 2;
        } else {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        if (board_index == -1) return;
        for (int i = 0; i < 8; i++) {
            board[i] = args[board_index].charAt(i);
            int pos = board[i] - '0';
            if (pos < 0 || pos > 7) {
                System.out.println("Invalid input: queen position(s)");
                return;
            }
        }

        State init = new State(board);
        init.printState(option, iteration, seed);
    }
}