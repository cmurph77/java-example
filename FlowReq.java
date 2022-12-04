/*
 * @author cianmurphy
 * 
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FlowReq extends PacketContent {

	String targetDestination;    // this is the destination for the packet\
	int node;                    // the node ID of the fowarder thats sending the packet


	// returns the target destination that the flow request is for
	public String getTargetDestination() {
		return targetDestination;
	}

	// constructs a flow request packet
	public FlowReq(String targetDestination, int node) {
		this.targetDestination = targetDestination;
		this.node = node;
		type= FlOW_REQ;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected FlowReq(ObjectInputStream oin) {
		try {
			type= FlOW_REQ;
			targetDestination= oin.readUTF();
			node = oin.readInt();
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
			oout.writeInt(node);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public String toString() {
		return targetDestination;
	}

	// method not implemented
	@Override
	public Header getHeader() {
		return null;
	}

	// returns the node id of the fowarding sending the packet
	@Override
	public int getNode() {
		return node;
	}

	// method not implemented
	@Override
	public String getNextNodeIP() {
		return null;
	}


}

