/*
 * @author cianmurphy
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.net.InetSocketAddress;
import java.net.InetAddress;


public class Server extends Node {
	static final int DEFAULT_PORT = 54321;
	static final String SERVER_NODE = "user";

	Server(int srcPort) {
		try {
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
		System.out.println("Packet received");
		this.notify();
	}


	public static void main(String[] args) {
		System.out.println("\n\nStarting Server Node...");
		try {
			(new Server(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
}
