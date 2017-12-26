package simulator;

public class Edge {
	//We may consider adding edge ID here in case there exist multiple edges between nodes
	//Second reason is that it would be easier to get all related info about an edge in Q(1) time
	int firstNode;
	int secondNode;
	int cost;
	public Edge(int firstNode,int secondNode,int cost) {
		this.firstNode=firstNode;
		this.secondNode=secondNode;
		this.cost=cost;
	}
}
