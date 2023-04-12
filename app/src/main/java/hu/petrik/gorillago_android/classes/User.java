package hu.petrik.gorillago_android.classes;

public class User {
    private int id;
    private String email;
    private String password;
    private String rePassword;
    private String firstName;
    private String lastName;
    private int addressId;

    public User(int id, String email, String password, String rePassword, String firstName, String lastName, int addressId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressId = addressId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
