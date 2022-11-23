/*
 * @author cianmurphy
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
import java.util.Arrays;


public class Controller extends Node {
	static final int DEFAULT_PORT = 54321;
    String[] fowardersAddresses = {
        "null",
        "null",
        "172.60.0.2", // node 2
        "172.60.0.3", // node 3
        "172.60.0.4", // node 4
        "172.60.0.5", // node 5
        "172.60.0.6", // node 6
        "172.60.0.7", // node 7
    };
	 RoutingTable table;

	Controller(int srcPort) {
		try {
			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	// Waiting here for contact.
	public synchronized void start() throws Exception {
		System.out.println(Arrays.toString(fowardersAddresses));
		this.wait();
	}

	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		// divert incoming packets to their respective handlers
		switch(content.getType()){
			case PacketContent.FlOW_REQ:
				handleFlowReq(packet);
				break; 
			}

		//this.notify();
	}


	private void handleFlowReq(DatagramPacket packet) {
		// TODO handle flow req in controller
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		//int fromNode = content.getNode();
		//String targetDestination = content.getTargetDestination();
	}

	public void setUpRoutingTable(){
		// TODO -controller routing table 
		
	}

	public static void main(String[] args) {
		System.out.println("\n\nStarting Controller Node...");
		try {
			(new Controller(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
}
