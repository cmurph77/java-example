import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class getConnections {

    static int NODE_COUNT = 6;
    String node_IPs[];

    static File connections = new File("connections.txt");
    

    // public static ArrayList<String> getPublicIPs(int node){
    //    File ipAddresses = new File("nodeIpAddresses.txt");
    //     ArrayList<String> myIPs = new ArrayList<>();
    //     try (
    //         Scanner scanner = new Scanner(ipAddresses)) {
    //         while (scanner.hasNextLine()) {
    //             String line = scanner.nextLine();
    //             String[] tuple = line.split("->"); // isolates the node number from its addresses
    //             int currentNode = Integer.parseInt(tuple[0]);
    //             if(currentNode == node){
    //                 String[] ipAddresses = tuple[1].split(",");
    //                 for(int i = 0;i< ipAddresses.length;i++){
    //                     myIPs.add(ipAddresses[i]);
    //                 }
    //             }
    //         }

    //     } catch (FileNotFoundException e) {
    //         e.printStackTrace();
    //     }
    //     return myIPs;
    // }
}

