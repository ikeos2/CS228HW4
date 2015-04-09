package tests;

import static org.junit.Assert.*;
import edu.iastate.cs228.hw4.*;

import org.junit.Test;

public class ConvexHullTest {

	@Test
	public void ConstructorTests() {
		ConvexHull t = new ConvexHull(20);
		t.quickSort();
		
		System.out.println(t.toString(1));
		
	}
	
	@Test
	public void lowestPointTest(){
		Point A = new Point(-1, 0);
		Point[] array ={new Point(0,0), A, new Point(0,0), new Point(0,0), new Point(0,0)};
		ConvexHull t = new ConvexHull(array);
		assertEquals(A,t.lowestPoint(1));
		
		Point B = new Point(-50, -50);
		array = new Point[]{new Point(-10,-10), B, new Point(4,2), new Point(-50,-49), new Point(50,50)};
		t = new ConvexHull(array);
		assertEquals(B,t.lowestPoint(1));
		
		Point C = new Point(25, -25);
		array = new Point[]{new Point(-10,-10), C, new Point(4,2), new Point(-5,-4), new Point(50,50)};
		t = new ConvexHull(array);
		assertEquals(C,t.lowestPoint(1));
	}

}
