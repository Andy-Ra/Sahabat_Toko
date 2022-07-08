package com.andyra.sahabattoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andyra.sahabattoko.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Registrasi extends AppCompatActivity {
    private TextInputLayout edregtoko, edregemail, edregpass;
    private Button btnlogin, btnregis;
    private ProgressBar pgdaftar;
    private String regtoko, regemail, regpass;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        setTitle(R.string.title_daftar);
        btnlogin  = findViewById(R.id.btnrmasuk);
        btnregis  = findViewById(R.id.btndaftar);
        edregtoko = findViewById(R.id.edregnama);
        edregemail = findViewById(R.id.edregemail);
        edregpass = findViewById(R.id.edregpass);
        pgdaftar = findViewById(R.id.pgdaftar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        edregtoko.getEditText().setText("");
        edregemail.getEditText().setText("");
        edregpass.getEditText().setText("");

        btnregis.setVisibility(View.VISIBLE);
        pgdaftar.setVisibility(View.GONE);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registrasi.this, Login.class));
                finish();
            }
        });

        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek();
            }
        });

    }

    private void cek() {
        regtoko = edregtoko.getEditText().getText().toString();
        regemail = edregemail.getEditText().getText().toString();
        regpass = edregpass.getEditText().getText().toString();

        resetnotif();
        if(regtoko.isEmpty() == true || !Patterns.EMAIL_ADDRESS.matcher(regemail).matches()
                ||regpass.length() < 8){
            if(regtoko.isEmpty() == true){
                edregtoko.getEditText().setError("Mohon Masukkan Nama Toko");
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(regemail).matches()){
                edregemail.getEditText().setError(getString(R.string.error_email));
            }
            if (regpass.length() < 8){
                edregpass.getEditText().setError(getString(R.string.error_pass));
            }
        }
        else{
            ConnectivityManager mconnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                    mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
                Toast.makeText(Registrasi.this, "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
            }
            else{
                btnregis.setVisibility(View.GONE);
                pgdaftar.setVisibility(View.VISIBLE);
                buatakun();
            }
        }
    }

    private void buatakun() {
        mAuth.createUserWithEmailAndPassword(regemail, regpass)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> mtask) {
                    if(mtask.isSuccessful()){
                        UserModel usermodel = new UserModel(regtoko, regemail);
                        FirebaseDatabase.getInstance().getReference("User")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(usermodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Registrasi.this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Registrasi.this, MainActivity.class));
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Registrasi.this, "Email Sebelumnya Sudah Pernah Terdaftar", Toast.LENGTH_SHORT).show();
                        btnregis.setVisibility(View.VISIBLE);
                        pgdaftar.setVisibility(View.GONE);
                    }
                }
            }
        );
    }

    private void resetnotif(){
        edregtoko.getEditText().setError(null);
        edregemail.getEditText().setError(null);
        edregpass.getEditText().setError(null);
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        finish();
    }
}