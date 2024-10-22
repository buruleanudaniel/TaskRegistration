import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private int userIdCounter = 1;

    public User registerUser(String username, String password, double balance){
        User user = new User(userIdCounter++, username, password, balance);
        users.add(user);
        return user;
    }

    public User findUserById(int id){
        for(User user : users){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public boolean deleteUser(int id) {
        User user = findUserById(id);
        if (user != null) {
            users.remove(user);
            return true; // Deletion successful
        }
        return false; // Task not found
    }
}
