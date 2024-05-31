package com.example.lego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;

public class addVehiculo extends AppCompatActivity {
    private EditText color;
    private EditText nombreConductor;
    private EditText modeloVehiculo;
    private EditText placa;
    private Button botonGuardar;

    private ProgressBar progressBar;
    uid uidInstance = uid.getInstance();
    String uidd = uidInstance.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehiculo);

        color = findViewById(R.id.editTextColor);
        nombreConductor = findViewById(R.id.editTextConductor);
        modeloVehiculo = findViewById(R.id.editTextModelo);
        placa = findViewById(R.id.editTextPlaca);
        botonGuardar = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String sColor = color.getText().toString().trim();
                String sNombreConductor = nombreConductor.getText().toString().trim();
                String sModeloVehiculo = modeloVehiculo.getText().toString().trim();
                String sPlaca = placa.getText().toString().trim();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("Dueño",uidd);
                userMap.put("carga","");
                userMap.put("color", sColor);
                userMap.put("conductor", sNombreConductor);
                userMap.put("Estado", false);
                userMap.put("Modelo", sModeloVehiculo);
                userMap.put("placa", sPlaca);
                db.collection("vehiculo").add(userMap);

                Intent intent = new Intent(addVehiculo.this, DVehiculo.class);
                startActivity(intent);
            }
        });
    }
}