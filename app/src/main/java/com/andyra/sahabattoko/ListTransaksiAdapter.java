package com.andyra.sahabattoko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andyra.sahabattoko.model.TransaksiModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListTransaksiAdapter extends RecyclerView.Adapter<ListTransaksiAdapter.TBViewHolder> {
    private Context mcontextTRM;
    ArrayList<TransaksiModel> mtransaksi;
    TextView tvlnotrx, tvltgltrx, tvlnmtrx, tvlkttrx, tvljmltrx, tvlhrgtrx;

    public ListTransaksiAdapter(Context mcontext, ArrayList<TransaksiModel> mlisttrx) {
        this.mcontextTRM = mcontext;
        this.mtransaksi = mlisttrx;
    }

    @NonNull
    @Override
    public ListTransaksiAdapter.TBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mlistview = LayoutInflater.from(mcontextTRM).inflate(R.layout.list_transaksi, parent, false);
        return new TBViewHolder(mlistview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTransaksiAdapter.TBViewHolder holder, int position) {
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
        }
    }
}