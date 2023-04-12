package hu.petrik.gorillago_android.classes;

public class User {
    private int id;
    private String email;
    private String password;
    private String rePassword;
    private String fullname;
    private String address;


    public User(int id, String email, String password, String rePassword, String fullname, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
        this.fullname = fullname;
        this.address = address;
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

    public String getAddress() {
        return address;
    }
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
