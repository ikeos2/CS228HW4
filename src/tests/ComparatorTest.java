package tests;

import static org.junit.Assert.*;
import edu.iastate.cs228.hw4.*;
import org.junit.Test;

/**
 * 
 * @author Alex Orman
 *
 */

public class ComparatorTest {

	@Test
	public void DistanceTest1() {
		Point a = new Point(1,1);
		Point b = new Point(1,2);
		Point c = new Point(1,3);
		
		PointComparator test = new PointComparator(a);
		assertEquals(-1, test.compareDistance(b, c));
		
	}
	
	@Test
	public void DistanceTest2() {
		Point a = new Point(0,0);
		Point b = new Point(1,2);
		Point c = new Point(2,3);
		
		PointComparator test = new PointComparator(a);
		assertEquals(-1, test.compareDistance(b, c));
		
	}
	
	@Test
	public void DistanceTest3() {
		Point a = new Point(0-50,0-50);
		Point b = new Point(1-50,2-50);
		Point c = new Point(2-50,3-50);
		
		PointComparator test = new PointComparator(a);
		assertEquals(-1, test.compareDistance(b, c));
		
	}
	
	@Test
	public void DistanceTest4() {
		Point a = new Point(0,0);
		Point b = new Point(1,0);
		Point c = new Point(2,0);
		
		PointComparator test = new PointComparator(a);
		assertEquals(-1, test.compareDistance(b, c));
		
	}
	
	@Test
	public void AngleTest1(){
		Point a = new Point(0,0);
		Point b = new Point(1,2);
		Point c = new Point(2,3);
		
		PointComparator test = new PointComparator(a);
		assertEquals(1, test.comparePolarAngle(b, c));
		assertEquals(-1, test.comparePolarAngle(c, b));
	}

	@Test
	public void AngleTest2(){
		Point a = new Point(0,0);
		Point c = new Point(1,1);
		Point b = new Point(2,2);
		
		PointComparator test = new PointComparator(a);
		assertEquals(0, test.comparePolarAngle(b, c));
		assertEquals(0, test.comparePolarAngle(c, b));	
	}
	

	@Test
	public void AngleTest3(){
		Point a = new Point(10,10);
		Point b = new Point(-4,25);
		Point c = new Point(25,15);
		
		PointComparator test = new PointComparator(a);
		assertEquals(1, test.comparePolarAngle(b, c));
		assertEquals(-1, test.comparePolarAngle(c, b));	
	}
	
	/**
	 * @return -1 if one of the following three conditions holds: 
	 *                a) p1 and referencePoint are the same point but p2 is a different point; 
	 *                b) neither p1 nor p2 equals referencePoint, and the polar angle of 
	 *                   p1 with respect to referencePoint is less than that of p2;
	 *                   
	 *                   p2 angle > p1 angle 
	 *                
	 *                c) neither p1 nor p2 equals referencePoint, p1 and p2 have the same polar 
	 *                   angle w.r.t. referencePoint, and p1 is closer to referencePoint than p2.
	 *                   
	 *                   p1 angle = p2 angle && p1 is closest
	 *                    
	 *         0  if p1 and p2 are the same point  
	 *         1  if one of the following three conditions holds:
	 *                a) p2 and referencePoint are the same point but p1 is a different point; 
	 *                b) neither p1 nor p2 equals referencePoint, and the polar angle of
	 *                   p1 with respect to referencePoint is greater than that of p2;
	 *                c) neither p1 nor p2 equals referencePoint, p1 and p2 have the same polar
	 *                   angle w.r.t. referencePoint, and p1 is further to referencePoint than p2.
	**/
	@Test
	public void CompareTest1(){
		Point a = new Point(0,0);
		Point b = new Point(1,1);
		Point c = new Point(2,2);
		
		PointComparator test = new PointComparator(a);
		assertEquals(0,test.compare(a, a));
		assertEquals(0,test.compare(b, b));
		assertEquals(0,test.compare(c, c));
		assertEquals(1, test.compare(c,a));
		assertEquals(-1, test.compare(a, c));

		assertEquals(-1, test.compare(b, c));
		assertEquals(1, test.compare(c, b));
	}
	
	@Test
	public void CompareTest2(){
		Point a = new Point(10,10);
		Point b = new Point(12,11);
		Point c = new Point(15,15);
		
		PointComparator test = new PointComparator(a);
		assertEquals(-1, test.compare(b, c));
	}
	
	@Test
	public void CompareTest3(){
		Point a = new Point(10,10);
		Point b = new Point(12,11);
		Point c = new Point(15,15);
		
		PointComparator test = new PointComparator(a);
		assertEquals(1, test.compare(c, b));
	}
	
	

}
