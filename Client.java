/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;

/**
 *
 * Client class
 *
 * An instance accepts user input
 *
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 54321;
	static final String CLIENT_NODE = "client";
	

	
	/*
	 * constructor for creating an new instance of a client and initializing the Inet Adresses
	 */
	Client(int srcPort) {
		try {
			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	 * This method gets called to start up the Client Node and send a file request out.
	 */
	public synchronized void start() throws Exception {
		DatagramPacket packet = new DatagramPacket(null, PACKETSIZE);
		// InetSocketAddress addr = new InetSocketAddress(null, CLIENT_PORT);
		this.wait();
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		//this.notify();
	}
	
	


	/**
	 * Test method
	 *
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		System.out.println("\n\nStarting Client Node...");
		try {
			(new Client(DEFAULT_SRC_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}








