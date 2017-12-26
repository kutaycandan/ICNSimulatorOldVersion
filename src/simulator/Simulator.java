package simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Simulator {
	ArrayList <Node> nodes = new ArrayList<Node>();
	ArrayList <Edge> edges = new ArrayList<Edge>();
	ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
	HashMap <String,Integer> edgePair = new HashMap<String,Integer>();
	int [] path1Distance;
	int [] path2Distance;
	int [] path3Distance; 
	int [] path1Previous; 
	int [] path2Previous; 
	int [] path3Previous; 
	int [] visitedNodes; 
	
	
	final int infinity = 2000000000;
	public Simulator(ArrayList <Prefix> prefixes, ArrayList <Node> nodes,ArrayList <Edge> edges) {
		this.prefixes=prefixes;
		this.nodes=nodes;
		this.edges=edges;
	}
	public void run() {
		run3DegDijsktra(0);
	}

	public void run3DegDijsktra(int nodeID) {
		PriorityQueue <Node>heap = new PriorityQueue<Node>(); //Holds next node which has the minimum distance
		Node initialNode; //The node that Dijsktra start running
		Node currentNode; //The node that is being examined
		Edge e; //An edge of the current node
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
		visitedNodes = new int[nodes.size()];
		for(int k =0 ; k<nodes.size(); k++) {
			path1Distance[k]= infinity;
			path2Distance[k]= infinity;
			path3Distance[k]= infinity;
			path1Previous[k]= -1;
			path2Previous[k]= -1;
			path3Previous[k]= -1;
			visitedNodes[k] = 0;
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
			if(visitedNodes[currentNode.nodeID] < 3) {
				//For each edge of current node
				for (int i =0; i< currentNode.edgeList.size(); i++) {
					e = currentNode.edgeList.get(i);   //first node = current node , second node = neighbor node
					edgePair.put(""+e.firstNode+"-"+e.secondNode, e.cost);
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
							heap.add(new Node(nodes.get(e.secondNode),dist,e.firstNode));
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
							heap.add(new Node(nodes.get(e.secondNode),dist,e.firstNode));
						}
						//Check neighbor's path3 distance is more than new calculated distance
						else if (dist < path3Distance[e.secondNode]) {
							path3Distance[e.secondNode]= dist;
							path3Previous[e.secondNode]=e.firstNode;
							heap.add(new Node(nodes.get(e.secondNode),dist,e.firstNode));
						}
					}
				}
			}
			visitedNodes[currentNode.nodeID]+=1;
		}
		
		System.out.println(levelOnePathBuilding(1, 0));
	}
	
	public String levelOnePathBuilding(int init, int nodeID) {
		String path;
		int prev;
		//////////////////Path1///////////////////
			path=""+init+"-";
			prev = path1Previous[init];
			while(prev!=nodeID) {
				path += prev+"-";
				prev = path1Previous[prev];
			}
			path +=""+nodeID;	
		return path;
	}
	
	public String levelTwoPathBuilding(int init, int nodeID) {
		String path;
		int prev;
		int dist = 0;
		//////////////////Path2///////////////////
			path=""+init+"-";
			prev = path1Previous[init];
			while(prev!=nodeID) {
				path += prev+"-";
				dist = path2Distance[init]-edgePair.get(""+init+"-"+prev);
				if(dist<path2Distance[prev]) { //go and check one level up
					return path + levelOnePathBuilding(path1Previous[prev], nodeID);
				} else {
					if(path2Previous[prev]!=init) {
						init = prev;
						prev = path2Previous[prev];
					} else { //go and check one level up
						return path + levelOnePathBuilding(path1Previous[prev], nodeID);
					}
				}
			}
			path +=""+nodeID;	
		return path;
	}
	
	public String levelThreePathBuilding(int init, int nodeID) {
		String path;
		int prev;
		int dist = 0;
		//////////////////Path2///////////////////
			path=""+init+"-";
			prev = path1Previous[init];
			while(prev!=nodeID) {
				path += prev+"-";
				dist = path3Distance[init]-edgePair.get(""+init+"-"+prev);
				if(dist<path3Distance[prev]) { //go and check one level up
					return path + levelTwoPathBuilding(path2Previous[prev], nodeID);
				} else {
					if(path3Previous[prev]!=init) {
						init = prev;
						prev = path3Previous[prev];
					} else { //go and check one level up
						return path + levelTwoPathBuilding(path2Previous[prev], nodeID);
					}
				}
			}
			path +=""+nodeID;	
		return path;
	}
	
	

	public void buildForwardingTable(int pathId, int initialNode, int targetNode, String path) {

	}




}
