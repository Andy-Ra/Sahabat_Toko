package com.andyra.sahabattoko;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andyra.sahabattoko.model.ListBarangModel;

import java.util.List;

public class ListBarangAdapter extends ArrayAdapter {
    private Activity mcontextLBA;
    List<ListBarangModel> mlistbarangAdapter;
    public ListBarangAdapter(Activity mcontext, List<ListBarangModel> mlistbarang) {
        super(mcontext, R.layout.list_barang, mlistbarang);
        this.mcontextLBA = mcontext;
        this.mlistbarangAdapter = mlistbarang;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater minflater = mcontextLBA.getLayoutInflater();
        View mlistview = minflater.inflate(R.layout.list_barang, null, true);
        TextView tvlnobrg = mlistview.findViewById(R.id.tvlnobrg);
        TextView tvlnmbrg = mlistview.findViewById(R.id.tvlnmbrg);
        TextView tvlktbrg = mlistview.findViewById(R.id.tvlktbrg);
        TextView tvlstbrg = mlistview.findViewById(R.id.tvlstbrg);

        ListBarangModel LBM = mlistbarangAdapter.get(position);
        tvlnobrg.setText(String.valueOf(position+1));
        tvlnmbrg.setText(LBM.Nama_barang);
        tvlktbrg.setText(LBM.Kategori);
        tvlstbrg.setText(String.valueOf(LBM.Stok));

        return mlistview;
    }
}
