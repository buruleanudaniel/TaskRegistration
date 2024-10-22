import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class MainGUI extends JFrame {
    private UserService userService = new UserService();
    private TaskService taskService = new TaskService();
    private User currentUser;

    private JTextField registerUsernameField, loginUsernameField, addBalanceField, removeBalanceField, viewBalanceField;
    private JPasswordField registerPasswordField, loginPasswordField;
    private JTextArea taskListTextArea;
    private JLabel statusLabel;

    public MainGUI() {
        setTitle("User and Task Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Welcome! Please register or login.");
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.NORTH);

        // Main panels
        JPanel userPanel = createUserPanel();
        JPanel taskPanel = createTaskPanel();

        // Organize into the main layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userPanel, taskPanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        // Footer section for future use (e.g., DB connection status)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel footerLabel = new JLabel("Ready for future database integration.");
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("User Management"));

        JPanel userFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Register Section
        gbc.gridx = 0;
        gbc.gridy = 0;
        userFormPanel.add(new JLabel("Register New User"), gbc);

        registerUsernameField = new JTextField(15);
        gbc.gridy++;
        userFormPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        userFormPanel.add(registerUsernameField, gbc);

        registerPasswordField = new JPasswordField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        userFormPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        userFormPanel.add(registerPasswordField, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setToolTipText("Register a new account");
        registerButton.addActionListener(e -> registerUser());
        gbc.gridy++;
        gbc.gridx = 1;
        userFormPanel.add(registerButton, gbc);

        // Login Section
        gbc.gridx = 0;
        gbc.gridy++;
        userFormPanel.add(new JLabel("Login Existing User"), gbc);

        loginUsernameField = new JTextField(15);
        gbc.gridy++;
        userFormPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        userFormPanel.add(loginUsernameField, gbc);

        loginPasswordField = new JPasswordField(15);
        gbc.gridx = 0;
        gbc.gridy++;
        userFormPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        userFormPanel.add(loginPasswordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setToolTipText("Login to your account");
        loginButton.addActionListener(e -> loginUser());
        gbc.gridy++;
        gbc.gridx = 1;
        userFormPanel.add(loginButton, gbc);

        // Balance Section
        addBalanceField = new JTextField(10);
        JButton addBalanceButton = new JButton("Add Balance");
        addBalanceButton.addActionListener(e -> addBalance());

        removeBalanceField = new JTextField(10);
        JButton removeBalanceButton = new JButton("Remove Balance");
        removeBalanceButton.addActionListener(e -> removeBalance());

        viewBalanceField = new JTextField(10);
        JButton viewBalanceButton = new JButton("View balance");
        viewBalanceButton.addActionListener(e -> updateBalance());

        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        balancePanel.setBorder(BorderFactory.createTitledBorder("Balance Management"));
        balancePanel.add(new JLabel("Amount:"));
        balancePanel.add(addBalanceField);
        balancePanel.add(addBalanceButton);
        balancePanel.add(removeBalanceField);
        balancePanel.add(removeBalanceButton);
        //balancePanel.add(viewBalanceField);
        balancePanel.add(viewBalanceButton);

        // Combine all sections
        userPanel.add(userFormPanel, BorderLayout.CENTER);
        userPanel.add(balancePanel, BorderLayout.SOUTH);

        return userPanel;
    }

    private JPanel createTaskPanel() {
        JPanel taskPanel = new JPanel(new BorderLayout(10, 10));
        taskPanel.setBorder(BorderFactory.createTitledBorder("Task Management"));

        JPanel taskButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
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

        taskListTextArea = new JTextArea(15, 30);
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
            statusLabel.setText("User registered successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginUser() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
        currentUser = userService.login(username, password);
        if (currentUser != null) {
            statusLabel.setText("Logged in as " + currentUser.getUsername());
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Invalid credentials.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalance() {
            //double balance = Double.parseDouble(viewBalanceField.getText());
            if (currentUser != null) {
                currentUser.viewBalance();
                statusLabel.setText("Your balance is: $" + currentUser.viewBalance());
            }
    }

    private void addBalance() {
        try {
            double balance = Double.parseDouble(addBalanceField.getText());
            if (currentUser != null) {
                currentUser.addBalance(balance);
                statusLabel.setText("Balance updated: $" + currentUser.viewBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Please login first.", "Login Required", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeBalance() {
        try {
            double balance = Double.parseDouble(removeBalanceField.getText());
            if (currentUser != null) {
                currentUser.removeBalance(balance);
                statusLabel.setText("Balance updated: $" + currentUser.viewBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Please login first.", "Login Required", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewTasks() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login to view tasks.", "Login Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Task> tasks = taskService.getAllTasks();
        taskListTextArea.setText("");
        for (Task task : tasks) {
            taskListTextArea.append(String.format("ID: %d, Title: %s, Due: %s, Status: %s\n", task.getId(), task.getTitle(), task.getDueDate(), task.getStatus()));
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
