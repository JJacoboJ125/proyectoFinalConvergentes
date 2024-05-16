package com.example.lego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ConfirmDetalleCargas extends AppCompatActivity {

    public String PruebaCarga = "QjOVbHBqdj2Vg484PUPx";
    private TextView ciudado = findViewById(R.id.ciudadO);
    private TextView departo= findViewById(R.id.departamentoO);
    private TextView direcciono= findViewById(R.id.direccionO);
    private TextView hora_fechao= findViewById(R.id.fechaHora);

    private TextView ciudadd= findViewById(R.id.ciudadD);
    private TextView departd= findViewById(R.id.departamentoD);
    private TextView direcciond= findViewById(R.id.direccionD);


    private TextView altura= findViewById(R.id.altura);
    private TextView ancho= findViewById(R.id.anchura);
    private TextView largo= findViewById(R.id.largoC);
    private TextView peso= findViewById(R.id.pesoC);
    private TextView titulo = findViewById(R.id.tituloC);
    private TextView descrip= findViewById(R.id.descripcion);
    private TextView tipo_merca = findViewById(R.id.tipoMerca);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detalle_cargas);
    }


}