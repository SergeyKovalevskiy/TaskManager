package ua.sumdu.j2se.Kovalevskiy.tasks;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedTaskList extends TaskList{
    private int numberOfTasks = 0;
    private LinkedTaskListElement firstElement;
    private LinkedTaskListElement lastElement;

    private class LinkedTaskListElement {
        private LinkedTaskListElement prevElement;
        private LinkedTaskListElement nextElement;
        private Task task;

        public LinkedTaskListElement(Task task) {
            this.task = task;
        }

        public Task getInnerTask() {
            return task;
        }

        public LinkedTaskListElement getNextElement() {
            return nextElement;
        }

        public LinkedTaskListElement getPrevElement() {
            return prevElement;
        }

        public void setPrevElement(LinkedTaskListElement prevElement) {
            this.prevElement = prevElement;
        }

        public void setNextElement(LinkedTaskListElement nextElement) {
            this.nextElement = nextElement;
        }

        public void setTask(Task task) {
            this.task = task;
        }
    }

    public void add(Task task) {
        if(task == null) {
            throw new IllegalArgumentException("You can not add null to the list ");
        }
        LinkedTaskListElement adding = new LinkedTaskListElement(task);
        if(this.numberOfTasks == 0) {
            this.firstElement = this.lastElement = adding;
            numberOfTasks++;
        }
        else {
            this.lastElement.setNextElement(adding);
            adding.setPrevElement(this.lastElement);
            this.lastElement = adding;
            numberOfTasks++;
        }
    }

    public boolean remove(Task task) {
        if(numberOfTasks == 0 || task == null) {
            return false;
        }
        for (int i = 0; i < this.numberOfTasks; i++) {
            if(this.getTask(i).equals(task)) {
                if(i == 0) {
                    this.firstElement = this.firstElement.getNextElement();
                    numberOfTasks--;
                    return true;
                }
                if(i == numberOfTasks - 1) {
                    this.lastElement = this.lastElement.getPrevElement();
                    numberOfTasks--;
                    return true;
                }
                LinkedTaskListElement current = this.firstElement;
                for (int j = 0; j < i ; j++) {
                    current = current.getNextElement();
                }
                LinkedTaskListElement before = current.getPrevElement();
                LinkedTaskListElement after = current.getNextElement();
                before.setNextElement(after);
                after.setPrevElement(before);
                numberOfTasks--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return numberOfTasks;
    }

    public Task getTask(int index) {
        LinkedTaskListElement current = this.firstElement;
        for (int i = 0; i < index; i++) {
            current = current.getNextElement();
        }
        return current.getInnerTask();
    }

    @Override
    public LinkedTaskList clone() throws CloneNotSupportedException {
        LinkedTaskList cloned = (LinkedTaskList) super.clone();
        cloned.numberOfTasks = 0;
        cloned.firstElement = null;
        cloned.lastElement = null;
        for (int i = 0; i < this.size() ; i++) {
            cloned.add(this.getTask(i).clone());
        }
        return cloned;
    }

    public Iterator<Task> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Task> {
        LinkedTaskListElement current = firstElement;
        LinkedTaskListElement returned = null;
        int currentIndex = 0;
        int returnedIndex = -1;

        public boolean hasNext() {
            return  (currentIndex < size());
        }


        public Task next() throws NoSuchElementException {
            if(currentIndex >= size()) {
                throw new NoSuchElementException();
            }
            returned = current;
            returnedIndex = currentIndex;
            current = current.getNextElement();
            currentIndex++;
            return returned.getInnerTask();
        }


        public void remove() {
            if(returnedIndex < 0) {
                throw new IllegalStateException();
            }
            if(returnedIndex == 0) {
                firstElement = firstElement.getNextElement();
                numberOfTasks--;
                currentIndex = 0;
                //current;
                returnedIndex = -1;
                returned = null;
                return ;
            }
            if(returnedIndex == numberOfTasks - 1) {
                lastElement = lastElement.getPrevElement();
                numberOfTasks--;
                currentIndex = returnedIndex;
                current = null;
                returnedIndex = -1;
                returned = null;
                return;
            }
            LinkedTaskListElement before = returned.getPrevElement();
            LinkedTaskListElement after = returned.getNextElement();
            before.setNextElement(after);
            after.setPrevElement(before);
            currentIndex = returnedIndex;
            current = after;
            returnedIndex = -1;
            returned = null;
            numberOfTasks--;
            return ;
        }
    }

}