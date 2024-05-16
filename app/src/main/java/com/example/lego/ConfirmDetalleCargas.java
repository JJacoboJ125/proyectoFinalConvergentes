package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmDetalleCargas extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("cargas");

    private TextView ciudado;
    private TextView departo;
    private TextView direcciono;
    private TextView hora_fechao;

    private TextView ciudadd;
    private TextView departd;
    private TextView direcciond;

    private TextView altura;
    private TextView ancho;
    private TextView largo;
    private TextView peso;
    private TextView titulo;
    private TextView descrip;
    private TextView tipo_merca;
    private TextView estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detalle_cargas);

        // Initialize TextViews
        ciudado = findViewById(R.id.ciudadO);
        departo = findViewById(R.id.departamentoO);
        direcciono = findViewById(R.id.direccionO);
        hora_fechao = findViewById(R.id.fechaHora);

        ciudadd = findViewById(R.id.ciudadD);
        departd = findViewById(R.id.departamentoD);
        direcciond = findViewById(R.id.direccionD);

        altura = findViewById(R.id.altura);
        ancho = findViewById(R.id.anchura);
        largo = findViewById(R.id.largoC);
        peso = findViewById(R.id.pesoC);
        titulo = findViewById(R.id.tituloC);
        descrip = findViewById(R.id.descripcion);
        tipo_merca = findViewById(R.id.tipoMerca);

        String documentId = getIntent().getStringExtra("DOCUMENT_ID");
        // Get the document data
        collectionRef.document(documentId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Extract data from document
                String ciudadOrigen = documentSnapshot.getString("ciudadO");
                String departamentoOrigen = documentSnapshot.getString("departamentoO");
                String direccionOrigen = documentSnapshot.getString("direccionO");
                String fechaHoraOrigen = documentSnapshot.getString("hora_fecha_recogidaO");

                String ciudadDestino = documentSnapshot.getString("ciudadE");
                String departamentoDestino = documentSnapshot.getString("departamentoE");
                String direccionDestino = documentSnapshot.getString("direccionE");

                String alturaCarga = documentSnapshot.getString("alto");
                String anchoCarga = documentSnapshot.getString("ancho");
                String largoCarga = documentSnapshot.getString("largo");
                String pesoCarga = documentSnapshot.getString("peso");
                String tituloCarga = documentSnapshot.getString("titulo");
                String descripcionCarga = documentSnapshot.getString("descrip");
                String tipoMercancia = documentSnapshot.getString("tipo_mercancia");

                // Update TextViews
                ciudado.append(ciudadOrigen);
                departo.append(departamentoOrigen);
                direcciono.append(direccionOrigen);
                hora_fechao.append(fechaHoraOrigen);

                ciudadd.append(ciudadDestino);
                departd.append(departamentoDestino);
                direcciond.append(direccionDestino);

                altura.append(alturaCarga);
                ancho.append(anchoCarga);
                largo.append(largoCarga);
                peso.append(pesoCarga);
                titulo.append(tituloCarga);
                descrip.append(descripcionCarga);
                tipo_merca.append(tipoMercancia);
            } else {
                // Handle the case where the document does not exist
                titulo.setText("Documento no encontrado");
            }
        }).addOnFailureListener(e -> {
            // Handle the error
            titulo.setText("Error al obtener los datos: " + e.getMessage());
        });
    }


}
