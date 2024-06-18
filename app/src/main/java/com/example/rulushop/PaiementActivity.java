package com.example.rulushop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.rulushop.api.RetrofitClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.example.rulushop.api.PayGateGlobalAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaiementActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://paygateglobal.com/v1/page/";
    private static final String API_KEY = "0f693398-5854-4abe-9739-4219bc2f800b";
    private static final int REQUEST_CALL_PERMISSION = 1;

    private Button payButton;
    private TextView phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement);

        phoneNumberTextView = findViewById(R.id.tel);
        payButton = findViewById(R.id.payer);
        payButton.setOnClickListener(v -> initiatePayment());
    }

    private void initiatePayment() {
        String phoneNumber = phoneNumberTextView.getText().toString();
        double amount = 1.00; // Montant à ajuster selon vos besoins
        String description = "Achat de produits";
        String identifier = "unique_transaction_id";
        String network = "TMONEY"; // ou "FLOOZ" selon votre choix

        JsonObject paymentData = new JsonObject();
        paymentData.addProperty("auth_token", API_KEY);
        paymentData.addProperty("phone_number", phoneNumber);
        paymentData.addProperty("amount", amount);
        paymentData.addProperty("description", description);
        paymentData.addProperty("identifier", identifier);
        paymentData.addProperty("network", network);

        PayGateGlobalAPI apiService = RetrofitClient.getClient(BASE_URL).create(PayGateGlobalAPI.class);
        Call<JsonObject> call = apiService.requestPayment(paymentData);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject responseBody = response.body();
                    if (responseBody != null) {
                        JsonElement txReferenceElement = responseBody.get("tx_reference");
                        if (txReferenceElement != null && !txReferenceElement.isJsonNull()) {
                            String txReference = txReferenceElement.getAsString();
                            int status = responseBody.get("status").getAsInt();
                            if (status == 0) {
                                Toast.makeText(PaiementActivity.this, "Transaction réussie: " + txReference, Toast.LENGTH_SHORT).show();
                                executeUssdCode();
                                startActivity(new Intent(PaiementActivity.this, OrderActivity.class));
                            } else {
                                Toast.makeText(PaiementActivity.this, "Erreur de transaction: " + status, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PaiementActivity.this, "Réponse invalide du serveur", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PaiementActivity.this, "Réponse vide du serveur", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaiementActivity.this, "Réponse non réussie: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("PaymentError", "Erreur de transaction", t);
                Toast.makeText(PaiementActivity.this, "Échec de la transaction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeUssdCode() {
        String ussdCode = "145*1*1*1*70701005*2#"; // Remplacer par votre code USSD
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startUssdCode(ussdCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String ussdCode = "*145*1*1*1*70701005*2#"; // Remplacer par votre code USSD
                startUssdCode(ussdCode);
            } else {
                Toast.makeText(this, "Permission refusée pour exécuter le code USSD", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startUssdCode(String ussdCode) {
        String encodedUssdCode = Uri.encode(ussdCode);
        Intent ussdIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + encodedUssdCode));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(ussdIntent);
        } else {
            Toast.makeText(this, "Permission refusée pour exécuter le code USSD", Toast.LENGTH_SHORT).show();
        }
    }
}
