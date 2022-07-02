import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.util.*;

public class MissionExploration {

    /**
     * Given a Galaxy object, prints the solar systems within that galaxy.
     * Uses exploreSolarSystems() and printSolarSystems() methods of the Galaxy object.
     *
     * @param galaxy a Galaxy object
     */
    public void printSolarSystems(Galaxy galaxy) {
        // TODO: YOUR CODE HERE

        List<SolarSystem> list = galaxy.exploreSolarSystems();



        galaxy.printSolarSystems(list);
    }

    /**
     * TODO: Parse the input XML file and return a list of Planet objects.
     *
     * @param filename the input XML file
     * @return a list of Planet objects
     */
    public Galaxy readXML(String filename) {
        List<Planet> planetList = new ArrayList<Planet>();
        try{
            File file = new File(filename);

            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();

            DocumentBuilder build = df.newDocumentBuilder();

            Document doc = build.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Planet");


            for (int i = 0; i < nodeList.getLength(); i++){

                ArrayList<String> neighbors = new ArrayList<String>();
                Node node = nodeList.item(i);
                Element e = (Element) node;
                String id = e.getElementsByTagName("ID").item(0).getTextContent();
                int tech = Integer.valueOf(e.getElementsByTagName("TechnologyLevel").item(0).getTextContent());




                NodeList neighList =e.getElementsByTagName("PlanetID");

                for(int j = 0;j<neighList.getLength();j++){

                    Element el = (Element)neighList.item(j);

                    neighbors.add(el.getTextContent());
                }

                Planet planet = new Planet(id,tech,neighbors);
                planetList.add(planet);

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new Galaxy(planetList);
    }
}
