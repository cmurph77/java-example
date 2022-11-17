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

	Fowarder(int srcPort, String ipAddr) {
		try {
			this.ipAddr = ipAddr;
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
		System.out.print("Node fowarding to ip" + ipAddr + "\n");
		//String ipAddress = String.format("IP Address : %s\n", InetAddress.getLocalHost().toString());
		//System.out.println(ipAddress);
		this.wait();
	}

	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		fowardPacket(packet);
		this.notify();
	}

	public void fowardPacket(DatagramPacket packet){
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
			(new Fowarder(DEFAULT_PORT,args[0])).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}


}








