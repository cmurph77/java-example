import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class messagePacket extends PacketContent {

	String message;
	Header header;

	/**
	 * Constructor that takes in information about a file.
	 * @param message Initial message.
	 * @param size Size of message.
	 */
	messagePacket(String message) {
		this.message = message;
	}

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
	 * Returns the file name contained in the packet.
	 *
	 * @return Returns the file name contained in the packet.
	 */
	public String getmessage() {
		return message;
	}

	@Override
	public Header getHeader() {
		return header;
	}

	@Override
	public String getTargetDestination() {
		return null;
	}

	@Override
	public int getNode() {
		return 0;
	}

	@Override
	public String getNextNodeIP() {
		return null;
	}

}
