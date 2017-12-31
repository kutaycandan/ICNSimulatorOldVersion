package simulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Simulator {
	static PriorityQueue<Event> eventQueue;
	static int MaxSimulationStep = 10;
	final int infinity = 2000000000;

	ArrayList <Node> nodes = new ArrayList<Node>();
	static HashMap <String,Edge> edges = new HashMap<String,Edge>();  //Please add an edge with following format: "<firstNodeID>-<secondNodeID>
	ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
	int [] path1Distance;
	int [] path2Distance;
	int [] path3Distance; 
	int [] path1Previous; 
	int [] path2Previous; 
	int [] path3Previous; 
	int [] levelOneVisitedNodes; 
	int [] levelTwoVisitedNodes; 
	int [] levelThreeVisitedNodes; 
	int time;

	public Simulator(ArrayList <Prefix> prefixes, ArrayList <Node> nodes,HashMap <String,Edge> edges,int time) {
		this.prefixes=prefixes;
		this.nodes=nodes;
		this.edges=edges;
		this.time = time;
		eventQueue = new PriorityQueue<Event>();
	}
	public void initialize(int simType) {
		//initialize all routes and paths
		//initialize events
		//we do not change link costs right now but can change it easily
		for(int i = 0 ; i < nodes.size() ; i++ ) {
			run3DegDijsktra(i);
			fillRoutingTable(i);
		}
		
		switch(simType) {
		case 1:
			for(int i = 0 ; i < nodes.size() ; i++ ) {
				nodes.get(i).initialize1DijEvents(MaxSimulationStep);
			}
			break;
		case 3:
			for(int i = 0 ; i < nodes.size() ; i++ ) {
				nodes.get(i).initialize3DijEvents(MaxSimulationStep);
			}
			break;
		default: //3WayDijsktra runs
			for(int i = 0 ; i < nodes.size() ; i++ ) {
				nodes.get(i).initialize3DijEvents(MaxSimulationStep);
			}
		break;
		}
	}
	public void run(int simType) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		initialize(simType);
		Event evt;
		while(!eventQueue.isEmpty()) {
			//System.out.println(eventQueue.size());
			evt = eventQueue.poll();//it is time to do this event
			//System.out.println(evt.toString());
			writer.write(evt.toString());
			if(evt.event_type == 0) { //send an event
				if(evt.event_packet.sourceID == evt.event_packet.path.peek()) { //initial send
					nodes.get(evt.event_packet.path.peek()).initialSend(evt);
				}
			} else { //receive an event
				nodes.get(evt.event_packet.path.peek()).receivePacket(evt);
			}
		}


		//System.out.println("Surprise!!!");
		calculateEdgeCount();
		writer.close();
	}

	public void calculateEdgeCount () {
		for(Edge e: edges.values()) {
			System.out.println(e.firstNode + " - "  +  e.secondNode + " " +Arrays.toString(e.countList));
		}
	}
	public void setSimulationStep (int simStep) {
		MaxSimulationStep = simStep;
	}
	public static void addEventQueue(Event e) {
		eventQueue.add(e);
	}
	public static Event firstEventFromQueue() {
		return eventQueue.poll();
	}
	public void fillRoutingTable(int nodeID) {
		Node node = nodes.get(nodeID);
		for(int i = 0 ; i<prefixes.size();i++) {
			node.addRoutingTable(prefixes.get(i).getName(), prefixes.get(i).getServingNode());
		}

	}

	public void run3DegDijsktra(int nodeID) {
		PriorityQueue <Node>heap = new PriorityQueue<Node>(); //Holds next node which has the minimum distance
		Node initialNode; //The node that Dijsktra start running
		Node currentNode; //The node that is being examined
		ArrayList<Integer> pathInfo;
		initialNode = nodes.get(nodeID);
		heap.add(initialNode);
		//////////////////////////////////////////////
		//////////// INITIALIZATIONS /////////////////
		path1Distance = new int[nodes.size()];
		path2Distance = new int[nodes.size()];
		path3Distance = new int[nodes.size()];
		path1Previous = new int[nodes.size()];
		path2Previous = new int[nodes.size()];
		path3Previous = new int[nodes.size()];
		levelOneVisitedNodes = new int[nodes.size()];
		levelTwoVisitedNodes = new int[nodes.size()];
		levelThreeVisitedNodes = new int[nodes.size()];
		for(int k =0 ; k<nodes.size(); k++) {
			path1Distance[k]= infinity;
			path2Distance[k]= infinity;
			path3Distance[k]= infinity;
			path1Previous[k]= -1;
			path2Previous[k]= -1;
			path3Previous[k]= -1;
			levelOneVisitedNodes[k] = 0;
			levelTwoVisitedNodes[k] = 0;
			levelThreeVisitedNodes[k] = 0;
		}
		path1Previous[initialNode.nodeID]=nodeID;
		path2Previous[initialNode.nodeID]=nodeID;
		path3Previous[initialNode.nodeID]=nodeID;
		path1Distance[initialNode.nodeID]= 0;
		path2Distance[initialNode.nodeID]= 0;
		path3Distance[initialNode.nodeID]= 0;
		//////////////  INITIALIZATIONS /////////////////
		/////////////////////////////////////////////////

		while(!heap.isEmpty()) {
			currentNode = heap.poll();
			//A node can be visited at most 3 times while dijsktra-3 running 
			if(levelOneVisitedNodes[currentNode.nodeID]==0 || levelTwoVisitedNodes[currentNode.nodeID]==0 || levelThreeVisitedNodes[currentNode.nodeID]==0) {
				//For each edge of current node
				for (Edge e:(currentNode.edgeList.values()) ) {
					//in an edge: first node = current node , second node = neighbor node
					//New distance to neighbors which is passing through the current node
					int dist = currentNode.dijDist + e.cost;
					//Check if the current node was reached via the current edge e
					//If so, do not compare distance because it includes a loop
					if(currentNode.dijPrev != e.secondNode) {
						//Check neighbor's path1 distance is more than new calculated distance
						if(dist < path1Distance[e.secondNode]) {
							if(path1Previous[e.secondNode]!=-1) {
								//If there exist a previously calculated value then shift the path informations
								//Path 2 Info to Path 3 and Path 1 Info to Path 2
								path3Distance[e.secondNode]= path2Distance[e.secondNode];
								path3Previous[e.secondNode]= path2Previous[e.secondNode];
								path2Distance[e.secondNode]= path1Distance[e.secondNode];
								path2Previous[e.secondNode]= path1Previous[e.secondNode];
							}
							path1Distance[e.secondNode]= dist;
							path1Previous[e.secondNode]=e.firstNode;
							heap.add(new Node(nodes.get(e.secondNode),dist,e.firstNode,1));
						}
						//Check neighbor's path2 distance is more than new calculated distance
						else if(dist < path2Distance[e.secondNode]) {
							if(path2Previous[e.secondNode]!=-1) {
								//If there exist a previously calculated value then shift the path informations
								//Path 2 Info to Path 3
								path3Distance[e.secondNode]= path2Distance[e.secondNode];
								path3Previous[e.secondNode]= path2Previous[e.secondNode];
							}
							path2Distance[e.secondNode]= dist;
							path2Previous[e.secondNode]=e.firstNode;
							heap.add(new Node(nodes.get(e.secondNode),dist,e.firstNode,2));
						}
						//Check neighbor's path3 distance is more than new calculated distance
						else if (dist < path3Distance[e.secondNode]) {
							path3Distance[e.secondNode]= dist;
							path3Previous[e.secondNode]=e.firstNode;
							heap.add(new Node(nodes.get(e.secondNode),dist,e.firstNode,3));
						}
					}
				}
			}
			if(currentNode.dijLevel == 0 || currentNode.dijLevel==1) {
				levelOneVisitedNodes[currentNode.nodeID]+=1;
			} else if(currentNode.dijLevel == 2) {
				levelTwoVisitedNodes[currentNode.nodeID]+=1;
			} else {
				levelThreeVisitedNodes[currentNode.nodeID]+=1;
			}
		}
		//System.out.println("For node: "+nodeID + " path1= "+Arrays.toString(path1Previous));
		//System.out.println("For node: "+nodeID + " path1= "+Arrays.toString(path1Distance));
		//System.out.println("For node: "+nodeID + " path2= "+Arrays.toString(path2Previous));
		//System.out.println("For node: "+nodeID + " path2= "+Arrays.toString(path2Distance));
		//System.out.println("For node: "+nodeID + " path3= "+Arrays.toString(path3Previous));
		//System.out.println("For node: "+nodeID + " path3= "+Arrays.toString(path3Distance)); 
		for(int i =0; i < nodes.size(); i++) {
			//System.out.println(levelTwoPathBuilding(i,nodeID));
			buildForwardingTable(1, nodeID, i, levelOnePathBuilding(i,nodeID));
			buildForwardingTable(2, nodeID, i, levelTwoPathBuilding(i,nodeID));
			buildForwardingTable(3, nodeID, i, levelThreePathBuilding(i,nodeID));
		} 
		//System.out.println("init:"+nodeID+" to 5: "+levelOnePathBuilding(5,nodeID));
		//System.out.println("init:"+nodeID+" to 0: "+levelOnePathBuilding(0,nodeID));
		//System.out.println(nodes.get(nodeID).forwardingtable.get(5).q1.toString());
		//System.out.println(nodes.get(nodeID).forwardingtable.get(5).q2.toString());
		//System.out.println(nodes.get(nodeID).forwardingtable.get(5).q3.toString());
	}

	public String levelOnePathBuilding(int init, int nodeID) {
		String path = "";
		int prev;
		//////////////////Path1///////////////////
		prev = path1Previous[init];
		while(prev!=nodeID) {
			path += prev+"-";
			prev = path1Previous[prev];
		}
		path +=""+nodeID;
		return path;
	}

	public String levelTwoPathBuilding(int init, int nodeID) {
		String path = "";
		int prev;
		int dist = 0;
		//////////////////////Path2//////////////////////////
		prev = path2Previous[init];
		while(prev!=nodeID) {
			if(!edges.containsKey(""+init+"-"+prev)) {
				//System.out.println(init + "     " + prev);
				//System.out.println(edges.get("3-1"));
			}


			dist = path2Distance[init]-edges.get(""+init+"-"+prev).cost;
			if( dist < path2Distance[prev]){
				path += prev+"-";
				return path + levelOnePathBuilding(prev, nodeID);
			} else if(dist == path2Distance[prev] && path2Previous[prev] !=init) {
				path += prev+"-";
				init = prev;
				prev = path2Previous[prev];
			}
		}
		path +=""+nodeID;	
		return path;
	}

	public String levelThreePathBuilding(int init, int nodeID) {
		String path="";
		int prev;
		int dist = 0;
		//////////////////Path3///////////////////
		prev = path3Previous[init];
		while(prev!=nodeID) {
			dist = path3Distance[init]-edges.get(""+init+"-"+prev).cost;
			//System.out.println("prev: " + prev+ " dist: " + dist);
			if( dist < path3Distance[prev] || (dist==path3Distance[prev]&& path3Previous[prev] ==init)){ //can go up only one level or two levels
				int dist2 = path2Distance[prev];  //distance if it came from level 2
				int dist1 = path1Distance[prev];  //distance if it came from level 1
				if(dist2 == dist) { //it goes level 2
					path += prev+"-";
					return path + levelTwoPathBuilding(prev, nodeID);
				} else if(dist1 == dist) { //it goes level 1
					path += prev+"-";
					return path + levelOnePathBuilding(prev, nodeID);
				}
			} else if(dist == path3Distance[prev] && path3Previous[prev] !=init) {
				path += prev+"-";
				init = prev;
				prev = path3Previous[prev];
			}
		}
		path +=""+nodeID;	
		return path;
	}

	public void buildForwardingTable(int pathId, int initialNode, int targetNode, String path) {
		String[] pathInfo = path.split("-");
		HashMap <Integer, ForwardingTableRow> fwTable = nodes.get(initialNode).forwardingtable;
		//Check if a row is previously added
		ForwardingTableRow row; 
		if(!fwTable.isEmpty()) {
			if(fwTable.containsKey(targetNode)) {
				row = fwTable.get(targetNode);
			} else {
				row = new ForwardingTableRow();
			}
		} else {
			row = new ForwardingTableRow();
		}
		Queue<Integer> list = new LinkedList <Integer>();
		int pathCost = pathCost(initialNode,targetNode,path);
		//construct the path
		for(int i = pathInfo.length-1 ; i>=0; i--) {
			list.add(Integer.parseInt(pathInfo[i]));
		}
		list.add(targetNode);
		//choose which row
		if(pathId == 1) {
			row.q1 = list;
			row.q1cost = pathCost;
		} else if(pathId ==2) {
			row.q2 = list;
			row.q2cost = pathCost;
		} else {
			row.q3 = list;
			row.q3cost = pathCost;
		}

		fwTable.put(targetNode, row);
	}
	//Path cost calculation
	public int pathCost(int initialNode,int targetNode, String path) {
		String[] pathInfo = path.split("-");
		int cost = 0;
		if(initialNode == targetNode) {
			return 0;
		}else if(pathInfo.length==1) {
			return edges.get(initialNode+"-"+targetNode).cost;
		}
		String first;
		String second;
		for(int i =pathInfo.length-1; i>0;i--) {
			first = pathInfo[i];
			second = pathInfo[i-1];
			cost+=edges.get(first+"-"+second).cost;
		}
		cost+=edges.get(pathInfo[0]+"-"+targetNode).cost;
		return cost;
	}

}
