package scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author williams e felipe
 */
public abstract class Scheduler {

    //new tasks 
    public List<Task> tasks = new ArrayList<>();
    //tasks ready for execution
    public List<Task> tasksScheduler = new ArrayList<>();
    //quantity of tasks
    public List<String> IDs = new ArrayList<>();
    //running time in seconds
    public int TIME = 0;

    private final File input;
    private final File output;

    public Scheduler(String input_path) {
        input = new File(input_path);
        output = new File(input.getParent() + "/output.txt");
        if (output.exists()) {
            //removes the existing file from previous run
            output.delete();
        }
        try {
            //creates a new file for output
            output.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Loads the process from input file to memory
    public void loadTasksFromInput() {

        try {
            FileReader reader = new FileReader(input);
            //creates a buffer from the file input
            BufferedReader br = new BufferedReader(reader);

            int quantityOfTasks = 1;
            while (br.ready()) {
                String ID = "P" + quantityOfTasks;
                IDs.add(ID);

                String[] linha = br.readLine().split(" ");
                Task task = new Task();
                task.setID(ID);
                task.setDate(Integer.parseInt(linha[0]));
                task.setTotalTime(Integer.parseInt(linha[1]));
                task.setPriority(Integer.parseInt(linha[2]));
                tasks.add(task);
                quantityOfTasks++;
            }
            br.close();

            //mounts the header of the diagram
            StringBuilder head = new StringBuilder()
                    .append("time ").append("    ");
            for (String id : IDs) {
                head.append(id).append("  ");
            }
            writeToFile(head.toString());
        } catch (IOException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract void addTasksToRun();

    //handles the execution of a task
    public abstract void scheduler(Task task);

    public void run() {
        while (!tasks.isEmpty() || !tasksScheduler.isEmpty()) {
            addTasksToRun();
            for (int x = 0; x < tasksScheduler.size(); x++) {
                Task task = tasksScheduler.remove(x);
                x--;
                //executes a task
                scheduler(task);
                if (task.getStatus().equals(Task.STATUS_FINISHED)) {
                    continue;
                }
                tasksScheduler.add(task);
            }
        }
    }

    public void generateLog(Task task) {
        String t1 = "";
        String t2 = "";

        if (TIME + 1 < 10) {
            t1 = " ";
            t2 = " ";
        } else if (TIME < 10) {
            t1 = " ";
        }
        t1 = t1.concat(String.valueOf(TIME));
        t2 = t2.concat(String.valueOf(TIME + 1));

        StringBuilder log = new StringBuilder()
                .append(t1).append("-").append(t2).append("    ");

        for (String id : IDs) {
            Optional<Task> optionalTask = tasksScheduler.stream().filter((t) -> (t.getID().equals(id))).findFirst();
            if (optionalTask.isPresent()) {
                String statusSymbol = optionalTask.get().getStatusSymbol();
                log.append(statusSymbol);
            } else if (id.equals(task.getID())) {
                String statusSymbol = task.getStatusSymbol();
                log.append(statusSymbol);
            } else {
                log.append("  ");
            }

            log.append("  ");
        }
        writeToFile(log.toString());
    }

    public void writeToFile(String text) {
        try {
            FileWriter fw = new FileWriter(output, true);
            fw.write(text);
            fw.write("\n");
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
