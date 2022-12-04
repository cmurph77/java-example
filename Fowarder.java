/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;





public class Fowarder extends Node {
	static final int DEFAULT_PORT = 54321;
	static final String FOWARDER_NODE = "Fowarder";
	static final String CONTROLLER_IP = "172.60.0.10"; // This is the ip for controller on fowarder-net
	String ipAddr;
	int myNodeID;
	ArrayList<String> myPublicIPs;
	RoutingTable routingTable;                       // custom data structure
	Stack<DatagramPacket> packetStack = new Stack(); // this is the packet buffer for packets waiting on flowmod


	/**
	 * @param srcPort This is the port that the fowarder shoudld listen on.
	 * @param node  this is the node ID for the fowarder.
	 */
	Fowarder(int srcPort, int node) {
		try {
			myNodeID = node;
			myPublicIPs = getPublicIPs(myNodeID); // an array of all the public ips for this node
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

	
	/* (non-Javadoc)
	 * @see Node#onReceipt(java.net.DatagramPacket)
	 * 
	 *  this method is called on incoming packets and diverts them to methods that handle them based on theri type.
	 */
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


	/**
	 * This method checks packet header and fowards to correct next node. If no route exists it sends flow request to controller.
	 * 
	 * @param packet this is the packet to be fowarded
	 */
	private void handlePacketFowarding(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		Header packetHeader = content.getHeader();                              // get the packet header of the incoming packet
		String packetDestinationGateWayIP = packetHeader.destinationGateWayIP;  // get packets destination ip
		if(packetAtRightNode(packetDestinationGateWayIP)){                      //checking if packet is at the right fowarder, if so send to subnet
			System.out.println("SENDING PACKET TO SUBNET IP:" + packetHeader.destinationSubnetIP);
			fowardPacket(packet, packetHeader.destinationSubnetIP);             // foward packet to network subnet IP.
		}
		else if(routingTable.routeExists(packetDestinationGateWayIP)){          // check if route exists in routing table
			fowardPacket(packet, routingTable.getRoute(packetDestinationGateWayIP)); // foward packet to next ip
		}
		else { // no route has been found
			System.out.println("NO ROUTE FOUND, SENDING FLOW REQUEST TO CONTOLLER");
			sendFlowReq(packetDestinationGateWayIP);              // send flow request packet to controller in order to get a route for packet
			packetStack.add(packet);                              // add packet to buffer, which will be taken off when a flow mode is recieved
		
		}
		
	}


	
	/**
	 * This method sends a Flow Request packet to the controller 
	 * 
	 * @param packetDestinationGateWayIP the ip address that the node is looking for a route to
	 */
	private void sendFlowReq(String packetDestinationGateWayIP) {
		FlowReq f = new FlowReq(packetDestinationGateWayIP,myNodeID);    // create packet with fowarderd node ID
		DatagramPacket packet = f.toDatagramPacket();
		InetAddress addr;
		try {  // send packet to controller
			addr = InetAddress.getByName(CONTROLLER_IP); 
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FLOW REQUEST SENT");
	}

	
	/**
	 * this method checks if the destination ip addres of packet matches the ip address of this node. Since nodes
	 *  can be on multiple networks it checks all of its public ips.
	 * 
	 * @param ip the destination ip for a packet
	 */
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
		routingTable.setRoute(content.getNextNodeIP(), content.getTargetDestination());	// update routing table
		handlePacketFowarding(packetStack.pop());	// take packet off packet buffer and send to next ip
	}


	/**
	 * @param packet packet to be fowarded
	 * @param ip the ip address to foward the packet to
	 */
	public void fowardPacket(DatagramPacket packet,String ip){
		System.out.println("FOWARDING PACKET TO IP:" + ip);
		try{
			InetAddress addr = InetAddress.getByName(ip);            // convert string ip address into InetAddress
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);  // all fowarders liste on the same port 54321
			packet.setSocketAddress(socket_addr);        			 // set the packet address and socket
			socket.send(packet);                       			     // send packet
		}
		catch(java.lang.Exception e){e.printStackTrace();}
	}

	/*
	 * This method reads the nodeIpAddresses and returns all the publiv ip addresses associated with the node.
	 */
	public static ArrayList<String> getPublicIPs(int node){
		File fileIpAddresses = new File("nodeIpAddresses.txt");
		 ArrayList<String> myIPs = new ArrayList<>();
		 try (
			 Scanner scanner = new Scanner(fileIpAddresses)) {
			 while (scanner.hasNextLine()) {
				 String line = scanner.nextLine();
				 String[] tuple = line.split("->"); // isolates the node number from its addresses
				 int currentNode = Integer.parseInt(tuple[0]);
				 if(currentNode == node){
					 String[] ipAddresses = tuple[1].split(",");
					 for(int i = 0;i< ipAddresses.length;i++){
						 myIPs.add(ipAddresses[i]);
					 }
				 }
			 }
 
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 }
		 return myIPs;
	 }
	



	public static void main(String[] args) {

		try {
			// Fowarder is started with parameter of its node Id 
			int node = Integer.parseInt(args[0]);
			(new Fowarder(DEFAULT_PORT,node)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}


}








