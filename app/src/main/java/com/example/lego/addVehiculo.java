package com.example.lego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    private EditText carga;
    private EditText color;
    private EditText nombreConductor;
    private EditText estado; // Assuming this is a CheckBox, use CheckBox instead of EditText if needed
    private EditText modeloVehiculo;
    private EditText placa;
    private Button botonGuardar;

    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehiculo);

        carga = findViewById(R.id.editTextCarga);
        color = findViewById(R.id.editTextColor);
        nombreConductor = findViewById(R.id.editTextConductor);
        estado = findViewById(R.id.checkBoxEstado); // Again, assuming CheckBox
        modeloVehiculo = findViewById(R.id.editTextModelo);
        placa = findViewById(R.id.editTextPlaca);
        botonGuardar = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String sCarga = carga.getText().toString().trim();
                String sColor = color.getText().toString().trim();
                String sNombreConductor = nombreConductor.getText().toString().trim();
                String sModeloVehiculo = modeloVehiculo.getText().toString().trim();
                String sPlaca = placa.getText().toString().trim();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("Carga", sCarga);
                userMap.put("Color", sColor);
                userMap.put("Conductor", sNombreConductor);
                userMap.put("Modelo", sModeloVehiculo);
                userMap.put("Placa", sPlaca);

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                db.collection("user").document().set(userMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(addVehiculo.this, "Exitosamente agregado", Toast.LENGTH_SHORT).show();
                                carga.getText().clear();
                                color.getText().clear();
                                nombreConductor.getText().clear();
                                    // estado.getText().clear(); // Not necessary for CheckBox, if it's a CheckBox use setChecked(false) instead
                                modeloVehiculo.getText().clear();
                                placa.getText().clear();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addVehiculo.this, "Falló al agregar los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}