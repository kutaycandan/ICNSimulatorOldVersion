package simulator;

import java.util.ArrayList;

public class Simulator {
	ArrayList <Node> nodes = new ArrayList<Node>();
	ArrayList <Edge> edges = new ArrayList<Edge>();
	ArrayList <Prefix> prefixes = new ArrayList<Prefix>();
	
	public Simulator(ArrayList <Prefix> prefixes, ArrayList <Node> nodes,ArrayList <Edge> edges) {
		this.prefixes=prefixes;
		this.nodes=nodes;
		this.edges=edges;
		
	}
	public void run() {
		
	}
}
