/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;





public class Fowarder extends Node {
	static final int DEFAULT_PORT = 54321;
	static final String FOWARDER_NODE = "Fowarder";
	String controllerIP = "172.60.0.10";
	ArrayList<Integer> connections = new ArrayList<>();
	String ipAddr;
	String ipAddresses;
	int node;

	Fowarder(int srcPort, int node) {
		try {
			this.node = node;
			String[] ipAddresses = getConnections.getConnections(node);
			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	 * This method gets called to start up the Fowarder
	 * Node and send a file request out.
	 */
	public synchronized void start() throws Exception {
		System.out.println("This is node: " + node);
		this.wait();
	}

	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		//TODO - HANDLE FOWARDING

		//fowardPacket(packet,);
		this.notify();
	}

	public void fowardPacket(DatagramPacket packet,String address){
		System.out.println("\n Fowarding packet to IP:" + ipAddr + "\n");
		try{
			FileInfoContent f = new FileInfoContent("hello",1);
			//DatagramPacket packet = f.toDatagramPacket();
			// setting the address
			InetAddress addr = InetAddress.getByName(ipAddr);
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








