package com.andyra.sahabattoko;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andyra.sahabattoko.model.JualModel;
import com.andyra.sahabattoko.model.TransaksiModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListTransaksiAdapter extends RecyclerView.Adapter<ListTransaksiAdapter.TBViewHolder> {
    private Context mcontextTRM;
    ArrayList<TransaksiModel> mtransaksi;
    LinearLayout lylist_trx;
	
    TextView tvlnotrx, tvltgltrx, tvlnmtrx, tvlkttrx, tvljmltrx, tvlhrgtrx;
    View mlistview;

    public ListTransaksiAdapter(Context mcontext, ArrayList<TransaksiModel> mlisttrx) {
        this.mcontextTRM = mcontext;
        this.mtransaksi = mlisttrx;
    }

    @NonNull
    @Override
    public ListTransaksiAdapter.TBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mlistview = LayoutInflater.from(mcontextTRM).inflate(R.layout.list_transaksi, parent, false);
        return new TBViewHolder(mlistview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTransaksiAdapter.TBViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TransaksiModel TRM = mtransaksi.get(position);
        tvlnotrx.setText(String.valueOf(position+1));
        tvlnmtrx.setText(TRM.Nama_barang);
        tvlkttrx.setText(TRM.Kategori);
        tvljmltrx.setText(String.valueOf(TRM.Jumlah));
        tvlhrgtrx.setText(String.valueOf(TRM.Hrg_Beli_PerItem));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date datetanggal = TRM.Tgl_beli;
            String strtanggal = dateFormat.format(datetanggal);
            tvltgltrx.setText(strtanggal);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        lylist_trx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ara = FirebaseDatabase.getInstance().getReference("barang_masuk")
                        ;
                ara.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot trxsnap : snapshot.getChildren()){
                            TransaksiModel TM = trxsnap.getValue(TransaksiModel.class);
                            String kunciii = trxsnap.getKey();
                            TM.setKey(kunciii);
                            System.out.println(TM.getKey());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                String key = TRM.getKey();
                System.out.println("KeyValue is ================================================>" + key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mtransaksi.size();
    }

    public class TBViewHolder extends RecyclerView.ViewHolder{
        public TBViewHolder(@NonNull View mview) {
            super(mview);
            tvlnotrx = mview.findViewById(R.id.tvlnotrx);
            tvltgltrx = mview.findViewById(R.id.tvltgltrx);
            tvlnmtrx = mview.findViewById(R.id.tvlnmtrx);
            tvlkttrx = mview.findViewById(R.id.tvlkttrx);
            tvljmltrx = mview.findViewById(R.id.tvljmltrx);
            tvlhrgtrx = mview.findViewById(R.id.tvlhrgtrx);
            lylist_trx = mview.findViewById(R.id.lylist_trx);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        }
    }


}

