import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = BCrypt.hashpw("adminpass", BCrypt.gensalt()); // Hashing the password for security

    // Method to establish a connection to the database
    private Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/userdb";
        String user = "root";
        String password = "yourpassword"; // Replace with your MySQL root password
        return DriverManager.getConnection(url, user, password);
    }

    // Method to register a new user
    public void register(String username, String password, double balance) {
        try (Connection conn = getConnection()) {
            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Prepare SQL query to insert the new user
            String sql = "INSERT INTO users (username, password, balance) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setDouble(3, balance);
            stmt.executeUpdate();

            System.out.println("User registered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to authenticate a user login
    public User login(String username, String password) {
        if(username.equals(ADMIN_USERNAME) && BCrypt.checkpw(password, ADMIN_PASSWORD)){
            return new User(ADMIN_USERNAME, 0.0);
        }
        try (Connection conn = getConnection()) {
            // Prepare SQL query to fetch user by username
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            // Check if user exists
            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Verify password using BCrypt
                if (BCrypt.checkpw(password, storedPassword)) {
                    // Return a new User object if the password matches
                    return new User(rs.getString("username"), rs.getDouble("balance"));
                } else {
                    System.out.println("Invalid password.");
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if login fails
    }
}
