package hu.petrik.gorillago_android;

public class User {
    private int id;
    private String email;
    private String password;
    private String rePassword;

    public User(int id, String email, String password, String rePassword) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
}
