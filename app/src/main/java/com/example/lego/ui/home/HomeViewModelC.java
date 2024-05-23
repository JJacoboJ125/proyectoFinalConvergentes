package com.example.lego.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lego.uid;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModelC extends ViewModel {

    private final MutableLiveData<List<String>> mDataT = new MutableLiveData<>();
    private final MutableLiveData<List<String>> mDataId = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    uid uidInstance = uid.getInstance();
    String uidd = uidInstance.getUid();

    CollectionReference collectionRef = db.collection("cargas");

    public HomeViewModelC() {
        collectionRef.whereEqualTo("active", true ).whereEqualTo("aceptada", false).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> dataListTitulo = new ArrayList<>();
            List<String> dataListId = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                dataListTitulo.add(documentSnapshot.getString("titulo"));
                dataListId.add(documentSnapshot.getId());
            }
            if (!dataListTitulo.isEmpty()) {
                mDataT.setValue(dataListTitulo);
                mDataId.setValue(dataListId);
            } else {
                List<String> emptyList = new ArrayList<>();
                emptyList.add("No has realizado ninguna solicitud de carga");
                mDataT.setValue(emptyList);
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });


    }

    public LiveData<List<String>> getDataT() {
        return mDataT;
    }

    public LiveData<List<String>> getDataId() {
        return mDataId;
    }
}
