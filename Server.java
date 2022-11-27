/*
 * @author cianmurphy
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.InetSocketAddress;


public class Server extends Node {
	static final int DEFAULT_PORT = 54321;
	static final String SERVER_NODE = "user";
	String myGateWayIp = "181.200.0.4";  // TODO SET THESE PROPERLY
	String mySubnetIP = "181.200.0.11";
	String testDesGateWayIP = "172.1.0.7"; 
	String testDesSubnetIP = "192.168.17.1";

	Server(int srcPort) {
		try {
			// TODO set gateway and subnet ip from start

			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	// Waiting here for contact.
	public synchronized void start() throws Exception {
		this.wait();
	}

	//@Override
	public void onReceipt(DatagramPacket packet) {
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		switch(content.getType()){
			case PacketContent.MESSAGE_PACKET:
			printMessage(packet);
			sendACKPacket(packet);
			break;
			case PacketContent.ACKPACKET:
			//handleACKPacket(packet);
			break;
		}
		//this.notify();
	}
	
	public void sendACKPacket(DatagramPacket recievedPacket){
		PacketContent content= PacketContent.fromDatagramPacket(recievedPacket);
		Header senderHeader = content.getHeader();
		mySubnetIP = senderHeader.getDestinationSubnetIP();
		AckPacketContent ack = new AckPacketContent("ACK"); //TODO add more parameters to ack packet
		ack.setHeader(mySubnetIP,myGateWayIp,testDesGateWayIP,testDesSubnetIP);
		DatagramPacket packet = ack.toDatagramPacket();
		System.out.println("SENDING ACK PACKET T0: " + myGateWayIp);
		//ack.getHeader().printHeader();
		try{
			InetAddress addr = InetAddress.getByName(myGateWayIp);
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("ACK PACKET SENT");
	}

	// public void sendACKPacket(){
	// 	AckPacketContent ack = new AckPacketContent("ACK"); //TODO add more parameters to ack packet
	// 	ack.setHeader(mySubnetIP,myGateWayIp,testDesGateWayIP,testDesSubnetIP);
	// 	System.out.println("SENDING ACK PACKET T0: " + myGateWayIp);
	// 	try{
	// 		InetAddress addr = InetAddress.getByName(myGateWayIp);
	// 		InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
	// 		packet.setSocketAddress(socket_addr);
	// 		socket.send(packet);
	// 	}catch(IOException e){
	// 		e.printStackTrace();
	// 	}
	// 	System.out.println("ACK PACKET SENT");
	// }

	public void printMessage(DatagramPacket packet){
		PacketContent p = PacketContent.fromDatagramPacket(packet);
		String message = p.toString();
		System.out.println("MESSAGE RECIEVED: " + message);

	}


	public static void main(String[] args) {
		System.out.println("\n\nStarting Server Node...");
		try {
			(new Server(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
}
