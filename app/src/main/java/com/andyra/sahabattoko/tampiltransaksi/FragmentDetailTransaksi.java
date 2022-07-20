package com.andyra.sahabattoko.tampiltransaksi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andyra.sahabattoko.R;
import com.andyra.sahabattoko.model.ListBarangModel;
import com.andyra.sahabattoko.model.TransaksiModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentDetailTransaksi extends Fragment {
    private TextInputLayout edbbtglbeli, edbbnmbrg, edbbjmlhbrg,
            edbbbeli, edbbbelisatu, edbbjual;
    private ImageView btnbbtglbeli;
    private AutoCompleteTextView ddaktgr;
    private Button btntmbhbrg;
    private ProgressBar pgtmbhbrg;
    private String strtanggal, strnama, strkategori, strjmlh,
            strbeli, strsbelin, strjual;
    private Date datetanggal;
    private int intbeli, intjmlh ,intsatuan, intlama, intstok;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_transaksi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edbbtglbeli = getActivity().findViewById(R.id.edbbtglbeli);
        edbbnmbrg = getActivity().findViewById(R.id.edbbnmbrg);
        edbbjmlhbrg = getActivity().findViewById(R.id.edbbjmlhbrg);
        edbbbeli = getActivity().findViewById(R.id.edbbbeli);
        edbbbelisatu = getActivity().findViewById(R.id.edbbbelisatu);
        edbbjual = getActivity().findViewById(R.id.edbbjual);

        ddaktgr = getActivity().findViewById(R.id.ddaktgr);
        btnbbtglbeli = getActivity().findViewById(R.id.btnbbtglbeli);


        resetdata();
        resetnotif();
        ambildata();
        deklarasi();
        settanggal();
        hitungsatu();

    }


    private void deklarasi() {
        strtanggal = edbbtglbeli.getEditText().getText().toString();
        strnama = edbbnmbrg.getEditText().getText().toString();
        strkategori = ddaktgr.getText().toString();
        strjmlh = edbbjmlhbrg.getEditText().getText().toString();
        strbeli = edbbbeli.getEditText().getText().toString();
        strsbelin = edbbbelisatu.getEditText().getText().toString();
        strjual = edbbjual.getEditText().getText().toString();
    }
    private void hitungsatu(){
        edbbjmlhbrg.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                deklarasi();
                if(strjmlh.matches("[0-9]+")){
                    intjmlh = Integer.parseInt(strjmlh);
                    persatu();
                }
                else{
                    edbbbelisatu.getEditText().setText("0");
                }
            }
        });

        edbbbeli.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                deklarasi();
                if(strbeli.matches("[0-9]+")){
                    intbeli = Integer.parseInt(strbeli);
                    persatu();
                }
                else{
                    edbbbelisatu.getEditText().setText("0");
                }
            }
        });
    }


    private void ambildata() {
        if(getActivity().getIntent().getExtras() != null){
            String ara = getActivity().getIntent().getStringExtra("aksi_nama");
            System.out.println("key" +ara);

            edbbnmbrg.getEditText().setText(ara);
//            if(!ara.isEmpty()) {
//                ConnectivityManager mconnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//                if (mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
//                        mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
//                    Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
//                } else {
//                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    DatabaseReference ambiltrx = FirebaseDatabase.getInstance().getReference("barang_masuk")
//                            .child(uid).child(ara);
//
//                    ambiltrx.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            TransaksiModel TM = snapshot.getValue(TransaksiModel.class);
//                            System.out.println("ara" +ara);
//                                System.out.println(TM);
//                                int Total = TM.Jumlah * TM.Hrg_Beli_PerItem;
//                                edbbtglbeli.getEditText().setText("TM");
//                                edbbnmbrg.getEditText().setText(TM.Nama_barang);
//                                ddaktgr.setText(TM.Kategori);
//                                edbbjmlhbrg.getEditText().setText(String.valueOf(TM.Jumlah));
//                                edbbbeli.getEditText().setText(String.valueOf(Total));
//                                edbbbelisatu.getEditText().setText(String.valueOf(TM.Hrg_Beli_PerItem));
//                                edbbjual.getEditText().setText(String.valueOf(TM.Hrg_jual_PerItem));
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }

            }

    }
    private void settanggal() {
        Calendar calendar = Calendar.getInstance();
        final int tahun = calendar.get(Calendar.YEAR);
        final int bulan = calendar.get(Calendar.MONTH);
        final int hari = calendar.get(Calendar.DAY_OF_MONTH);

        //jika tombol tanggal ditekan
        btnbbtglbeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String hari = String.valueOf(i2);
                        String bulan = String.valueOf(i1+1);

                        if (i2 < 10){
                            hari = "0"+hari;
                        }
                        if (i1 < 9){
                            bulan = "0"+bulan;
                        }
                        strtanggal = hari + "/" + bulan + "/" + i;
                        edbbtglbeli.getEditText().setText(strtanggal);
                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });
    }

    private void persatu(){
        if(intbeli > 0 && intjmlh > 0){
            intsatuan = intbeli / intjmlh;
            edbbbelisatu.getEditText().setText(String.valueOf(intsatuan));
        }
    }
    private void tanggal(){
        deklarasi();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            datetanggal = dateFormat.parse(strtanggal);

            //dikirim pada database
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetanggal);
            final int tahun = calendar.get(Calendar.YEAR);
            final int bulan = calendar.get(Calendar.MONTH)+1;
            final int hari = calendar.get(Calendar.DAY_OF_MONTH);
            datetanggal = dateFormat.parse( hari + "/" + bulan + "/" + tahun);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void inputdata() {
        tanggal();
        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference id = FirebaseDatabase.getInstance().getReference("barang_masuk")
                    .child(uid);

            TransaksiModel TBM = new TransaksiModel(datetanggal, strnama, strkategori,
                    Integer.parseInt(strjmlh), intsatuan,
                    Integer.parseInt(strjual));

            id.push().setValue(TBM)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                cek_barang();
                            }
                        }
                    });
        }
    }

    private void cek_barang(){
        intlama = 0;
        DatabaseReference drbarang = FirebaseDatabase.getInstance()
                .getReference("list_barang").child(uid).child(strkategori+ "_" +strnama);
        drbarang.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListBarangModel LBM = snapshot.getValue(ListBarangModel.class);
                //jika data sudah ada
                if (LBM != null){
                    intlama = LBM.Stok;
                    intstok = intjmlh + intlama;
                    System.out.println(intlama);
                    System.out.println(intjmlh);
                    System.out.println(intstok);
                    drbarang.child("Stok").setValue(intstok);
                    Toast.makeText(getActivity(), "Barang Telah berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                }

                //jika data barang masih kosong
                else{
                    ListBarangModel LBMM = new ListBarangModel(strnama, strkategori, Integer.parseInt(strjmlh));
                    drbarang.setValue(LBMM)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Barang Telah berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void resetnotif(){
        edbbtglbeli.getEditText().setError(null);
        edbbnmbrg.getEditText().setError(null);
        edbbnmbrg.getEditText().setError(null);
        edbbjmlhbrg.getEditText().setError(null);
        edbbbeli.getEditText().setError(null);
        edbbjual.getEditText().setError(null);
    }

    private void resetdata(){
        deklarasi();
        resetnotif();
        strtanggal = "";
        strnama = "";
        strkategori = "";
        strjmlh = "";
        strbeli = "";
        strsbelin = "";
        strjual = "";

        edbbtglbeli.getEditText().setText("");
        edbbnmbrg.getEditText().setText("");
        ddaktgr.setText("");
        edbbjmlhbrg.getEditText().setText("");
        edbbbeli.getEditText().setText("");
        edbbbelisatu.getEditText().setText("");
        edbbjual.getEditText().setText("");
    }
}
