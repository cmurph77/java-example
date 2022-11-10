/*
 * @author cianmurphy
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


public class Server extends Node {
	static final int DEFAULT_PORT = 50000;



	
	Server(int port) {
		try {
			socket = new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	// Waiting here for contact.
	public synchronized void start() throws Exception {
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
