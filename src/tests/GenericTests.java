package tests;

import edu.iastate.cs228.hw4.*;

public class GenericTests {

	public static void main(String[] args) {

		ConvexHull h = new ConvexHull(10);
		h.quickSort();
		System.out.println(h.toString(1));
	}

}
