package com.andyra.sahabattoko.tampilmenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andyra.sahabattoko.R;

import java.util.ArrayList;

public class FragmentListBarang extends Fragment {
    TextView lvlistnull;
    ListView lvlistbrg;
    ArrayList<String> listbarang = new ArrayList<>();
    ArrayAdapter<String> marrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.list_brg);
        lvlistnull = getActivity().findViewById(R.id.lvlistnull);
        lvlistbrg = getActivity().findViewById(R.id.lvlistbrg);

        lvlistnull.setVisibility(View.GONE);
        lvlistbrg.setVisibility(View.GONE);



    }
}