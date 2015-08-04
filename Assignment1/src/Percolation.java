/****************************************************************************
 *  by Ruizhu Yang
 *  date: 7/22/2015
 *  implement Percolation data structure using Weighted Quick Union-Find.
 *  Compilation:  javac Percolation.java
 *  Dependencies: WeightedQuickUnionUF.java 
 ****************************************************************************/

public class Percolation {
	//Weighted Quick Union-Find object as the percaltion system
	private WeightedQuickUnionUF WQU;
	//to avoid backwash cell. 
	private WeightedQuickUnionUF backwash;
	//a 2D array to store open status of each cell. If open, store false. Default value is false
	private boolean siteIsOpen[];
	//the size of grid
	private int N;	
	/**
	 * constructor
	 * @param creates an N-by-N grid
	 * @throws IllegalArgumentException
	 */
   public Percolation(int N) throws IllegalArgumentException { 
	   if (N <= 0) throw new IllegalArgumentException(); 
	   this.WQU = new WeightedQuickUnionUF(1+(N+1)*(N)+N); //row 0 for fake top, row N+1 for fake bottom
	   this.backwash = new WeightedQuickUnionUF(1+N*N+N); //row 0 for fake top, no fake bottom
	   this.siteIsOpen = new boolean[1+(N+1)*(N)+N]; // store open status for WQU
	   this.N = N;
	   for (int i = 1; i <= N; i++) {
		   WQU.union(0*N+1, 0*N+i);  
           backwash.union(0*N+1, 0*N+i);  
           siteIsOpen[0*N+i] = true;  
           WQU.union((N+1)*N+1, (N+1)*N+i);  
           siteIsOpen[(N+1)*N+i] = true;  
	   }
   } 
   /**
    * open the cell(i,j)
    * @param i the row number
    * @param j the col number
    * @throws IndexOutOfBoundsException
    */
   public void open(int i, int j) throws IndexOutOfBoundsException {   // open site (row i, column j) if it is not open already  
	   if (!this.isOpen(i, j)) {
		   this.siteIsOpen[i*N +j] = true;
		   //connect left and right
	       if (j != 1 && this.siteIsOpen[i*N + (j-1)]) { 
		       this.WQU.union(i * N + (j-1), N * i+j);
		       this.backwash.union(i * N + (j-1), N * i+j); 
	       }
	       if (j != N && this.siteIsOpen[i*N + (j+1)]) {
		       this.WQU.union(i * N + (j+1), N * i+j);
		       this.backwash.union(i * N + (j+1), N * i+j); 
	       }
	    // connect top and down
	       if (this.siteIsOpen[(i -1)*N +j]) { 
		       this.WQU.union((i-1) * N + j, N * i+j);
		       this.backwash.union((i-1) * N + j, N * i+j); 
	       }
	       if (this.siteIsOpen[(i +1)*N +j]) {
	    	 //to connect bottom cell to fake bottom. no matter which bottom cell connect to top, the system percolates
	    	   this.WQU.union((i+1) * N + j, N * i+j); 
	    	   //each bottom cell is independent to avoid backwash: avoid that when one cell connect to top, other bottom cell all connect to top
		       if (i != N) this.backwash.union((i+1) * N + j, N * i+j); 
	       }
	   }
   }
   /**
    * check if the cell is opened
    * @param i row number
    * @param j col number
    * @return if the cell is opened, return true, otherwise, return false
    * @throws IndexOutOfBoundsException
    */
   public boolean isOpen(int i, int j) throws IndexOutOfBoundsException {     // is site (row i, column j) open?
	   if (i < 1 || j < 1 || i > N || j > N) throw new IndexOutOfBoundsException();
	   return siteIsOpen[i*N +j];
   }
   /**
    * check if the cell is Full. A full cell is a cell which connects to the top of the percolation system
    * @param i row number
    * @param j col number
    * @return if full return true, otherwise return false
    * @throws IndexOutOfBoundsException
    */
   public boolean isFull(int i, int j) throws IndexOutOfBoundsException {     // is site (row i, column j) full? 
	   if (!isOpen(i, j)) return false;	  
	   return this.backwash.connected(1, N *i+j);
   }
   /**
    * check if the system percolates
    * @return if percolates return true, otherwise return false
    */
   public boolean percolates() {             // does the system percolate?
	   //no matter which bottom cell connects to the top, the system is percolated.
	   return this.WQU.connected(1, (N+1)*N+1);
   }
   public static void main(String[] args) {   // test client (optional)
	   
   }
}