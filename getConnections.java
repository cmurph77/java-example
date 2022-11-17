import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class getConnections {

    static int NODE_COUNT = 6;
    String node_IPs[];

    static File connections = new File("connections.txt");
    
    public static String[] getConnections(int node){
        try (
            Scanner scanner = new Scanner(connections)) {
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=>");
                int currentNode = Integer.parseInt(parts[0]);
                if(currentNode == node){
                    return convertLineToConnectionArray(parts[1]);
                }
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // this should not be reached as we should 
        String[] errorString = {"Error"};
        return errorString;
    }

    // this method converts the line containing conected nodes and their respective ip
    // addresses into an array
    public static String[] convertLineToConnectionArray(String line){
        String[] ipAddresses = new String[NODE_COUNT + 2];
        String[] nodeAndIps = line.split(",");
        for(int i = 0; i< nodeAndIps.length ;i = i+2){
            int currentNode = Integer.parseInt(nodeAndIps[i]); // the first entry is the node
            String ip = nodeAndIps[i+1];                // second entry is its ip address
            ipAddresses[currentNode] = ip;
        }
        return ipAddresses;
    }
}

