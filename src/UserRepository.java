import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users = new ArrayList<>();
    private int userIdCounter = 1;

    public User registerUser(String username, String password){
        User user = new User(userIdCounter++, username, password);
        users.add(user);
        return user;
    }

    public User findUserByUsername(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

}
