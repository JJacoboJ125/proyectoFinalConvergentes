package com.example.lego.ui.home;

import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lego.uid;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> mDatat = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    uid uidInstance = uid.getInstance();
    String uidd = uidInstance.getUid();

    CollectionReference collectionRef = db.collection("cargas");

    public HomeViewModel() {
        collectionRef.whereEqualTo("uid", uidd).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> dataList = new ArrayList<>();
            List<String> dataListId = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                dataList.add(documentSnapshot.getString("titulo"));
                dataListId.add(documentSnapshot.getId());
            }
            if (!dataList.isEmpty()) {
                mData.setValue(dataList);
                mDatat.setValue(dataListId);
            } else {
                List<String> emptyList = new ArrayList<>();
                emptyList.add("No has realizado ninguna solicitud de carga");
                mData.setValue(emptyList);
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });


    }

    public LiveData<List<String>> getData() {
        return mData;
    }

    public LiveData<List<String>> getDatat() {
        return mDatat;
    }
}
