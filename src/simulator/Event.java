package simulator;

public class Event implements Comparable{
	int event_type;   //0 for send 1 for receive
	int event_time = 0;
	Packet event_packet;
	public Event (int e_type, int e_time) {
		this.event_type = e_type;  
		this.event_time = e_time; 
	}
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
	@Override
	public String toString() {
		return "Event type: "+event_type+ " at time "+ event_time + " and the packet is " + event_packet.type;
		
	}
}
