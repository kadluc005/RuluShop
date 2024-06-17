package com.example.rulushop;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalTxt;
    private TextView emptyCartTxt;
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

        cartRecyclerView = findViewById(R.id.cartView);
        totalTxt = findViewById(R.id.totaltTxt);
        emptyCartTxt = findViewById(R.id.textView4);

        // Récupération des articles du panier
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();

        // Initialisation de l'adaptateur et configuration du RecyclerView
        cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Mise à jour du prix total
        updateTotalPrice();

        // Affichage d'un message si le panier est vide
        if (cartItems.isEmpty()) {
            emptyCartTxt.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {
            emptyCartTxt.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateTotalPrice() {
        double totalPrice = CartManager.getInstance().getTotalPrice();
        totalTxt.setText(String.format("$%.2f", totalPrice));
    }
}