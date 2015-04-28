package edu.iastate.cs228.hw4;

/**
 *  
 * @author Alex Orman
 *
 */

import java.util.Comparator;

/**
 * 
 * This class compares two points p1 and p2 by polar angle with respect to a reference point.  
 * It is known that the reference point is not above either p1 or p2, and in the case that
 * either or both of p1 and p2 have the same y-coordinate, not to their right. 
 *
 */
public class PointComparator implements Comparator<Point>
{
	private Point referencePoint; 
	
	/**
	 * 
	 * @param p reference point
	 */
	public PointComparator(Point p)
	{
		referencePoint = p; 
	}
	
	/**
	 * Use cross product and dot product to implement this method.  Do not take square roots 
	 * or use trigonometric functions. See the PowerPoint notes on how to carry out cross and 
	 * dot products. 
	 * 
	 * Call comparePolarAngle() and compareDistance(). 
	 * 
	 * @param p1
	 * @param p2
	 * @return -1 if one of the following three conditions holds: 
	 *                a) p1 and referencePoint are the same point but p2 is a different point; 
	 *                
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
	 *          
	 *         1  if one of the following three conditions holds:
	 *                a) p2 and referencePoint are the same point but p1 is a different point; 
	 *                
	 *                b) neither p1 nor p2 equals referencePoint, and the polar angle of
	 *                   p1 with respect to referencePoint is greater than that of p2;
	 *                   
	 *                c) neither p1 nor p2 equals referencePoint, p1 and p2 have the same polar
	 *                   angle w.r.t. referencePoint, and p1 is further to referencePoint than p2. 
	 *                   
	 */
	public int compare(Point p1, Point p2)
	{
		if(p1.equals(p2)) return 0; //0 case
		if(p1.equals(referencePoint)) return -1; //-1a case
		if(p2.equals(referencePoint)) return 1; //1a case
		if(comparePolarAngle(p1,p2) == -1) return -1; //case -1b
		if(comparePolarAngle(p1,p2) == 1) return 1; //case 1b
		if((comparePolarAngle(p1,p2) == 0) && (compareDistance(p1,p2) == -1)) return -1; //case -1c
		return 1; //only could be case 1C at this point 
	}
	
	
	/**
	 * Compare the polar angles of two points p1 and p2 with respect to referencePoint.  Use 
	 * cross products.  Do not use trigonometric functions. 
	 * 
	 * Precondition:  p1 and p2 are distinct points. 
	 * 
	 * @param p1
	 * @param p2
	 * @return   -1  if p1 equals referencePoint or its polar angle with respect to referencePoint
	 *               is less than that of p2. 
	 *               p1 < p2 || p1 = ref
	 *               
	 *            0  if p1 and p2 have the same polar angle. 
	 *            
	 *            1  if p2 equals referencePoint or its polar angle with respect to referencePoint
	 *               is less than that of p1. 
	 *               p1 > p2
	 */
    public int comparePolarAngle(Point p1, Point p2) 
    {
//    	float res = (p1.getX() - referencePoint.getX()) * ((p2.getY() - referencePoint.getY())) - (p2.getX() - referencePoint.getX()) * (p1.getY() - referencePoint.getY());
    	float q0 = (p1.getX() - referencePoint.getX()) * (p2.getY() - referencePoint.getY());
    	float q1 = (p2.getX() - referencePoint.getX()) * (p1.getY() - referencePoint.getY());
    	
    	//for p1 angle < p2 angle return -1
    	//for p1 angle > p2 angle return 1
    	if(q0 < q1 || p2 == referencePoint) return 1;
    	if(q1 < q0 || p1 == referencePoint) return -1;
    	return 0; 
    }
    
    
    /**
     * Functions identically to the default, but uses a custom 3 point instead of ref point
     * @param p1
     * @param p2
     * @return
     */
    public int comparePolarAngle(Point p1, Point p2, Point ref) 
    {
    	float q0 = (p1.getX() - ref.getX()) * (p2.getY() - ref.getY());
    	float q1 = (p2.getX() - ref.getX()) * (p1.getY() - ref.getY());
    	
    	//for p1 angle < p2 angle return -1
    	//for p1 angle > p2 angle return 1
    	if(q0 < q1 || p2 == referencePoint) return 1;
    	if(q1 < q0 || p1 == referencePoint) return -1;
    	return 0; 
    }
    
    
    /**
     * Compare the distances of two points p1 and p2 to referencePoint.  Use dot products. 
     * Do not take square roots. 
     * 
     * @param p1
     * @param p2
     * @return   -1   if p1 is closer to referencePoint 
     *            0   if p1 and p2 are equidistant to referencePoint
     *            1   if p2 is closer to referencePoint
     */
    public int compareDistance(Point p1, Point p2)
    {
    	// Add 51 to each vector to remove any 0's from the equations.
//    	float d1 = (referencePoint.getX()+51) * (p1.getX()+51) + (referencePoint.getY()+51) * (p1.getY()+51);
//    	float d2 = (referencePoint.getX()+51) * (p2.getX()+51) + (referencePoint.getY()+51) * (p2.getY()+51);
//    	if(d1 > d2) return -1;
//    	if(d2 > d1) return 1;
//    	return 0; 
    	
    	//use d^2 = a^2 + b^2, compare d^2 values
    	int d1 = ((p1.getX()-referencePoint.getX()) * (p1.getX()-referencePoint.getX())) + ((p1.getY()-referencePoint.getY()) * (p1.getY()-referencePoint.getY()));
    	int d2 = ((p2.getX()-referencePoint.getX()) * (p2.getX()-referencePoint.getX())) + ((p2.getY()-referencePoint.getY()) * (p2.getY()-referencePoint.getY()));
    	if(d1 > d2) return 1;
    	if(d1 < d2) return -1;
    	return 0;
    }
}
