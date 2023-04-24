package hu.petrik.gorillago_android.classes;

public class CartItem {
    private String url;
    private String name;
    private int pricePerItem;
    private int quantity;

    public CartItem(String url, String name, int pricePerItem, int quantity) {
        this.url = url;
        this.name = name;
        this.pricePerItem = pricePerItem;
        this.quantity = quantity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(int pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return quantity * pricePerItem;
    }
}
