/*
 * @author cianmurphy
 * 
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FlowReq extends PacketContent {

	String targetDestination;    // this is the destination for the packet\
	int node;


	public String getTargetDestination() {
		return targetDestination;
	}

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

	@Override
	public Header getHeader() {
		return null;
	}

	@Override
	public int getNode() {
		return node;
	}


}

