package simulator;

import java.util.LinkedList;
import java.util.Queue;

public class ForwardingTableRow {
	Queue<Integer> q1 = new LinkedList <Integer>(); //Path-1 information (nodeIDs)
	Queue<Integer> q2 = new LinkedList <Integer>(); //Path-2 information (nodeIDs)
	Queue<Integer> q3 = new LinkedList <Integer>(); //Path-3 information (nodeIDs)
	int degree;
	int q1cost = 0; //Total cost of Path1
	int	q2cost = 0; //Total cost of Path2
	int q3cost = 0; //Total cost of Path3
	public ForwardingTableRow() {
		this.q1 = new LinkedList <Integer>();
		this.q2 = new LinkedList <Integer>();
		this.q3 = new LinkedList <Integer>();
		this.degree = 0;
	}
	public void addFirstPath(int nodeID) {
		q1.add(nodeID);
	}
	
	public void addSecondPath(int nodeID) {
		q2.add(nodeID);
	}
	
	public void addThirdPath(int nodeID) {
		q3.add(nodeID);
	}
	/*
	 * Keeps track of how many path exists
	 */
	public void calculateDegree() {
		if(q1.isEmpty()) {
			degree=0;
		}
		else if(q2.isEmpty()) {
			degree=1;
		}
		else if(q3.isEmpty()) {
			degree=2;
		}
		else {
			degree=3;
		}
	}
	
}
