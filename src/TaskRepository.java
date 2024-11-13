import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private int taskIdCounter = 1;

    public Task createTask(String title, String description, LocalDate dueDate, String status, String priority) {
        Task task = new Task(taskIdCounter++, title, description, dueDate, status, priority);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null; // Task not found
    }

    public boolean updateTask(int id, String title, String description, LocalDate dueDate, String status, String priority) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setStatus(priority);
            return true; // Update successful
        }
        return false; // Task not found
    }

    public boolean updateTaskStatus(int id, String title, String description, LocalDate dueDate, String status, String priority) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setStatus(status);
            return true; // Update successful
        }
        return false; // Task not found
    }

    public boolean deleteTask(int id) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId() == id) {
                iterator.remove();
                return true; // Successfully deleted
            }
        }
        return false; // Task not found
    }

}