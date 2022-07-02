import java.io.Serializable;
import java.util.*;

public class RoutingTable implements Serializable {

    static final long serialVersionUID = 99L;
    private final Router router;
    private final Network network;
    private final List<RoutingTableEntry> entryList;

    public RoutingTable(Router router) {
        this.router = router;
        this.network = router.getNetwork();
        this.entryList = new ArrayList<>();
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Router getRouter() {
        return router;
    }

    public Network getNetwork() {
        return network;
    }

    /**
     * updateTable() should calculate routing information and then instantiate RoutingTableEntry objects, and finally add
     * them to RoutingTable object’s entryList.
     */
    public void updateTable() {
        // TODO: YOUR CODE HERE

        network.resetDist();
        network.resetPath();
        List<Link> links = network.getLinks();

        HashSet<Router> unsettled = new HashSet<Router>();
        HashSet<Router> settled = new HashSet<Router>();





        unsettled.add(this.router);
        this.router.setDistance(0);

        while(!unsettled.isEmpty()){
            Router curr = Lowest(unsettled);

            unsettled.remove(curr);

            for(Link link : links){
                if(link.getIpAddress1().equals(curr.getIpAddress())||link.getIpAddress2().equals(curr.getIpAddress())){

                    String otherIP = link.getOtherIpAddress(curr.getIpAddress());

                    Router adjacent = network.getRouterByAdress(otherIP);// adjacent router now

                    if(adjacent!=null){
                        double cost = link.getCost();


                        if(!settled.contains(adjacent)){

                            Dist(adjacent,cost,curr,link);
                            unsettled.add(adjacent);

                        }
                    }

                }
            }

            settled.add(curr);

        }


        for (Router router : this.network.getRouters()) {
            if (!router.equals(this.router)) {
                RoutingTableEntry entry = new RoutingTableEntry(this.router.getIpAddress(), router.getIpAddress(), router.getShortestPath(), router.getDistance());
                entryList.add(entry);
            }
        }






    }
    private static void Dist(Router adjacent , double cost, Router curr,Link link){
        double sourceDistance = curr.getDistance();


        if (sourceDistance + cost < adjacent.getDistance()) {
            adjacent.setDistance(sourceDistance + cost);
            Stack<Link> shortestPath;
            shortestPath=(Stack<Link>)(curr.getShortestPath().clone());


            shortestPath.push(link);
            adjacent.setShortestPath(shortestPath);
        }
    }
    private static Router Lowest(HashSet<Router> unsettled){
        Router minRouter = null;
        double lowest = Integer.MAX_VALUE;
        for(Router router : unsettled){
            if(router.getDistance()<lowest){
                lowest = router.getDistance();
                minRouter = router;
            }
        }

        return minRouter;
    }

    /**
     * pathTo(Router destination) should return a Stack<Link> object which contains a stack of Link objects,
     * which represents a valid path from the owner Router to the destination Router.
     *
     * @param destination Destination router
     * @return Stack of links on the path to the destination router
     */
    public Stack<Link> pathTo(Router destination) {
        // TODO: YOUR CODE
        return this.router.getShortestPath();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingTable that = (RoutingTable) o;
        return router.equals(that.router) && entryList.equals(that.entryList);
    }

    public List<RoutingTableEntry> getEntryList() {
        return entryList;
    }
}
