package simulator;
import java.util.Queue;

public class Packet {
	static int ID = 1000;
	int size;
	int sourceID;
	int sequenceNumber;
	int destinatonID;
	int hopNumber;
	int type;
	Prefix prefix;
	Queue<Integer> path;
	public Packet(int sourceID , int destinationID, int type, Prefix pref, Queue<Integer> pathInfo) {
		ID++;
		size =1024;
		hopNumber = 0;
		sequenceNumber = 1;
		this.sourceID = sourceID;
		this.destinatonID = destinationID;
		this.type = type;   //0 interest 1 data 
		this.prefix= pref; 
		this.path = pathInfo;
	}
	public int getSourceID() {
		return this.sourceID;
	}
	public int getDestinationID() {
		return this.destinatonID;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
}
