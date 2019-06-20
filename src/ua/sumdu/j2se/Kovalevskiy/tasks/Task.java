package ua.sumdu.j2se.Kovalevskiy.tasks;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Task implements Cloneable, Serializable{

    private Date startTime;
    private Date endTime;
    private long timeInterval;
    private String title;
    private boolean active;
    private boolean repeat;

    //constructors
    public Task(String title, Date time) throws IllegalArgumentException {
        if(time == null ) {
            throw new IllegalArgumentException("Time can not be null");
        }
        this.title = title;
        this.startTime = this.endTime = time;
        this.active = false;
        this.repeat = false;
        this.timeInterval = 0;
    }

    public Task(String title, Date start, Date end, int interval) throws IllegalArgumentException {
        if(start == null || end == null || end.before(start) ||start.equals(end)) {
            throw new IllegalArgumentException("Start and/or end time can not be null");
        }
        if(interval <= 0) {
            throw new IllegalArgumentException("Time Interval of repeatable Task execution should be greater than zero");
        }
        this.title = title;
        this.startTime = start;
        this.endTime = end;
        this.active = false;
        this.repeat = true;
        this.timeInterval = (long) interval * 1000;
    }

    //get and set title of the task
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //check and set if it is active
    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    //check if it is repeated
    public boolean isRepeated(){
        return this.repeat;
    }

    //get and set time options for unrepeatable task
    public void setTime(Date time) {
        if(time == null) {
            throw new IllegalArgumentException("Time can not be null");
        }
        this.startTime = this.endTime = time;
        this.timeInterval = 0;
        this.repeat = false;

    }

    public Date getTime() {
        return this.startTime;
    }

    //get and set time options for repeatable task
    public Date getStartTime() {
        return this.startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public long getRepeatInterval() {
        return this.timeInterval;
    }

    public void setTime(Date start, Date end, int interval) {
        if(start == null || end == null) {
            throw new IllegalArgumentException("Start and/or end time can not be null");
        }
        if(interval <= 0) {
            throw new IllegalArgumentException("Time Interval of repeatable Task's execution should be greater than zero");
        }
        this.startTime = start;
        this.endTime = end;
        this.timeInterval = (long)interval * 1000;
        this.repeat = true;
    }

    public Date nextTimeAfter(Date current) {
        if(current == null || !this.active || this.endTime.before( current) || this.endTime.equals(current)) {
            return null;
        }
        if(!this.repeat) {
            return this.startTime;
        }
        for(Date i = (Date) this.startTime.clone(); i.before(this.endTime) || i.equals(this.endTime); i.setTime(i.getTime()
                + this.timeInterval)) {
            if(i.after( current)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(startTime, task.startTime) &&
                Objects.equals(endTime, task.endTime) &&
                Objects.equals(timeInterval, task.timeInterval) &&
                Objects.equals(active, task.active) &&
                Objects.equals(repeat, task.repeat) &&
                Objects.equals(title, task.title);
    }


    @Override
    public int hashCode() {
        //return Objects.hash(startTime, endTime, timeInterval, title, active, repeat);
        String string = "{" +
                " title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeInterval=" + timeInterval +
                ", active=" + active +
                ", repeat=" + repeat +
                '}';
        return string.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder taskToStringBuilder = new StringBuilder();

        //write Task's title
        taskToStringBuilder.append("\"");
        taskToStringBuilder.append(this.getTitle().replace("\"", "\"\""));
        taskToStringBuilder.append("\" ");

        //create format for Task's time
        DateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

        if(this.isRepeated()) {
            taskToStringBuilder.append("from ");
        }
        else {
            taskToStringBuilder.append("at ");
        }

        //write start time
        taskToStringBuilder.append(format.format(this.getStartTime()));

        //if Task is repeated write endtime and interval
        if(this.isRepeated()) {
            //write end time
            taskToStringBuilder.append(" to ");
            taskToStringBuilder.append(format.format(this.getEndTime()));
            taskToStringBuilder.append(" every ");

            //get interval of Task's execution in seconds
            int interval = (int) this.getRepeatInterval() / 1000;
            //count whole days
            int days = interval / 86400;
            //
            interval %= 86400;
            //start writing interval
            taskToStringBuilder.append("[");
            if(days > 0) {
                taskToStringBuilder.append(days + " day");
                if(days > 1) {
                    taskToStringBuilder.append("s");
                }
                if(interval > 0) {
                    taskToStringBuilder.append(" ");
                }
            }
            int hours = interval / 3600;
            interval %= 3600;
            if(hours > 0) {
                taskToStringBuilder.append(hours + " hour");
                if(hours > 1) {
                    taskToStringBuilder.append("s");
                }
                if(interval > 0) {
                    taskToStringBuilder.append(" ");
                }
            }
            int minutes = interval / 60;
            interval %= 60;
            if(minutes > 0) {
                taskToStringBuilder.append(minutes + " minute");
                if(minutes > 1) {
                    taskToStringBuilder.append("s");
                }
                if(interval > 0) {
                    taskToStringBuilder.append(" ");
                }
            }
            int seconds = interval;
            if(seconds > 0) {
                taskToStringBuilder.append(seconds + " second");
                if (seconds > 1) {
                    taskToStringBuilder.append("s");
                }
            }
            //finish writing interval
            taskToStringBuilder.append("]");
        }
        //if Task is not active write 'inactive'
        if(!this.isActive()) {
            taskToStringBuilder.append(" inactive");
        }


        String taskToString = new String(taskToStringBuilder);
        return taskToString;
    }


    @Override
    public Task clone() throws CloneNotSupportedException {
        Task task = (Task) super.clone();
        task.title = this.title;
        task.startTime = new Date( this.startTime.getTime()) ;
        task.endTime = new Date(this.endTime.getTime()) ;
        task.timeInterval = this.timeInterval ;
        task.repeat = this.repeat ;
        task.active = this.active ;
        return task;
    }
}