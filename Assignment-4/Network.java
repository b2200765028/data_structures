import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Network implements Serializable {


    static final long serialVersionUID = 55L;
    private List<Router> routers = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setRouters(List<Router> routers) {
        this.routers = routers;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * The constructor should read the given file and generate necessary Router and Link objects and initialize link
     * and router arrays.
     * Also, you should implement Link class’s calculateAndSetCost() method in order for the costs to be calculated
     * based on the formula given in the instructions.
     *
     * @param filename Input file to generate the network from
     * @throws FileNotFoundException
     */
    public Network(String filename) throws FileNotFoundException {
        // TODO: YOUR CODE HERE
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        Pattern pattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Pattern patternDoubleIP = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}-[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Pattern routerPat = Pattern.compile("RouterIP");
        Pattern linkPat = Pattern.compile("Link");
        Pattern bandPat = Pattern.compile("[0-9]{1,5} Mbps");


        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            Matcher matcherIP = pattern.matcher(line);

            Matcher matcherRouter = routerPat.matcher(line);
            Matcher matcherLink = linkPat.matcher(line);

            if(matcherRouter.find()){
                if (matcherIP.find()) {
                    String str = matcherIP.group(0);
                    Router router = new Router(str,this);

                    routers.add(router);
                }
            }
            else if(matcherLink.find()){
                Matcher matcherDouble = patternDoubleIP.matcher(line);
                Matcher matcherBandwidth = bandPat.matcher(line);
                if (matcherDouble.find()) {
                    if(matcherBandwidth.find()){
                        String str = matcherDouble.group();
                        String ip1 = str.split("-")[0];
                        String ip2 = str.split("-")[1];

                        Link link = new Link(ip1,ip2,Integer.parseInt(matcherBandwidth.group().split(" ")[0]));
                        links.add(link);
                    }

                }
            }


        }

        updateAllRoutingTables();
    }



    /**
     * IP address of the router should be placed in group 1
     * Subnet of the router should be placed group 2
     *
     * @return regex for matching RouterIP lines
     */
    public static String routerRegularExpression() {
        // TODO: REGEX HERE
        return "([0-9]{1,3}\\.[0-9]{1,3}\\.([0-9]{1,3})\\.[0-9]{1,3})";
    }

    /**
     * IP address of the router 1 should be placed in group 1
     * IP address of the router 2 should be placed in group 2
     * Bandwidth of the link should be placed in group 3
     *
     * @return regex for matching Link lines
     */
    public static String linkRegularExpression() {
        // TODO: REGEX HERE
        return "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})-([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}).*((?<![0-9])[0-9]{3,4})";
    }

    public List<Router> getRouters() {
        return routers;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<RoutingTable> getRoutingTablesOfAllRouters() {
        if (routers != null) {
            List<RoutingTable> routingTableList = new ArrayList<>();
            for (Router router : routers)
                routingTableList.add(router.getRoutingTable());
            return routingTableList;
        }
        return null;
    }

    public Router getRouterWithIp(String ip) {
        if (routers != null) {
            for (Router router : routers) {
                if (router.getIpAddress().equals(ip))
                    return router;
            }
        }
        return null;
    }

    public void resetDist(){
        for(Router router:routers){
            router.setDistance(Double.POSITIVE_INFINITY);
        }
    }

    public void resetPath(){
        for(Router router : routers){
            router.resetPath();
        }
    }

    public Link getLinkBetweenRouters(String ipAddr1, String ipAddr2) {
        if (links != null) {
            for (Link link : links) {
                if (link.getIpAddress1().equals(ipAddr1) && link.getIpAddress2().equals(ipAddr2)
                        || link.getIpAddress1().equals(ipAddr2) && link.getIpAddress2().equals(ipAddr1))
                    return link;
            }
        }
        return null;
    }

    public List<Link> getLinksOfRouter(Router router) {
        List<Link> routersLinks = new ArrayList<>();
        if (links != null) {
            for (Link link : links) {
                if (link.getIpAddress1().equals(router.getIpAddress()) ||
                        link.getIpAddress2().equals(router.getIpAddress())) {
                    routersLinks.add(link);
                }
            }
        }
        return routersLinks;
    }

    public void updateAllRoutingTables() {
        for (Router router : getRouters()) {
            router.getRoutingTable().getEntryList().clear();
            router.getRoutingTable().updateTable();
        }
    }

    public  Router getRouterByAdress(String ip){
        for(Router router : routers){
            if(router.getIpAddress().equals(ip)){
                return router;
            }
        }
        return null;
    }

    /**
     * Changes the cost of the link with a new value, and update all routing tables.
     *
     * @param link    Link to update
     * @param newCost New link cost
     */
    public void changeLinkCost(Link link, double newCost) {
        // TODO: YOUR CODE HERE
        for(Link ln :this.links){
            if(ln.equals(link)){
                ln.setCost(newCost);
            }
        }
        updateAllRoutingTables();
    }

    /**
     * Add a new Link to the Network, and update all routing tables.
     *
     * @param link Link to be added
     */
    public void addLink(Link link) {
        // TODO: YOUR CODE HERE
        this.links.add(link);
        updateAllRoutingTables();

    }

    /**
     * Remove a Link from the Network, and update all routing tables.
     *
     * @param link Link to be removed
     */
    public void removeLink(Link link) {
        // TODO: YOUR CODE HERE
        this.links.remove(link);
        updateAllRoutingTables();
    }

    /**
     * Add a new Router to the Network, and update all routing tables.
     *
     * @param router Router to be added
     */
    public void addRouter(Router router) {
        // TODO: YOUR CODE HERE
        this.routers.add(router);
        updateAllRoutingTables();

    }

    /**
     * Remove a Router from the Network, and update all routing tables. Beware that removing a router also causes the
     * removal of any links connected to it from the Network. Also beware that a router which was removed should not
     * appear in any routing table entry.
     *
     * @param router Router to be removed
     */
    public void removeRouter(Router router) {
        // TODO: YOUR CODE HERE
        this.routers.remove(router);
        updateAllRoutingTables();

    }

    /**
     * Change the state of the router (down or live), and update all routing tables. Beware that a router which is down
     * should not be reachable and should not appear in any routing table entry’s path. However, this router might appear
     * in other router’s routing-tables as a separate entry with a totalRouteCost=Infinity value because it was not
     * completely removed from the network.
     *
     * @param router Router to update
     * @param isDown New status of the router
     */
    public void changeStateOfRouter(Router router, boolean isDown) {
        // TODO: YOUR CODE HERE
    }

}
