package com.example.lego.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lego.databinding.FragmentHomeBinding;
import com.example.lego.detalleCargaDC;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.listaCargas;
        List<String> dataListt = new ArrayList<>();
        List<String> dataListId = new ArrayList<>();

        homeViewModel.getData().observe(getViewLifecycleOwner(), dataList -> {
            if (dataList != null) {
                dataListt.clear();
                dataListt.addAll(dataList);
            }
        });

        homeViewModel.getDatat().observe(getViewLifecycleOwner(), dataList -> {
            if (dataList != null) {
                dataListId.clear();
                dataListId.addAll(dataList);
            }
        });

        homeViewModel.getData().observe(getViewLifecycleOwner(), dataList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String codigo = dataListId.get(position);

            Intent intent = new Intent(requireContext(), detalleCargaDC.class);
            intent.putExtra("DOCUMENT_ID", codigo);
            startActivity(intent);

            Toast.makeText(requireContext(), "Elemento seleccionado: " + codigo, Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
