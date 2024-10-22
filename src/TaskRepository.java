import java.time.LocalDate;
import java.util.ArrayList;
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
            task.setStatus(status);
            return true; // Update successful
        }
        return false; // Task not found
    }

    public boolean deleteTask(int id) {
        Task task = getTaskById(id);
        if (task != null) {
            tasks.remove(task);
            return true; // Deletion successful
        }
        return false; // Task not found
    }

}