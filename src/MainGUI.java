import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MainGUI extends JFrame {
    private UserService userService = new UserService();
    private TaskService taskService = new TaskService();
    private User currentUser;

    private JTextField registerUsernameField, loginUsernameField, addBalanceField;
    private JPasswordField registerPasswordField, loginPasswordField;
    private JTextArea taskListTextArea;

    public MainGUI() {
        setTitle("User and Task Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(createUserPanel());
        mainPanel.add(createTaskPanel());

        add(mainPanel);
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new GridLayout(8, 1));
        userPanel.setBorder(BorderFactory.createTitledBorder("User Management"));

        // Register Section
        userPanel.add(new JLabel("Register:"));
        registerUsernameField = new JTextField(10);
        registerPasswordField = new JPasswordField(10);
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        userPanel.add(new JLabel("Username:"));
        userPanel.add(registerUsernameField);
        userPanel.add(new JLabel("Password:"));
        userPanel.add(registerPasswordField);
        userPanel.add(registerButton);

        // Login Section
        userPanel.add(new JLabel("Login:"));
        loginUsernameField = new JTextField(10);
        loginPasswordField = new JPasswordField(10);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginUser());
        userPanel.add(new JLabel("Username:"));
        userPanel.add(loginUsernameField);
        userPanel.add(new JLabel("Password:"));
        userPanel.add(loginPasswordField);
        userPanel.add(loginButton);

        // Add balance button
        addBalanceField = new JTextField(10);
        JButton addBalanceButton = new JButton("Add Balance");
        addBalanceButton.addActionListener(e -> addBalance());
        userPanel.add(new JLabel("Add Balance:"));
        userPanel.add(addBalanceField);
        userPanel.add(addBalanceButton);

        return userPanel;
    }

    private JPanel createTaskPanel() {
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBorder(BorderFactory.createTitledBorder("Task Management"));

        JPanel taskButtonPanel = new JPanel(new GridLayout(4, 1));
        JButton viewTasksButton = new JButton("View Tasks");
        JButton createTaskButton = new JButton("Create Task");
        JButton updateTaskButton = new JButton("Update Task");
        JButton deleteTaskButton = new JButton("Delete Task");

        viewTasksButton.addActionListener(e -> viewTasks());
        createTaskButton.addActionListener(e -> createTask());
        updateTaskButton.addActionListener(e -> updateTask());
        deleteTaskButton.addActionListener(e -> deleteTask());

        taskButtonPanel.add(viewTasksButton);
        taskButtonPanel.add(createTaskButton);
        taskButtonPanel.add(updateTaskButton);
        taskButtonPanel.add(deleteTaskButton);

        taskListTextArea = new JTextArea(10, 20);
        taskListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskListTextArea);

        taskPanel.add(taskButtonPanel, BorderLayout.NORTH);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        return taskPanel;
    }

    private void registerUser() {
        String username = registerUsernameField.getText();
        String password = new String(registerPasswordField.getPassword());
        if (!username.isEmpty() && !password.isEmpty()) {
            userService.register(username, password, 0);
            JOptionPane.showMessageDialog(this, "User registered successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
        }
    }

    private void loginUser() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
        currentUser = userService.login(username, password);
        if (currentUser != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Invalid credentials.");
        }
    }

    private void addBalance() {
        try {
            double balance = Double.parseDouble(addBalanceField.getText());
            if (currentUser != null) {
                currentUser.addBalance(balance);
                JOptionPane.showMessageDialog(this, "Balance added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please login first.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount.");
        }
    }

    private void viewTasks() {
        List<Task> tasks = taskService.getAllTasks();
        taskListTextArea.setText("");
        for (Task task : tasks) {
            taskListTextArea.append("ID: " + task.getId() + ", Title: " + task.getTitle() + "\n");
        }
    }

    private void createTask() {
        String title = JOptionPane.showInputDialog(this, "Enter Task Title:");
        String description = JOptionPane.showInputDialog(this, "Enter Task Description:");
        String dueDateStr = JOptionPane.showInputDialog(this, "Enter Due Date (YYYY-MM-DD):");
        LocalDate dueDate = LocalDate.parse(dueDateStr);
        String status = JOptionPane.showInputDialog(this, "Enter Task Status:");
        String priority = JOptionPane.showInputDialog(this, "Enter Task Priority:");
        taskService.createTask(title, description, dueDate, status, priority);
        JOptionPane.showMessageDialog(this, "Task created successfully!");
    }

    private void updateTask() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Task ID to update:"));
        String title = JOptionPane.showInputDialog(this, "Enter new Task Title:");
        taskService.updateTask(id, title, "", LocalDate.now(), "", "");
        JOptionPane.showMessageDialog(this, "Task updated!");
    }

    private void deleteTask() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Task ID to delete:"));
        taskService.deleteTask(id);
        JOptionPane.showMessageDialog(this, "Task deleted!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
