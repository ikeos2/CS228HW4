package tests;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import edu.iastate.cs228.hw4.*;

public class GenericTests {

	public static void main(String[] args) {

		ConvexHull h = new ConvexHull(10);
		h.quickSort();
		System.out.println(h.toString(1));
		System.out.println("End test 1============\n\n");
		
		try {
			h = new ConvexHull("test1.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(h.toString(1));
	}

}
