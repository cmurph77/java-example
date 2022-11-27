/*
 * @author sweber
 * 
 * This class was provided in the java example
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Class for packet content that represents acknowledgements
 *
 */
public class AckPacketContent extends PacketContent {

	String info;
	Header header;

	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 * @param size Size of filename.
	 */
	AckPacketContent(String info) {
		type= ACKPACKET;
		this.info = info;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected AckPacketContent(ObjectInputStream oin) {
		try {
			type= ACKPACKET;
			info= oin.readUTF();
			String destinationGateWayIP = oin.readUTF();
    		String destinationSubnetIP = oin.readUTF();
			String senderSubNetIP = oin.readUTF();
    		String senderGatewayIP = oin.readUTF();
			header = new Header(senderSubNetIP, senderGatewayIP, destinationGateWayIP, destinationSubnetIP);
    		
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public void setHeader( String senderSubNetIP,String senderGatewayIP,String destinationGateWayIP,String destinationSubnetIP){
		header = new Header(senderSubNetIP, senderGatewayIP, destinationGateWayIP, destinationSubnetIP);
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(info);
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
		return "ACK:" + info;
	}

	/**
	 * Returns the info contained in the packet.
	 *
	 * @return Returns the info contained in the packet.
	 */
	public String getPacketInfo() {
		return info;
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
