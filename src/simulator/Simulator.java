package simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Simulator {
	ArrayList <Node> nodes = new ArrayList<Node>();
	ArrayList <Edge> edges = new ArrayList<Edge>();
	ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
	final int infinity = (int) Math.pow(2, 31);
	public Simulator(ArrayList <Prefix> prefixes, ArrayList <Node> nodes,ArrayList <Edge> edges) {
		this.prefixes=prefixes;
		this.nodes=nodes;
		this.edges=edges;

	}
	public void run() {

	}

	public void run3DegDijsktra(int nodeID) {
		PriorityQueue <Node>heap = new PriorityQueue<Node>(); 
		Node initialNode;
		Node currentNode;
		//Node targetNode; 
		Edge e;
		initialNode = nodes.get(nodeID);
		heap.add(initialNode);
		int [] path1Distance = new int[nodes.size()];
		int [] path2Distance = new int[nodes.size()];
		int [] path3Distance = new int[nodes.size()];

		int [] visitedNodes = new int[nodes.size()];

		int [] path1Previous = new int[nodes.size()];
		int [] path2Previous = new int[nodes.size()];
		int [] path3Previous = new int[nodes.size()];

		for(int k =0 ; k<nodes.size(); k++) {
			path1Distance[k]= infinity;
			path2Distance[k]= infinity;
			path3Distance[k]= infinity;
			path1Previous[k]= -1;
			path2Previous[k]= -1;
			path3Previous[k]= -1;
			visitedNodes[k] = -1;
		}
		path1Distance[initialNode.nodeID]= 0;
		path2Distance[initialNode.nodeID]= 0;
		path3Distance[initialNode.nodeID]= 0;


		while(!heap.isEmpty()) {
			currentNode = heap.poll();
			if(visitedNodes[currentNode.nodeID] !=1) {
				for (int i =0; i< currentNode.edgeList.size(); i++) {
					e = currentNode.edgeList.get(i);
					int dist = path1Distance[currentNode.nodeID] + e.cost;
					if(dist < path1Distance[e.secondNode]) {
						if(path1Previous[e.secondNode] == -1) {
							path1Distance[e.secondNode] = dist;
							path1Previous[e.secondNode] = e.firstNode;
						}  else {
							path3Distance[e.secondNode] = path2Distance[e.secondNode];
							path3Previous[e.secondNode] = path2Previous[e.secondNode];
							path2Distance[e.secondNode] = path1Distance[e.secondNode];
							path2Previous[e.secondNode] = path1Previous[e.secondNode];
							path1Distance[e.secondNode] = dist;
							path1Previous[e.secondNode] = e.firstNode;
						}
						nodes.get(e.secondNode).dijDis1 = dist;
					} else if(dist < path2Distance[e.secondNode]) {
						if(path2Previous[e.secondNode] == -1) {
							path2Distance[e.secondNode] = dist;
							path2Previous[e.secondNode] = e.firstNode;
						}  else {
							path3Distance[e.secondNode] = path2Distance[e.secondNode];
							path3Previous[e.secondNode] = path2Previous[e.secondNode];
							path2Distance[e.secondNode] = dist;
							path2Previous[e.secondNode] = e.firstNode;
						}
						nodes.get(e.secondNode).dijDis2 = dist;
					} else if(dist < path3Distance[e.secondNode]) {
						path3Distance[e.secondNode] = dist;
						path3Previous[e.secondNode] = e.firstNode;
						nodes.get(e.secondNode).dijDis3 = dist;
					}
					heap.add(nodes.get(e.secondNode));
				}
				visitedNodes[currentNode.nodeID] = 1;
			}
		}

	}



}
