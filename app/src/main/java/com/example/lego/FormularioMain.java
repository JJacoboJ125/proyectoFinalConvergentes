package com.example.lego;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;


import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormularioMain extends AppCompatActivity{
    TextView Departamento,ciudad,direccion,postal,hora, fecha;
    TextView depD, ciudadD, direccionD ,postaD,fechaEntrega;
    TextView titulo,tipoMerca,largo,ancho,alto,descrip;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    uid uidInstance = uid.getInstance();
    String uidd = uidInstance.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_origen);
        Departamento = (TextView) findViewById(R.id.DepartamentoO);
        ciudad = (TextView) findViewById(R.id.CiudadO);
        direccion = (TextView) findViewById(R.id.DireccionO);
        postal = (TextView) findViewById(R.id.postal);
        hora = (TextView) findViewById(R.id.horaRe);
        fecha = (TextView) findViewById(R.id.fechaRe);
    }

    public void seguiaADestino(View view){
        setContentView(R.layout.activity_formulario_destino);
        depD = (TextView) findViewById(R.id.DepartamentoD);
        ciudadD = (TextView) findViewById(R.id.CiudadD);
        direccionD = (TextView) findViewById(R.id.DireccionD);
        postaD = (TextView) findViewById(R.id.postalD);
        fechaEntrega = (TextView) findViewById(R.id.fechaEntrega);
    }
    public void seguiaACarga(View view){
        setContentView(R.layout.activity_formulario_carga);
        titulo = (TextView) findViewById(R.id.titulo);
        tipoMerca = (TextView) findViewById(R.id.tipoMercancia);
        largo = (TextView) findViewById(R.id.largo);
        ancho = (TextView) findViewById(R.id.ancho);
        alto = (TextView) findViewById(R.id.alto);
        descrip = (TextView) findViewById(R.id.detAdd);
    }
    public void VolverAInicio(View view){
        AlmacenaDatos();
        Intent intent = new Intent(this, HomeDCActivity.class);
        startActivity(intent);
    }

    public void AlmacenaDatos(){
        Map <String, Object> ColeccionDatos = new HashMap<>();
        ColeccionDatos.put("uid",uidd);
        ColeccionDatos.put("aceptada",false);
        ColeccionDatos.put("active",true);
        ColeccionDatos.put("ancho",ancho.getText().toString());
        ColeccionDatos.put("ciudadE",ciudadD.getText().toString());
        ColeccionDatos.put("ciudadO",ciudad.getText().toString());
        ColeccionDatos.put("conductor","");
        ColeccionDatos.put("departamentoE",depD.getText().toString());
        ColeccionDatos.put("departamentoO",Departamento.getText().toString());
        ColeccionDatos.put("descrip",descrip.getText().toString());
        ColeccionDatos.put("dig_postalO",postal.getText().toString());
        ColeccionDatos.put("digitpostalE",postaD.getText().toString());
        ColeccionDatos.put("direccionE",direccion.getText().toString());
        ColeccionDatos.put("direccionO",direccionD.getText().toString());
        ColeccionDatos.put("fechaDesEntrega",fechaEntrega.getText().toString());
        ColeccionDatos.put("hora_fecha_recogidaO",hora.getText().toString()+"  "+fecha.getText().toString());
        ColeccionDatos.put("largo",largo.getText().toString());
        ColeccionDatos.put("peso",Departamento.getText().toString());
        ColeccionDatos.put("responsable","");
        ColeccionDatos.put("tipo_mercancia",tipoMerca.getText().toString());
        ColeccionDatos.put("alto",alto.getText().toString());
        ColeccionDatos.put("titulo",titulo.getText().toString());
        CollectionReference dbRef = db.collection("cargas");
        dbRef.add(ColeccionDatos);
    }


}