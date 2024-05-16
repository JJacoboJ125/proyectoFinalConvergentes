package com.example.lego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.com.example.lego.MyAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeDVActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid ="SaYBa1T4JvOwxoDMyqm2HLm6a113";
    ListView lv;
    List<String> dataList = new ArrayList<>();
    CollectionReference collectionRef = db.collection("cargas");
    private MyAdapter myAdapter = new MyAdapter(HomeDVActivity.this, dataList);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dv);

        lv = findViewById(R.id.listaCargass);
        lv.setAdapter(myAdapter);
        drawerLayout = findViewById(R.id.drawer_layoutt);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.drawer_menu_driver);
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                collectionRef.whereEqualTo("active", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                dataList.clear();
                                dataList.add("hola");
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Log.d("HomeDVActivity", "Documentos recuperados: " + queryDocumentSnapshots.size());
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String dato = documentSnapshot.getString("titulo");
                                        dataList.add(dato);
                                    }
                                } else {
                                    Toast.makeText(HomeDVActivity.this, "No hay cargas activas.", Toast.LENGTH_SHORT).show();
                                }

                                myAdapter.notifyDataSetChanged();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                Toast.makeText(HomeDVActivity.this, "Clicked on Home", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.favourite) {
                //setContentView(R.layout.activity_home);
                Toast.makeText(HomeDVActivity.this, "Clicked on Favourite", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.ajustes) {
                Toast.makeText(HomeDVActivity.this, "Clicked on Ajustes", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.logout) {
                Toast.makeText(HomeDVActivity.this, "Clicked on Logout", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
