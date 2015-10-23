package scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import scheduler.Task;

/**
 * @author felipe
 */
public class FCFS extends scheduler.Scheduler {

    public FCFS(String input_path) {
        super(input_path);
    }

    @Override
    public void addTasksToRun() {
        //gets the tasks that are ready for execution from the list with new tasks
        List<Task> collect = tasks.stream().filter((task) -> (task.getDate() == TIME))
                .collect(Collectors.toList());
        //Change the status of tasks for READY
        collect.stream().forEach((task) -> {
            task.setStatus(Task.STATUS_READY);
        });
        //Adds the tasks to the queue of execution
        tasksScheduler.addAll(collect);
        //return 
        tasksScheduler.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        //Removes it from list of new tasks
        tasks.removeAll(collect);
    }

    @Override
    public void scheduler(Task task) {
        task.setStatus(Task.STATUS_RUNNING);
        while (task.getElapsedTime() < task.getTotalTime()) {
            task.addElapsedTime(1);
            generateLog(task);
            TIME++;
            addTasksToRun();
        }
        task.setStatus(Task.STATUS_FINISHED);
    }
}
