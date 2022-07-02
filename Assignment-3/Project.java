import java.util.*;

public class Project {
    private final String name;
    private final List<Task> tasks;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        // TODO: YOUR CODE HERE

        int taskNum = this.tasks.size();

        int[] arr = new int[taskNum]; // arrays of start dates
        HashSet<Integer> doneTasks = new HashSet<Integer>();

        if(tasks.size()!=0){ // starting with begining task
            doneTasks.add(0);
            Task begin =tasks.get(0);
            begin.start = 0;
            begin.finish= begin.getDuration();

        }

        while(doneTasks.size()< taskNum){
            for(int i = 1;i<tasks.size();i++){
                Task task = tasks.get(i);
                List<Integer> dependencies = task.getDependencies();
                if(doneTasks.containsAll(dependencies)){//all the jobs before this done
                    int latestFinish = -1;
                    for(Integer it : dependencies){
                        Task tsk = tasks.get(it);
                        if(tsk.finish>latestFinish){
                            latestFinish = tsk.finish;
                        }
                    }
                    task.start = latestFinish;
                    task.finish = latestFinish+task.getDuration();
                    doneTasks.add(task.getTaskID());
                }

            }
        }
        for(int i = 0;i<taskNum;i++) {
            Task task =tasks.get(i);
            arr[i]=task.start;
        }

        return arr;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
       /* int projectDuration = 0;

        for(Task task : tasks){
            if(projectDuration<task.finish){
                projectDuration=task.finish;
            }
        }

        return projectDuration;*/
        int[] sched = getEarliestSchedule();
        int lastStart = sched[sched.length-1];
        int time = lastStart+ tasks.get(tasks.size()-1).getDuration();
        return time;
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }


    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s", "Task ID", "Description", "Start", "End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i] + t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length - 1).getDuration() + schedule[schedule.length - 1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
