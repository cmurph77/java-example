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
	String myGateWayIp;
	String mySubnetIP;
	String testDesGateWayIP = "172.2.0.4"; 
	String testDesSubnetIP = "181.200.0.11";

	EndNode(int srcPort, String mySubnetIP,String myGateWayIp ) {
		this.myGateWayIp = myGateWayIp;
		this.mySubnetIP = mySubnetIP;
		testDesGateWayIP = "172.2.0.4"; 
		testDesSubnetIP = "181.200.0.11";
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
		System.out.println("ENTER MESSAGE TO SEND: ");
		if(s.hasNext()){
				String message = s.nextLine();
	            sendMessagePacket(message,myGateWayIp);

		}
		this.wait();
	}

	// sends a specific message packet
	public void sendMessagePacket(String messageToSend,String ip) throws IOException{
		messagePacket m = new messagePacket(messageToSend);
		m.setHeader(mySubnetIP, myGateWayIp, testDesGateWayIP, testDesSubnetIP);
		DatagramPacket packet = m.toDatagramPacket();
		// setting the address
		InetAddress addr = InetAddress.getByName(ip);
		InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_SRC_PORT);
		packet.setSocketAddress(socket_addr);
		socket.send(packet);
	}

	// sends a generic message packet
	public void sendMessagePacket(String ip) throws IOException {
		messagePacket m = new messagePacket("Hello from EndNode with ip address:  " + mySubnetIP);
		m.setHeader(mySubnetIP, myGateWayIp, testDesGateWayIP, testDesSubnetIP);
		//m.getHeader().printHeader();
		DatagramPacket packet = m.toDatagramPacket();
		// setting the address
		InetAddress addr = InetAddress.getByName(ip);
		InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_SRC_PORT);
		packet.setSocketAddress(socket_addr);
		socket.send(packet);
	}



	
	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		// divert incoming packets to their respective handlers
		switch(content.getType()){ 
			case PacketContent.MESSAGE_PACKET:
				handleMessagePacket(packet);
				break;
			case PacketContent.ACKPACKET:
				System.out.println("ACK RECIEVED");
				break;
			}
			

		this.notify();
	}

	private void handleMessagePacket(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		System.out.println(content.toString());
	}

	public static void main(String[] args) {
		try {
			(new EndNode(DEFAULT_SRC_PORT,args[0],args[1])).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}


}








