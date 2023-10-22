package bugmanager.model;

public class User {
    String username;
    String password;
    boolean activated;
    UserRole role;
    public enum UserRole{
        Admin,
        Tester,
        Programmer;
    }

    public User() {
    }

    public User(String username, String password, boolean activated, UserRole role) {
        this.username = username;
        this.password = password;
        this.activated = activated;
        this.role = role;
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

    public boolean isActivated() {
        return activated;
    }
    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
