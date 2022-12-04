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
    String[] fowardersAddresses = {    // addresses for all the fowarder nodes on the netowrk.
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
		setUpRoutingTable();  
		this.wait();
	}

	public synchronized void onReceipt(DatagramPacket packet) {
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



	/**
	 * This method looks up controller table and sends back a flow mod packet to the controller who send the flow request
	 * 
	 * @param packet
	 */
	private void handleFlowReq(DatagramPacket packet) {
		PacketContent content= PacketContent.fromDatagramPacket(packet);
		int fromNode = content.getNode(); 								// get the NODE ID of the fowarder that send flowrequest
		String targetDestination = content.getTargetDestination();      // get the destination IP the fowarder node is looking for a route to
		System.out.println("FLOW REQUEST RECIEVED FROM NODE: " + fromNode + ", FOR TARGET DESTINATION: " + targetDestination);
		RoutingTable r = controllerTable.get(fromNode);                // get specific routing table for the node
		String nextIP = r.getRoute(targetDestination);                 // find the ip for the next node that packet should be fowarded to
		sendFlowMod(fromNode, nextIP, targetDestination);              // send a flow mode packet to the fowarder

	}


	/**
	 * This method sends a flow mod packet for a fowarder to update its routing table
	 * 
	 * @param toNode The node to send the flowmod to.
	 * @param nextIP The next ip address for fowarder to send the packet to 
	 * @param targetDestination the targer destination the packet want to to get to.
	 */
	public void sendFlowMod(int toNode, String nextIP, String targetDestination){
		System.out.println("SENDING FLOW MOD TO NODE: " + toNode + ",WITH NEXT IP: " + nextIP);

		FlowMod f = new FlowMod(targetDestination,nextIP); 			// create flow mod packet
		DatagramPacket packet = f.toDatagramPacket();
		
		try {
			System.out.println("SENDING FLOW MOD TO IP: " + fowardersAddresses[toNode]);
			System.out.println("---------------------------------------------------------");

			InetAddress addr = InetAddress.getByName(fowardersAddresses[toNode]);  // send to fowardes address that is stored by the controller in an array
			InetSocketAddress socket_addr = new InetSocketAddress(addr, DEFAULT_PORT);
			packet.setSocketAddress(socket_addr);
			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method sets up the routing table for the controller. the fowarding data is hardcoded in. 
	 */
	public void setUpRoutingTable(){
		controllerTable = new HashMap<>();
		RoutingTable r;

		// NODE 2 ROUTES
		r = new RoutingTable();
		r.setRoute("172.1.0.3", "172.1.0.3"); // node 3
		r.setRoute("172.1.0.3", "172.2.0.4"); // node 4
		r.setRoute("172.4.0.5", "172.4.0.5"); // node 5
		r.setRoute("172.1.0.7", "172.1.0.7"); // node 7
		r.setRoute("172.1.0.3", "172.3.0.6"); // node 6
		controllerTable.put(2,r);
		
		// node 3
		r = new RoutingTable();
		r.setRoute("172.1.0.2", "172.1.0.2"); // node2
		r.setRoute("172.2.0.4", "172.2.0.4"); // node 4
		r.setRoute("172.1.0.2", "172.4.0.5"); // node 5
		r.setRoute("172.2.0.6", "172.3.0.6"); // node 6	
		r.setRoute("172.1.0.7", "172.1.0.7"); // node 7
		controllerTable.put(3,r);
		
		//node 4
		r = new RoutingTable();
		r.setRoute("172.1.0.3", "172.1.0.2"); // node2
		r.setRoute("172.1.0.3", "172.1.0.3"); // node 3
		r.setRoute("172.2.0.6", "172.4.0.5"); // node 5
		r.setRoute("172.2.0.6", "172.2.0.6"); // node 6
		r.setRoute("172.2.0.3", "172.1.0.7"); // node 7
		controllerTable.put(4,(r));
		
		// node5
		r = new RoutingTable();
		r.setRoute("172.4.0.2", "172.1.0.2"); // node2
		r.setRoute("172.3.0.6", "172.1.0.3");
		r.setRoute("172.3.0.6", "172.2.0.3");
		r.setRoute("172.3.0.6", "172.2.0.4"); // node 4
		r.setRoute("172.4.0.2", "172.1.0.7"); // node 7
		r.setRoute("172.3.0.6", "172.3.0.6"); // node 6
		controllerTable.put(5,r);
		
		// node 6
		r = new RoutingTable();
		r.setRoute("172.2.0.3", "172.1.0.2"); // node2
		r.setRoute("172.2.0.3", "172.1.0.3"); //node 3
		r.setRoute("172.2.0.4", "172.2.0.4"); // node 4
		r.setRoute("172.3.0.5", "172.4.0.5"); // node 5
		r.setRoute("172.2.0.3", "172.1.0.7"); // node 7
		controllerTable.put(6,r);
		
		// node 7
		r = new RoutingTable();
		r.setRoute("172.1.0.2", "172.1.0.2"); // node2
		r.setRoute("172.1.0.3", "172.1.0.3"); //node 3
		r.setRoute("172.1.0.3", "172.2.0.4"); // node 4
		r.setRoute("172.1.0.2", "172.4.0.5"); // node 5
		r.setRoute("172.1.0.3", "172.3.0.6"); // node 6
		controllerTable.put(7,r);
	}


	public static void main(String[] args) {
		System.out.println("\n\nSTARTING CONTOLLER NODE...");
		try {
			(new Controller(DEFAULT_PORT)).start();
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
}
