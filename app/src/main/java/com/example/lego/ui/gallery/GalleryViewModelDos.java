package com.example.lego.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lego.uid;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModelDos extends ViewModel {

    private final MutableLiveData<List<String>> placas = new MutableLiveData<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    uid uidInstance = uid.getInstance();
    String uidd = uidInstance.getUid();

    CollectionReference collectionRef = db.collection("vehiculo");

    public GalleryViewModelDos() {
       collectionRef.whereEqualTo("Dueño", uidd).get().addOnSuccessListener(queryDocumentSnapshots -> {
        List<String> listPlacas = new ArrayList<>();
        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
            listPlacas.add(documentSnapshot.getString("placa"));
        }
        if (!listPlacas.isEmpty()) {
            placas.setValue(listPlacas);
        } else {
            List<String> emptyList = new ArrayList<>();
            emptyList.add("No hay autos añadidos.");
            placas.setValue(emptyList);
        }
    }).addOnFailureListener(e -> {
        // Handle failure
    });


}

    public LiveData<List<String>> placas() {
        return placas;
    }
}