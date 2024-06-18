package com.example.rulushop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        cartManager = new CartManager(this);

        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        float rating = getIntent().getFloatExtra("rating", 0);

        // Mettre à jour les vues avec les données récupérées
        TextView titleTxt = findViewById(R.id.titleTxt);
        TextView priceTxt = findViewById(R.id.priceTxt);
        TextView descriptionTxt = findViewById(R.id.textView10);
        RatingBar ratingBar = findViewById(R.id.ratingBar2);
        ImageView imageView = findViewById(R.id.image_detail);
        Button addToCartBtn = findViewById(R.id.addToCart);

        Glide.with(this).load(image).into(imageView);
        titleTxt.setText(title);
        priceTxt.setText(price);
        descriptionTxt.setText(description);
        ratingBar.setRating(rating);

        ImageView img = findViewById(R.id.back_btn);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addToCartBtn.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(title, parsePrice(price), 1, image);
            cartManager.addItemToCart(cartItem);
            Toast.makeText(this, "Produit ajouté au panier", Toast.LENGTH_SHORT).show();
        });

    }

    private double parsePrice(String price) throws NumberFormatException {
        // Replace comma with dot
        String normalizedPrice = price.replace(",", ".");
        normalizedPrice = normalizedPrice.replace(" F CFA", "");
        return Double.parseDouble(normalizedPrice);
    }
}