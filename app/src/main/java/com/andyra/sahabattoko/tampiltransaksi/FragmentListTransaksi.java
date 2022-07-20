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

import com.andyra.sahabattoko.ListTransaksiAdapter;
import com.andyra.sahabattoko.R;
import com.andyra.sahabattoko.model.TransaksiModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentListTransaksi extends Fragment {
    TextView lvbltrxnull;
    RecyclerView rvblisttrx;
    ListTransaksiAdapter lbtrx;
    ArrayList<TransaksiModel> arlistbtrx;
    View mview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_list_transaksi, container, false);
        return(mview);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Daftar Transaksi Pembelian");
        lvbltrxnull = getActivity().findViewById(R.id.lvbltrxnull);
        rvblisttrx = getActivity().findViewById(R.id.rvblisttrx);

        lvbltrxnull.setVisibility(View.VISIBLE);
        rvblisttrx.setVisibility(View.GONE);


        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvblisttrx.getContext(),
                DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.rowrecycle));
        rvblisttrx.addItemDecoration(mDividerItemDecoration);

        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            listbtrx();
        }
    }

    private void listbtrx() {
        rvblisttrx.setHasFixedSize(true);
        rvblisttrx.setLayoutManager(new LinearLayoutManager(getActivity()));

        arlistbtrx = new ArrayList<>();
        lbtrx = new ListTransaksiAdapter(getActivity(), arlistbtrx);
        rvblisttrx.setAdapter(lbtrx);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference listbtrx = FirebaseDatabase.getInstance().getReference("barang_masuk")
                .child(uid);

        listbtrx.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arlistbtrx.clear();
                System.out.println("snap" +snapshot);
                for (DataSnapshot trxsnap : snapshot.getChildren()){
                    arlistbtrx.add(trxsnap.getValue(TransaksiModel.class));
                    System.out.println(trxsnap.getValue(TransaksiModel.class));
                }

                lvbltrxnull.setVisibility(View.GONE);
                rvblisttrx.setVisibility(View.VISIBLE);

                lbtrx.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}