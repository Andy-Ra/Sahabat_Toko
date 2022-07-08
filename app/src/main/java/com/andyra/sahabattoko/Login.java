package com.andyra.sahabattoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextInputLayout edlgnemail, edlgnpass;
    Button btnlogin, btnregis;
    ProgressBar pgmasuk;
    private FirebaseAuth mauth;
    private String lgnemail, lgnpass;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin  = findViewById(R.id.btnlmasuk);
        btnregis  = findViewById(R.id.btnldaftar);
        pgmasuk = findViewById(R.id.pgmasuk);
        TextView tvlgnlupa = findViewById(R.id.tvlgnlupa);


        edlgnemail = findViewById(R.id.edlgnemail);
        edlgnpass = findViewById(R.id.edlgnpass);


        edlgnemail.getEditText().setText("");
        edlgnpass.getEditText().setText("");
        btnlogin.setVisibility(View.VISIBLE);
        pgmasuk.setVisibility(View.GONE);
        mauth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        session();

        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registrasi.class));
                finish();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek();
            }
        });

        tvlgnlupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void cek() {
        lgnemail = edlgnemail.getEditText().getText().toString().trim();
        lgnpass = edlgnpass.getEditText().getText().toString().trim();

        resetnotif();
        if(!Patterns.EMAIL_ADDRESS.matcher(lgnemail).matches()
                ||lgnpass.length() < 8){
            if(!Patterns.EMAIL_ADDRESS.matcher(lgnemail).matches()){
                edlgnemail.getEditText().setError(getString(R.string.error_email));
            }
            if (lgnpass.length() < 8){
                edlgnpass.getEditText().setError(getString(R.string.error_pass));
            }
        }
        else{
            ConnectivityManager mconnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                    mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
                Toast.makeText(Login.this, "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
            }
            else{
                btnlogin.setVisibility(View.GONE);
                pgmasuk.setVisibility(View.VISIBLE);
                menglogin();
            }
        }
    }

    private void menglogin() {
        mauth.signInWithEmailAndPassword(lgnemail, lgnpass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
                else{
                    Toast.makeText(Login.this, "Email / Kata Sandi Salah", Toast.LENGTH_SHORT).show();
                    btnlogin.setVisibility(View.VISIBLE);
                    pgmasuk.setVisibility(View.GONE);
                }
            }
        });
    }

    private void session() {
        if(user != null){
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }

    private void resetnotif(){
        edlgnemail.getEditText().setError(null);
        edlgnpass.getEditText().setError(null);
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        finish();
    }
}