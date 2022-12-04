import java.util.ArrayList;
/**
 * This class represents a packet header 
 * 
 * @author cianmurphy
 */

public class Header {
    
    ArrayList<Integer> nodesOnRoute;
    String senderSubNetIP; 
    String senderGatewayIP;  // this is the ip address of the local fowarder on the nodes netword
    String destinationGateWayIP;
    String destinationSubnetIP; // this isthe ip for the fowarder on the destination nodes netowrk

    String ACK_GatewayIP;

    public String getSenderSubNetIP() {
        return senderSubNetIP;
    }




    public String getSenderGatewayIP() {
        return senderGatewayIP;
    }




    public String getDestinationGateWayIP() {
        return destinationGateWayIP;
    }




    public String getDestinationSubnetIP() {
        return destinationSubnetIP;
    }




    // creates a header
    public Header(String senderSubNetIP, String senderGatewayIP,String destinationGateWayIP,String destinationSubnetIP) {
        this.senderSubNetIP = senderSubNetIP;
        this.senderGatewayIP = senderGatewayIP;
        this.destinationGateWayIP = destinationGateWayIP;
        this.destinationSubnetIP = destinationSubnetIP;
    }

    /**
     * This method prints out the header, Mainly used for debugging purposes
     */
    public void printHeader(){
        System.out.println(" - senderSubnetIP: " + senderSubNetIP);
        System.out.println(" - senderGateWayIP: " + senderGatewayIP);
        System.out.println(" - destinationGateWayIP: "+ destinationGateWayIP);
        System.out.println(" - destinationSubNetIP: "+ destinationSubnetIP);
    }




    public void addNode(int node){
        nodesOnRoute.add(node);
    }

    public ArrayList<Integer> getRouteTaken() {
        return nodesOnRoute;
    }


    
}
