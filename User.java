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






public class User extends Node {
	static final int DEFAULT_SRC_PORT = 54321;
	static final String USER_NODE = "user";
	static final int CONTROLLER_PORT = 50000;
	//static final String USER_NODE = "user";
	static final String CONTROLLER_NODE = "controller";
	static final String FOWARDER_NODE = "fowarder";
	InetSocketAddress addr;
	String fowarderIP = "192.168.17.7";

	

	

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
		//String ipAddress = String.format("IP Address : %s\n", InetAddress.getLocalHost().toString());
		//System.out.println(ipAddress);
		sendPacket();
		//this.wait();
	}

	public void sendPacket() throws IOException{
		FileInfoContent f = new FileInfoContent("hello",1);
		DatagramPacket packet = f.toDatagramPacket();
		// setting the address
		InetAddress addr = InetAddress.getByName(fowarderIP);
		InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_SRC_PORT);
		packet.setSocketAddress(socket_addr);
		socket.send(packet);
	}



	
	public synchronized void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved");
		this.notify();
	}
	
	



	public static void main(String[] args) {
		try {
			(new User(DEFAULT_SRC_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}








