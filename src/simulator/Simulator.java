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
	static PriorityQueue<Event> eventQueue; //global event node
	static int MaxSimulationStep = 100; //maximum step number of the simulation
	final int infinity = 2000000000;

	ArrayList <Node> nodes = new ArrayList<Node>(); //List of all nodes
	static HashMap <String,Edge> edges = new HashMap<String,Edge>();  //Please add an edge with following format: "<firstNodeID>-<secondNodeID>
	ArrayList <Prefix> prefixes = new ArrayList<Prefix>();  //List of all prefixes
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
		default: //3DegreeDijsktra runs by default
			for(int i = 0 ; i < nodes.size() ; i++ ) {
				nodes.get(i).initialize3DijEvents(MaxSimulationStep);
			}
		break;
		}
	}
	/*
	 * The simulation
	 */
	public void run(int simType) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		initialize(simType);
		Event evt;
		while(!eventQueue.isEmpty()) {
			evt = eventQueue.poll();//it is time to do this event
			writer.write(evt.toString());
			if(evt.event_type == 0) { //send an event
				if(evt.event_packet.sourceID == evt.event_packet.path.peek()) { //initial send, all other sends are called by receive method
					nodes.get(evt.event_packet.path.peek()).initialSend(evt);
				}
			} else { //receive an event
				nodes.get(evt.event_packet.path.peek()).receivePacket(evt);
			}
		}
		calculateEdgeCount();
		writer.close();
	}
	/*
	 * Calculate how many packets passed through an edge and write it into a file
	 */
	public void calculateEdgeCount () {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter("edgeload.txt");
			bw = new BufferedWriter(fw);
			int count = 0;
			int[] tmp = new int[MaxSimulationStep];
			int firstNode=0;
			int secondNode=0;
			for(Edge e: edges.values()) {
				if(count==0) {
					tmp = new int[MaxSimulationStep];
					firstNode=e.firstNode;
					secondNode=e.secondNode;
				}
				count++;
				for(int i =0;i<tmp.length;i++) {
					tmp[i]+=e.countList[i];
				}
				
				if (count == 2) {
					count = 0;
					//System.out.println(firstNode + " - "  +  secondNode + " " +Arrays.toString(tmp));
					bw.write(firstNode + " - "  +  secondNode + " " +Arrays.toString(tmp)+"\n");
				}
			}
			bw.close();
			fw.close();
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
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
	/*
	 * Construct the RoutingTable which holds information of who serves what for each nodes 
	 */
	public void fillRoutingTable(int nodeID) {
		Node node = nodes.get(nodeID);
		for(int i = 0 ; i<prefixes.size();i++) {
			node.addRoutingTable(prefixes.get(i).getName(), prefixes.get(i).getServingNode());
		}
	}
	/*
	 * Calculates three different way to go from the currentNode to all other nodes
	 * @param:nodeID the node that 3-Degree-Dijsktra runs currently
	 */
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
		//System.out.println(nodeID + ": " + Arrays.toString(path2Distance));
		//System.out.println(nodeID + ": " + Arrays.toString(path2Previous));
		//Build forwarding table of each node
		for(int i =0; i < nodes.size(); i++) {
			buildForwardingTable(1, nodeID, i, levelOnePathBuilding(i,nodeID));
			buildForwardingTable(2, nodeID, i, levelTwoPathBuilding(i,nodeID));
			buildForwardingTable(3, nodeID, i, levelThreePathBuilding(i,nodeID));
		} 
	}
	
	/*
	 * Construct Level 1 pathInfo
	 */
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
	/*
	 * Construct Level 2 pathInfo
	 */
	public String levelTwoPathBuilding(int init, int nodeID) {
		String path = "";
		int prev;
		int dist = 0;
		//////////////////////Path2//////////////////////////
		prev = path2Previous[init];
		while(prev!=nodeID) {
			dist = path2Distance[init]-edges.get(""+init+"-"+prev).cost;
			if( dist < path2Distance[prev]){
				path += prev+"-";
				return path + levelOnePathBuilding(prev, nodeID); //it goes level 1
			} else if(dist == path2Distance[prev]) {// && path2Previous[prev] !=init) {//can go up only one level 
				path += prev+"-";
				init = prev;
				prev = path2Previous[prev];
			}
		}
		path +=""+nodeID;	
		return path;
	}
	/*
	 * Construct Level 3 pathInfo
	 */
	public String levelThreePathBuilding(int init, int nodeID) {
		String path="";
		int prev;
		int dist = 0;
		//////////////////Path3///////////////////
		prev = path3Previous[init];
		while(prev!=nodeID) {
			dist = path3Distance[init]-edges.get(""+init+"-"+prev).cost;
			if( dist < path3Distance[prev] || (dist==path3Distance[prev]&& path3Previous[prev] ==init)){ //can go up one level or two levels
				int dist2 = path2Distance[prev];  //distance if it came from level 2
				int dist1 = path1Distance[prev];  //distance if it came from level 1
				if(dist2 == dist) { //it goes level 2
					path += prev+"-";
					return path + levelTwoPathBuilding(prev, nodeID);
				} else if(dist1 == dist) { //it goes level 1
					path += prev+"-";
					return path + levelOnePathBuilding(prev, nodeID);
				}
			} else if(dist == path3Distance[prev]) { // && path3Previous[prev] !=init) {
				path += prev+"-";
				init = prev;
				prev = path3Previous[prev];
			}
		}
		path +=""+nodeID;	
		return path;
	}
	/*
	 * This method constructs the ForwardingTable
	 * @param:pathId refers which path information will be updated
	 * @param:initialNode refers node of which ForwardingTable table belongs
	 * @param:targetNode refers index of ForwardingTable
	 * @param:path refers the path in between initial and target node
	 */
	public void buildForwardingTable(int pathId, int initialNode, int targetNode, String path) {
		String[] pathInfo = path.split("-");
		HashMap <Integer, ForwardingTableRow> fwTable = nodes.get(initialNode).forwardingtable;
		//Check if a row is previously added
		//If so, just update the row
		//If not, create the row
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
		//Construct the path as a list from path string
		for(int i = pathInfo.length-1 ; i>=0; i--) {
			list.add(Integer.parseInt(pathInfo[i]));
		}
		list.add(targetNode);
		//Choose which path should be updated.
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
	/*
	 * Calculate the cost of an path
	 * @param: start node
	 * @param: end node
	 * @param: path of between start and end node, nodeIDs are separated by -
	 */
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
