package com.example.rulushop;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rulushop.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View mainView = findViewById(R.id.main);

        // Lier les vues avec leurs ID
        ImageView profileIcon = findViewById(R.id.username);
        TextView profileText = findViewById(R.id.profil);

        // Gérer le clic sur l'icône de profil (ImageView)
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité Profil ici
                Intent intent = new Intent(HomeActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le texte de profil (TextView)
        profileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité Profil ici
                Intent intent = new Intent(HomeActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        // Configuration du slider d'images
        ImageSlider imageSlider = binding.slider;
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.iphone151, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.watch, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.airpods, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Configuration du RecyclerView pour afficher les produits populaires
        recyclerView = findViewById(R.id.recycleViewPopular);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialisation de la liste des produits et de l'adaptateur
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // Référence à la base de données Firebase Realtime
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Écouteur pour récupérer les données depuis Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear(); // Efface la liste actuelle des produits
                // Parcourt les données de snapshot pour ajouter les produits à la liste
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged(); // Met à jour l'UI avec les nouveaux produits
                findViewById(R.id.progressBarPopular).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Échec du chargement des produits : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                findViewById(R.id.progressBarPopular).setVisibility(View.GONE);
            }
        });
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        EditText searchEditText = findViewById(R.id.editTextText);

        // Ajouter un TextWatcher à l'EditText pour écouter les changements de texte
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Rien à faire après le changement de texte
            }
        });
    }
    public List getProductList(){
        return productList;
    }

    private void filter(String text) {
        List<Product> filteredList = productList.stream()
                .filter(product -> product.getTitle().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());

        adapter.filterList(filteredList);
    }
}