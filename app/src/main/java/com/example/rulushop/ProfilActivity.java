package com.example.rulushop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;
    private TextView usernameTextView;
    private TextView emailTextView;
    private ImageView profileImage;
    private Button logoutButton;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Trouver les références des vues
        profileImage = findViewById(R.id.profile_image);
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        logoutButton = findViewById(R.id.return_to_cart_button);

        // Récupérer l'utilisateur actuellement connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Nom d'utilisateur de l'utilisateur connecté
            String username = user.getDisplayName();
            // Email de l'utilisateur connecté
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Afficher les informations dans les TextView appropriés
            usernameTextView.setText(user.getDisplayName());
            emailTextView.setText(email);
            if (photoUrl != null) {
                Glide.with(this)
                        .load(photoUrl)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.btn_4); // Image par défaut
            }
        }

        // Ajouter un OnClickListener au bouton de déconnexion
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Déconnexion de l'utilisateur
                // Redirection vers l'écran de connexion (LoginActivity)
                Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Ferme l'activité actuelle pour empêcher de revenir en arrière
            }
        });

        // Ajouter un OnClickListener à l'icône de la caméra
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher un dialogue pour choisir entre la galerie et la caméra
                showImageSourceChooser();
            }
        });
    }

    private void showImageSourceChooser() {
        // Afficher un dialogue pour choisir entre la galerie et la caméra
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Créer une intention pour choisir l'option de la galerie
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Choisir une photo");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });

        // Lancer l'intention avec choix
        startActivityForResult(chooserIntent, REQUEST_IMAGE_PICK);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Gérer le résultat de l'intention lancée (ici pour la prise de photo ou la sélection depuis la galerie)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Photo prise avec l'appareil photo
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // Mettre à jour l'ImageView avec l'image capturée
                    profileImage.setImageBitmap(imageBitmap);
                }
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Image sélectionnée depuis la galerie
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    // Mettre à jour l'ImageView avec l'image sélectionnée
                    profileImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
