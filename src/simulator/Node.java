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
	int dijPrev = 0;
	int time;
	public Node(int nodeID){
		this.nodeID=nodeID;
		this.routingTable=new HashMap <String,ArrayList<Integer>>();
		this.forwardingtable= new HashMap<Integer,ForwardingTableRow>();
		this.type=0;
		this.edgeList = new ArrayList<Edge>();
		this.demandedPrefixes = new ArrayList<String>();
		this.servedPrefixes = new ArrayList<String>();
		this.time = 0;
	}
	//copy constructor to be used in 3th degree dijsktra algorithm
	public Node(Node n, int newDijDist, int newDijPrev) {
		this.nodeID = n.nodeID;
		this.type = n.type;
		this.dijDist =  newDijDist;
		this.dijPrev = newDijPrev;
		this.edgeList = n.getEdgeList();
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
	public void addRoutingTable(String prefixName,ArrayList<Integer> nodes) {
		this.routingTable.put(prefixName, nodes);
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
	public void hasEvent(int time) {
		//Bu alan doldurulacak
		if(time==0) {
			//Send interest
			//send(time)
		}
		else {
			//Check events
			//heapten çekip timeları eşit mi diye bakacaksın receive varsa ve 
			//send olması gerekiyorsa packetten poplayıp idsini yazıcaksın
		}
	}
	public void send(int time,Packet packet,int nodeID) {
		int tmptime = time +1;
		//packet yapısını tam bilmiyorum değiştireceksen burada pop falan yapman gerekecek
		//nodes.get(nodeID).receive(packet,tmptime) tadında olacak ama kafam yine basmadı :D
		System.out.println("Packet: "+packet.ID +" sent by Node: "+this.nodeID+" to Node: "+nodeID+" at time: "+ time);
	}
	public void receive(int time,Packet packet) {
		//Heapi oluşturduktan sonra bakacaksın
		System.out.println("Packet: "+packet.ID +" received by Node: "+this.nodeID+" at time: "+ time);

	}
}

















