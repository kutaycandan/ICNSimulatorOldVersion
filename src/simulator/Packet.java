package simulator;
import java.util.LinkedList;
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
	Queue<Integer> path = new LinkedList<Integer>();
	Queue<Integer> returnPath =new LinkedList<Integer>();
	//InterestPacketConstructor
	public Packet(int sourceID , int destinationID, int type, Prefix pref, Queue<Integer> pathInfo) {
		ID++;
		size =1024;
		hopNumber = 0;
		sequenceNumber = 1;
		this.sourceID = sourceID;
		this.destinatonID = destinationID;
		this.type = type;   //0 interest 1 data 
		this.prefix= pref; 
		/*int size = pathInfo.size();
		for(int i = 0 ; i< size;i++) {
			this.path.add(pathInfo.remove());
		}*/
		this.path=pathInfo;
		
	}
	//Data Packet Constructor
	public Packet(int sourceID , int destinationID, int type ,Queue<Integer> pathInfo) {
		ID++;
		size =1024;
		hopNumber = 0;
		sequenceNumber = 1;
		this.sourceID = sourceID;
		this.destinatonID = destinationID;
		this.type = type;   //0 interest 1 data 
		/*int size = pathInfo.size();
		for(int i = 0 ; i< size;i++) {
			this.path.add(pathInfo.remove());
		}*/
		this.path=pathInfo;
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
