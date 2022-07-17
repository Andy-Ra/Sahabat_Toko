package com.andyra.sahabattoko.tampilmenu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyra.sahabattoko.ListBarangAdapter;
import com.andyra.sahabattoko.R;
import com.andyra.sahabattoko.model.ListBarangModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentListBarang extends Fragment {
    TextView lvlistnull;
    RecyclerView rvlistbrg;
    ListBarangAdapter lbAdapter;
    ArrayList<ListBarangModel> arlistbarang;

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
        rvlistbrg = getActivity().findViewById(R.id.rvlistbrg);

        lvlistnull.setVisibility(View.VISIBLE);
        rvlistbrg.setVisibility(View.GONE);

        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            listbarang();
        }
    }

    private void listbarang() {
        rvlistbrg.setHasFixedSize(true);
        rvlistbrg.setLayoutManager(new LinearLayoutManager(getActivity()));

        arlistbarang = new ArrayList<>();
        lbAdapter = new ListBarangAdapter(getActivity(), arlistbarang);
        rvlistbrg.setAdapter(lbAdapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference listbarang = FirebaseDatabase.getInstance().getReference("list_barang")
                .child(uid);

        listbarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arlistbarang.clear();
                for (DataSnapshot barangsnap : snapshot.getChildren()){
                        ListBarangModel LBM = snapshot.getValue(ListBarangModel.class);
                        System.out.println(barangsnap.getValue().toString());
                        arlistbarang.add(barangsnap.getValue(ListBarangModel.class));

                    System.out.println("ara" +LBM.Nama_barang);
                }

                lvlistnull.setVisibility(View.GONE);
                rvlistbrg.setVisibility(View.VISIBLE);

                lbAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}