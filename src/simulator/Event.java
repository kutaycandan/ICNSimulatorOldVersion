package simulator;

public class Event implements Comparable{
	int event_type;   //0 for send 1 for receive
	int event_time = 0;
	Packet event_packet;
	public Event (int e_type, int e_time) {
		this.event_type = e_type;  
		this.event_time = e_time; 
	}
	/* Event that has smaller time unit comes first
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object evt) {
		if(this.event_time == ((Event)evt).event_time) {
			return 0;
		} else if ( this.event_time > ((Event)evt).event_time) {
			return 1;
		} else {
			return -1;
		}
	}
	public void addPacket(Packet p) {
		this.event_packet = p;
	}
	/*
	 * It is for debug purposes
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event type: "+event_type+ " at time "+ event_time + " and the packet is " + event_packet.type + "\n"
				+event_packet.sourceID+"-"+event_packet.path.peek()+"\n"
						+event_packet.destinatonID +"\n";	
	}
}
