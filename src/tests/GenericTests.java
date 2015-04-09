package tests;

import edu.iastate.cs228.hw4.*;

public class GenericTests {

	public static void main(String[] args) {
		ConvexHull h = new ConvexHull(10);

		int n = 10;
		Point[] array = new Point[n];
		boolean f = false;
		for(int i = 0; i < n; i++){
			if(f){
				array[i] = new Point(i,i);
				f = false;
			} else {
				array[i] = null;
				f = true;
			}
		}
		for(Point E : array) System.out.println(E);
		System.out.println();
		h.resize(array);
		for(Point E : array) System.out.println(E);
		System.out.println("\n\n\n\n\n");
		
		
		h.GrahamScan();
		
		h.hullToFile();
		h.pointsToFile();
		h.pointsScannedToFile();
		System.out.println(h.toString(1));
		//System.out.println(h.toString(2));
		System.out.println(h.toString());
		
		//System.out.println(h.testingLowestPoint);
		
		
	}

}
