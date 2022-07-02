import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
public class MissionGroundwork {

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for(int i = 0;i<projectList.size();i++){
            Project pr = projectList.get(i);
            int [] arr = projectList.get(i).getEarliestSchedule();
            pr.printSchedule(arr);
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        try{
            File file = new File(filename);

            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();

            DocumentBuilder build = df.newDocumentBuilder();

            Document doc = build.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Project");


            for (int i = 0; i < nodeList.getLength(); i++){

                ArrayList<String> neighbors = new ArrayList<String>();
                Node node = nodeList.item(i);
                Element e = (Element) node;
                String name = e.getElementsByTagName("Name").item(0).getTextContent();
                int tech = Integer.valueOf(e.getElementsByTagName("Duration").item(0).getTextContent());




                NodeList taskGenel =e.getElementsByTagName("Task");
                List<Task> tasks = new ArrayList<>(taskGenel.getLength());


                for(int j = 0;j<taskGenel.getLength();j++) {

                    Element el = (Element) taskGenel.item(j);
                    int id = Integer.valueOf(el.getElementsByTagName("TaskID").item(0).getTextContent());
                    String desc = el.getElementsByTagName("Description").item(0).getTextContent();
                    int duration = Integer.valueOf(el.getElementsByTagName("Duration").item(0).getTextContent());
                    NodeList depend = el.getElementsByTagName("DependsOnTaskID");


                    List<Integer> dependencies = new ArrayList<Integer>();
                    for (int z = 0; z < depend.getLength(); z++) {
                        Element dep = (Element) depend.item(z);
                        int depends = Integer.valueOf(dep.getTextContent());
                        dependencies.add(depends);
                    }


                    Task task = new Task(id, desc, duration, new ArrayList<Integer>(dependencies));

                    tasks.add(task);
                    dependencies.clear();
                }

                Project project = new Project(name,tasks);
                projectList.add(project);

            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return projectList;
    }


}
