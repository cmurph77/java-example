/*
 * @author cianmurphy
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;




public class Controller extends Node {
	static final int DEFAULT_PORT = 54321;
    String[] fowardersAddresses = {
        "null",
        "null",
        "172.60.0.2", // node 2
        "172.60.0.3", // node 3
        "172.60.0.4", // node 4
        "172.60.0.5", // node 5
        "172.60.0.6", // node 6
        "172.60.0.7", // node 7
    };
	int nodeCount = 6;
	 HashMap<Integer,RoutingTable> controllerTable;

	Controller(int srcPort) {
		try {
			socket = new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	// Waiting here for contact.
	public synchronized void start() throws Exception {
		//System.out.println(Arrays.toString(fowardersAddresses));
		setUpRoutingTable();
		this.wait();
	}

	public synchronized void onReceipt(DatagramPacket packet) {
		//System.out.println("Packet Recieved");
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		// divert incoming packets to their respective handlers
		switch(content.getType()){
			case PacketContent.FlOW_REQ:
			System.out.println("\n---------------------------------------------------------");

				handleFlowReq(packet);
				break; 
			}

		//this.notify();
	}


	private void handleFlowReq(DatagramPacket packet) {
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		int fromNode = content.getNode();
		String targetDestination = content.getTargetDestination();
		System.out.println("FLOW REQUEST RECIEVED FROM NODE: " + fromNode + ", FOR TARGET DESTINATION: " + targetDestination);

		RoutingTable r = controllerTable.get(fromNode);
		//r.printTable();
		String nextIP = r.getRoute(targetDestination);
		sendFlowMod(fromNode, nextIP, targetDestination);

	}

	public void sendFlowMod(int toNode, String nextIP, String targetDestination){
		System.out.println("SENDING FLOW MOD TO NODE: " + toNode + ",WITH NEXT IP: " + nextIP);

		FlowMod f = new FlowMod(targetDestination,nextIP);
		DatagramPacket packet = f.toDatagramPacket();
		
		try {
			System.out.println("SENDING FLOW MOD TO IP: " + fowardersAddresses[toNode]);
			System.out.println("---------------------------------------------------------");

			InetAddress addr = InetAddress.getByName(fowardersAddresses[toNode]);
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void setUpRoutingTable(){
		controllerTable = new HashMap<>();
		// TODO write algorithm to find route
		RoutingTable r;

		// node 2
		controllerTable.put(2,(new RoutingTable()));
		
		// node 3
		r = new RoutingTable();
		r.setRoute("172.2.0.4", "172.2.0.4");
		r.setRoute("172.1.0.7", "172.1.0.7");
		controllerTable.put(3,r);
		
		//node 4
		r = new RoutingTable();
		r.setRoute("172.2.0.3", "172.1.0.7");
		controllerTable.put(4,(r));
		
		// node5
		controllerTable.put(5,(new RoutingTable()));
		
		// node 6
		controllerTable.put(6,(new RoutingTable()));
		
		// node 7
		r = new RoutingTable();
		r.setRoute("172.1.0.3", "172.2.0.4");
		controllerTable.put(7,r);
	}


	public static void main(String[] args) {
		System.out.println("\n\nSTARTING CONTOLLER NODE...");
		try {
			(new Controller(DEFAULT_PORT)).start();
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
}
