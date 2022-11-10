import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents acknowledgements
 * This class represents a packet that signifies a file transfer being complete. It wad built off the AckPacketContent class provided
 * 
 * @author cianmurphy
 */
public class TransferComplete 

 extends PacketContent {

	String info;
	
	/**
	 * Constructor that takes in information about a file.
	 * @param size Size of filename.
	 */
	TransferComplete(String info) {
		type= COMPLETE_PACKET;
		this.info = info;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected TransferComplete(ObjectInputStream oin) {
		try {
			type= COMPLETE_PACKET;
			info= oin.readUTF();
		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(info);
		}
		catch(Exception e) {e.printStackTrace();}
	}



	/**
	 * Returns the content of the packet as String.
	 *
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return "FILE_REQUEST:" + info;
	}

	/**
	 * Returns the info contained in the packet.
	 *
	 * @return Returns the info contained in the packet.
	 */
	public String getPacketInfo() {
		return info;
	}
}
