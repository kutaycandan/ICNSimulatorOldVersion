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
