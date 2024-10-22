public class User {
    private String username;
    private String password;
    private int id;
    private double balance;

    public User(int id, String username, String password, double balance){
        this.username = username;
        this.password = password;
        this.id = id;
        this.balance = 0;
    }

    public User(int id){
        this.id = id;
    }

    public User(String username, double balance) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double viewBalance(){
        return this.balance;
    }

    public void addBalance(double balance){
        this.balance += balance;
        System.out.println("Your balance is " + this.balance);
    }

    public void removeBalance(double balance){
        this.balance -= balance;
        System.out.println("Your balance is " + this.balance);
    }
}
