
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author williams
 */
public class Scheduler {

    //new tasks 
    public List<Task> tasks = new ArrayList<>();
    //tasks ready for execution
    public List<Task> tasksScheduler = new ArrayList<>();
    //quantity of tasks
    public int quantityTasks = 0;
    //running time in seconds
    private int TIME = 0;
    //quantum in seconds
    int quantum = 2;

    public void loadTasksFromInput(String pathFile) {
        File input = new File(pathFile);
        try {
            FileReader reader = new FileReader(input);
            //creates a buffer from the file input
            BufferedReader br = new BufferedReader(reader);
            //build head 
            StringBuilder head = new StringBuilder()
                    .append("time ").append("    ");
            while (br.ready()) {
                quantityTasks++;
                head.append("P").append(quantityTasks).append("  ");
                String[] linha = br.readLine().split(" ");
                Task task = new Task();
                task.setID(quantityTasks);
                task.setDate(Integer.parseInt(linha[0]));
                task.setTotalTime(Integer.parseInt(linha[1]));
                task.setPriority(Integer.parseInt(linha[2]));
                tasks.add(task);
            }
            br.close();

            //print head
            System.out.println(head);
        } catch (IOException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addTasksToRun() {
        List<Task> collect = tasks.stream().filter((task) -> (task.getDate() == TIME)).collect(Collectors.toList());
        collect.stream().forEach((task) -> {
            task.setStatus(Task.STATUS_READY);
        });
        tasksScheduler.addAll(collect);
        tasks.removeAll(collect);
    }

    public void run() {
        addTasksToRun();
        while (!tasks.isEmpty() || !tasksScheduler.isEmpty()) {
            for (int x = 0; x < tasksScheduler.size(); x++) {
                Task task = tasksScheduler.get(x);
                task.setStatus(Task.STATUS_RUNNING);
                for (int q = 0; q < quantum; q++) {
                    TIME++;
                    generateLog();
                    boolean isFinished = task.addElapsedTime(1);
                    if (isFinished) {
                        tasksScheduler.remove(task);
                        if (x > 0) {
                            x--;
                        }
                    }
                    addTasksToRun();
                }
                task.setStatus(Task.STATUS_READY);
            }
        }

    }

    public void generateLog() {
        String t1 = "";
        String t2 = "";

        if (TIME < 10) {
            t1 = " ";
            t2 = " ";
        } else if (TIME - 1 < 10) {
            t1 = " ";
        }
        t1 = t1.concat(String.valueOf(TIME - 1));
        t2 = t2.concat(String.valueOf(TIME));

        StringBuilder log = new StringBuilder()
                .append(t1).append("-").append(t2).append("    ");

        for (int i = 1; i <= quantityTasks; i++) {
            int aux = i;
            Optional<Task> optionalTask = tasksScheduler.stream().filter((t) -> (t.getID() == aux)).findFirst();
            if (optionalTask.isPresent()) {
                String statusSymbol = optionalTask.get().getStatusSymbol();
                log.append(statusSymbol);
            } else {
                log.append("  ");
            }

            log.append("  ");
        }
        System.out.println(log);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        Scheduler scheduler = new Scheduler();
        scheduler.loadTasksFromInput("src\\input.txt");
        scheduler.run();
    }
}
