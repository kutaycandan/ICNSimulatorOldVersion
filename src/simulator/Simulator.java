package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

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
	int time;

	final int infinity = 2000000000;
	public Simulator(ArrayList <Prefix> prefixes, ArrayList <Node> nodes,ArrayList <Edge> edges,int time) {
		this.prefixes=prefixes;
		this.nodes=nodes;
		this.edges=edges;
		this.time = time;
	}
	public void run() {
		for(int i = 0 ; i < nodes.size() ; i++ ) {
			run3DegDijsktra(i);
			fillRoutingTable(i);
		}
		//Eventlerin başlangıcı
		for(int i =0;i<time;i++) {
			for(int j=0;j<nodes.size();j++) {
				//Bu alan doldurulacak
				//Burda bir mesaj iletilebilir has event metodu var onu doldurmak lazım eventi sen yapıcam dedin ellemedim
				nodes.get(i).hasEvent(i);
			}
		}
		
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
		
		for(int i =0; i < nodes.size(); i++) {
			buildForwardingTable(1, nodeID, i, levelOnePathBuilding(i,nodeID));
			buildForwardingTable(2, nodeID, i, levelTwoPathBuilding(i,nodeID));
			buildForwardingTable(3, nodeID, i, levelThreePathBuilding(i,nodeID));
		}
		//System.out.println(nodes.get(0).forwardingtable.get(5).q1.toString());
		//System.out.println(nodes.get(0).forwardingtable.get(5).q2.toString());
		//System.out.println(nodes.get(0).forwardingtable.get(5).q3.toString());
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
			dist = path2Distance[init]-edgePair.get(""+init+"-"+prev);
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
			dist = path3Distance[init]-edgePair.get(""+init+"-"+prev);
			if( dist < path3Distance[prev]){ //can go up only one level or two levels
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
		//construct the path
		for(int i = pathInfo.length-1-1 ; i>=0; i--) {
			list.add(Integer.parseInt(pathInfo[i]));
		}
		//choose which row
		if(pathId == 1) {
			row.q1 = list;
		} else if(pathId ==2) {
			row.q2 = list;
		} else {
			row.q3 = list;
		}

		fwTable.put(targetNode, row);
	}




}
