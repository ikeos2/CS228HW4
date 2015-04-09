package tests;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import edu.iastate.cs228.hw4.*;

public class GenericTests {

	public static void main(String[] args) {

		ConvexHull h = new ConvexHull(20);
		h.GrahamScan();
		
		h.hullToFile();
		System.out.println(h.toString(1));
		//System.out.println(h.toString(2));
		System.out.println(h.toString());
		
		//System.out.println(h.testingLowestPoint);
		
		
	}

}
