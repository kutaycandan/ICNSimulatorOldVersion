package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;


public class Node implements Comparable{
	int nodeID;
	int type;
	HashMap <Integer,Edge> edgeList; // Keeps all incoming and outgoing channels information
	ArrayList <String> demandedPrefixes; // Keeps demanded Prefix
	ArrayList <String> servedPrefixes; // Keeps served Prefix
	HashMap <String, ArrayList<Integer>> routingTable; // Keeps prefixName and servedNodeIDs
	HashMap <Integer, ForwardingTableRow> forwardingtable; //Keeps route information over multiple paths for each node
	int dijDist = 0;
	int dijPrev = 0;
	int dijLevel =0;
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
	public Node(Node n, int newDijDist, int newDijPrev, int newDijLevel) {
		this.nodeID = n.nodeID;
		this.type = n.type;
		this.dijDist =  newDijDist;
		this.dijPrev = newDijPrev;
		this.dijLevel = newDijLevel;
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
	/*
	 * This is to decide whether a node is a producer or a consumer or both
	 */
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
	/*
	 * Comparator for nodes to be used directly by Dijsktra minimum path calculation
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
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
	/*
	 * All interest messages are initialized at the beginning of the simulation
	 * We DO NOT use random interest times but instead we USE pre-decided interest time to be able to compare two algorithms
	 * @param: simStep maximum simulation time
	 */
	public void initialize1DijEvents(int simStep) {
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
		int producer_size;
		
		//For step 1-11,21,31..... up to maximum simulation step/time
		for (int time = 1; time<simStep; time+=10){
			//For each prefix that a node wants 
			for (int i =0; i< demandedPrefixes.size(); i++) {
				PriorityQueue<PathChoice> pathOrder = new PriorityQueue<PathChoice>(); //Priority queue that keeps minimum path information
				prefixName = demandedPrefixes.get(i);
				pref = new Prefix(prefixName, prefDataSize);
				event_time = time; //rand.nextInt(simStep);
				producer_size = routingTable.get(prefixName).size(); 
				//For each producer of that prefix
				for (int j = 0; j<producer_size; j++) {
					packetDestID = routingTable.get(prefixName).get(j);
					ForwardingTableRow ftr = forwardingtable.get(packetDestID);
					//Add these path into pathOrder queue if there exist a path which has a cost different than 0
					if(ftr.q1cost != 0) {
						pathOrder.add(new PathChoice(packetDestID, 1, ftr.q1cost));
					}
				}
				//Pre-decided maximum number of interest packet 
				int totalPacket = 30;
				PathChoice pc = pathOrder.poll();
				pathInfo = forwardingtable.get(pc.destinationNodeId).q1;
				int[] tmp = new int[pathInfo.size()];
				for(int a = 0 ;a<tmp.length;a++) {
					tmp[a]=pathInfo.poll();
					pathInfo.add(tmp[a]);
				}
				//Create new packets
				//Create new events
				//Put each packet into event queue
				for(int numPacket = 0; numPacket<totalPacket; numPacket++) {
					packetDestID = pc.destinationNodeId;
					Queue <Integer> tmpPathInfo = new LinkedList<Integer>();
					for(int a = 0 ;a<tmp.length;a++) {
						tmpPathInfo.add(tmp[a]);
					}
					p = new Packet(this.nodeID, packetDestID,packetType, pref, tmpPathInfo);
					e = new Event(event_type, event_time);
					e.addPacket(p);
					Simulator.addEventQueue(e);	
				}
			} // for each prefix initialization is done here
		}
		
	}
	/*
	 * All interest messages are initialized at the beginning of the simulation
	 * We DO NOT use random interest times but instead we USE pre-decided interest time to be able to compare two algorithms
	 * @param: simStep maximum simulation time
	 */
	public void initialize3DijEvents(int simStep) {
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
		int producer_size;
		//For step 1-11,21,31..... up to maximum simulation step/time
		for (int time = 1; time<simStep; time+=10) {
			//For each prefix that a node wants 
			for (int i =0; i< demandedPrefixes.size(); i++) {
				PriorityQueue<PathChoice> pathOrder = new PriorityQueue<PathChoice>();
				prefixName = demandedPrefixes.get(i);
				pref = new Prefix(prefixName, prefDataSize);
				event_time = time; //rand.nextInt(simStep);
				producer_size = routingTable.get(prefixName).size(); 
				//For each producer of that prefix
				for (int j = 0; j<producer_size; j++) {
					packetDestID = routingTable.get(prefixName).get(j);
					//Add these paths into pathOrder queue if there exist a path which has a cost different than 0
					//Add all calculated paths for a producer
					ForwardingTableRow ftr = forwardingtable.get(packetDestID);
					if(ftr.q1cost != 0) {
						pathOrder.add(new PathChoice(packetDestID, 1, ftr.q1cost));
					}
					if(ftr.q2cost != 0) {
						pathOrder.add(new PathChoice(packetDestID,2, ftr.q2cost));
					}
					if(ftr.q3cost != 0) {
						pathOrder.add(new PathChoice(packetDestID,3, ftr.q3cost));
					}

				}
				int poSize = pathOrder.size();
				//Pre-decided maximum number of interest packet 
				//Send 30 interest packet if only one path exists
				//Send 15 interest packet each if two paths exist
				//Send 10 interest packet each if three paths exists
				int totalPacket = 30/Math.min(3, poSize);
				for(int w =0; w < poSize; w++) {
					if(w ==3) {
						break;
					}
					PathChoice pc = pathOrder.poll();
					if(pc.queueId == 1) {
						pathInfo = forwardingtable.get(pc.destinationNodeId).q1;
					} else if(pc.queueId == 2) {
						pathInfo = forwardingtable.get(pc.destinationNodeId).q2;
					} else {
						pathInfo = forwardingtable.get(pc.destinationNodeId).q3;
					}
					int[] tmp = new int[pathInfo.size()];
					for(int a = 0 ;a<tmp.length;a++) {
						tmp[a]=pathInfo.poll();
						pathInfo.add(tmp[a]);
					}
					//Create new packets
					//Create new events
					//Put each packet into event queue
					for(int numPacket = 0; numPacket<totalPacket; numPacket++) {
						packetDestID = pc.destinationNodeId;
						Queue <Integer> tmpPathInfo = new LinkedList<Integer>();
						for(int a = 0 ;a<tmp.length;a++) {
							tmpPathInfo.add(tmp[a]);
						}
						p = new Packet(this.nodeID, packetDestID,packetType, pref, tmpPathInfo);
						e = new Event(event_type, event_time);
						e.addPacket(p);
						Simulator.addEventQueue(e);	
					}
				}	
			}  ///for each prefix initialization is done here
		}

	}
	/*
	 * Start sending interest packets or data packets
	 */
	public void initialSend(Event evt) {
		if(evt.event_packet.type == 0) //if it is an interest packet, this node should be added into the returnPath
			evt.event_packet.returnPath.add(this.nodeID);
		evt.event_packet.path.remove(); //Remove the first=currentNode from the path
		////////PACKET COUNT ///////
		Edge e = Simulator.edges.get(""+this.nodeID+"-"+evt.event_packet.path.peek());
		e.addCount(evt.event_time);
		////////////////////////////
		evt.event_type = 1; //Change this event type to a receive event
		evt.event_time++; //Update event time to next step
		Simulator.addEventQueue(evt);
	}
	/*
	 * Sends interest or data packets
	 */
	public void sendPacket(Event evt){
		if(evt.event_packet.type == 0) //if it is an interest packet, this node should be added into the returnPath
			evt.event_packet.returnPath.add(this.nodeID);
		evt.event_packet.path.remove(); //Remove the first=currentNode from the path
		////////PACKET COUNT ///////
		Edge e = Simulator.edges.get(""+this.nodeID+"-"+evt.event_packet.path.peek());
		e.addCount(evt.event_time);
		////////////////////////////
		evt.event_type = 1; //Change this event type to a receive event
		evt.event_time++; //Update event time to next step
		Simulator.addEventQueue(evt);
	}
	
	/*
	 * Receive interest or data packets
	 */
	public void receivePacket(Event evt) {
		if(evt.event_packet.destinatonID == this.nodeID && evt.event_packet.type==0) { //this node is the destination for an interest packet
			createDataPacket(evt);
		}else if(evt.event_packet.destinatonID == this.nodeID && evt.event_packet.type==1) { //this node is the destination for a data packet

		}
		else {  //this node is just an inner node which transfers the packet
			evt.event_type = 0;
			sendPacket(evt);
		} 
	}
	/*
	 * Create new data packet for each incoming interest
	 * Multiple data packets can be produced for each interest by calling this function multiple time 
	 */
	public void createDataPacket(Event evt) {
		int event_time = evt.event_time;
		int event_type = 0; //initially data packets should be a send event
		int packetDestID = evt.event_packet.sourceID;
		int packetSourceID = evt.event_packet.destinatonID;
		int packetType =1; //new packets should be a data packet
		evt.event_packet.returnPath.add(this.nodeID);
		Queue<Integer> path = reversePath(evt.event_packet.returnPath); //Make the packet's path = reverse returnPath 
		Packet p;
		Event e;
		p = new Packet(packetSourceID, packetDestID,packetType, path);
		e = new Event(event_type, event_time);
		e.addPacket(p);
		Simulator.addEventQueue(e);	
	}
	/*
	 * Re-construct the path
	 * Just reversed the returnPath and send 
	 */
	public Queue<Integer> reversePath (Queue<Integer> path) {
		Queue<Integer> reversePath = new LinkedList<Integer>();
		Stack<Integer> ps = new Stack<Integer> ();
		int size = path.size();
		while(!path.isEmpty()) {
			ps.push(path.poll());
		}
		while(!ps.isEmpty()) {
			reversePath.add(ps.pop());
		}
		return reversePath;
	}

}

















