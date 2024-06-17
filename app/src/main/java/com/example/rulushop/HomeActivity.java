package com.example.rulushop;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rulushop.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Utilisation correcte de ViewBinding
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Assurez-vous que l'ID "main" est bien défini dans le layout XML
        View mainView = findViewById(R.id.main);

        // Suppression de la vérification de nullité
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);*/

        // Utilisation correcte de ViewBinding
        /*binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        ImageSlider imageSlider = binding.slider;
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.iphone151, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.watch, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.airpods, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        /*String[] flowerName = {"Rose", "Lotus", "Lily", "Jasmine", "Ruth"};
        int[] flowerImages = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.f};

        GridAdapter gridAdapter = new GridAdapter(HomeActivity.this, flowerName, flowerImages);
        binding.gridView.setAdapter(gridAdapter);

        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this, "You Clicked on " + flowerName[position], Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
