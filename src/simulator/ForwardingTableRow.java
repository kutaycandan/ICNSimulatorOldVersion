package simulator;

import java.util.LinkedList;
import java.util.Queue;

public class ForwardingTableRow {
	Queue<Integer> q1 = new LinkedList <Integer>();
	Queue<Integer> q2 = new LinkedList <Integer>();
	Queue<Integer> q3 = new LinkedList <Integer>();
	int degree;
	public ForwardingTableRow() {
		this.q1 = new LinkedList <Integer>();
		this.q2 = new LinkedList <Integer>();
		this.q3 = new LinkedList <Integer>();
		this.degree = 0;
	}
	public void addFirstQueue(int nodeID) {
		q1.add(nodeID);
	}
	
	public void addSecondQueue(int nodeID) {
		q2.add(nodeID);
	}
	
	public void addThirdQueue(int nodeID) {
		q3.add(nodeID);
	}
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
