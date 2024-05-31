package com.example.lego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class selectVehiculo extends AppCompatActivity {

    uid uidInstance = uid.getInstance();
    String uidd = uidInstance.getUid();
    private final MutableLiveData<List<String>> placasV = new MutableLiveData<>();
    private final MutableLiveData<List<String>> idV = new MutableLiveData<>();
    private final MutableLiveData<List<String>> correoV = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser prueba = FirebaseAuth.getInstance().getCurrentUser();
    private ListView listaPlacas;
    private String documentId;

    CollectionReference collectionRef = db.collection("vehiculo");
    CollectionReference Conductor = db.collection("detUsuarios");
    CollectionReference Cargas = db.collection("cargas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehiculo);
        listaPlacas = findViewById(R.id.listaVehiculos);

        // Obtener el extra del Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DOCUMENT_ID")) {
            documentId = intent.getStringExtra("DOCUMENT_ID");
        } else {
            Log.e("selectVehiculo", "No se recibió el DOCUMENT_ID en el Intent");
            // Manejar este caso adecuadamente
            return;
        }

        collectionRef.whereEqualTo("Estado", false).whereEqualTo("Dueño", uidd).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> placas = new ArrayList<>();
            List<String> idVe = new ArrayList<>();
            List<String> correos = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                placas.add(documentSnapshot.getString("placa"));
                idVe.add(documentSnapshot.getId());
                correos.add(documentSnapshot.getString("conductor"));
            }
            if (!placas.isEmpty()) {
                placasV.setValue(placas);
                idV.setValue(idVe);
                correoV.setValue(correos);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, placasV.getValue());
                listaPlacas.setAdapter(adapter);

                listaPlacas.setOnItemClickListener((parent, view, position, id) -> {
                    String codigo = idVe.get(position);
                    String correo = correos.get(position);
                    collectionRef.document(codigo).update("carga", documentId);
                    String uid;

                    Conductor.whereEqualTo("correo", correo).get().addOnSuccessListener(querySnapshot -> {
                        String uiid="";
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
                            uiid = documentSnapshot.getString("uid");
                        }
                        if (!uiid.isEmpty()) {
                            collectionRef.document(codigo).update("Estado", true);
                            Cargas.document(documentId).update("conductor", uiid);
                            Cargas.document(documentId).update("aceptada", true);
                            Cargas.document(documentId).update("responsable", uidd);
                            Intent intentt = new Intent(this, DVehiculo.class);
                            startActivity(intentt);
                        }
                    }).addOnFailureListener(e -> {
                        // Manejar fallo
                        Log.e("FirestoreError", "Error en la consulta", e);
                    });
                });

            } else {
                List<String> emptyList = new ArrayList<>();
                emptyList.add("No tienes vehiculos disponibles");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emptyList);
                listaPlacas.setAdapter(adapter);
            }
        }).addOnFailureListener(e -> {
            // Manejar fallo en la consulta
            Log.e("FirestoreError", "Error en la consulta", e);
        });
    }

    private interface OnConductorUidReceivedListener {
        void onUidReceived(String uid);
    }
}
