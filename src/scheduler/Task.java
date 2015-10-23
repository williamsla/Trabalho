package scheduler;

public class Task {

    //variables constant
    //status of the task
    public static final String STATUS_NEW = "new";
    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_READY = "ready";
    public static final String STATUS_FINISHED = "finished";

    //variables of control
    //properties of task
    private String ID;
    private int date;
    private int totalTime;
    private int elapsedTime = 0;
    private int priority;
    private String status = STATUS_NEW;

    //gets the Task ID 
    public String getID() {
        return ID;
    }

    //sets the Task ID
    public void setID(String ID) {
        this.ID = ID;
    }

    //gets the initial date in that the task will be inserted into enqueue
    public int getDate() {
        return date;
    }

    //sets the initial date in that the task will be inserted into enqueue
    public void setDate(int date) {
        this.date = date;
    }

    //gets the total time of execution of the task
    public int getTotalTime() {
        return totalTime;
    }

    //sets the total time of execution of the task
    public void setTotalTime(int totalTime) {
        if (validateTime(totalTime)) {
            this.totalTime = totalTime;
        }
    }

    //gets the elapsed time of execution of the task
    public int getElapsedTime() {
        return elapsedTime;
    }

    //increases the elapsed time of execution of the task
    public void addElapsedTime(int elapsedTime) {
        this.elapsedTime += elapsedTime;
    }

    //gets the priority of execution for task
    public int getPriority() {
        return priority;
    }

    //sets the priority of execution for task
    public void setPriority(int priority) {
        if (validateTime(priority)) {
            this.priority = priority;
        }
    }

    //gets the state of the task
    public String getStatus() {
        return status;
    }

    //gets the symbol of the state of the task
    public String getStatusSymbol() {
        switch (status) {
            case STATUS_READY:
                return "--";
            case STATUS_RUNNING:
                return "##";
            default:
                return "  ";
        }
    }

    public void setStatus(String status) {
        if (status.equals(STATUS_NEW) || status.equals(STATUS_RUNNING)
                || status.equals(STATUS_READY) || status.equals(STATUS_FINISHED)) {
            this.status = status;
        }
    }

    //validate if time is less than 0 (zero)
    private boolean validateTime(int time) {
        return time >= 0;
    }
}
