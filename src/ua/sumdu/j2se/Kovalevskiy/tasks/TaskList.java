package ua.sumdu.j2se.Kovalevskiy.tasks;

import java.util.Iterator;

public abstract class TaskList implements Iterable<Task>, Cloneable {

    public abstract void add(Task task);

    public abstract int size();

    public abstract Task getTask(int index);

    public abstract boolean remove(Task task);

    public abstract Iterator<Task> iterator();

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TaskList)) return false;

        TaskList that = (TaskList) obj;

        if (size() != that.size()) return false;

        Iterator<Task> iter1 = this.iterator();
        Iterator<Task> iter2 = that.iterator();

        while (iter1.hasNext() & iter2.hasNext()) {
            Task task1 = iter1.next();
            Task task2 = iter2.next();
            if (task1 != null ? !task1.equals(task2) : task2 != null)
                return false;
        }

        return true;

    }

    @Override
    public int hashCode() {

        int result = 1;

        Iterator<Task> iter = iterator();
        while (iter.hasNext()) {
            Task task = iter.next();
            result = 13 * result + (task == null ? 0 : task.hashCode());
        }

        result = 13 * result + size();

        return result;

    }


}
