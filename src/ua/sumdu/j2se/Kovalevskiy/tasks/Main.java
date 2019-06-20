package ua.sumdu.j2se.Kovalevskiy.tasks;
import java.io.*;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class Main {
     static ArrayTaskList spisok=new ArrayTaskList();
     static Scanner s = new Scanner(System.in);
     static File inputFile=new File("input.txt");

    public static void main(String[] args){
        try{
            TaskIO.readBinary(spisok, inputFile);
        }
        catch(IOException ex){
            //ex.printStackTrace();
            System.out.println("File is empty");
        }

        System.out.println("Welcome!");

        startPageInvite();

        System.exit(1);

    }




    static void startPageInvite(){
        int numb;
        System.out.println("\nLocation: *Start Page*");
        System.out.println("List of available actions");
        System.out.println("1.Full task list");
        System.out.println("2.Tasks for a next week");
        System.out.println("3.Add a new task");
        System.out.println("4.Edit task");
        System.out.println("5.Detailed task info");
        System.out.println("6.Exit");
        System.out.println("\nEnter number of desired action:");
        do {
            numb=s.nextInt();
            switch (numb) {
                case 1:
                    taskListInvite(); break;
                case 2:
                    weekListInvite(); break;
                case 3:
                    newTaskInvite(); break;
                case 4:
                    editTaskInvite(); break;
                case 5:
                    taskInfoInvite(); break;
                case 6:
                    System.out.println("Have a good day, sir!"); System.exit(0); break;
                default:
                    System.out.println("This number incorrect, try again:");
            }
        }
        while(true);
    }




    static void taskListInvite(){
        System.out.println("Location: *TaskList*");
        System.out.println("Full list:");
        if(spisok.size()==0){
            System.out.println("List is empty for now, but you can fill it!");
        }
        else{
            StringBuilder sb=new StringBuilder();
            Date tempDate=new Date();
            int i=0;
            for(Task a:spisok){
                sb.append("Index:"+i+" Title:"+a.getTitle()+" at "+a.nextTimeAfter(tempDate)+"\n");
                i++;
            }
            System.out.println(sb.toString());
        };


        System.out.println("Location: *Start Page*");
        System.out.println("List of available actions");
        System.out.println("1.Full task list");
        System.out.println("2.Tasks for a next week");
        System.out.println("3.Add a new task");
        System.out.println("4.Edit task");
        System.out.println("5.Detailed task info");
        System.out.println("6.Exit");
        System.out.println("What's next?");
        return;

    };



     static void weekListInvite(){
         System.out.println("Location: *WeekList*");
         System.out.println("Tasks for next 7 days:");
         ArrayTaskList tempTasks=(ArrayTaskList)Tasks.incoming(spisok, new Date(),new Date(new Date().getTime()+1000*60*60*3600*7));
         if(tempTasks.size()==0){
             System.out.println("There are no tasks for the next week.");
         }
         else{
             StringBuilder sb=new StringBuilder();
             for(Task a:tempTasks){
                 sb.append("Title:"+a.getTitle()+" at "+a.getTime()+"\n");
             }
             System.out.println(sb.toString());
         };
         return;


     };



     static void newTaskInvite(){
         int numb;
         String title;
         Task tempTask;
         String tempDate;
         SimpleDateFormat simple = new SimpleDateFormat("d/MM/yyyy");

         System.out.println("Location: *New task*");

         System.out.println("\nEnter title:");
         //fake
         s.nextLine();
         title=s.nextLine();

         //fake
         //s.nextLine();
         System.out.println("Is task repeated?");
         System.out.println("1.No(default)");
         System.out.println("2.Yes");
         numb=s.nextInt();
         //fake
         //s.nextLine();
         Date startTime;
         //fake
         s.nextLine();
        //start
         System.out.println("\nEnter start time:");

         tempDate = s.nextLine();
         try {
             startTime = simple.parse(tempDate);
         } catch (ParseException ex) {
             System.out.println("\nThis date is not correct. Try later");
             return;
         }

         //if is repeated
         if(numb==2){
             //end
             Date endTime;
             System.out.println("\nEnter end time:");
             tempDate = s.nextLine();
             try {
                 endTime = simple.parse(tempDate);
             } catch (ParseException ex) {
                 System.out.println("\nThis date is not correct. Try later");
                 return;
             }

             //fake
             //s.nextLine();
             //interval
             System.out.println("\nEnter repeat interval(seconds):");
             int repeatInterval = s.nextInt();


             tempTask=new Task(title, startTime, endTime, repeatInterval);
         }
         else
         {
             tempTask=new Task(title, startTime);
         }

         spisok.add(tempTask);

         try{
             TaskIO.writeBinary(spisok, inputFile);
         }
         catch(IOException ex){
             //ex.printStackTrace();
             System.out.println("Something went wrong");
         }

     };



    static void editTaskInvite(){
        String tempDate;
        SimpleDateFormat simple = new SimpleDateFormat("d/MM/yyyy");
        int numb;
        int index;
        Task tempTask;
        System.out.println("Location: *Task editor*");
        System.out.println("Enter index of task to edit:");
        index=s.nextInt();
        if(index>=spisok.size() && index<0){
            System.out.println("There is no such task!");
        }
        else{
            tempTask=spisok.getTask(index);
            System.out.println("What field do you want to edit?");
            System.out.println("1.Title");
            System.out.println("2.Activity");
            System.out.println("3.Execute time(makes task unrepeatable)");
            System.out.println("4.Start+end+interval(makes repeatable)");
            System.out.println("0.Return");
            System.out.println("\nEnter number of desired action:");

            do {
                numb=s.nextInt();
                switch (numb) {
                    case 1:
                        System.out.println("Enter new title:");
                        //fake
                        s.nextLine();
                        tempTask.setTitle(s.nextLine());

                            //Saving changes
                            try{
                                TaskIO.writeBinary(spisok, inputFile);
                            }
                            catch(IOException ex){
                                //ex.printStackTrace();
                                System.out.println("Something went wrong");
                            }
                            //Changes saved

                        System.out.println("What's next?");
                        break;
                    case 2:
                        tempTask.setActive(!tempTask.isActive());
                        System.out.println("Activity changed");

                            //Saving changes
                            try{
                                TaskIO.writeBinary(spisok, inputFile);
                            }
                            catch(IOException ex){
                                //ex.printStackTrace();
                                System.out.println("Something went wrong");
                            }
                            //Changes saved

                        System.out.println("What's next?");
                        break;
                    case 3:
                        System.out.println("Enter new time:");
                        //fake
                        s.nextLine();
                        tempDate=s.nextLine();
                        try {
                            tempTask.setTime(simple.parse(tempDate));

                                //Saving changes
                                try{
                                    TaskIO.writeBinary(spisok, inputFile);
                                }
                                catch(IOException ex){
                                    //ex.printStackTrace();
                                    System.out.println("Something went wrong");
                                }
                                //Changes saved
                            System.out.println("What's next?");
                        } catch (ParseException ex) {
                            System.out.println("\nThis date is not correct. Try later");
                            System.out.println("Location: *Start Page*");
                            System.out.println("List of available actions");
                            System.out.println("1.Full task list");
                            System.out.println("2.Tasks for a next week");
                            System.out.println("3.Add a new task");
                            System.out.println("4.Edit task");
                            System.out.println("5.Detailed task info");
                            System.out.println("6.Exit");
                            System.out.println("What's next?");
                            return;
                        }
                        break;
                    case 4:
                        System.out.println("Enter new start:");
                        //fake
                        s.nextLine();
                        String tempStart=s.nextLine();
                        System.out.println("Enter new end:");
                        String tempEnd=s.nextLine();
                        System.out.println("Enter new interval:");
                        int interval=s.nextInt();
                        try {
                            tempTask.setTime(simple.parse(tempStart),simple.parse(tempEnd), interval);

                                //Saving changes
                                try{
                                    TaskIO.writeBinary(spisok, inputFile);
                                }
                                catch(IOException ex){
                                    //ex.printStackTrace();
                                    System.out.println("Something went wrong");
                                }
                                //Changes saved

                            System.out.println("Edit successful");
                            System.out.println("What's next?");
                        } catch (ParseException ex) {
                            System.out.println("\nSome data is not correct. Try later");
                            System.out.println("Location: *Start Page*");
                            System.out.println("List of available actions");
                            System.out.println("1.Full task list");
                            System.out.println("2.Tasks for a next week");
                            System.out.println("3.Add a new task");
                            System.out.println("4.Edit task");
                            System.out.println("5.Detailed task info");
                            System.out.println("6.Exit");
                            System.out.println("What's next?");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Location: *Start Page*");
                        System.out.println("List of available actions");
                        System.out.println("1.Full task list");
                        System.out.println("2.Tasks for a next week");
                        System.out.println("3.Add a new task");
                        System.out.println("4.Edit task");
                        System.out.println("5.Detailed task info");
                        System.out.println("6.Exit");
                        System.out.println("What's next?");
                        return;
                }
            }
            while(true);

        }
    }



     static void taskInfoInvite(){
         int index;
         System.out.println("Location: *TaskInfo*");
         System.out.println("Enter index of task to search:");
         index=s.nextInt();

         if(index>=spisok.size() && index<0){
             System.out.println("There is no such task!");
         }
         else{
             StringBuilder sb=new StringBuilder();
                Task tempTask=spisok.getTask(index);
                 sb.append("Task:"+tempTask.getTitle()+"\n");
                 sb.append("Activity:"+tempTask.isActive()+"\n");
                 sb.append("Is repeatable:"+tempTask.isRepeated()+"\n");
                 if(!tempTask.isRepeated()){
                     sb.append("Happens at: "+tempTask.getTime()+"\n");
                 }
                 else{
                     sb.append("Starts at: "+tempTask.getStartTime()+"\n");
                     sb.append("Ends at: "+tempTask.getEndTime()+"\n");
                     sb.append("With interval: "+tempTask.getRepeatInterval()/1000+"\n");
                 }

             System.out.println(sb.toString());
         };

         System.out.println("Location: *Start Page*");
         System.out.println("List of available actions");
         System.out.println("1.Full task list");
         System.out.println("2.Tasks for a next week");
         System.out.println("3.Add a new task");
         System.out.println("4.Edit task");
         System.out.println("5.Detailed task info");
         System.out.println("6.Exit");
         System.out.println("What's next?");
         return;

     };



     //end of main
}

