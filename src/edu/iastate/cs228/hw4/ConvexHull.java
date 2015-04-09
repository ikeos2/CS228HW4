package edu.iastate.cs228.hw4;

/**
 *  
 * @author Alex Orman
 * with help from
 * http://www.java2s.com/Code/Java/Collections-Data-Structure/Quicksortimplementationforsortingarrays.htm
 * http://www.geeksforgeeks.org/convex-hull-set-2-graham-scan/
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * This class implements Graham's scan that constructs the convex hull of a
 * finite set of points.
 *
 */

@SuppressWarnings("unused")
public class ConvexHull {
	// ---------------
	// Data Structures
	// ---------------

	/**
	 * The array points[] holds an input set of Points, which may be randomly
	 * generated or input from a file. Duplicates may appear.
	 */
	private Point[] points;
	private int numPoints; // size of points[]

	/**
	 * Lowest point from points[]; and in case of a tie, the leftmost one of all
	 * such points. To be set by the private method lowestPoint().
	 */
	private Point lowestPoint;

	/**
	 * This array stores the same set of points from points[] with all
	 * duplicates removed.
	 */
	private Point[] pointsNoDuplicate;
	private int numDistinctPoints; // size of pointsNoDuplicate[]

	/**
	 * Points on which Graham's scan is performed. They are copied from
	 * pointsNoDuplicate[] with some points removed. More specifically, if
	 * multiple points from the array pointsNoDuplicate[] have the same polar
	 * angle with respect to lowestPoint, only the one furthest away from
	 * lowestPoint is included.
	 */
	private Point[] pointsToScan;
	private int numPointsToScan; // size of pointsToScan[]

	/**
	 * Vertices of the convex hull in counterclockwise order are stored in the
	 * array hullVertices[], with hullVertices[0] storing lowestPoint.
	 */
	private Point[] hullVertices;
	private int numHullVertices; // number of vertices on the convex hull

	/**
	 * Stack used by Grahma's scan to store the vertices of the convex hull of
	 * the points scanned so far. At the end of the scan, it stores the hull
	 * vertices in the counterclockwise order.
	 */
	private PureStack<Point> vertexStack;

	// ------------
	// Constructors
	// ------------

	/**
	 * Generate n random points within the box range [-50, 50] x [-50, 50].
	 * Duplicates are allowed. Store the points in the private array points[].
	 * 
	 * @param n
	 *            >= 1; otherwise, exception thrown.
	 */
	public ConvexHull(int n) throws IllegalArgumentException {
		if (n < 1)
			throw new IllegalArgumentException();

		// Create a random number generator
		Random rand = new Random();

		// resize the array to fit our numbers
		points = new Point[n];

		// set each element in the array to a point with random coordinates.
		for (int i = 0; i < n; i++) {
			points[i] = new Point(50 - rand.nextInt(101),
					50 - rand.nextInt(101));
		}

		numPoints = n;
	}
	
	
	/**
	 * Creates a convex hull based on given input, mostly for debugging purposes
	 * @param given array of points to be used
	 */
	public ConvexHull(Point[] given) {
		points = given;
		numPoints = given.length;
	}

	/**
	 * Read integers from an input file. Every pair of integers represent the x-
	 * and y-coordinates of a point. Generate the points and store them in the
	 * private array points[]. The total number of integers in the file must be
	 * even.
	 * 
	 * You may declare a Scanner object and call its methods such as hasNext(),
	 * hasNextInt() and nextInt(). An ArrayList may be used to store the input
	 * integers as they are read in from the file.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException
	 *             when the input file contains an odd number of integers
	 */
	@SuppressWarnings("resource")
	public ConvexHull(String inputFileName) throws FileNotFoundException,
			InputMismatchException {
		// scan in the file, parse into point objects.
		File file = new File(inputFileName);
		// check for file
		if (!file.exists())
			throw new FileNotFoundException();
		Scanner in = new Scanner(file);

		// grab up all the ints
		ArrayList<Integer> temp = new ArrayList<Integer>();
		while (in.hasNextInt()) {
			temp.add(in.nextInt());
		}

		// ensure we have enough ints
		if (temp.size() % 2 != 0)
			throw new InputMismatchException();

		points = new Point[temp.size() / 2];

		int j = 0;
		for (int i = 0; i < temp.size() / 2; i += 2) {
			points[j] = new Point(temp.get(i), temp.get(i + 1));
			j++;
		}

		numPoints = points.length;
		in.close();

	}

	// -------------
	// Graham's scan
	// -------------

	/**
	 * This method carries out Graham's scan in several steps below:
	 * 
	 * 1) Call the private method lowestPoint() to find the lowest point from
	 * the input point set and store it in the variable lowestPoint.
	 * 
	 * 2) Call the private method setUpScan() to sort all points by polar angle
	 * with respect to lowestPoint. After elimination of all duplicates in
	 * points[], the points are stored in pointsNoDuplicate[]. Next, for
	 * multiple points having the same polar angle with respect to lowestPoint,
	 * keep only the one furthest from lowestPoint. The points after the second
	 * round of elimination are stored in the array pointsToScan[].
	 * 
	 * 3) Perform Graham's scan on the points in the array pointsToScan[]. To
	 * initialize the scan, push pointsToScan[0] and pointsToScan[1] onto
	 * vertexStack.
	 * 
	 * 4) As the scan terminates, vertexStack holds the vertices of the convex
	 * hull. Pop the vertices out of the stack and add them to the array
	 * hullVertices[], starting at index numHullVertices - 1, and decreasing the
	 * index toward 0. Set numHullVertices.
	 * 
	 * Two special cases below must be handled:
	 * 
	 * 1) The array pointsToScan[] could contain just one point, in which case
	 * the convex hull is the point itself.
	 * 
	 * 2) Or it could contain two points, in which case the hull is the line
	 * segment connecting them.
	 */
	public void GrahamScan() {
		vertexStack = new ArrayBasedStack<Point>();
		lowestPoint();
		setUpScan();
		
		if (pointsToScan.length == 1) {
			hullVertices = pointsToScan;
			numHullVertices = 1;
		}
		if (pointsToScan.length == 2) {
			hullVertices = pointsToScan;
			numHullVertices = 2;
		}

		// Graham's scan, as implemented at
		// http://www.geeksforgeeks.org/convex-hull-set-2-graham-scan/
		// push top 3 points to stack
		vertexStack.push(pointsToScan[0]);
		vertexStack.push(pointsToScan[1]);
		vertexStack.push(pointsToScan[2]);

		for (int i = 3; i < pointsToScan.length; i++) {
			// Keep removing top while the angle formed by points next-to-top,
			// top, and points[i] makes a non-left turn
			// while(angle is
			//TODO: change this to use comparator
			while (orientation(nextToTop(), vertexStack.peek(), points[i]) != 2)
				vertexStack.pop();
			vertexStack.push(points[i]);
		}
		//****End Grahams Scan
		
		//push all the Points back into our hullVertices array
		hullVertices = new Point[vertexStack.size()];
		for(int i = vertexStack.size() - 1; i > 0; i--){
			hullVertices[i] = vertexStack.pop();
		}
		
		//for some reason this particular point gets lost
		hullVertices[0] = lowestPoint;
		pointsToScan[0] = lowestPoint;
		numHullVertices = hullVertices.length;
		//quickSort();
	}

	// ------------------------------------------------------------
	// toString() and Files for Convex Hull Plotting in Mathematica
	// ------------------------------------------------------------

	/**
	 * The string displays the convex hull with vertices in counter clockwise
	 * order starting at lowestPoint. When printed out, it will list five points
	 * per line with three blanks in between. Every point appears in the format
	 * "(x, y)".
	 * 
	 * For illustration, the convex hull example in the project description will
	 * have its toString() generate the output below:
	 * 
	 * (-7, -10) (0, -10) (10, 5) (0, 8) (-10, 0)
	 * 
	 * lowestPoint is listed only ONCE.
	 */
	public String toString() {
		String out = "";
		int count = 0;
		while(count < numHullVertices){
			for(int i = 0; i < 5; i++){
				out += (hullVertices[count] + "   ");
				//incase we have a power not of 5
				count++;
				if(count >= numHullVertices) return out;
			}
			out+=("\n"); //new line every 5
		}

		return out;
	}

	/**
	 * Basically functions a static comparator
	 *
	 * @param p point 1
	 * @param q point 2
	 * @param r ref point
	 * @return 
	 */
	// from http://www.geeksforgeeks.org/convex-hull-set-2-graham-scan/
	public int orientation(Point p, Point q, Point r) {
		int val = (q.getY() - p.getY()) * (r.getX() - q.getX())
				- (q.getX() - p.getX()) * (r.getY() - q.getY());

		if (val == 0)
			return 0; // colinear
		return (val > 0) ? 1 : 2; // clock or counterclock wise
	}

	/**
	 * Stores result of pop, then peek, pushes original pop back on
	 * @return the point below top
	 */
	public Point nextToTop(){
		Point tmp = vertexStack.pop();
		Point val = vertexStack.peek();
		vertexStack.push(tmp);
		return val;
	}
	
	public String toString(int i) {
		// for DEBUG
		String out = "";

		if (i == 1) {
			for (Point E : points) {
				System.out.print(E + " ");
			}
		}
		if(i == 2){
			for(Point E : hullVertices)
				System.out.print(E + " ");
		}

		return out;
	}

	/**
	 * For plotting in Mathematica.
	 * 
	 * Writes to the file "hull.txt" the vertices of the constructed convex hull
	 * in counterclockwise order for rendering in Mathematica. These vertices
	 * are in the array hullVertices[], starting at lowestPoint. Every line in
	 * the file displays the x and y coordinates of only one point. Write the
	 * coordinates of lowestPoint again to end the file.
	 * 
	 * For instance, the file "hull.txt" generated for the convex hull example
	 * in the project description will have the following content:
	 * 
	 * -7 -10 0 -10 10 5 0 8 -10 0 -7 -10
	 * 
	 * Note that lowestPoint (-7, -10) has its coordinates listed in the first
	 * and last lines. This is for Mathematica to plot the hull as a polygon
	 * rather than one missing the edge connecting (-10, 0) and (-7, -10).
	 * 
	 * Called only after GrahamScan().
	 * 
	 * 
	 * @throws IllegalStateException
	 *             if hullVertices[] has not been populated (i.e., the convex
	 *             hull has not been constructed)
	 */
	public void hullToFile() throws IllegalStateException {
		if(numHullVertices < 1) throw new IllegalStateException();
		
		String out = "";
		
		for(Point E : hullVertices) out += (E.getX() + " " + E.getY() + " ");
		//add the lowest point to the end
		out += " " + lowestPoint.getX() + " " + lowestPoint.getY();

		toFile(out, "hull.txt");
	}

	/**
	 * For plotting in Mathematica.
	 * 
	 * Writes to the file "points.txt" the points stored in the array
	 * pointsNoDuplicate[]. The format is the same as required for the method
	 * hullToFile(), except that the coordinates of lowestPoint appear only
	 * once.
	 * 
	 * Called only after setUpScan() or GrahamScan().
	 * 
	 * @throws IllegalStateException
	 *             if pointsNoDuplicate[] has not been populated.
	 */
	public void pointsToFile() throws IllegalStateException {
		if(numDistinctPoints < 1) throw new IllegalStateException();
		
		String out = "";
		
		
		for(Point E : pointsNoDuplicate) out += (E.getX() + " " + E.getY() + " ");
		
		toFile(out, "points.txt");
	}

	/**
	 * Also implement this method for testing purpose.
	 * 
	 * Writes to the file "pointsScanned.txt" the points stored in the array
	 * pointsToScan[]. The format is the same as required for the method
	 * pointsToFile().
	 * 
	 * Called only after setUpScan() or GrahamScan().
	 * 
	 * @throws IllegalStateException
	 *             if pointsToScan[] has not been populated.
	 */
	public void pointsScannedToFile() throws IllegalStateException {
		if(pointsToScan.length < 1) throw new IllegalStateException();
		
		String out = "";
		
		//for(Point E : pointsToScan) out += (E.getX() + " " + E.getY() + " ");
		for(int i = 0; i < pointsToScan.length; i++){
			if(pointsToScan[i] == null) break;
			out += pointsToScan[i].getX() + " " + pointsToScan[i].getY() + " ";
		}
		
		toFile(out, "pointsScanned.txt");
	}

	// ---------------
	// Private Methods
	// ---------------

	/**
	 * Find the point in the array points[] that has the smallest y-coordinate.
	 * In case of a tie, pick the point with the smallest x-coordinate. Set the
	 * variable lowestPoint to the found point.
	 * 
	 * Multiple elements from points[] could coincide at the lowestPoint (i.e.,
	 * they are the same point). This situation could happen, though with a very
	 * small chance. In this case, any of them can be lowestPoint.
	 * 
	 * Ought to be private, but is made public for testing convenience.
	 */
	public void lowestPoint() {
		Point small = points[0];
		for (int i = 1; i < points.length; i++) {
			if (points[i].getY() < small.getY())
				small = points[i]; // if we find a lower point, set it

			else if (points[i].getY() == small.getY()) { // if we find an equal
															// point, find a
															// smaller x
				if (points[i].getX() < small.getX())
					small = points[i];
			}

		}

		lowestPoint = small;
	}
	
	/**
	 * Debugging lowestPoint function
	 * @param i
	 * @return
	 */
	public Point lowestPoint(int i) {
		lowestPoint();
		return lowestPoint;
	}

	/**
	 * Call quickSort() on points[]. After the sorting, duplicates in points[]
	 * will appear next to each other, with those equal to lowestPoint at the
	 * beginning of the array.
	 * 
	 * Copy the points from points[] into the array pointsNoDuplicate[],
	 * eliminating all duplicates. Update numDistinctPoints.
	 * 
	 * Copy the points from pointsNoDuplicate[] into the array pointsToScan[]
	 * and eliminate some as follows. If multiple points have the same polar
	 * angle, eliminate all but the one that is the furthest from lowestPoint.
	 * Update numPointsToScan.
	 * 
	 * Ought to be private, but is made public for testing convenience.
	 *
	 */
	public void setUpScan() {
		quickSort();

		// eliminate duplicates
		// allocated worst case space for array
		pointsNoDuplicate = new Point[points.length];
		pointsNoDuplicate[0] = points[0];
		for (int i = 1; i < points.length; i++) {
			if (points[i].equals(points[i - 1])) {
				// do nothing and skip this
			} else {
				pointsNoDuplicate[i] = points[i];
			}
		}
		numDistinctPoints = pointsNoDuplicate.length;

		resize(pointsNoDuplicate);

		PointComparator t = new PointComparator(lowestPoint);
		// allocate worst case space for array
		pointsToScan = new Point[pointsNoDuplicate.length];
		// eliminate duplicate angle points
		for (int i = 0; i < pointsNoDuplicate.length - 1; i++) {
			if (t.comparePolarAngle(pointsNoDuplicate[i], pointsNoDuplicate[i + 1]) == 0) {
				// The angle is the same, keep only the one with the greater
				// distance
				if (t.compareDistance(pointsNoDuplicate[i], pointsNoDuplicate[i + 1]) < 0) {
					// point 1 is closer, keep point 2
					pointsToScan[i] = pointsNoDuplicate[i + 1];
					i++;
				} else {
					// point 2 is closer, keep point 1
					pointsToScan[i] = pointsNoDuplicate[i];
					i++;
				}
			} else {
				// Different angles
				pointsToScan[i] = pointsNoDuplicate[i];
			}
		}
		resize(pointsToScan);
		
		//pointsNoDuplicate[0] = lowestPoint;
		numPointsToScan = pointsToScan.length;
	}

	/**
	 * Sort the array points[] in the increasing order of polar angle with
	 * respect to lowestPoint. Use quickSort. Construct an object of the
	 * pointComparator class with lowestPoint as the argument for point
	 * comparison.
	 * 
	 * Ought to be private, but is made public for testing convenience.
	 */
	public void quickSort() {
		int left = 0;
		int right = points.length-1;
		quicksort(left, right);

	}
	public void quicksort(int left, int right){
		int index = partition(left,right);
		if(left < index - 1)
			quicksort(left,index-1);
		if(index < right)
			quicksort(index, right);

	}


	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 *            starting index of the subarray
	 * @param last
	 *            ending index of the subarray
	 */
	private void quickSortRec(int first, int last) {
		quicksort( first,  last);
	}

	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int left, int right) {
		int i = left, j = right;
		PointComparator t = new PointComparator(lowestPoint);
		Point tmp;
		Point pivot = points[(left+right)/2];
		
		while(i <= j){
			while(t.compare(points[i], pivot) == -1)
				i++;
			while(t.compare(points[j], pivot) == 1)
				j--;
			if(i <= j){
				tmp = points[i];
				points[i] = points[j];
				points[j] = tmp;
				i++;
				j--;
			}
				
		}
		
		
		return i;
	}

	public void resize(Point[] array) {
		// TODO Fix this, doesn't resize arrays
		// removes any null values from the list, shrinks to fit only real values
		if (array == null)
			return;

		 ArrayList<Point> list = new ArrayList<Point>();
		 for(Point E : array){
			 if( E != null){
				 list.add(E);
			 }
		 }
		 Point[] tmp = list.toArray(new Point[list.size()]);
		 array = tmp;
		
//		int count = 0; //number of real values
//		Point[] vals = new Point[array.length];
//		
//		//put all the non-null values in a new array
//		for(Point E : array){
//			if(null == E){
//				//do nothing!
//			} else {
//				vals[count] = E;
//				count++;
//			}
//		}
//		
//		//then resize the given array and repopulate it
//		array = new Point[count];
//		
//		for(int i = 0; i < count; i++){
//			array[i] = vals[i];
//		}
		
	}
	
	/**
	 * Outputs the input to a file with a chosen name
	 * @param content the contents of the file
	 * @param filename the of the file you wish to create/overwrite
	 */
	private void toFile(String content, String filename){
		PrintWriter file = null;
		try {
			file = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		file.println(content);
		file.close();
	}

}
