package com.example.lego.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lego.addVehiculo;
import com.example.lego.databinding.FragmentGalleryDosBinding;
import com.example.lego.detalleCargaDC;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragmentDos extends Fragment {

private FragmentGalleryDosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModelDos galleryViewModelDos = new ViewModelProvider(this).get(GalleryViewModelDos.class);

    binding = FragmentGalleryDosBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final ListView listView = binding.listaCargas;
        List<String> listPlacas = new ArrayList<>();

        galleryViewModelDos.placas().observe(getViewLifecycleOwner(), dataList -> {
            if (dataList != null) {
                listPlacas.clear();
                listPlacas.addAll(dataList);
            }
        });

        galleryViewModelDos.placas().observe(getViewLifecycleOwner(), dataList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);
        });
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}