package simulator;

public class Packet {
	static int ID = 1000;
	int size;
	int sourceID;
	int sequenceNumber;
	int destinatonID;
	int hopNumber;
	public Packet(int sourceID , int destinationID) {
		ID++;
		size =1024;
		hopNumber = 0;
		sequenceNumber = 1;
		this.sourceID = sourceID;
		this.destinatonID = destinationID;
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
