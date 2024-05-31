package com.example.lego;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class GoogleMaps extends AppCompatActivity implements OnMapReadyCallback {
    private static final int EARTH_RADIUS = 6371000;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private uid udi = uid.getInstance();
    private String uidd = udi.getUid();
    private String documentId;
    private GoogleMap gMap;
    private View buttonToHide, buttonToShow, btnsigue, reportU,history, inicio, finalizar;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Button btnReportLocation, btnViewHistory;

    CollectionReference Cargas = db.collection("cargas");
    CollectionReference collectionRef = db.collection("vehiculo");

    private OdometerService odometro;
    private boolean bound=false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            OdometerService.OdometerBinder odometerBinder = (OdometerService.OdometerBinder)binder;
            odometro = odometerBinder.getOdometer();
            bound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound=false;
        }
    };


    private List<String> locationHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DOCUMENT_ID")) {
            documentId = intent.getStringExtra("DOCUMENT_ID");
        } else {
            Log.e("selectVehiculo", "No se recibió el DOCUMENT_ID en el Intent");
            // Manejar este caso adecuadamente
            return;
        }

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
        btnsigue = findViewById(R.id.siguiente);
        reportU = findViewById(R.id.reportarUbi);
        history = findViewById(R.id.btnViewHistory);
        inicio = findViewById(R.id.iniciar);
        finalizar = findViewById(R.id.finalizav);

        buttonToShow.setVisibility(View.GONE);
        buttonToHide.setVisibility(View.GONE);
        btnsigue.setVisibility(View.GONE);
        reportU.setVisibility(View.GONE);
        history.setVisibility(View.GONE);
        finalizar.setVisibility(View.GONE);



        DelayedMessageService.createNotification(this, "Nueva solicitud de carga", "Se ha recibido una nueva solicitud de carga.");
        DelayedMessageService.createNotification(this, "Entrega de carga", "La carga ha sido entregada.");
        DelayedMessageService.createNotification(this, "Recepción de carga", "La carga ha sido recibida. Valoración del servicio: X/5");

    }
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, OdometerService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop(){
        super.onStop();
        if (bound){
            unbindService(connection);
            bound=false;
        }
    }

    public void iniciarTrans(View view) {
        reportLocation();
        buttonToHide.setVisibility(View.VISIBLE);
        btnsigue.setVisibility(View.VISIBLE);
        reportU.setVisibility(View.VISIBLE);
        history.setVisibility(View.VISIBLE);
        inicio.setVisibility(View.GONE);
        TextView ubicacion = (TextView) findViewById(R.id.textoubicacion);
        TextView distanciaT = (TextView) findViewById(R.id.textometors);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                reportLocationS(new LocationCallback() {
                    @Override
                    public void onLocationResult(Location location) {
                        if (location != null) {
                            String distanciaStr = "Lat: " + location.getLatitude() + "\n Lon: " + location.getLongitude();
                            double x = location.getLatitude();
                            double y = location.getLongitude();
                            String distancia = String.valueOf(calcular(x,y));
                            ubicacion.setText(distanciaStr);
                            distanciaT.setText(distancia);
                        } else {
                        }
                    }
                });
                handler.postDelayed(this, 1000);
            }
        });
    }

    interface LocationCallback {
        void onLocationResult(Location location);
    }

    public double calcular(double x, double y){
        ubicacion ubi = ubicacion.getInstanceInicio();
        String inicio = ubi.getInicio();
        String [] separar = inicio.split(" ");
        double lat1Rad = Math.toRadians(Double.valueOf(separar[0]));
        double lon1Rad = Math.toRadians(Double.valueOf(separar[1]));
        double lat2Rad = Math.toRadians(x);
        double lon2Rad = Math.toRadians(y);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance;
    }

    public void ubiConDireccion(View view) {
        List<String> direccionOrigen = new ArrayList<>();
        List<String> direccionDestino = new ArrayList<>();

        db.collection("cargas").whereEqualTo("piloto", uidd).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        direccionOrigen.add(documentSnapshot.getString("direccionO"));
                        direccionOrigen.add(documentSnapshot.getString("ciudadO"));
                        direccionOrigen.add(documentSnapshot.getString("departamentoO"));
                        direccionDestino.add(documentSnapshot.getString("direccionE"));
                        direccionDestino.add(documentSnapshot.getString("ciudadE"));
                        direccionDestino.add(documentSnapshot.getString("departamentoE"));
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
        db.collection("cargas").whereEqualTo("conductor", uidd).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        direccionOrigen.add(documentSnapshot.getString("direccionO"));
                        direccionOrigen.add(documentSnapshot.getString("ciudadO"));
                        direccionOrigen.add(documentSnapshot.getString("departamentoO"));
                    }
                    if (!direccionOrigen.isEmpty()) {
                        String dirOr = String.join(", ", direccionOrigen);
                        String encodedDirOr = "";
                        try {
                            encodedDirOr = URLEncoder.encode(dirOr, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+encodedDirOr);
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
        finalizar.setVisibility(View.VISIBLE);
        btnsigue.setVisibility(View.GONE);
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
                    ubicacion ubi = ubicacion.getInstanceInicio();
                    ubi.setInicio(String.valueOf(location.getLatitude())+" "+String.valueOf(location.getLongitude()));
                }
            }
        });
    }

    private void reportLocationS(final LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            callback.onLocationResult(null); // Manejar permisos no otorgados
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                callback.onLocationResult(location); // Devuelve la ubicación obtenida
            }
        });
    }

    public void setFinalizar(View view){
        Cargas.document(documentId).update("active", false);

        collectionRef.whereEqualTo("conductor", uidd).get().addOnSuccessListener(queryDocumentSnapshots -> {
            String codigo = "";
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                codigo = documentSnapshot.getId();
            }

            collectionRef.document(codigo).update("carga", "");
        }).addOnFailureListener(e -> {
            // Manejar fallo en la consulta
            Log.e("FirestoreError", "Error en la consulta", e);
        });


        }
}

