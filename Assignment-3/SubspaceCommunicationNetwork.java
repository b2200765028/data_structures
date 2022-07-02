
import java.util.*;

public class SubspaceCommunicationNetwork {

    private List<SolarSystem> solarSystems;
    private List<Planet> highTechPlanets = new ArrayList<Planet>();
    private List<HyperChannel> channels = new ArrayList<HyperChannel>();


    /**
     * Perform initializations regarding your implementation if necessary
     * @param solarSystems a list of SolarSystem objects
     */
    public SubspaceCommunicationNetwork(List<SolarSystem> solarSystems) {


        this.solarSystems = solarSystems;

        for(int i = 0;i<solarSystems.size();i++){ //taking most advanced planets to a list
            highTechPlanets.add(solarSystems.get(i).getMostAdvanced());

        }


        //creating inital hyperchannels (afterwards we will reduce them using kruskals or prims algorithm)

        Collections.sort(highTechPlanets, new Comparator<Planet>() {
            @Override
            public int compare(Planet o1, Planet o2) {
                return o2.getTechnologyLevel()-o1.getTechnologyLevel();
            }
        });



        for(int i = 0;i<highTechPlanets.size();i++){
            for(int j = i+1;j<highTechPlanets.size();j++){
                Double avg = Double.valueOf((Double.valueOf(highTechPlanets.get(i).getTechnologyLevel())+Double.valueOf(highTechPlanets.get(j).getTechnologyLevel()))/2);
                Double cost =Double.valueOf(Constants.SUBSPACE_COMMUNICATION_CONSTANT/avg);


                HyperChannel ch = new HyperChannel(highTechPlanets.get(i),highTechPlanets.get(j),cost);

                channels.add(ch);
            }
        }

        Collections.sort(channels);



    }

    /**
     * Using the solar systems of the network, generates a list of HyperChannel objects that constitute the minimum cost communication network.
     * @return A list HyperChannel objects that constitute the minimum cost communication network.
   */


    public List<HyperChannel> getMinimumCostCommunicationNetwork() {
        List<HyperChannel> mst = new ArrayList<>();

        //gonna include first planet with high tech

        Set<Planet> addedPlanets = new HashSet<Planet>();


        int vertices = highTechPlanets.size();
        int added = 0;
        Planet pl =null;
        if(channels.size()>0){
           pl = highTechPlanets.get(0);
            addedPlanets.add(pl);//adding 0 indexed
            added ++;
            PriorityQueue<HyperChannel> pq = new PriorityQueue<HyperChannel>();
            addEdges(pq,pl,channels);

            while(added<vertices){


                HyperChannel nextChannel = pq.poll();
                if(nextChannel==null){
                    System.out.println("null");
                }
                Planet pl1 = nextChannel.getFrom();
                Planet pl2 = nextChannel.getTo();
                if((addedPlanets.contains(pl1)^addedPlanets.contains(pl2))){

                    Planet next = null ;

                    mst.add(nextChannel);
                    if(addedPlanets.contains(pl1)){

                        addedPlanets.add(pl2);
                        next = pl2;
                        added++;
                    }
                    else if(addedPlanets.contains(pl2)){

                        addedPlanets.add(pl1);
                        next = pl1;
                        added++;
                    }

                    addEdges(pq,next,channels);
                }

            }



        }

        return mst;
    }
    public static void addEdges(PriorityQueue<HyperChannel> pq,Planet planet,List<HyperChannel>channels){
        for(int i = 0;i<channels.size();i++) {
            HyperChannel ch = channels.get(i);
            Planet pl1 = ch.getFrom();
            Planet pl2 = ch.getTo();
            if(pl1==planet||pl2==planet){//edge available
                if(!pq.contains(ch))
                    pq.add(ch);
            }
        }

    }


    public void printMinimumCostCommunicationNetwork(List<HyperChannel> network) {
        double sum = 0;
        for (HyperChannel channel : network) {
            Planet[] planets = {channel.getFrom(), channel.getTo()};
            Arrays.sort(planets);
            System.out.printf("Hyperchannel between %s - %s with cost %f", planets[0], planets[1], channel.getWeight());
            System.out.println();
            sum += channel.getWeight();
        }
        System.out.printf("The total cost of the subspace communication network is %f.", sum);
        System.out.println();
    }
}
