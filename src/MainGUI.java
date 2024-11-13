import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MainGUI extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private UserService userService = new UserService();
    private User currentUser;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JLabel statusLabel;

    // Task Repository and Task List
    private TaskRepository taskRepository = new TaskRepository();
    private DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private JList<Task> taskList;

    public MainGUI() {
        setTitle("Emissions Task System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Welcome! Please login or register.");
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.NORTH);

        // Set up CardLayout and panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create login, register, and task panels
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();
        JPanel taskPanel = createTaskPanel(); // New Task Panel

        // Add them to the CardLayout container
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        mainPanel.add(taskPanel, "Tasks"); // Add task panel to the layout

        add(mainPanel, BorderLayout.CENTER);

        // Show the login panel at the start
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add login components
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        loginUsernameField = new JTextField(15);
        gbc.gridx = 1;
        loginPanel.add(loginUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(new JLabel("Password:"), gbc);

        loginPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        loginPanel.add(loginPasswordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy++;
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login
                String username = loginUsernameField.getText();
                String password = new String(loginPasswordField.getPassword());
                currentUser = userService.login(username, password);
                if (currentUser != null) {
                    statusLabel.setText("Access granted. Welcome, " + currentUser.getUsername() + "!");
                    cardLayout.show(mainPanel, "Tasks");
                } else {
                    statusLabel.setText("Login failed. Please try again.");
                }
            }
        });

        // Add "Register Here" hyperlink-like label
        JLabel registerLink = new JLabel("<html><a href=''>New user? Register here</a></html>");
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Switch to the Register panel
                cardLayout.show(mainPanel, "Register");
            }
        });

        gbc.gridx = 1;
        gbc.gridy++;
        loginPanel.add(registerLink, gbc);

        return loginPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add register components
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("Username:"), gbc);

        JTextField registerUsernameField = new JTextField(15);
        gbc.gridx = 1;
        registerPanel.add(registerUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(new JLabel("Password:"), gbc);

        JPasswordField registerPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        registerPanel.add(registerPasswordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy++;
        registerPanel.add(registerButton, gbc);

        // Add "Back to Login" hyperlink-like label
        JLabel loginLink = new JLabel("<html><a href=''>Back to login</a></html>");
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Switch to the Login panel
                cardLayout.show(mainPanel, "Login");
            }
        });

        gbc.gridx = 1;
        gbc.gridy++;
        registerPanel.add(loginLink, gbc);

        return registerPanel;
    }

    private JPanel createTaskPanel() {
        JPanel taskPanel = new JPanel(new BorderLayout());

        // Create task list
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for adding tasks
        JPanel addTaskPanel = new JPanel(new FlowLayout());

        JTextField titleField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField dueDateField = new JTextField(10); // Format: YYYY-MM-DD
        JTextField priorityField = new JTextField(10);
        JButton addButton = new JButton("Add Task");

        addTaskPanel.add(new JLabel("Title:"));
        addTaskPanel.add(titleField);
        addTaskPanel.add(new JLabel("Description:"));
        addTaskPanel.add(descriptionField);
        addTaskPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        addTaskPanel.add(dueDateField);
        addTaskPanel.add(new JLabel("Priority: "));
        addTaskPanel.add(priorityField);
        addTaskPanel.add(addButton);

        taskPanel.add(addTaskPanel, BorderLayout.SOUTH);

        // Add action listener for the Add button
        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String priority = priorityField.getText();
            LocalDate dueDate;
            try {
                dueDate = LocalDate.parse(dueDateField.getText());
                Task task = taskRepository.createTask(title, description, dueDate, "Pending", priority);
                taskListModel.addElement(task);
                titleField.setText("");
                descriptionField.setText("");
                dueDateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
            }
        });

        // Add right-click functionality to show description
        taskList.setComponentPopupMenu(createTaskPopupMenu());

        return taskPanel;
    }
    private JPopupMenu createTaskPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem descriptionItem = new JMenuItem("Show Description");
        JMenuItem changeDescription = new JMenuItem("Change description");
        JMenuItem updatePriority = new JMenuItem("Update priority"); // priority update
        JMenuItem updateStatus = new JMenuItem("Update status"); // status update
        JMenuItem updateDueDate = new JMenuItem("Update due date"); // due date
        JMenuItem updateTitle = new JMenuItem("Modify the title"); // modify title
        JMenuItem deleteItem = new JMenuItem("Delete Task"); // Add delete option

        updateTitle.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if(selectedTask != null){
                String newTitle = JOptionPane.showInputDialog(this, "Change the title: ");
                if(newTitle.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Invalid entry");
                }else{
                    selectedTask.setTitle(newTitle);
                }
            }
        });

        // Action for showing description
        descriptionItem.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                JOptionPane.showMessageDialog(this, "Description: " + selectedTask.getDescription());
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task.");
            }
        });

        // Action for changing description
        changeDescription.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                String newDescription = JOptionPane.showInputDialog(this, "Change the description: ");
                if(newDescription.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Invalid entry");
                }else{
                    selectedTask.setDescription(newDescription);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task.");
            }
        });

        // Action for changing priority
        updatePriority.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                String newPriority = JOptionPane.showInputDialog(this, "Change the priority: ");
                if(newPriority.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Invalid entry");
                }else{
                    selectedTask.setPriority(newPriority);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task.");
            }
        });

        // Action for changing status
        updateStatus.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                String newStatus = JOptionPane.showInputDialog(this, "Change the status: ");
                selectedTask.setStatus(newStatus);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task.");
            }
        });

        // Action for deleting a task
        deleteItem.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this task?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    TaskService taskService = new TaskService();
                    boolean isDeleted = taskService.deleteTask(selectedTask.getId()); // Correctly delete by ID
                    if (!isDeleted) {
                        JOptionPane.showMessageDialog(this, "Task deleted successfully.");
                        taskListModel.removeElement(selectedTask); // Remove from the model
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete task.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task.");
            }
        });

        // update due date
        updateDueDate.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue();
            if (selectedTask != null) {
                try {
                    String inputDate = JOptionPane.showInputDialog(this, "Change the due date (format: yyyy-MM-dd): ");
                    if (inputDate != null && !inputDate.trim().isEmpty()) {
                        LocalDate newDueDate = LocalDate.parse(inputDate);
                        selectedTask.setDueDate(newDueDate);
                    } else {
                        JOptionPane.showMessageDialog(this, "Date change canceled or empty input.");
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task.");
            }
        });

        popupMenu.add(updateTitle);
        popupMenu.add(descriptionItem);
        popupMenu.add(changeDescription); // changing description
        popupMenu.add(deleteItem); // Add the delete item to the popup menu
        popupMenu.add(updatePriority); // priority change
        popupMenu.add(updateStatus); // status change
        popupMenu.add(updateDueDate);
        return popupMenu;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
