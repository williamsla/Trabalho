import schedulerImpl.FCFS;
import schedulerImpl.RoundRobinScheduler;
import scheduler.Scheduler;

/**
 * @author williams e felipe
 */
public class Main {

    public static void main(String[] args) {
        String file = args[0];
        String param = args[1];
        Scheduler scheduler = null;
        if (param.equals("FCFS")) {
            scheduler = new FCFS(file);
        } else if (param.equals("RoundRobin")) {
            scheduler = new RoundRobinScheduler(file);
        }
        try {
            scheduler.loadTasksFromInput();
            scheduler.run();
        } catch (NullPointerException n) {
            System.out.println("Scheduler invalid.");
        }

    }

}
