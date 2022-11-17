import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class messagePacket extends PacketContent {

	String message;
	int size;
    String[] header;

	/**
	 * Constructor that takes in information about a file.
	 * @param message Initial message.
	 * @param size Size of message.
	 */
	messagePacket(String message, int size) {
		type= MESSAGE_PACKET;
		this.message = message;
		this.size= size;
        
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected messagePacket(ObjectInputStream oin) {
		try {
			type= MESSAGE_PACKET;
			message= oin.readUTF();
			size= oin.readInt();
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
			oout.writeInt(size);
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

	/**
	 * Returns the file size contained in the packet.
	 *
	 * @return Returns the file size contained in the packet.
	 */
	public int getFileSize() {
		return size;
	}
}
