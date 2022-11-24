/*
 * @author cianmurphy
 * 
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FlowMod extends PacketContent {

	String targetDestination;    // this is the destination for the packet
	String nextNodeIP;           // this is the next ip address the fowarder must foward to.
	int nodeID;
	

	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public String getTargetDestination() {
		return targetDestination;
	}

	public String getNextNodeIP() {
		return nextNodeIP;
	}

	public FlowMod(String targetDestination, String nextNodeIP) {
		this.targetDestination = targetDestination;
		this.nextNodeIP = nextNodeIP;
		type= FlOW_MOD;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected FlowMod(ObjectInputStream oin) {
		try {
			type= FlOW_MOD;
			targetDestination= oin.readUTF();
			nextNodeIP= oin.readUTF();
			nodeID = oin.readInt();

		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(targetDestination);
			oout.writeUTF(nextNodeIP);
			oout.writeInt(nodeID);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public String toString() {
		String output = "FLOWMODE: SEND TO IP-> " + nextNodeIP + " TO GET TO IP-> " + targetDestination;
		return output;
	}

	@Override
	public Header getHeader() {
		return null;
	}

	@Override
	public int getNode() {
		return 0;
	}


}

