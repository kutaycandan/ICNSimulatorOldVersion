package simulator;

public class Edge {
	final int EDGECAPACITY = 200; //Maximum number of packet that can be passed through an edge in a unit time is assumed to be 200
	static int ID;
	int firstNode;
	int secondNode;
	int cost;
	int[] countList; //countList[x]=20 means at time x, 20 packet pass through this edge
	public Edge(int firstNode,int secondNode,int cost) {
		ID++;
		this.firstNode=firstNode;
		this.secondNode=secondNode;
		this.cost=cost;
		countList = new int [Simulator.MaxSimulationStep];
	}
	//Copy constructor for Edge
	public Edge(Edge e) {
		this.ID = e.ID;
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
