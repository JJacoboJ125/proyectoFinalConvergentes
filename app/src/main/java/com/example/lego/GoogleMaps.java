package com.example.lego;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GoogleMaps extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uidd = "tu_uid_aqui";  // Asegúrate de asignar el valor correcto
    private GoogleMap gMap;
    private View buttonToHide;
    private View buttonToShow;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Button btnReportLocation, btnViewHistory;


    private List<String> locationHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        btnReportLocation = findViewById(R.id.reportarUbi);
        btnViewHistory = findViewById(R.id.btnViewHistory);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnReportLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                reportLocation();
                btnViewHistory.setVisibility(View.VISIBLE);
            }
        });

        buttonToHide = findViewById(R.id.button_ir_direccion);
        buttonToShow = findViewById(R.id.button_ubi_direccion);
        buttonToShow.setVisibility(View.GONE);

        DelayedMessageService.createNotification(this, "Nueva solicitud de carga", "Se ha recibido una nueva solicitud de carga.");
        DelayedMessageService.createNotification(this, "Entrega de carga", "La carga ha sido entregada.");
        DelayedMessageService.createNotification(this, "Recepción de carga", "La carga ha sido recibida. Valoración del servicio: X/5");

    }

    public void recorridoOrigenDireccion(View view) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", "4.632339710,-74.065350");
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void ubiConDireccion(View view) {
        List<String> direccionOrigen = new ArrayList<>();
        List<String> direccionDestino = new ArrayList<>();

        db.collection("cargas").whereEqualTo("piloto", uidd).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        direccionOrigen.add(documentSnapshot.getString("direccionO"));
                        direccionOrigen.add(documentSnapshot.getString("ciudadO"));
                        direccionOrigen.add(documentSnapshot.getString("departamentO"));
                        direccionDestino.add(documentSnapshot.getString("direccionE"));
                        direccionDestino.add(documentSnapshot.getString("ciudadE"));
                        direccionDestino.add(documentSnapshot.getString("departamentE"));
                    }

                    if (!direccionOrigen.isEmpty() && !direccionDestino.isEmpty()) {
                        String dirOr = String.join(", ", direccionOrigen);
                        String dirDes = String.join(", ", direccionDestino);

                        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + Uri.encode(dirOr) + "&destination=" + Uri.encode(dirDes));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } else {
                        showErrorAndNavigateDefault();
                    }
                })
                .addOnFailureListener(e -> showErrorAndNavigateDefault());
    }

    private void showErrorAndNavigateDefault() {

        Toast.makeText(this, "No se pudo encontrar las direcciones", Toast.LENGTH_SHORT).show();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + latitude + "," + longitude + "&destination=" + Uri.encode("Bogotá, Cundinamarca, Colombia"));

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        } else {
            Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + Uri.encode("Bogotá, Cundinamarca") + "&destination=" + Uri.encode("Neiva, Huila, Colombia"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    public void irAUnaDireccion(View view) {
        List<String> direccionOrigen = new ArrayList<>();
        db.collection("cargas").whereEqualTo("piloto", uidd).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        direccionOrigen.add(documentSnapshot.getString("direccionO"));
                        direccionOrigen.add(documentSnapshot.getString("ciudadO"));
                        direccionOrigen.add(documentSnapshot.getString("departamentO"));
                    }
                    if (!direccionOrigen.isEmpty()) {
                        String dirOr = String.join(", ", direccionOrigen);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+dirOr);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } else {
                        showErrorAndNavigateDefault();
                    }
                })
                .addOnFailureListener(e -> showErrorAndNavigateDefault());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        LatLng Col = new LatLng(4.6326049, -74.0678582);
        gMap.addMarker(new MarkerOptions().position(Col).title("Copenhagen"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Col, 12));
    }

    public void siguiente(View view) {
        buttonToHide.setVisibility(View.GONE);
        buttonToShow.setVisibility(View.VISIBLE);
    }

    private void reportLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String locationString = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                    locationHistory.add(locationString);
                    Toast.makeText(GoogleMaps.this, "Ubicación reportada: " + locationString, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

