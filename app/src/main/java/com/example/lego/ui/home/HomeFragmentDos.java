package com.example.lego.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lego.ConfirmDetalleCargas;
import com.example.lego.databinding.FragmentHomeBinding;
import com.example.lego.databinding.FragmentHomeDosBinding;
import com.example.lego.detalleCargaDC;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentDos extends Fragment {

    private FragmentHomeDosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModelDos homeViewModelDos = new ViewModelProvider(this).get(HomeViewModelDos.class);


        binding = FragmentHomeDosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.listaCargas;
        List<String> dataListt = new ArrayList<>();
        List<String> dataListId = new ArrayList<>();

        homeViewModelDos.getDataT().observe(getViewLifecycleOwner(), dataList -> {
            if (dataList != null) {
                dataListt.clear();
                dataListt.addAll(dataList);
            }
        });

        homeViewModelDos.getDataId().observe(getViewLifecycleOwner(), dataList -> {
            if (dataList != null) {
                dataListId.clear();
                dataListId.addAll(dataList);
            }
        });

        homeViewModelDos.getDataT().observe(getViewLifecycleOwner(), dataList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String codigo = dataListId.get(position);

            Intent intent = new Intent(requireContext(), ConfirmDetalleCargas.class);
            intent.putExtra("DOCUMENT_ID", codigo);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
