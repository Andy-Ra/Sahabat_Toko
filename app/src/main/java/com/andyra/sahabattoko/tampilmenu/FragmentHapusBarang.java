package com.andyra.sahabattoko.tampilmenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.andyra.sahabattoko.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHapusBarang extends Fragment {
    String stringid, fungsi, kategori, nama;
    Spinner sp_hkat, sp_hnam;
    TextInputLayout tilstok;
    Button btnhps;
    ArrayList<String> arraykategori = new ArrayList<>();
    ArrayList<String> arraybrg = new ArrayList<>();
    DatabaseReference listbarang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hapus_barang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_hapus_brg);
        sp_hkat = getActivity().findViewById(R.id.sp_hkat);
        sp_hnam = getActivity().findViewById(R.id.sp_hnama);
        tilstok = getActivity().findViewById(R.id.edhstok);
        btnhps = getActivity().findViewById(R.id.btnhpsbrg);
        reset();

        ambil_data();

        sp_hkat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arraybrg.clear();
                arraybrg.add("Pilih Barang");
                sp_hnam.setSelection(0);
                if(i > 0){
                    fungsi = "ambil nama";
                    ambil_data();
                }

                ArrayAdapter<String> barangadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arraybrg);
                barangadapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                sp_hnam.setAdapter(barangadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_hnam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tilstok.getEditText().setText("0");
                if(i>0){
                    ambil_stok();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnhps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapus_barang();
            }
        });
    }

    private void ambil_data(){
        stringid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listbarang = FirebaseDatabase.getInstance().getReference("list_barang")
                .child(stringid);

        listbarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot barangsnap : snapshot.getChildren()) {
                    if(fungsi.equals("ambil nama")){
                        kategori = sp_hkat.getSelectedItem().toString();
                        if(barangsnap.child("Kategori").getValue(String.class).contains(kategori)){
                            String getnama = barangsnap.child("Nama_barang").getValue().toString();
                            arraybrg.add(getnama);
                        }
                    }
                    else{
                        String getkategori = barangsnap.child("Kategori").getValue().toString();
                        if(!arraykategori.contains(getkategori)){
                            arraykategori.add(getkategori);
                        }
                        ArrayAdapter<String> kategoriadapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, arraykategori);
                        kategoriadapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                        sp_hkat.setAdapter(kategoriadapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ambil_stok() {
        nama = sp_hnam.getSelectedItem().toString();
        DatabaseReference liststok = FirebaseDatabase.getInstance().getReference("list_barang")
                .child(stringid).child(kategori +"_"+ nama);
        liststok.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tilstok.getEditText().setText(snapshot.child("Stok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hapus_barang() {
        if (sp_hkat.getSelectedItem().equals("Pilih Kategori") || sp_hnam.getSelectedItem().equals("Pilih Barang")){
            Toast.makeText(getActivity(), "Mohon Pilih Barang", Toast.LENGTH_SHORT).show();
        }

        else {
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setTitle("Anda Yakin?");

            ad.setMessage("Hapus Barang" +nama).setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("list_barang")
                                    .child(stringid).child(kategori + "_" + nama);
                            data.removeValue();
                            Toast.makeText(getActivity(), "Barang " + nama + " Telah Berhasil dihapus", Toast.LENGTH_SHORT).show();
                            reset();
                            ambil_data();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog = ad.create();
            alertDialog.show();
        }

    }

    private  void reset(){
        kategori = "";
        nama = "";
        fungsi="";
        arraykategori.clear();
        arraykategori.add("Pilih Kategori");;
        arraybrg.add("Pilih Barang");
        sp_hkat.setSelection(0);
    }

}