package com.example.lego.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lego.addVehiculo;
import com.example.lego.databinding.FragmentGalleryBinding;

public class GalleryFragmentC extends Fragment {

private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModelDos galleryViewModelDos =
                new ViewModelProvider(this).get(GalleryViewModelDos.class);

    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addVehiculo(){
        Intent intent = new Intent(requireContext(), addVehiculo.class);
        startActivity(intent);
    }
}