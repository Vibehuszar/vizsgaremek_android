package hu.petrik.gorillago_android.classes;

public class Token {
    String token;
    int userId;

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Token(String token, int id) {
        this.token = token;
        this.userId = id;
    }
}
