/**
 * @author cianmurphy
 */
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;


/**
 *
 * User class
 *
 * An instance accepts User input
 *
 */
public class User extends Node {
	static final int DEFAULT_SRC_PORT = 54321;
	static final int SERVER_PORT = 50000;
	static final String USER_NODE = "User";
	

	
	/*
	 * constructor for creating an new instance of a User and initializing the Inet Adresses
	 */
	User(int srcPort) {
		try {
			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/*
	 * This method gets called to start up the User Node and send a file request out.
	 */
	public synchronized void start() throws Exception {
		FileInfoContent f = new FileInfoContent("hello",1);
		DatagramPacket packet = f.toDatagramPacket();
		InetSocketAddress serverAddr = new InetSocketAddress(DEFAULT_SRC_PORT);
		packet.setSocketAddress(serverAddr);
		socket.send(packet);
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
		try {
			(new User(DEFAULT_SRC_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}








