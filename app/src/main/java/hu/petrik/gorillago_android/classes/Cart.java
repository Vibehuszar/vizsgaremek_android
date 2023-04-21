package hu.petrik.gorillago_android.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static final String PREF_NAME = "shopping_cart";
    private static final String CART_ITEMS = "cart_items";
    private int totalPrice;

    private SharedPreferences preferences;
    private Gson gson;

    public Cart(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        totalPrice = 0;
    }

    public void addItem(CartItem item) {
        List<CartItem> cartItems = getItems();
        cartItems.add(item);
        saveItems(cartItems);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (CartItem item : getItems()) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

    public void removeItem(int index) {
        List<CartItem> cartItems = getItems();
        cartItems.remove(index);
        saveItems(cartItems);
    }

    public List<CartItem> getItems() {
        String json = preferences.getString(CART_ITEMS, "");
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> cartItems = gson.fromJson(json, type);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    private void saveItems(List<CartItem> cartItems) {
        String json = gson.toJson(cartItems);
        preferences.edit().putString(CART_ITEMS, json).apply();
    }
}
