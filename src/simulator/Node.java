package simulator;

import java.util.ArrayList;
import java.util.HashMap;


public class Node implements Comparable{
	int nodeID;
	int type;
	ArrayList <Edge> edgeList; // Keeps all incoming and outgoing channels information
	ArrayList <String> demandedPrefixes; // Keeps demanded Prefix
	ArrayList <String> servedPrefixes; // Keeps served Prefix
	HashMap <String, ArrayList<Integer>> routingTable; // Keeps prefixName and servedNodeID
	HashMap <Integer, ForwardingTableRow> forwardingtable; //Keeps route information over multiple paths for each node
	int dijDist = 0;
	public Node(int nodeID){
		this.nodeID=nodeID;
		this.routingTable=new HashMap <String,ArrayList<Integer>>();
		this.forwardingtable= new HashMap<Integer,ForwardingTableRow>();
		this.type=0;
		this.edgeList = new ArrayList<Edge>();
		this.demandedPrefixes = new ArrayList<String>();
		this.servedPrefixes = new ArrayList<String>();
		
		
	}
	//copy constructor
	public Node(Node n, int newDijDist) {
		Node copyNode = new Node(n.nodeID);
		copyNode.type = n.type;
		copyNode.dijDist =  newDijDist;
		copyNode.edgeList = n.getEdgeList();
	}
	
	public ArrayList<Edge> getEdgeList () {
		return this.edgeList;
	}
	public void addServedPrefix(String prefixName) {
		servedPrefixes.add(prefixName);
	}
	public void addDemandedPrefix(String prefixName) {
		demandedPrefixes.add(prefixName);
	}
	public void addForwardingTableRow(int nodeID,ForwardingTableRow row) {
		
	}
	public void calculateType() {
		this.type = 0;
		if(this.demandedPrefixes.isEmpty() ) {
			this.type ++;
		}
		if(this.servedPrefixes.isEmpty()) {
			this.type += 2;
		}			
	}
	public void updateForwardingTableRow(int nodeID,ForwardingTableRow row) {
			forwardingtable.put(nodeID, row);
	}
	public void addEdge(Edge e) {
		edgeList.add(e);
	}
	@Override
	public int compareTo(Object anotherNode) {
		if(this.dijDist == ((Node)anotherNode).dijDist) {
			return 0;
		} else if(this.dijDist > ((Node)anotherNode).dijDist) {
			return 1;
		} else {
			return -1;
		}
	}
}

















