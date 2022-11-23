/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.DatagramPacket;
import java.util.ArrayList;





public class Fowarder extends Node {
	static final int DEFAULT_PORT = 54321;
	static final String FOWARDER_NODE = "Fowarder";
	String controllerIP = "172.60.0.10";
	String ipAddr;
	int myNodeID;
	// String myPublicIP;
	ArrayList<String> myPublicIPs;
	RoutingTable routingTable;

	Fowarder(int srcPort, int node) {
		try {
			myNodeID = node;
			//String[] ipAddresses = getConnections.getConnections(myNodeID);
			myPublicIPs = getConnections.getPublicIPs(myNodeID); 
			socket = new DatagramSocket(srcPort);
			routingTable = new RoutingTable(); // initalize a routing table data structure.
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	 * This method gets called to start up the Fowarder
	 * Node and send a file request out.
	 */
	public synchronized void start() throws Exception {
		System.out.println("This is node: " + myNodeID);
		this.wait();
	}

	
	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		// divert incoming packets to their respective handlers
		switch(content.getType()){
			case PacketContent.FlOW_MOD:
				handleFloMod(packet);
				break; 
			case PacketContent.MESSAGE_PACKET:
				handleMessagePacket(packet);
				break;
			}

		//this.notify();
	}

	private void handleMessagePacket(DatagramPacket packet) {
		// TODO handle message packet
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		Header packetHeader = content.getHeader();
	// check if message is meant for this node if so foward to subnet ip //TODO add subnet to header
		String packetDestinationGateWayIP = packetHeader.destinationGateWayIP;
		if(packetAtRightNode(packetDestinationGateWayIP)){
			fowardPacket(packet, packetHeader.destinationSubnetIP);
		}
		else if(routingTable.routeExists(packetDestinationGateWayIP)){
			fowardPacket(packet, routingTable.getRoute(packetDestinationGateWayIP));
		}
		else {
			// TODO - send flow request packet
			// this.wait(); // wait here for flowmod packet to be recieved
			// TODO - foward packet to next node. 

		}
		
	}



	private boolean packetAtRightNode(String ip){
		for(int i = 0;i<myPublicIPs.size();i++){
			if(ip == myPublicIPs.get(i)){
				return true;
			}
		}
		return false;
	}

	private void handleFloMod(DatagramPacket packet) {
		// TODO handle flow mode
		// unpack flowmod packet.
		// update routing table.
		// this.notify
	}

	public void fowardPacket(DatagramPacket packet,String ip){
		System.out.println("\n Fowarding packet to IP:" + ipAddr + "\n");
		try{
			// setting the address
			InetAddress addr = InetAddress.getByName(ip);
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);
		}
		catch(java.lang.Exception e){e.printStackTrace();}
	}
	
	



	public static void main(String[] args) {

		try {
			int node = Integer.parseInt(args[0]);
			(new Fowarder(DEFAULT_PORT,node)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}


}








