package ua.sumdu.j2se.Kovalevskiy.tasks;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class ArrayTaskList extends TaskList{

    private Task[] taskList = new Task[10];
    private int currentIndex = 0;
    private int numberOfTasks = 0;


    public void add(Task task) throws IllegalArgumentException {
        if(task == null) {
            throw new IllegalArgumentException("You can not add null to the list ");
        }
        if(currentIndex == this.taskList.length - 1) {
            Task[] newTaskList = new Task[2*this.taskList.length];
            System.arraycopy(taskList, 0, newTaskList, 0, this.taskList.length);
            taskList = newTaskList;
            newTaskList = null;
        }
        this.taskList[currentIndex] = task;
        currentIndex++;
        numberOfTasks++;
    }


    public boolean remove(Task task) {
        if(numberOfTasks == 0 || task == null) {
            return false;
        }
        for(int i = 0; i < this.taskList.length; i++) {
            if (this.taskList[i] == null) {
                continue;
            }
            if( this.taskList[i].equals(task)){
                Task[] newTaskList = new Task[this.taskList.length];
                System.arraycopy(taskList, 0, newTaskList, 0, i);
                System.arraycopy(taskList, i+1, newTaskList, i, this.taskList.length - i -1 );
                newTaskList[this.taskList.length - 1] = null;
                taskList = newTaskList;
                newTaskList = null;
                numberOfTasks--;
                currentIndex--;
                return true;
            }
        }
        return false;
    }


    public int size() {
        return numberOfTasks;
    }


    public Task getTask(int index) throws IllegalArgumentException {
        if(index >=this.currentIndex) {
            throw new IllegalArgumentException("Index of the Task you want to get should be less than current index" +
                    "(~number of tasks in the list)");
        }
        return this.taskList[index];
    }



    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList cloned = (ArrayTaskList) super.clone();
        cloned.numberOfTasks = 0;
        cloned.currentIndex = 0;
        cloned.taskList = new Task[10];
        for (int i = 0; i < this.size() ; i++) {
            cloned.add(this.getTask(i).clone());
        }
        return cloned;
    }


    public Iterator<Task> iterator() {
        return new Itr();
    }


    private class Itr implements Iterator<Task> {
        int current = 0;
        int returned = -1;

        public boolean hasNext() {
            return  (current < numberOfTasks);
        }


        public Task next() throws NoSuchElementException {
            if(current >= numberOfTasks) {
                throw new NoSuchElementException();
            }
            returned = current;
            current++;
            return taskList[returned];
        }

 
        public void remove() {
            if(returned < 0) {
                throw new IllegalStateException();
            }
            Task[] newTaskList = new Task[taskList.length];
            System.arraycopy(taskList, 0, newTaskList, 0, returned);
            System.arraycopy(taskList, returned+1, newTaskList, returned, taskList.length - returned -1 );
            newTaskList[taskList.length - 1] = null;
            taskList = newTaskList;
            numberOfTasks--;
            currentIndex--;
            current = returned;
            returned = -1;
            newTaskList = null;
        }
    }

}
