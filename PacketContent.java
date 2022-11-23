import java.net.DatagramPacket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * The class is the basis for packet contents of various types.
 * 
 * This class was provided in the example and was not altered in a major way
 * 
 * @author sweber
 */
public abstract class PacketContent {

	public static final int ACKPACKET= 10;
	public static final int MESSAGE_PACKET = 14;
	public static final int FlOW_MOD = 15;
	public static final int FlOW_REQ = 16;

	

	int type= 0;

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet to analyse.
	 */
	public static PacketContent fromDatagramPacket(DatagramPacket packet) {
		PacketContent content= null;

		try {
			int type;

			byte[] data;
			ByteArrayInputStream bin;
			ObjectInputStream oin;

			data= packet.getData();  // use packet content as seed for stream
			bin= new ByteArrayInputStream(data);
			oin= new ObjectInputStream(bin);

			type= oin.readInt();  // read type from beginning of packet

			switch(type) {   // depending on type create content object
			case ACKPACKET:
				content= new AckPacketContent(oin);
				break;
			case FlOW_MOD:
				content= new FlowMod(oin);
				break;
			case FlOW_REQ:
				content= new FlowReq(oin);
				break;
			case MESSAGE_PACKET:
				content= new messagePacket(oin);
				break;
			default:
				content= null;
				break;
			}
			oin.close();
			bin.close();

		}
		catch(Exception e) {e.printStackTrace();}

		return content;
	}


	/**
	 * This method is used to transform content into an output stream.
	 *
	 * @param out Stream to write the content for the packet to.
	 */
	protected abstract void toObjectOutputStream(ObjectOutputStream out);

	/**
	 * Returns the content of the object as DatagramPacket.
	 *
	 * @return Returns the content of the object as DatagramPacket.
	 */
	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet= null;

		try {
			ByteArrayOutputStream bout;
			ObjectOutputStream oout;
			byte[] data;

			bout= new ByteArrayOutputStream();
			oout= new ObjectOutputStream(bout);

			oout.writeInt(type);         // write type to stream
			toObjectOutputStream(oout);  // write content to stream depending on type

			oout.flush();
			data= bout.toByteArray(); // convert content to byte array

			packet= new DatagramPacket(data, data.length); // create packet from byte array
			oout.close();
			bout.close();
		}
		catch(Exception e) {e.printStackTrace();}

		return packet;
	}


	/**
	 * Returns the content of the packet as String.
	 *
	 * @return Returns the content of the packet as String.
	 */
	public abstract String toString();


	/**
	 * Returns the type of the packet.
	 *
	 * @return Returns the type of the packet.
	 */
	public int getType() {
		return type;
	}

	public abstract Header getHeader();
	public abstract String getTargetDestination();
	public abstract int getNode();
	public abstract String getNextNodeIP();

}
