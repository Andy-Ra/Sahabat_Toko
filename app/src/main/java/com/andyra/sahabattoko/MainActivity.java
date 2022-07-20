package com.andyra.sahabattoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.andyra.sahabattoko.model.UserModel;
import com.andyra.sahabattoko.tampilmenu.FragmentHapusBarang;
import com.andyra.sahabattoko.tampilmenu.FragmentListBarang;
import com.andyra.sahabattoko.tampilmenu.FragmentPembelianBarang;
import com.andyra.sahabattoko.tampilmenu.FragmentPenjualanBarang;
import com.andyra.sahabattoko.tampiltransaksi.FragmentDetailTransaksi;
import com.andyra.sahabattoko.tampiltransaksi.FragmentTampilTransaksi;
import com.andyra.sahabattoko.tampilmenu.UbahPassword;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout m_maindrawer;
    private NavigationView mnavigation;
    private FirebaseUser user;
    private DatabaseReference dbuser;
    private String iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mtoolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(mtoolbar);

        m_maindrawer = findViewById(R.id.drawer_layout);
        mnavigation = findViewById(R.id.mainnav_tampil);
        mnavigation.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle mtoogle = new ActionBarDrawerToggle(
                this, m_maindrawer, mtoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        m_maindrawer.addDrawerListener(mtoogle);
        mtoogle.syncState();

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbuser = FirebaseDatabase.getInstance().getReference("User");
        iduser = user.getUid();
        ambil_user();
        tampil_awal();
    }

    @Override
    public void onBackPressed(){
        if(m_maindrawer.isDrawerOpen(GravityCompat.START)){
            m_maindrawer.closeDrawer(GravityCompat.START);
        }
        else{
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem mitem) {
        switch (mitem.getItemId()){
            case R.id.nav_list_brg:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new FragmentListBarang()).commit();
                getSupportFragmentManager().beginTransaction().addToBackStack(null);
                break;

            case R.id.nav_hps_brg:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new FragmentHapusBarang()).commit();
                break;

            case R.id.nav_list_trx:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new FragmentTampilTransaksi()).commit();
                break;

            case R.id.nav_beli:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new FragmentPembelianBarang()).commit();
                break;

            case R.id.nav_jual:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new FragmentPenjualanBarang()).commit();
                break;

            case R.id.nav_ubah_sandi:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new UbahPassword()).commit();
                break;

            case R.id.nav_penggunaan:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new PenggunaanAplikasi()).commit();
                break;

            case R.id.nav_tentang:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new TentangAplikasi()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                break;

            case R.id.nav_out:
                moveTaskToBack(true);
                break;
        }
        System.out.println("itemmm " +mitem);
        m_maindrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void tampil_awal() {
        if(getIntent().getExtras() == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                    new FragmentListBarang()).commit();
        }
        else{
            String ara = getIntent().getStringExtra("aksi_pindaahh");
            if(ara.equals("detail tbeli")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainfLayout,
                        new FragmentDetailTransaksi()).commit();
            }
        }
    }

    private void ambil_user(){
        dbuser.child(iduser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel umodel = snapshot.getValue(UserModel.class);

                if(umodel != null){
                    View mheader = mnavigation.getHeaderView(0);
                    TextView tvheader = mheader.findViewById(R.id.tvheader);
                    tvheader.setText(umodel.Toko);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}