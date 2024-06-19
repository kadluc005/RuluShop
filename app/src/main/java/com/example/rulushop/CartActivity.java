package com.example.rulushop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalTxt, subtotalTxt, deliveryTxt, taxTxt, emptyCartTxt, removeCartTxt;
    private EditText couponEditText;
    private Button applyCouponButton, checkOutBtn;
    private CartManager cartManager;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView img = findViewById(R.id.back_img);
        img.setOnClickListener(v -> finish());

        cartRecyclerView = findViewById(R.id.cartView);
        totalTxt = findViewById(R.id.totaltTxt);
        subtotalTxt = findViewById(R.id.totalFeetTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        taxTxt = findViewById(R.id.taxTxt);
        emptyCartTxt = findViewById(R.id.textView4);
        couponEditText = findViewById(R.id.editTextText2);
        applyCouponButton = findViewById(R.id.button2);
        checkOutBtn = findViewById(R.id.checkOutBtn);

        // Initialize CartManager
        cartManager = new CartManager(this);

        // Setup RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch cart items
        List<CartItem> cartItems = getCartItems();
        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        // Check if cart is empty
        if (cartItems.isEmpty()) {
            emptyCartTxt.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {
            emptyCartTxt.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
        }

        // Update total amounts
        updateTotals(cartItems);

        // Apply coupon
        applyCouponButton.setOnClickListener(v -> applyCoupon(couponEditText.getText().toString()));

        // Checkout button
        checkOutBtn.setOnClickListener(v -> checkOut());
    }

    private List<CartItem> getCartItems() {
        return cartManager.getCartItems();
    }

    public void updateTotals(List<CartItem> cartItems) {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        double delivery = calculateDelivery(subtotal);
        double tax = calculateTax(subtotal);
        double total = subtotal + delivery + tax;

        subtotalTxt.setText(String.format("F CFA %.2f", subtotal));
        deliveryTxt.setText(String.format("F CFA %.2f", delivery));
        taxTxt.setText(String.format("F CFA %.2f", tax));
        totalTxt.setText(String.format("F CFA %.2f", total));
    }

    private double calculateDelivery(double subtotal) {
        return 50.0;
    }

    private double calculateTax(double subtotal) {
        return subtotal * 0.18;
    }

    private void applyCoupon(String couponCode) {
        // Replace with your coupon application logic
        if ("DISCOUNT10".equals(couponCode)) {
            // Apply 10% discount
            List<CartItem> cartItems = getCartItems();
            double subtotal = 0.0;
            for (CartItem item : cartItems) {
                subtotal += item.getPrice() * item.getQuantity();
            }
            double discount = subtotal * 0.10;
            double totalAfterDiscount = subtotal - discount;
            subtotalTxt.setText(String.format("F CFA %.2f", totalAfterDiscount));
        }
    }

    private void checkOut() {
        startActivity(new Intent(CartActivity.this, PaiementActivity.class));
    }

    public void removeCartItem(int position) {
        List<CartItem> cartItems = getCartItems();

        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position); // Supprime l'élément à la position donnée

            // Mettre à jour les préférences partagées, la base de données, ou tout autre moyen de stockage des données du panier
            cartManager.saveCartItems(cartItems); // Exemple fictif de méthode pour enregistrer les modifications du panier

            // Mettre à jour l'interface utilisateur
            if (cartItems.isEmpty()) {
                emptyCartTxt.setVisibility(View.VISIBLE);
                cartRecyclerView.setVisibility(View.GONE);
            } else {
                emptyCartTxt.setVisibility(View.GONE);
                cartRecyclerView.setVisibility(View.VISIBLE);
            }

            updateTotals(cartItems); // Méthode pour mettre à jour les totaux du panier

            // Notifier l'adaptateur que l'article a été supprimé
            cartAdapter.notifyItemRemoved(position);
        }
    }

}

