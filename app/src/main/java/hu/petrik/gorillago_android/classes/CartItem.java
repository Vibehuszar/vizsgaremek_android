package hu.petrik.gorillago_android.classes;

public class CartItem {
    private int menuId;
    private int pricePerItem;
    private int quantity;
    public CartItem(int menuId, int pricePerItem, int quantity) {
        this.menuId = menuId;
        this.pricePerItem = pricePerItem;
        this.quantity = quantity;
    }
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
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
