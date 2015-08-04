/****************************************************************************
 *  by Ruizhu Yang
 *  date: 7/22/2015
 *  This program:
 *    -- reads grid size N from the first argument of input
 *    -- reads repeat time T from the second argument of input
 *    -- performs T independent experiments on N-by-N grid
 *    -- Estimates the percolation threshold 
*     -- Estimates 95% confidence interval for the percolation threshold
 *  Compilation:  javac PercolationStats.java
 *  Dependencies: Percolation.java 
 ****************************************************************************/
public class PercolationStats {
	   //repeat time to perform independent experiments
	   private int times;
	   //percolation threshold of each experiment
	   private double openFraction[ ];
	   /**
	    * constructor. Perform T independent experiments on an N-by-N grid
	    * @param N grid size
	    * @param T repeat time
	    * @throws IllegalArgumentException
	    */
	   public PercolationStats(int N, int T) throws IllegalArgumentException {     
		   if (N < 1 || T < 1) throw new IllegalArgumentException();
		   this.times = T;
		   openFraction = new double[T];
		   for (int t = 0; t < times; t++) {
			   if (N == 1) openFraction[t] = 1.0/ (N*N); 
			   else {
			       Percolation each = new Percolation(N);
			       int openCount = 0;
			       while (!each.percolates()){
				       int i = StdRandom.uniform(N) + 1;
				       int j = StdRandom.uniform(N) +1;
				       if (!each.isOpen(i, j)){
				     	   each.open(i, j);
				    	   openCount++;
				       }		   		   
			       }
			       openFraction[t] = ((double)openCount) / (N*N);
		       }
		   }
	   }
	   /**
	    * calculates mean value of percolation threshold
	    * @return mean value
	    */
	   public double mean() {                      
		   return StdStats.mean(openFraction);
	   }	   
	  /**
	   * @return the sample standard deviation
	   */
	   public double stddev() {                    
		   if (this.times == 1) return Double.NaN;
		   return StdStats.stddev(openFraction);
	   }	
	   /**
	    * @return low  endpoint of 95% confidence interval
	    */
	   public double confidenceLo() {               
		   return this.mean()- 1.96* this.stddev()/Math.sqrt(this.times);
	   }	   
	   /**
	    * @return high endpoint of 95% confidence interval
	    */
	   public double confidenceHi() {               
		   return this.mean()+ 1.96*this.stddev()/Math.sqrt(this.times);
	   }
	   
	   public static void main(String[] args) {    
		   StdOut.println("Please type in the N and T:");
		   int N = StdIn.readInt();
		   int T = StdIn.readInt();
		   System.out.println(N + " " + T);
		   Stopwatch sw = new Stopwatch();
		   PercolationStats  ps = new PercolationStats(N, T);
		   double timeCost = sw.elapsedTime();
		   StdOut.println("mean = " + ps.mean());
		   StdOut.println("stddev = " + ps.stddev());
		   StdOut.println("95% confidence interval = "+ps.confidenceLo()+", " + ps.confidenceHi());
		   StdOut.println("the time cost is " + timeCost + "seconds");
		   StdOut.close();
	   }
	};