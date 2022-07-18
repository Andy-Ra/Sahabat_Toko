package com.andyra.sahabattoko.tampilmenu;

import android.app.DatePickerDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andyra.sahabattoko.ListBarangAdapter;
import com.andyra.sahabattoko.R;
import com.andyra.sahabattoko.model.TransaksiModel;
import com.andyra.sahabattoko.model.ListBarangModel;
import com.andyra.sahabattoko.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FragmentPembelianBarang extends Fragment {
    private TextInputLayout edtanggal, ednama, edjmlh,
            edbeli, edsbelin, edjual;
    private ImageView btntglbeli;
    private AutoCompleteTextView attktgr;
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
        return inflater.inflate(R.layout.fragment_pembelian_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edtanggal = getActivity().findViewById(R.id.edttglbeli);
        ednama = getActivity().findViewById(R.id.edtnmbrg);
        edjmlh = getActivity().findViewById(R.id.edtjmlhbrg);
        edbeli = getActivity().findViewById(R.id.edtbeli);
        edsbelin = getActivity().findViewById(R.id.edtbelisatu);
        edjual = getActivity().findViewById(R.id.edtjual);

        attktgr = getActivity().findViewById(R.id.attktgr);
        btntglbeli = getActivity().findViewById(R.id.btntglbeli);
        btntmbhbrg = getActivity().findViewById(R.id.btntmbhbrg);
        pgtmbhbrg = getActivity().findViewById(R.id.pgtmbhbrg);

        pgtmbhbrg.setVisibility(View.GONE);
        btntmbhbrg.setVisibility(View.VISIBLE);

        resetdata();
        deklarasi();
        settanggal();
        resetnotif();
        hitungsatu();

        btntmbhbrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datetanggal = null;
                tanggal();
                resetnotif();
                if(datetanggal == null|| strnama.isEmpty() == true || strkategori.isEmpty() == true ||
                        !strjmlh.matches("[0-9]+") || Integer.parseInt(strjmlh) < 1 ||
                        !strbeli.matches("[0-9]+") || Integer.parseInt(strbeli) < 1 ||
                        !strjual.matches("[0-9]+") || Integer.parseInt(strjual) < 1 ){
                    if(datetanggal == null){
                        edtanggal.getEditText().setError("Format tanggal (dd/MM/YYY)");
                    }
                    if(strnama.isEmpty() == true){
                        ednama.getEditText().setError("Masukkan Nama Barang");
                    }

                    if(strkategori.isEmpty() == true){
                        attktgr.setError("Masukkan Kategori Barang");
                    }
                    if(!strjmlh.matches("[0-9]+") || Integer.parseInt(strjmlh) < 1){
                        edjmlh.getEditText().setError("Masukkan Hanya Angka lebih dari 0");
                    }
                    if(!strbeli.matches("[0-9]+") || Integer.parseInt(strbeli) < 1){
                        edbeli.getEditText().setError("Masukkan Hanya Angka lebih dari 0");
                    }
                    if(!strjual.matches("[0-9]+") || Integer.parseInt(strjual) < 1){
                        edjual.getEditText().setError("Masukkan Hanya Angka lebih dari 0");
                    }
                }
                else {
                    inputdata();
                }
            }
        });
    }

    private void deklarasi() {
        strtanggal = edtanggal.getEditText().getText().toString();
        strnama = ednama.getEditText().getText().toString();
        strkategori = attktgr.getText().toString();
        strjmlh = edjmlh.getEditText().getText().toString();
        strbeli = edbeli.getEditText().getText().toString();
        strsbelin = edsbelin.getEditText().getText().toString();
        strjual = edjual.getEditText().getText().toString();
    }
    private void hitungsatu(){
        edjmlh.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                deklarasi();
                if(strjmlh.matches("[0-9]+")){
                    intjmlh = Integer.parseInt(strjmlh);
                    persatu();
                }
                else{
                    edsbelin.getEditText().setText("0");
                }
            }
        });

        edbeli.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                deklarasi();
                if(strbeli.matches("[0-9]+")){
                    intbeli = Integer.parseInt(strbeli);
                    persatu();
                }
                else{
                    edsbelin.getEditText().setText("0");
                }
            }
        });
    }
    private void settanggal() {
        Calendar calendar = Calendar.getInstance();
        final int tahun = calendar.get(Calendar.YEAR);
        final int bulan = calendar.get(Calendar.MONTH);
        final int hari = calendar.get(Calendar.DAY_OF_MONTH);

        //jika tombol tanggal ditekan
        btntglbeli.setOnClickListener(new View.OnClickListener() {
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
                        edtanggal.getEditText().setText(strtanggal);
                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });
    }

    private void persatu(){
        if(intbeli > 0 && intjmlh > 0){
            intsatuan = intbeli / intjmlh;
            edsbelin.getEditText().setText(String.valueOf(intsatuan));
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
        pgtmbhbrg.setVisibility(View.VISIBLE);
        btntmbhbrg.setVisibility(View.GONE);
        tanggal();
        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            pgtmbhbrg.setVisibility(View.GONE);
            btntmbhbrg.setVisibility(View.VISIBLE);
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
            pgtmbhbrg.setVisibility(View.GONE);
            btntmbhbrg.setVisibility(View.VISIBLE);
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

        pgtmbhbrg.setVisibility(View.GONE);
        btntmbhbrg.setVisibility(View.VISIBLE);
    }

    private void resetnotif(){
        edtanggal.getEditText().setError(null);
        ednama.getEditText().setError(null);
        ednama.getEditText().setError(null);
        edjmlh.getEditText().setError(null);
        edbeli.getEditText().setError(null);
        edjual.getEditText().setError(null);
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

        edtanggal.getEditText().setText("");
        ednama.getEditText().setText("");
        attktgr.setText("");
        edjmlh.getEditText().setText("");
        edbeli.getEditText().setText("");
        edsbelin.getEditText().setText("");
        edjual.getEditText().setText("");
    }
}
