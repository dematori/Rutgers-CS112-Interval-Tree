package structures;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 */
public class Sorter {

	private Sorter() { }

	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		ArrayList<Interval> temp = new ArrayList<Interval>();
		for(int i = 1; i < intervals.size(); i++){
			Interval interval = intervals.get(i);
			int tempIndex = i - 1;
			if(lr == 'l'){
				while((intervals.get(tempIndex).leftEndPoint > interval.leftEndPoint) && tempIndex >=0){
					intervals.set(tempIndex + 1, intervals.get(tempIndex));
					tempIndex--;
				}
			}else if(lr == 'r'){
				while((intervals.get(tempIndex).rightEndPoint > interval.rightEndPoint) && tempIndex >=0){
					intervals.set(tempIndex + 1, intervals.get(tempIndex));
					tempIndex--;
				}
			}
			intervals.set(tempIndex + 1, interval);
		}
		for(Interval tempInterval : intervals){
			temp.add(tempInterval);
		}
		intervals = temp;
		//System.out.println(lr + " : " + intervals);
	}

	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		ArrayList<Integer> endPoints = new ArrayList<Integer>();
		for(Interval leftEnd : leftSortedIntervals){
			boolean found = false;
			for(int i = 0; i < endPoints.size(); i++){
				if(endPoints.get(i) == leftEnd.leftEndPoint){
					found = true;
				}
			}
			if(!found){
				endPoints.add(leftEnd.leftEndPoint);
			}
		}
		for(Interval rightEnd : rightSortedIntervals){
			boolean found = false;
			for(int i = 0; i < endPoints.size(); i++){
				if(endPoints.get(i) == rightEnd.rightEndPoint){
					found = true;
				}
			}
			if(!found){
				endPoints.add(rightEnd.rightEndPoint);
			}
		}
		Collections.sort(endPoints);
		//System.out.println("Endpoints : " + endPoints);
		return endPoints;
	}
}
