public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public User register(String username, String password, double balance) {
        return userRepository.registerUser(username, password, balance);
    }

    public User login(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login successful
        }
        return null; // Login failed
    }

    public User addBalance(String username, double balance){
        User user = userRepository.findUserByUsername(username);
        if(user != null){
            user.addBalance(balance);
            return user;
        }
        return null;
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteUser(id);
    }
}