import java.util.*;

public class Galaxy {

    private int comNum=-1;
    private final List<Planet> planets;
    private List<SolarSystem> solarSystems;

    public Galaxy(List<Planet> planets) {
        this.planets = planets;
    }

    /**
     * Using the galaxy's list of Planet objects, explores all the solar systems in the galaxy.
     * Saves the result to the solarSystems instance variable and returns a shallow copy of it.
     *
     * @return List of SolarSystem objects.
     */
    public List<SolarSystem> exploreSolarSystems() {
        solarSystems = new ArrayList<SolarSystem>();


        for(int i = 0;i<this.planets.size();i++){

           // List<Planet> connected= new ArrayList<Planet>();
            Planet planet = this.planets.get(i);
            if(!planet.visited){
                comNum++;
                DFS(planet);

            }


            /*if(connected.size()!=0){
                List<Planet> temp = new ArrayList<Planet>(connected);
                SolarSystem solar = new SolarSystem(temp);
                solarSystems.add(solar);
            }*/


            //connected.clear();


        }
        int totalSolar = comNum+1;
        List<Planet> temp = new ArrayList<Planet>();
        for(int i = 0;i<comNum+1;i++){


            for(int j = 0;j<planets.size();j++){
                if(planets.get(j).component==i){
                    temp.add(planets.get(j));
                }
            }
            if(temp.size()!=0){
                List<Planet> temp2 = new ArrayList<Planet>(temp);
                SolarSystem solar = new SolarSystem(temp2);
                solarSystems.add(solar);
            }else{
                totalSolar--;
            }

            temp.clear();
        }


        return solarSystems;
    }
    public void DFS (Planet planet){


        planet.visited = true;
        if(planet.component==-1){
            planet.component=comNum;
        }



        for(int i = 0;i<planet.getNeighbors().size();i++){
            String id = planet.getNeighbors().get(i);
            Planet pl = getPlanet(id);
            if(pl.component!=-1){
                planet.component=pl.component;
            }else{
                pl.component=planet.component;
            }

            if(!pl.visited){
                DFS(pl);
            }

        }
    }

    public  Planet getPlanet(String id){

        for(int i = 0;i<this.planets.size();i++){

            if(this.planets.get(i).getId().equals(id)){
                return this.planets.get(i);
            }
        }
        return null;
    }
    public List<SolarSystem> getSolarSystems() {
        return solarSystems;
    }

    // FOR TESTING
    public List<Planet> getPlanets() {
        return planets;
    }

    // FOR TESTING
    public int getSolarSystemIndexByPlanetID(String planetId) {
        for (int i = 0; i < solarSystems.size(); i++) {
            SolarSystem solarSystem = solarSystems.get(i);
            if (solarSystem.hasPlanet(planetId)) {
                return i;
            }
        }
        return -1;
    }

    public void printSolarSystems(List<SolarSystem> solarSystems) {
        System.out.printf("%d solar systems have been discovered.%n", solarSystems.size());
        for (int i = 0; i < solarSystems.size(); i++) {
            SolarSystem solarSystem = solarSystems.get(i);
            List<Planet> planets = new ArrayList<>(solarSystem.getPlanets());
            Collections.sort(planets);
            System.out.printf("Planets in Solar System %d: %s", i + 1, planets);
            System.out.println();
        }
    }
}
