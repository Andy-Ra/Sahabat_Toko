package com.andyra.sahabattoko.tampiltransaksi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andyra.sahabattoko.ListJualAdapter;
import com.andyra.sahabattoko.ListTransaksiAdapter;
import com.andyra.sahabattoko.R;
import com.andyra.sahabattoko.model.JualModel;
import com.andyra.sahabattoko.model.TransaksiModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentListPenjualan extends Fragment {
    TextView lvjtrxnull;
    RecyclerView rvjlist;
    ListJualAdapter ljtrx;
    ArrayList<JualModel> arlistjtrx;
    View mview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_list_penjualan, container, false);
        return(mview);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Daftar Transaksi Penjualan");
        lvjtrxnull = getActivity().findViewById(R.id.lvjltrxnullj);
        rvjlist = getActivity().findViewById(R.id.rvjlisttrxj);

        lvjtrxnull.setVisibility(View.VISIBLE);
        rvjlist.setVisibility(View.GONE);


        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvjlist.getContext(),
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.rowrecycle));
        rvjlist.addItemDecoration(mDividerItemDecoration);

        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            listjtrx();
        }
    }

    private void listjtrx() {
        rvjlist.setHasFixedSize(true);
        rvjlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        arlistjtrx = new ArrayList<>();
        ljtrx = new ListJualAdapter(getActivity(), arlistjtrx);
        rvjlist.setAdapter(ljtrx);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference listjtrx = FirebaseDatabase.getInstance().getReference("barang_keluar")
                .child(uid);

        listjtrx.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arlistjtrx.clear();
                System.out.println("snap" +snapshot);
                for (DataSnapshot trxsnap : snapshot.getChildren()){
                    arlistjtrx.add(trxsnap.getValue(JualModel.class));
                    System.out.println(trxsnap.getValue(JualModel.class));
                }

                lvjtrxnull.setVisibility(View.GONE);
                rvjlist.setVisibility(View.VISIBLE);

                ljtrx.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}