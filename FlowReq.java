/*
 * @author cianmurphy
 * 
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FlowReq extends PacketContent {

	String targetDestination;    // this is the destination for the packet


	public String getTargetDestination() {
		return targetDestination;
	}

	public FlowReq(String targetDestination) {
		this.targetDestination = targetDestination;
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
		// TODO Auto-generated method stub
		return null;
	}


}

