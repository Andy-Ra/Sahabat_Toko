package com.andyra.sahabattoko.tampilmenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.andyra.sahabattoko.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentTampilTransaksi extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView btm_navtrx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tampil_transaksi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tampil(new FragmentListTransaksi());
        btm_navtrx = getActivity().findViewById(R.id.btm_navtrx);
        btm_navtrx.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.m_trxb:
                fragment = new FragmentListTransaksi();
                break;
        }
        return tampil(fragment);
    }

    private boolean tampil(Fragment mfragment){
        if (mfragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.trxfLayout, mfragment)
                    .commit();
            return true;
        }
        return false;
    }

}
