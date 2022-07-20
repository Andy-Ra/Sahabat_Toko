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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListJualAdapter extends RecyclerView.Adapter<ListJualAdapter.TBViewHolder> {
    private Context mcontextJM;
    ArrayList<JualModel> mjtrx;
    LinearLayout lyjlist_trx;

    TextView tvljnotrx, tvljtgltrx, tvljnmtrx, tvljkttrx, tvljjmltrx, tvljhrgtrx, tvljttltrx;
    View mjlistview;

    public ListJualAdapter(Context mcontext, ArrayList<JualModel> mlisttrx) {
        this.mcontextJM = mcontext;
        this.mjtrx = mlisttrx;
    }

    @NonNull
    @Override
    public ListJualAdapter.TBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mjlistview = LayoutInflater.from(mcontextJM).inflate(R.layout.listjual, parent, false);
        return new TBViewHolder(mjlistview);
    }

    @Override
    public void onBindViewHolder(@NonNull ListJualAdapter.TBViewHolder holder, @SuppressLint("RecyclerView") int position) {
        JualModel JM = mjtrx.get(position);
        tvljnotrx.setText(String.valueOf(position+1));
        tvljnmtrx.setText(JM.Nama_barang);
        tvljkttrx.setText(JM.Kategori);
        tvljjmltrx.setText(String.valueOf(JM.Jumlah));
        tvljhrgtrx.setText(String.valueOf(JM.Hrg_Satuan));
        tvljttltrx.setText(String.valueOf(JM.Total_Harga));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date datetanggal = JM.Tgl_beli;
            String strtanggal = dateFormat.format(datetanggal);
            tvljtgltrx.setText(strtanggal);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mjtrx.size();
    }

    public class TBViewHolder extends RecyclerView.ViewHolder{
        public TBViewHolder(@NonNull View mview) {
            super(mview);
            tvljnotrx = mview.findViewById(R.id.tvljnotrx);
            tvljtgltrx = mview.findViewById(R.id.tvljtgltrx);
            tvljnmtrx = mview.findViewById(R.id.tvljnmtrx);
            tvljkttrx = mview.findViewById(R.id.tvljkttrx);
            tvljjmltrx = mview.findViewById(R.id.tvljjmltrx);
            tvljhrgtrx = mview.findViewById(R.id.tvljhrgtrx);
            tvljttltrx = mview.findViewById(R.id.tvljttltrx);
            lyjlist_trx = mview.findViewById(R.id.lyjlist_trx);
        }
    }

}

