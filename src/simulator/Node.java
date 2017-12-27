package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;


public class Node implements Comparable{
	int nodeID;
	int type;
	HashMap <Integer,Edge> edgeList; // Keeps all incoming and outgoing channels information
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
		this.edgeList = new HashMap<Integer,Edge>();
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
	public HashMap<Integer,Edge> getEdgeList () {
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
	public void addEdge(int neighbor, Edge e) {
		edgeList.put(neighbor,e);
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

	public void initializeEvents(int simStep) {
		Random rand = new Random(simStep);
		int event_time;
		int event_type = 0; //initial ones should be a send event

		String prefixName;
		int prefDataSize = 1000;
		Prefix pref;
		
		int packetDestID;
		int packetType =0; //initial ones should be a interest packet
		Queue<Integer> pathInfo;
		Packet p;
		Event e; 

		for (int i =0; i< demandedPrefixes.size(); i++) {
			prefixName = demandedPrefixes.get(i);
			pref = new Prefix(prefixName, prefDataSize);
			event_time = rand.nextInt(simStep);
			for (int numProducer = 0; numProducer < 3; numProducer++) {
				//Send an interest to 3 producer assuming there exists 3 producer
				//Send an interest to their best route
				packetDestID = routingTable.get(prefixName).get(numProducer);
				pathInfo = forwardingtable.get(packetDestID).q1;
				p = new Packet(this.nodeID, packetDestID,packetType, pref, pathInfo);
				for(int numPacket = 0; numPacket<10; numPacket++) {
					e = new Event(event_type, event_time);
					e.addPacket(p);
					Simulator.addEventQueue(e);	
				}
			}
		}
	}

	public void sendPacket(Event evt){
		//System.out.println("At node: "+ nodeID +" " + evt.toString());
		//if path is empty them I am sending the packet to myself or 
		if(!evt.event_packet.path.isEmpty()) {
			//If this node is sending something which means it should be at the head of the queue in that time
			evt.event_packet.path.remove();
		}
		if(evt.event_packet.type == 0) //if it is an interest packet, return path should be added
			evt.event_packet.returnPath.add(nodeID);

		evt.event_type = 1; //change event type to receive because
		evt.event_time +=1;	//next time whom is at the head of the path will receive it
		Simulator.addEventQueue(evt);
		
	}

	public void receivePacket(Event evt) {
/////////////////
//// KUTAY  /////
/////////////////
//Check this part of the code to count which edge is used
//Head of the path is the node that packet will be sent or has just received
//You need to check on which edge the current node and the next node is connected
//We assumed there exist only one edge between any two nodes.
		if(evt.event_packet.destinatonID == nodeID) { //this node is the destination 
			sendDataPacket(evt);
		} else {
			evt.event_type = 0;
			sendPacket(evt);
		}
	}

	public void sendDataPacket(Event evt) {
		//System.out.println("At node: "+ nodeID +" " + evt.toString());
		int event_time = evt.event_time+1;
		int event_type = 0; //initially data packets should be a send event
		int packetDestID;
		int packetType =1; //initial ones should be a data packet
		Queue<Integer> returnPathInfo;
		Packet p;
		Event e;
		packetDestID = evt.event_packet.sourceID;
		returnPathInfo = evt.event_packet.returnPath;
		p = new Packet(this.nodeID, packetDestID,packetType, returnPathInfo);
		e = new Event(event_type, event_time);
		e.addPacket(p);
		Simulator.addEventQueue(e);	

	}


}

















