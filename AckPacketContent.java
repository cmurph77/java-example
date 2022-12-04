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
	 * @param info this is a mesage that can be encoded into the ack packet
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
			String destinationGateWayIP = oin.readUTF(); // read all the ip address 
    		String destinationSubnetIP = oin.readUTF();
			String senderSubNetIP = oin.readUTF();
    		String senderGatewayIP = oin.readUTF();
			header = new Header(senderSubNetIP, senderGatewayIP, destinationGateWayIP, destinationSubnetIP); // contstruct a header from the object input stream
    		
		}
		catch(Exception e) {e.printStackTrace();}
	}



	/**
	 * this method sets the header for the packet
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
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(info);
			oout.writeUTF(header.getDestinationGateWayIP()); // we have to write all the difference components of the header seperatley
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
