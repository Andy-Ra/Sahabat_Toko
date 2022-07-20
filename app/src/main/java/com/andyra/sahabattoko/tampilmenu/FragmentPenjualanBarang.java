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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.andyra.sahabattoko.R;
import com.andyra.sahabattoko.model.JualModel;
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

public class FragmentPenjualanBarang extends Fragment {
    private ImageView btntgljual;
    private Spinner sp_jkat, sp_jjnama;
    private TextInputLayout edttgljual, edtjualsatu, edtjmlhbrg, edttljual;
    private Button btnjualbrg;
    private ProgressBar pgjualbrg;
    private ArrayList<String> arrayjkategori = new ArrayList<>();
    private ArrayList<String> arrayjabarang = new ArrayList<>();
    private DatabaseReference listbarang;

    private String stringid, fungsi, jnama,
            jkategori, strtgl, strjmlh , strjualsatu, strtjual;
    private int dbstok, ijumlahs, iharga;
    private Date datetanggaljual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_penjualan_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_tambah_brg);
        sp_jkat = getActivity().findViewById(R.id.sp_jkat);
        sp_jjnama = getActivity().findViewById(R.id.sp_jnama);


        edttgljual = getActivity().findViewById(R.id.edttgljual);
        edtjualsatu = getActivity().findViewById(R.id.edtjualsatu);
        edtjmlhbrg = getActivity().findViewById(R.id.edtjualjmlh);
        edttljual = getActivity().findViewById(R.id.edttljual);

        btntgljual = getActivity().findViewById(R.id.btntgljual);
        btnjualbrg = getActivity().findViewById(R.id.btnjualbrg);
        pgjualbrg = getActivity().findViewById(R.id.pgjualbrg);

        pgjualbrg.setVisibility(View.GONE);
        btnjualbrg.setVisibility(View.VISIBLE);

        resetdata();
        settanggal();
        ambil_dataj();
        deklarasi();
        hitungjmlh();

        sp_jkat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arrayjabarang.clear();
                arrayjabarang.add("Pilih Barang");
                sp_jjnama.setSelection(0);
                if(i > 0){
                    fungsi = "ambil jnama";
                    ambil_dataj();
                }

                ArrayAdapter<String> barangjadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayjabarang);
                barangjadapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                sp_jjnama.setAdapter(barangjadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_jjnama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edttljual.getEditText().setText("0");
                if(i>0){
                    ambil_stok();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnjualbrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek_barang();
            }
        });
    }

    private void ambil_dataj(){
        stringid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listbarang = FirebaseDatabase.getInstance().getReference("list_barang")
                .child(stringid);

        listbarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot barangsnap : snapshot.getChildren()) {
                    if(fungsi.equals("ambil jnama")){
                        jkategori = sp_jkat.getSelectedItem().toString();
                        if(barangsnap.child("Kategori").getValue(String.class).contains(jkategori)){
                            String getjnama = barangsnap.child("Nama_barang").getValue().toString();
                            arrayjabarang.add(getjnama);
                        }
                    }
                    else{
                        String getjkategori = barangsnap.child("Kategori").getValue().toString();
                        if(!arrayjkategori.contains(getjkategori)){
                            arrayjkategori.add(getjkategori);
                        }
                        ArrayAdapter<String> jkategoriadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arrayjkategori);
                        jkategoriadapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                        sp_jkat.setAdapter(jkategoriadapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ambil_stok() {
        jnama = sp_jjnama.getSelectedItem().toString();
        DatabaseReference liststok = FirebaseDatabase.getInstance().getReference("list_barang")
                .child(stringid).child(jkategori +"_"+ jnama);
        liststok.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbstok = Integer.parseInt(snapshot.child("Stok").getValue().toString());
                edtjmlhbrg.getEditText().setText(String.valueOf(dbstok));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cek_barang() {
        deklarasi();
        datetanggaljual = null;
        tanggal();
        resetnotif();
        if( datetanggaljual == null || sp_jkat.getSelectedItem().equals("Pilih Kategori") ||
                sp_jjnama.getSelectedItem().equals("Pilih Barang") || dbstok < ijumlahs ||
                !strjmlh.matches("[0-9]+") || Integer.parseInt(strjmlh) < 1 ||
                !strjualsatu.matches("[0-9]+") || Integer.parseInt(strjualsatu) < 1){
            if(datetanggaljual == null){
                edttgljual.getEditText().setError("Format tanggal (dd/MM/YYY)");
            }
            if(dbstok < ijumlahs){
                edtjmlhbrg.getEditText().setError("Stok harus kurang dari " +String.valueOf(dbstok));
            }
            if(!strjmlh.matches("[0-9]+") || Integer.parseInt(strjmlh) < 1){
                edtjmlhbrg.getEditText().setError("Masukkan Hanya Angka lebih dari 0");
            }
            if(!strjualsatu.matches("[0-9]+") || Integer.parseInt(strjualsatu) < 1){
                edtjualsatu.getEditText().setError("Masukkan Hanya Angka lebih dari 0");
            }

            Toast.makeText(getActivity(), "Mohon Isi Field dengan benar", Toast.LENGTH_SHORT).show();
        }

        else {
            cek_jual();
        }

    }
    private void deklarasi() {
        strtgl = edttgljual.getEditText().getText().toString();
        strjmlh = edtjmlhbrg.getEditText().getText().toString();
        strjualsatu = edtjualsatu.getEditText().getText().toString();
        strtjual = edttljual.getEditText().getText().toString();
        if (strjmlh.isEmpty() || !strjmlh.matches("[0-9]+")){
            ijumlahs = 0;
        }
        else{
            ijumlahs = Integer.parseInt(strjmlh);
        }
    }


    private void settanggal() {
        Calendar calendar = Calendar.getInstance();
        final int tahun = calendar.get(Calendar.YEAR);
        final int bulan = calendar.get(Calendar.MONTH);
        final int hari = calendar.get(Calendar.DAY_OF_MONTH);

        //jika tombol tanggal ditekan
        btntgljual.setOnClickListener(new View.OnClickListener() {
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
                        strtgl = hari + "/" + bulan + "/" + i;
                        edttgljual.getEditText().setText(strtgl);
                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });
    }

    private void tanggal(){
        deklarasi();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            datetanggaljual = dateFormat.parse(strtgl);

            //dikirim pada database
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetanggaljual);
            final int tahun = calendar.get(Calendar.YEAR);
            final int bulan = calendar.get(Calendar.MONTH)+1;
            final int hari = calendar.get(Calendar.DAY_OF_MONTH);
            datetanggaljual = dateFormat.parse( hari + "/" + bulan + "/" + tahun);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void resetnotif(){
        edttgljual.getEditText().setError(null);
        edtjualsatu.getEditText().setError(null);
        edtjmlhbrg.getEditText().setError(null);
        edttljual.getEditText().setError(null);
    }

    private void resetdata(){
        deklarasi();
        resetnotif();
        strtgl = "";
        jnama = "";
        jkategori = "";
        strjmlh = "";
        strjualsatu = "";
        strtjual = "";

        fungsi="";
        ijumlahs = 0 ;

        edttgljual.getEditText().setText("");
        edtjualsatu.getEditText().setText("");
        edtjmlhbrg.getEditText().setText("");
        edttljual.getEditText().setText("");

        arrayjkategori.clear();
        arrayjkategori.add("Pilih Kategori");;
        arrayjabarang.add("Pilih Barang");
        sp_jkat.setSelection(0);
    }



    private void hitungjmlh(){
        edtjmlhbrg.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                deklarasi();
                if(strjmlh.matches("[0-9]+")){
                    ijumlahs = Integer.parseInt(strjmlh);
                    total_harga();
                }
                else{
                    edttljual.getEditText().setText("0");
                }
            }
        });

        edtjualsatu.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                deklarasi();
                if(strjualsatu.matches("[0-9]+")){
                    iharga = Integer.parseInt(strjualsatu);
                    total_harga();
                }
                else{
                    edttljual.getEditText().setText("0");
                }
            }
        });
    }

    private void total_harga(){
        if(ijumlahs > 0 && iharga > 0){
            int intotal = ijumlahs * iharga;
            edttljual.getEditText().setText(String.valueOf(intotal));
        }
    }

    private void cek_jual(){
        DatabaseReference drbarang = FirebaseDatabase.getInstance()
                .getReference("list_barang").child(stringid).child(jkategori+ "_" +jnama);
        drbarang.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int intstok = dbstok - ijumlahs;
                drbarang.child("Stok").setValue(intstok);
                inputdata();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pgjualbrg.setVisibility(View.GONE);
        btnjualbrg.setVisibility(View.VISIBLE);
    }

    private void inputdata() {
        pgjualbrg.setVisibility(View.VISIBLE);
        btnjualbrg.setVisibility(View.GONE);
        tanggal();
        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            pgjualbrg.setVisibility(View.GONE);
            btnjualbrg.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference id = FirebaseDatabase.getInstance().getReference("barang_keluar")
                    .child(stringid);

            JualModel JBM = new JualModel(datetanggaljual, jnama, jkategori,
                    ijumlahs, iharga,
                    ijumlahs * iharga);

            id.push().setValue(JBM)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                System.out.println(ijumlahs);
                                System.out.println("harga" +iharga);
                                Toast.makeText(getActivity(), "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                                resetdata();
                                ambil_dataj();
                            }
                        }
                    });
            pgjualbrg.setVisibility(View.GONE);
            btnjualbrg.setVisibility(View.VISIBLE);
        }
    }



}