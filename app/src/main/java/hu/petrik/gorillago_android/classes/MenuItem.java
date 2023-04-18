package hu.petrik.gorillago_android.classes;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private int price;
    private String url;

    public MenuItem(int id, String name, String description, int price, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
