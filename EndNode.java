/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.io.IOException;
import java.net.InetAddress;






public class EndNode extends Node {
	static final int DEFAULT_SRC_PORT = 54321;
	static final String EndNode_NODE = "EndNode";

	String gateWayIP; // this is the IP Address to send to packets out to

	String myGatewayIP;  // this nodes local fowarders IP for recieving packets
	String mySubnetIP;   // this nodes subnet IP address

	String destinationGateWayIP; // Destination IP Addresses 
	String destinationSubnetIP;

	EndNode(int srcPort,String gateWayIP, String myGateWayIP, String mySubnetIP ) {
		this.gateWayIP = gateWayIP;
		this.myGatewayIP = myGateWayIP;
		this.mySubnetIP = mySubnetIP;
		
		try {
			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	 * This method gets called to start up the EndNode Node and send a file request out.
	 */
	public synchronized void start() throws Exception {
		Scanner s = new Scanner(System.in);
		System.out.println("Woudld you like to receive or send messages? Y/N?");
		String result = s.nextLine();
		if(result.equals("Y")){
			System.out.println("Enter the destination gateway IP address");
		destinationGateWayIP = s.nextLine();
		System.out.println("enter destination subnet ip");
		destinationSubnetIP = s.nextLine();
		System.out.println("ENTER MESSAGE TO SEND: ");
		if(s.hasNext()){
				String message = s.nextLine();
	            sendMessagePacket(message,gateWayIP);

		}
		}
		this.wait();
	}

	
	/**
	 * This method sends out an message packet.
	 * 
	 * @param messageToSend This is the message to be sent
	 * @param ip This is the destinaiton ip address for the packet to be sent. It is set to the nodes gatewayIp so it 
	 * 			  gets onto the fowarder network.
	 * @throws IOException
	 */
	public void sendMessagePacket(String messageToSend,String ip) throws IOException{
		messagePacket m = new messagePacket(messageToSend);
		m.setHeader(mySubnetIP, myGatewayIP, destinationGateWayIP, destinationSubnetIP); // A header is created with the destination Ip addresses provided
		m.getHeader().printHeader();
		DatagramPacket packet = m.toDatagramPacket();
		InetAddress addr = InetAddress.getByName(ip);
		InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_SRC_PORT);
		packet.setSocketAddress(socket_addr);
		socket.send(packet);
	}

	/**
	 * This method sends an ack packet to the sender of the datagram packet it has revieved
	 * 
	 * @param recievedPacket the packet thats been recieved for which the acknowledgment is being sent.
	 * 
	 * 
	 */
	public void sendACKPacket(DatagramPacket recievedPacket){
		PacketContent content= PacketContent.fromDatagramPacket(recievedPacket);
		Header recievedHeader = content.getHeader(); // the header for the packet thats been recieved
		AckPacketContent ack = new AckPacketContent("ACK"); // create a ACK packet
		// we must now create the header for the new ack packet
		String ackDestinationGatewayIP = recievedHeader.getSenderGatewayIP();
		String ackDestiationSubnetIP = recievedHeader.getSenderSubNetIP();
		ack.setHeader(mySubnetIP,gateWayIP,ackDestinationGatewayIP,ackDestiationSubnetIP); // set the header for the ack packet
		DatagramPacket packet = ack.toDatagramPacket();
		System.out.println("SENDING ACK PACKET T0: " + gateWayIP);
		//ack.getHeader().printHeader();
		try{
			InetAddress addr = InetAddress.getByName(gateWayIP); // send the packet to the networks gateWayIP
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_SRC_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("ACK PACKET SENT");
	}

	// this method prints out the message received in the packet
	public void printMessage(DatagramPacket packet){
		PacketContent p = PacketContent.fromDatagramPacket(packet);
		String message = p.toString();
		System.out.println("MESSAGE RECIEVED: " + message);

	}



	// This method directs the packets bases on their type
	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		// divert incoming packets to their respective handlers
		switch(content.getType()){ 
			case PacketContent.MESSAGE_PACKET:
				printMessage(packet); // print out the message to console
				sendACKPacket(packet); // send an ack packet for the mesage recieved
				break;
			case PacketContent.ACKPACKET:
				System.out.println("ACK RECIEVED");
				break;
			}
			

		this.notify();
	}

	
	public static void main(String[] args) {
		try {
			(new EndNode(DEFAULT_SRC_PORT,args[0],args[1],args[2])).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}


}








