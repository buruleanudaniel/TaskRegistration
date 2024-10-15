import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository = new TaskRepository();

    public Task createTask(String title, String description, LocalDate dueDate, String status, String priority) {
        return taskRepository.createTask(title, description, dueDate, status, priority);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public Task getTaskById(int id) {
        return taskRepository.getTaskById(id);
    }

    public boolean updateTask(int id, String title, String description, LocalDate dueDate, String status, String priority) {
        return taskRepository.updateTask(id, title, description, dueDate, status, priority);
    }

    public boolean deleteTask(int id) {
        return taskRepository.deleteTask(id);
    }
}