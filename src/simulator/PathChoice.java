package simulator;

public class PathChoice implements Comparable {
	int destinationNodeId;
	int queueId;
	int cost;
	
	public PathChoice(int destinationNodeId, int queueId, int cost) {
		this.destinationNodeId = destinationNodeId;
		this.queueId = queueId;
		this.cost = cost;
	}
	/*
	 * Identify the order of different paths
	 * Smallest cost is preferred 
	 * If costs are equal smallest pathOrder number is preferred (Node7.q1.cost = Node5.q2.cost, q1 and q2 path order number)
	 * If costs and pathOrder numbers are equal than the node of smallest id/name is preferred
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		if(this.cost == ((PathChoice)o).cost) {
			if(this.queueId == ((PathChoice)o).queueId) {
				if(this.destinationNodeId == ((PathChoice)o).destinationNodeId) {
					return 0;
				} else if(this.destinationNodeId >((PathChoice)o).destinationNodeId) {
					return 1;
				} else { 
					return -1;
				}
			} else if(this.queueId >((PathChoice)o).queueId) {
				return 1;
			} else {
				return -1;
			}
		} else if ( this.cost > ((PathChoice)o).cost) {
			return 1;
		} else {
			return -1;
		}
	}

}
