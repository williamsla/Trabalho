
public class Task {

    //variables constant
    public static final String STATUS_NEW = "new";
    public static final String STATUS_RUNNING = "running";
    public static final String STATUS_READY = "ready";
    public static final String STATUS_FINISHED = "finished";

    //variables of control
    private int ID;
    private int date;
    private int totalTime;
    private int elapsedTime = 0;
    private int priority;
    private String status = STATUS_NEW;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTempoTotal() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        if (validateTime(totalTime)) {
            this.totalTime = totalTime;
        }
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public boolean addElapsedTime(int elapsedTime) {
        this.elapsedTime += elapsedTime;
        if (this.elapsedTime >= totalTime) {
            setStatus(STATUS_FINISHED);
            return true;
        }
        return false;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (validateTime(priority)) {
            this.priority = priority;
        }
    }

    public String getStatus() {
        return status;
    }

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

    private boolean validateTime(int time) {
        return time >= 0;
    }
}
