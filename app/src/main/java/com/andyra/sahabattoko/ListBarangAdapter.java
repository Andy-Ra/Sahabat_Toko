package com.andyra.sahabattoko;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.andyra.sahabattoko.model.ListBarangModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListBarangAdapter extends RecyclerView.Adapter<ListBarangAdapter.BarangViewHolder> {
    private Context mcontextLBA;
    ArrayList<ListBarangModel> mlistbarangAdapter;
    TextView tvlnobrg, tvlnmbrg, tvlktbrg, tvlstbrg;

    public ListBarangAdapter(Context mcontext, ArrayList<ListBarangModel> mlistbarang) {
        this.mcontextLBA = mcontext;
        this.mlistbarangAdapter = mlistbarang;
    }

    @NonNull
    @Override
    public BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mlistview = LayoutInflater.from(mcontextLBA).inflate(R.layout.list_barang, parent, false);
        return new BarangViewHolder(mlistview);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangViewHolder holder, int position) {
        ListBarangModel LBM = mlistbarangAdapter.get(position);
        tvlnobrg.setText(String.valueOf(position+1));
        tvlnmbrg.setText(LBM.Nama_barang);
        tvlktbrg.setText(LBM.Kategori);
        tvlstbrg.setText(String.valueOf(LBM.Stok));
    }

    @Override
    public int getItemCount() {
        return mlistbarangAdapter.size();
    }

    public class BarangViewHolder extends RecyclerView.ViewHolder{
        public BarangViewHolder(@NonNull View mview) {
            super(mview);
            tvlnobrg = mview.findViewById(R.id.tvlnobrg);
            tvlnmbrg = mview.findViewById(R.id.tvlnmbrg);
            tvlktbrg = mview.findViewById(R.id.tvlktbrg);
            tvlstbrg = mview.findViewById(R.id.tvlstbrg);
        }
    }
}
