import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static UserService userService = new UserService();
    private static TaskService taskService = new TaskService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            // if logged -> show task options
            if (currentUser != null) {
                taskMenu();
            }
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if(password.isEmpty() || username.isEmpty()){
            System.out.println("You cannot enter an empty username / password.");
        }else{
            User user = userService.register(username, password, 0);
            System.out.println("Registration successful! User ID: " + user.getId());
        }

    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        currentUser = userService.login(username, password);
        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + currentUser.getUsername());
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private static void taskMenu() {
        while (currentUser != null) {
            System.out.println("\nTask Menu:");
            System.out.println("1. View balance");
            System.out.println("2. Add balance");
            System.out.println("3. Create Task");
            System.out.println("4. View All Tasks");
            System.out.println("5. Update Task");
            System.out.println("6. Delete Task");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    seeBalance();
                    break;
                case 2:
                    addBalance();
                    break;
                case 3:
                    createTask();
                    break;
                case 4:
                    viewTasks();
                    break;
                case 5:
                    updateTask();
                    break;
                case 6:
                    deleteTask();
                    break;
                case 7:
                    currentUser = null; // Logout
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private static void seeBalance(){
        System.out.println("So you want to view your balance, huh?");
        System.out.println("\n Your balance is " + currentUser.viewBalance());
    }

    private static void addBalance(){
        System.out.println("Enter the balance you want to add: ");
        double balanceToAdd = Integer.valueOf(scanner.nextLine());
        currentUser.addBalance(balanceToAdd);
    }

    private static void createTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        System.out.print("Enter due date (YYYY-MM-DD): ");
        String dueDateStr = scanner.nextLine();
        LocalDate dueDate = LocalDate.parse(dueDateStr);
        System.out.print("Enter task status (Pending/Completed): ");
        String status = scanner.nextLine();
        System.out.print("Enter task priority (High/Medium/Low): ");
        String priority = scanner.nextLine();

        Task task = taskService.createTask(title, description, dueDate, status, priority);
        System.out.println("Task created! Task ID: " + task.getId());
    }

    private static void viewTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (Task task : tasks) {
                System.out.println("ID: " + task.getId() + ", Title: " + task.getTitle() +
                        ", Status: " + task.getStatus() + ", Due Date: " + task.getDueDate());
            }
        }
    }

    private static void updateTask() {
        System.out.print("Enter task ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Task task = taskService.getTaskById(id);
        if (task != null) {
            System.out.print("Enter new task title (leave blank for no change): ");
            String title = scanner.nextLine();
            System.out.print("Enter new task description (leave blank for no change): ");
            String description = scanner.nextLine();
            System.out.print("Enter new due date (YYYY-MM-DD, leave blank for no change): ");
            String dueDateStr = scanner.nextLine();
            LocalDate dueDate = (dueDateStr.isEmpty()) ? task.getDueDate() : LocalDate.parse(dueDateStr);
            System.out.print("Enter new status (leave blank for no change): ");
            String status = scanner.nextLine();
            System.out.print("Enter new priority (leave blank for no change): ");
            String priority = scanner.nextLine();

            boolean updated = taskService.updateTask(id, title.isEmpty() ? task.getTitle() : title,
                    description.isEmpty() ? task.getDescription() : description,
                    dueDate, status.isEmpty() ? task.getStatus() : status,
                    priority.isEmpty() ? task.getPriority() : priority);
            if (updated) {
                System.out.println("Task updated successfully.");
            } else {
                System.out.println("Task not found.");
            }
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void deleteTask() {
        System.out.print("Enter task ID to delete: ");
        int id = scanner.nextInt();
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }
}