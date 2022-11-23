import java.util.HashMap;

public class RoutingTable {
    static final int NODE_COUNT = 6;
    HashMap<String,String> table;
    RoutingTable(){
        table = new HashMap<>(); 
    }

    public void setRoute(String nextIP, String destinationIP){
        table.put(destinationIP, nextIP);
    }

    public boolean routeExists(String destinationIP){
        String nextIP = table.get(destinationIP);
        if(nextIP == null) return false;
        return true;
    }

    public String getRoute(String destinationIP){
        return table.get(destinationIP);
    }
}
