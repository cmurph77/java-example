/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Stack;





public class Fowarder extends Node {
	static final int DEFAULT_PORT = 54321;
	static final String FOWARDER_NODE = "Fowarder";
	static final String CONTROLLER_IP = "172.60.0.10";
	String ipAddr;
	int myNodeID;
	// String myPublicIP;
	ArrayList<String> myPublicIPs;
	RoutingTable routingTable;
	Stack<DatagramPacket> packetStack = new Stack();

	public Fowarder() {
	}

	Fowarder(int srcPort, int node) {
		try {
			myNodeID = node;
			myPublicIPs = getConnections.getPublicIPs(myNodeID); 
			socket = new DatagramSocket(srcPort);
			routingTable = new RoutingTable(); // initalize a routing table data structure.
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	 * 
	 */
	public synchronized void start() throws Exception {
		System.out.println("NODE- " + myNodeID + " IS NOW ACTIVE\n");
		this.wait();
	}

	
	public synchronized void onReceipt(DatagramPacket packet) {
		
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		// divert incoming packets to their respective handlers
		switch(content.getType()){
			case PacketContent.FlOW_MOD:
				handleFloMod(packet);
				break; 
			case PacketContent.MESSAGE_PACKET:
				System.out.println("---------------------------------------------------------");
				System.out.println("MESSAGE PACKET RECIEVED");
				handlePacketFowarding(packet);
				break;
			case PacketContent.ACKPACKET:
				System.out.println("---------------------------------------------------------");
				System.out.println("ACK PACKET RECIEVED");
				handlePacketFowarding(packet);
			}

		//this.notify();
	}

	private void handlePacketFowarding(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		Header packetHeader = content.getHeader();
		//packetHeader.printHeader();
		String packetDestinationGateWayIP = packetHeader.destinationGateWayIP;
		if(packetAtRightNode(packetDestinationGateWayIP)){ //checking if packet is at the right fowarder, if so send to subnet
			System.out.println("SENDING PACKET TO SUBNET IP:" + packetHeader.destinationSubnetIP);
			fowardPacket(packet, packetHeader.destinationSubnetIP);
		}
		else if(routingTable.routeExists(packetDestinationGateWayIP)){
			fowardPacket(packet, routingTable.getRoute(packetDestinationGateWayIP));
		}
		else {
			System.out.println("NO ROUTE FOUND, SENDING FLOW REQUEST TO CONTOLLER");
			sendFlowReq(packetDestinationGateWayIP);
			packetStack.add(packet);
		
		}
		
	}


	private void sendFlowReq(String packetDestinationGateWayIP) {
		FlowReq f = new FlowReq(packetDestinationGateWayIP,myNodeID);
		DatagramPacket packet = f.toDatagramPacket();
		InetAddress addr;
		try {
			addr = InetAddress.getByName(CONTROLLER_IP);
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FLOW REQUEST SENT");
	}

	private boolean packetAtRightNode(String ip){
		for(int i = 0; i < myPublicIPs.size();i++){
			if(ip.equals(myPublicIPs.get(i))){
				return true;
			}
		}
		return false;
	}

	/*
	 * This method handles a flow modification packet.
	 */
	private void handleFloMod(DatagramPacket packet) {
		System.out.println("\nFLOW_MOD PACKET RECIEVED, UPDATING ROUTING TABLE.");
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		routingTable.setRoute(content.getNextNodeIP(), content.getTargetDestination());	
		handlePacketFowarding(packetStack.pop());	
		//this.notify();
	}

	public void fowardPacket(DatagramPacket packet,String ip){
		System.out.println("FOWARDING PACKET TO IP:" + ip);
		//System.out.println("---------------------------------------------------------");
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








