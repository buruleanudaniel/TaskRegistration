public class UserService {
    private UserRepository userRepository = new UserRepository();

    public User register(String username, String password) {
        return userRepository.registerUser(username, password);
    }

    public User login(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login successful
        }
        return null; // Login failed
    }
}