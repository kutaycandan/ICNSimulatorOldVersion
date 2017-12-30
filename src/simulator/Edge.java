package simulator;

public class Edge {
	//We may consider adding edge ID here in case there exist multiple edges between nodes
	//Second reason is that it would be easier to get all related info about an edge in Q(1) time
	int firstNode;
	int secondNode;
	int cost;
	int[] countList;
	final int EDGECAPACITY = 200;
	int count;
	public Edge(int firstNode,int secondNode,int cost) {
		this.firstNode=firstNode;
		this.secondNode=secondNode;
		this.cost=cost;
		countList = new int [Simulator.MaxSimulationStep];
	}
	
	public Edge(Edge e) {
		this.firstNode=e.firstNode;
		this.secondNode=e.secondNode;
		this.cost=e.cost;
		this.countList = e.getCountList();
	}
	public void addCount (int time) {
		countList[time]+=1;
	}
	public int[]getCountList() {
		return this.countList;
	}
}
