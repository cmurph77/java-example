/*
 * @author cianmurphy
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


public class Server extends Node {
	static final int DEFAULT_PORT = 54321;
	static final int CLIENT_PORT = 5000;
	static final String CLIENT_NODE = "client";
	static final String WORKER_NODE = "worker";

	InetSocketAddress worker_addr;
	InetSocketAddress client_addr;


	
	Server(int port) {
		try {
			socket= new DatagramSocket(port);
			//worker_addr = new InetSocketAddress(WORKER_NODE, WORKER_PORT);
			client_addr = new InetSocketAddress(CLIENT_NODE, CLIENT_PORT);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	// Waiting here for contact.
	public synchronized void start() throws Exception {
			System.out.println("Waiting for contact");
			this.wait();
		}


	public static void main(String[] args) {
		System.out.println("\n\nStarting Server Node...");
		try {
			(new Server(DEFAULT_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		System.out.println("Packet received");
		
	}
}
