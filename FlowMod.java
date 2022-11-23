/*
 * @author cianmurphy
 * 
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FlowMod extends PacketContent {

	String targetDestination;    // this is the destination for the packet
	String nextNodeIP;           // this is the next ip address the fowarder must foward to.

	

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
		}
		catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public String toString() {
		// TODO flow mod to string
		return null;
	}

	@Override
	public Header getHeader() {
		return null;
	}


}

