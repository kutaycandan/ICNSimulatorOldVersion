package simulator;

import java.util.ArrayList;

public class Prefix {
	String prefixName;
	int dataSize;
	ArrayList<Integer> servingNode;
	public Prefix(String prefixName, int dataSize) {
		this.prefixName = prefixName;
		this.dataSize = dataSize;
		this.servingNode = new ArrayList<Integer>();
	}
	
	
	public void addServingNode(int nodeID) {
		this.servingNode.add(nodeID);
	}
	public String getName() {
		return this.prefixName;
	}
	public ArrayList<Integer> getServingNode(){
		return this.servingNode;
	}
}
