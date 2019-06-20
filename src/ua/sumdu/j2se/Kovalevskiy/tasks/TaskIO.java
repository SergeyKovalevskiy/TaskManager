package ua.sumdu.j2se.Kovalevskiy.tasks;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class TaskIO {

    //writing for binary stream
    public static void write(TaskList tasks, OutputStream out) throws IOException {
        //cover abstract OutputStream with DataInput to simplify reading
        DataOutputStream dataOut = new DataOutputStream(out);
        //write size of TaskList that is being written
        int sizeOfTaskList = tasks.size();
        dataOut.writeInt(sizeOfTaskList);
        //write number of Task's according to size
        for (int i = 0; i < sizeOfTaskList; i++) {
            //get Task from the TaskList that will be written in this iteration
            Task task = tasks.getTask(i);
            //get title of the Task and write its length to the stream
            String taskTitle = task.getTitle();
            int taskTitleSize = taskTitle.length();
            dataOut.writeInt(taskTitleSize);
            //write Task's title to the stream
            dataOut.writeChars(taskTitle);
            //write value of Task's field active to the stream
            dataOut.writeBoolean(task.isActive());
            //write  Task's repeat interval to the stream
            int repeatInterval = (int) (task.getRepeatInterval() / 1000);
            dataOut.writeInt(repeatInterval);
            //if repeat interval == 0 (Task is not repeatable)
            //get time of Task's execution and write it to the stream
            if(repeatInterval == 0) {
                dataOut.writeLong(task.getTime().getTime());
            }
            //else Task is repeatable
            //get starttime of Task's execution and endtime of Task's execution in milliseconds
            //write them to the stream
            else {
                dataOut.writeLong(task.getStartTime().getTime());
                dataOut.writeLong(task.getEndTime().getTime());
            }
        }
    }

    //reading for binary stream
    public static void read(TaskList tasks, InputStream in) throws IOException {
        //cover abstract InputStream with DataInput to simplify reading
        DataInputStream dataIn = new DataInputStream(in);
        //read size of TaskList that is being read
        int sizeOfTaskList = dataIn.readInt();

        //read number of Task's according to size
        for (int i = 0; i < sizeOfTaskList ; i++) {
            //read size(number of characters) of the title of the task
            int taskTitleSize = dataIn.readInt();
            StringBuilder taskTitleBuilder = new StringBuilder();
            //read char's according to size of title that was read and add them to string
            for (int j = 0; j < taskTitleSize; j++) {
                char c = dataIn.readChar();
                taskTitleBuilder.append(c);
            }
            String taskTitle = new String(taskTitleBuilder);
            //read boolean value of field active of Task
            boolean taskActiveness = dataIn.readBoolean();
            //read repeat interval of the Task in seconds, not milliseconds
            int taskRepeatInterval = dataIn.readInt();
            //create Task variable that will be added to the TaskList
            Task task;
            //if repeat interval == 0 (Task is not repeatable)
            //read time of Task's execution
            //convert them to Date format
            //create Task with a constructor for unrepeatable Task
            if(taskRepeatInterval == 0) {
                long taskTime = dataIn.readLong();
                Date time = new Date(taskTime);
                task = new Task(taskTitle, time);
            }
            //else Task is repeatable
            //read starttime of Task's execution and endtime of Task's execution in milliseconds
            //convert them to Date format
            //create Task with a constructor for repeatable Task
            else {
                long taskStartTime = dataIn.readLong();
                long taskEndTime = dataIn.readLong();
                Date startTime = new Date(taskStartTime);
                Date endTime = new Date(taskEndTime);
                task = new Task(taskTitle, startTime, endTime, taskRepeatInterval);
            }
            //set value of Task's field active to read from the Stream
            task.setActive(taskActiveness);
            tasks.add(task);
        }

    }

    //writing binary in file
    static void writeBinary(TaskList tasks, File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        write(tasks, fileOut);
        fileOut.close();
    }


    //reading binary from file
    static void readBinary(TaskList tasks, File file) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        read(tasks, fileIn);
        fileIn.close();
    }


    //writing for symbol stream
    public static void write(TaskList tasks, Writer out) throws IOException {
        //wrap Writer with BufferedWriter
        BufferedWriter outStream = new BufferedWriter(out);
        //Loop through the TaskList
        for (int i = 0; i < tasks.size(); i++) {
            //get Task for current iteration
            Task task = tasks.getTask(i);

            //write Task's title to the stream
            outStream.write("\"");
            outStream.write(task.getTitle().replace("\"", "\"\""));
            outStream.write("\" ");

            //create format for Task's time
            DateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

            if(task.isRepeated()) {
                outStream.write("from ");
            }
            else {
                outStream.write("at ");
            }

            //write start time
            outStream.write(format.format(task.getStartTime()));

            //if Task is repeated write endtime and interval
            if(task.isRepeated()) {
                //write end time
                outStream.write(" to ");
                outStream.write(format.format(task.getEndTime()));
                outStream.write(" every ");

                //get interval of Task's execution in seconds
                int interval = (int) task.getRepeatInterval() / 1000;
                //count whole days
                int days = interval / 86400;
                //
                interval %= 86400;
                //start writing interval
                outStream.write("[");
                if(days > 0) {
                    outStream.write(days + " day");
                    if(days > 1) {
                        outStream.write("s");
                    }
                    if(interval > 0) {
                        outStream.write(" ");
                    }
                }
                int hours = interval / 3600;
                interval %= 3600;
                if(hours > 0) {
                    outStream.write(hours + " hour");
                    if(hours > 1) {
                        outStream.write("s");
                    }
                    if(interval > 0) {
                        outStream.write(" ");
                    }
                }
                int minutes = interval / 60;
                interval %= 60;
                if(minutes > 0) {
                    outStream.write(minutes + " minute");
                    if(minutes > 1) {
                        outStream.write("s");
                    }
                    if(interval > 0) {
                        outStream.write(" ");
                    }
                }
                int seconds = interval;
                if(seconds > 0) {
                    outStream.write(seconds + " second");
                    if (seconds > 1) {
                        outStream.write("s");
                    }
                }
                //finish writing interval
                outStream.write("]");
            }
            //if Task is not active write 'inactive'
            if(!task.isActive()) {
                outStream.write(" inactive");
            }
            //write end of line. If it's last Task - write '.' , else ';' end go to next line
            if(i == tasks.size() - 1) {
                outStream.write(".");
            }
            else {
                outStream.write(";\n");
            }

        }
        outStream.flush();
        outStream.close();
    }

    //reading for symbol stream
    public static void read(TaskList tasks, Reader in) throws IOException, ParseException {
        BufferedReader inStream = new BufferedReader(in);
        String taskStr;

        while((taskStr = inStream.readLine()) != null) {
            char flag = taskStr.charAt(taskStr.length() - 1);
            //delete last symbol from the String containing Task
            taskStr = taskStr.substring(0, taskStr.length() - 1);
            //split String with '"'
            String[] taskArr = taskStr.split("\"");
            //create variable for Task's title
            String taskTitle;
            //create Builder for title
            StringBuilder buildTitle = new StringBuilder();
            //join parts of title with '"'
            for (int i = 0; i < taskArr.length - 1; i++) {
                buildTitle.append(taskArr[i]);
                if(i != taskArr.length - 2 && !taskArr[i].equals("")){
                    buildTitle.append("\"");
                }
            }
            //get title for the Task
            taskTitle = buildTitle.toString();

            //last element of taskArr contains all other info about Task
            String otherInfo = taskArr[taskArr.length - 1];
            otherInfo = otherInfo.substring(1, otherInfo.length());
            String otherInfoArr[] = otherInfo.split(" ");
            //create variables for fields of the Task
            Task task;
            boolean activeness = false;
            boolean repeatable = false;
            Date startTime, endTime;
            int interval = 0;

            //if we find from - it means Task is repeatable
            if(otherInfoArr[0].equals("from")) {
                repeatable = true;
            }

            //if we find 'inactive' at the end of the String - Task is not active
            if(!(otherInfoArr[otherInfoArr.length-1].equals("inactive"))) {
                activeness = true;
            }
            //create DateFormat to parse String to Date
            DateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");
            if(repeatable) {
                //parse Dates
                String start = otherInfoArr[1] + " " + otherInfoArr[2];
                String end = otherInfoArr[4] + " " + otherInfoArr[5];
                startTime = format.parse(start);
                endTime = format.parse(end);

                //get String [...] containing info about interval
                StringBuilder strBuilderInterval = new StringBuilder();
                int counter = activeness ? otherInfoArr.length : otherInfoArr.length - 1;
                for (int i = 7; i < counter; i++) {
                    strBuilderInterval.append(otherInfoArr[i]);
                    if(i != counter - 1) {
                        strBuilderInterval.append(" ");
                    }
                }
                String strInterval = new String(strBuilderInterval);

                //delete brackets '[' & ']' from start and end of the String
                strInterval = strInterval.substring(1, strInterval.length() - 1);
                String[] strIntervalArr = strInterval.split(" ");
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("day", 86400);
                map.put("days", 86400);
                map.put("hour", 3600);
                map.put("hours", 3600);
                map.put("minutes", 60);
                map.put("minute", 60);
                map.put("second", 1);
                map.put("seconds", 1);

                for (int i = 0; i < strIntervalArr.length; i+=2) {
                    interval += Integer.parseInt(strIntervalArr[i]) * map.get(strIntervalArr[i+1]);
                }

                task = new Task(taskTitle, startTime, endTime, interval);
                task.setActive(activeness);
            }
            else {
                String start = otherInfoArr[1] + " " + otherInfoArr[2];
                startTime = format.parse(start);
                task = new Task(taskTitle, startTime);
                task.setActive(activeness);
            }


            tasks.add(task);

            if(flag == '.'){
                break;
            }
        }
    }

    //writing symbols in file
    public static void writeText(TaskList tasks, File file) throws IOException {
        FileWriter fileOut = new FileWriter(file);
        write(tasks, fileOut);
        fileOut.close();
    }

    //reading symbols from file
    public static void readText(TaskList tasks, File file) throws IOException, ParseException {
        FileReader fileIn = new FileReader(file);
        read(tasks, fileIn);
        fileIn.close();
    }
}