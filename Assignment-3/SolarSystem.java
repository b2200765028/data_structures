
import java.util.List;
import java.util.stream.Collectors;

public class SolarSystem {
    private final List<Planet> planets;
    private Planet mostAdvanced;

    public SolarSystem(List<Planet> planet){
        this.planets=planet;
    }
    public boolean hasPlanet(String planetId) {
        return planets.stream().map(Planet::getId).collect(Collectors.toList()).contains(planetId);
    }

    public Planet getMostAdvanced(){
        int tech = -1;
        Planet pl= null;
        for(int i = 0;i<planets.size();i++){
            Planet curr = planets.get(i);
            if(curr.getTechnologyLevel()>tech){
                pl = curr;
                tech = curr.getTechnologyLevel();
            }
        }
        return pl;
    }
    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public List<Planet> getPlanets() {
        return planets;
    }
}
