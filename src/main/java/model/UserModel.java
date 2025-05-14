package main.java.model;

public class UserModel {
    private String username;
    private String password;
    private String preferredName;

    public UserModel(String username, String password, String preferredName) {
        this.username = username;
        this.password = password;
        this.preferredName = preferredName;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPreferredName() { return preferredName; }
}
