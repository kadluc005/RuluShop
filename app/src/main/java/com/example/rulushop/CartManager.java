package com.example.rulushop;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final String CART_PREFS = "cart_prefs";
    private static final String CART_ITEMS_KEY = "cart_items";
    private SharedPreferences sharedPreferences;

    public CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
    }

    public void addItemToCart(CartItem item) {
        List<CartItem> cartItems = getCartItems();
        cartItems.add(item);
        saveCartItems(cartItems);
    }

    public List<CartItem> getCartItems() {
        String cartItemsJson = sharedPreferences.getString(CART_ITEMS_KEY, "[]");
        return new Gson().fromJson(cartItemsJson, new TypeToken<List<CartItem>>(){}.getType());
    }

    public void saveCartItems(List<CartItem> cartItems) {
        String cartItemsJson = new Gson().toJson(cartItems);
        sharedPreferences.edit().putString(CART_ITEMS_KEY, cartItemsJson).apply();
    }


}
