import java.util.ArrayList;

public class Header {
    
    ArrayList<Integer> nodesOnRoute;
    // messagepacket header
    String senderSubNetIP;
    String senderGatewayIP;
    String destinationGateWayIP;
    String destinationSubnetIP;


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




    // this is a header for a message packet
    public Header(String senderSubNetIP, String senderGatewayIP, String destinationGateWayIP,String destinationSubnetIP) {
        this.senderSubNetIP = senderSubNetIP;
        this.senderGatewayIP = senderGatewayIP;
        this.destinationGateWayIP = destinationGateWayIP;
        this.destinationSubnetIP = destinationSubnetIP;
    }

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
