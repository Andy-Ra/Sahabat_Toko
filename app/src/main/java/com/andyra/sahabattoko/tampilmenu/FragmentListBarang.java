package com.andyra.sahabattoko.tampilmenu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    ListView lvlistbrg;
    List<ListBarangModel> arlistbarang;
    ListBarangModel LBM;

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

        arlistbarang = new ArrayList<>();
        lvlistnull.setVisibility(View.VISIBLE);
        lvlistbrg.setVisibility(View.GONE);

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
//                System.out.println(arlistbarang.size());
//                if(arlistbarang.size() <= 0){
//                    lvlistnull.setVisibility(View.VISIBLE);
//                    lvlistbrg.setVisibility(View.GONE);
//                }
//                else{
//                    lvlistnull.setVisibility(View.GONE);
//                    lvlistbrg.setVisibility(View.VISIBLE);
//                    ListBarangAdapter madapter = new ListBarangAdapter(getActivity(), arlistbarang);
//                    lvlistbrg.setAdapter(madapter);
//                }

                lvlistnull.setVisibility(View.GONE);
                lvlistbrg.setVisibility(View.VISIBLE);
                ListBarangAdapter madapter = new ListBarangAdapter(getActivity(), arlistbarang);
                lvlistbrg.setAdapter(madapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }
}