package scheduler;

import scheduler.Task;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author williams
 */
public class RoundRobinScheduler extends scheduler.Scheduler {

    //quantum in seconds
    int quantum = 2;

    public RoundRobinScheduler(String input_path) {
        super(input_path);
    }

    @Override
    public void addTasksToRun() {
        //gets the tasks that are ready for execution from the list with new tasks
        List<Task> collect = tasks.stream().filter((task) -> (task.getDate() == TIME))
                .collect(Collectors.toList());
        //
        collect.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        //Change the status of tasks for READY
        collect.stream().forEach((task) -> {
            task.setStatus(Task.STATUS_READY);
        });
        //Adds the tasks to the queue of execution
        tasksScheduler.addAll(collect);
        

        //Removes it from list of new tasks
        tasks.removeAll(collect);
    }

    //handles the execution of a task
    @Override
    public void scheduler(Task task) {
        task.setStatus(Task.STATUS_RUNNING);
        for (int q = 0; q < quantum; q++) {
            task.addElapsedTime(1);
            generateLog(task);
            TIME++;
            addTasksToRun();
            if (task.getElapsedTime() >= task.getTotalTime()) {
                task.setStatus(Task.STATUS_FINISHED);
                return;
            }
        }
        task.setStatus(Task.STATUS_READY);
    }
}
