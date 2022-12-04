import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class messagePacket extends PacketContent {

	String message;
	Header header; // packet header

	/**
	 * Constructor that takes in information about a file.
	 * @param message Initial message.
	 * @param size Size of message.
	 */
	messagePacket(String message) {
		this.message = message;
		type= MESSAGE_PACKET;
	}

	/**
	 * This method sets the header for the message packet
	 * 
	 * @param senderSubNetIP
	 * @param senderGatewayIP
	 * @param destinationGateWayIP
	 * @param destinationSubnetIP
	 */
	public void setHeader( String senderSubNetIP,String senderGatewayIP,String destinationGateWayIP,String destinationSubnetIP){
		header = new Header(senderSubNetIP, senderGatewayIP, destinationGateWayIP, destinationSubnetIP);
	}



	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected messagePacket(ObjectInputStream oin) {
		try {
			type= MESSAGE_PACKET;
			message= oin.readUTF();
			String destinationGateWayIP = oin.readUTF();
    		String destinationSubnetIP = oin.readUTF();
			String senderSubNetIP = oin.readUTF();
    		String senderGatewayIP = oin.readUTF();
			header = new Header(senderSubNetIP, senderGatewayIP, destinationGateWayIP, destinationSubnetIP);
    		
		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(message);
			oout.writeUTF(header.getDestinationGateWayIP());
			oout.writeUTF(header.getDestinationSubnetIP());
			oout.writeUTF(header.getSenderSubNetIP());
			oout.writeUTF(header.getSenderGatewayIP());


		}
		catch(Exception e) {e.printStackTrace();}
	}


	/**
	 * Returns the content of the packet as String.
	 *
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return "message: " + message;
	}

	/**
	 * 
	 *
	 * @return Returns the messagge contained in the packet.
	 */
	public String getmessage() {
		return message;
	}

	// returns the message header
	@Override
	public Header getHeader() {
		return header;
	}

	// No needed for this packet type
	@Override
	public String getTargetDestination() {
		return null;
	}

	// No needed for this packet type
	@Override
	public int getNode() {
		return 0;
	}

	// No needed for this packet type
	@Override
	public String getNextNodeIP() {
		return null;
	}

}
