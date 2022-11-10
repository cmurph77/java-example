import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 * This class represents file packets that are being sent. It wad built off the AckPacketContent class provided
 * 
 * @author cianmurphy
 */
public class FilePackets extends PacketContent {

    static final int MAX_FRAME_SIZE = 100;
    static final int HEADER_LENGTH = 10;
    static final int MAX_PAYLOAD_LENGTH = MAX_FRAME_SIZE - HEADER_LENGTH;


    byte[] packetBytes;
    byte[] header;
    int length;

    FilePackets(byte[] packetBytes,byte[] header, int length){
        this.header = header;
        this.packetBytes = packetBytes;
        this.length = length;
    }

    FilePackets(byte[] bytes, int length ){
        this.length = length;  // set the length
        type = FILEPACKET;     // set type
        packetBytes = bytes;   
        header = new byte[HEADER_LENGTH];
        byte len = (byte) length;
        header[0] = len;
    }

    public byte[] getPacketBytes(){
        return packetBytes;
    }

    public byte[] getHeader(){
        return header;
    }

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected FilePackets(ObjectInputStream oin) {
		try {
			type= FILEPACKET;
            header = new byte[HEADER_LENGTH];
            oin.readFully(header,0,HEADER_LENGTH);
            byte len = header[0];
            length = (int) len;
            //System.out.println("length" + length);
            packetBytes = new byte[length + HEADER_LENGTH];
			oin.readFully(packetBytes,HEADER_LENGTH,length);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
            //System.out.println("packetbytes length: " + packetBytes.length);
            oout.write(header);
			oout.write(packetBytes);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}


}
