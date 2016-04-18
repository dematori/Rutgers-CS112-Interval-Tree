package structures;

import java.util.*;

/**
 * Encapsulates an interval tree.
 */
public class IntervalTree {

	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;

	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {

		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}

		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;

		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');

		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);

		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);

		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}

	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		Queue<IntervalTreeNode> tree = new Queue<IntervalTreeNode>();
		IntervalTreeNode returnNode = null;
		for(int endPoint : endPoints){
			IntervalTreeNode tempNode = new IntervalTreeNode(endPoint, endPoint, endPoint);
			tempNode.leftIntervals = new ArrayList<Interval>();
			tempNode.rightIntervals = new ArrayList<Interval>();
			tree.enqueue(tempNode);
		}
		int size = tree.size();
		while(size > 0){
			if(size == 1){
				returnNode = tree.dequeue();
				return returnNode;
			}else{
				int tempSize = size;
				while(tempSize > 1){
					IntervalTreeNode treeNodeLeft = tree.dequeue();
					IntervalTreeNode treeNodeRight = tree.dequeue();
					IntervalTreeNode newTreeNode = new IntervalTreeNode((treeNodeLeft.maxSplitValue + treeNodeRight.minSplitValue) / 2, treeNodeLeft.minSplitValue, treeNodeRight.maxSplitValue);
					newTreeNode.leftIntervals = new ArrayList<Interval>();
					newTreeNode.rightIntervals = new ArrayList<Interval>();
					newTreeNode.leftChild = treeNodeLeft;
					newTreeNode.rightChild = treeNodeRight;
					tree.enqueue(newTreeNode);
					tempSize -=2;
				}
				if(tempSize == 1){
					IntervalTreeNode temp = tree.dequeue();
					tree.enqueue(temp);
				}
				size = tree.size();
			}
		}
		returnNode = tree.dequeue();
		return returnNode;
	}

	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		for(Interval interval : leftSortedIntervals){
			mapIntervals(interval, root, 'l');
		}
		for(Interval interval: rightSortedIntervals){
			mapIntervals(interval, root,'r');
		}

	}

	private void mapIntervals(Interval interval, IntervalTreeNode child, char lr){
		if(interval.contains(child.splitValue)){
			if(lr == 'l'){
				child.leftIntervals.add(interval);
			}else{
				child.rightIntervals.add(interval);
			}
			return;
		}
		if(child.splitValue < interval.leftEndPoint){
			mapIntervals(interval, child.rightChild, lr);
		}else{
			mapIntervals(interval, child.leftChild, lr);
		}
		return;
	}

	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		return findIntervals(root, q);
	}

	private ArrayList<Interval> findIntervals(IntervalTreeNode base, Interval q){
		ArrayList<Interval> returnArray = new ArrayList<Interval>();
		ArrayList<Interval> leftEnds = new ArrayList<Interval>();
		ArrayList<Interval> rightEnds = new ArrayList<Interval>();;
		if(base != null){
			leftEnds = base.leftIntervals;
			rightEnds = base.rightIntervals;
			if(q.contains(base.splitValue)){
				for(Interval tempInterval : leftEnds){
					returnArray.add(tempInterval);
				}
				returnArray.addAll(findIntervals(base.rightChild,q));
				returnArray.addAll(findIntervals(base.leftChild, q));
			}else if(base.splitValue < q.leftEndPoint){
				int i = rightEnds.size() - 1;
				while(i >= 0 && rightEnds.get(i).intersects(q)){
					returnArray.add(rightEnds.get(i));
					i--;
				}
				returnArray.addAll(findIntervals(base.rightChild, q));
			}else if(base.splitValue > q.rightEndPoint){
				int i = 0;
				while(i < leftEnds.size() && leftEnds.get(i).intersects(q)){
					returnArray.add(leftEnds.get(i));
					i++;
				}
				returnArray.addAll(findIntervals(base.leftChild, q));
			}
		}
		return returnArray;
	}

	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
}

