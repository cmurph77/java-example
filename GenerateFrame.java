import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * This class contains a method for converting a file into a list of datagram packets of file bytes 
 * and a method for translating incoming datagram packets back into a file
 * 
 * @author cianmurphy
 */
public class GenerateFrame {

    static final int HEADER_LENGTH = FilePackets.HEADER_LENGTH;
    static final int MAX_PAYLOAD_LENGTH = FilePackets.MAX_PAYLOAD_LENGTH;
    static final int MAX_FRAME_SIZE = FilePackets.MAX_FRAME_SIZE;

    // takes a file and returns a buffer of Datagram packets to be sent
    public ArrayList<DatagramPacket> fileToPackets(String fileName) throws IOException{
        ArrayList<DatagramPacket> packetBuffer = new ArrayList<>();  // create the list of data packets to be sent
        File file = new File(fileName);               // create the file from the file name
        FileInputStream fin = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];  // create a buffer the length of the file
        int bufferSize = fin.read(buffer); // read the fin into the byte buffer
        byte[] subArray;
        int count = 0;
        for(int i = 0; i < bufferSize; i = i + MAX_PAYLOAD_LENGTH){
            if(i+MAX_PAYLOAD_LENGTH <= bufferSize ){
                subArray = Arrays.copyOfRange(buffer, i, i+MAX_PAYLOAD_LENGTH);
            }else {
                subArray = Arrays.copyOfRange(buffer, i, bufferSize - 1);
            }
            FilePackets filePacket = new FilePackets(subArray,subArray.length);
            DatagramPacket packet = filePacket.toDatagramPacket();
            packetBuffer.add(packet);
        } 
        fin.close();
        return packetBuffer;
    }

    // takes a list of FILEPACKET datagram packets and rebuilds the the file
    public static File packetsToFile(ArrayList<DatagramPacket> packetBuffer, String fileName) throws IOException{
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        ArrayList<Byte> byteBuffer;
        int packetCount = packetBuffer.size();
        for(int i = 0; i < packetCount;i++ ){
            DatagramPacket currentPacket = packetBuffer.get(i);
            PacketContent content = PacketContent.fromDatagramPacket(currentPacket);
            byte[] currentHeader = ((FilePackets)content).getHeader();
            byte[] currentPacketBytes = ((FilePackets)content).getPacketBytes();
            int packetLength = (int) currentHeader[0];
            fos.write(currentPacketBytes);
        } 
        fos.close();
        return file;

    }
    

}
