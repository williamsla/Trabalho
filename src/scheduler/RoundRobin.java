package scheduler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author williams
 */
public class RoundRobin extends scheduler.Scheduler {

    //quantum in seconds
    int quantum = 2;

    //receives an input file with the process properties
    public RoundRobin(String input_path) {
        super(input_path);
    }

    /**
     * Remove the tasks that start on elapsed time to date, 
	 * and adds in the escalation list, 
     * in which case the tasks change to the state "ready"
     */
    @Override
    public void addTasksToRun() {
        //gets the tasks that are ready for execution from the list with new tasks
        List<Task> collect = tasks.stream().filter((task) -> (task.getDate() == TIME))
                .collect(Collectors.toList());
        //sort the tasks inserted. The sort is based in "priority" value for all the tasks.
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

    //Runs the task to end the quantum, or time it needs
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
