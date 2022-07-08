package com.andyra.sahabattoko.tampilmenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andyra.sahabattoko.Login;
import com.andyra.sahabattoko.MainActivity;
import com.andyra.sahabattoko.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UbahPassword extends Fragment {
    TextInputLayout ededpass, ednewpass, ednewpass2;
    Button btnresetpass;
    ProgressBar pgreset;
    private FirebaseUser user;
    private FirebaseAuth mauth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ubah_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.list_brg);
        ededpass = getActivity().findViewById(R.id.ededpass);
        ednewpass = getActivity().findViewById(R.id.ednewpass);
        ednewpass2 = getActivity().findViewById(R.id.ednewpass2);

        btnresetpass = getActivity().findViewById(R.id.btnresetpass);
        pgreset = getActivity().findViewById(R.id.pgreset);

        btnresetpass.setVisibility(View.VISIBLE);
        pgreset.setVisibility(View.GONE);
        clear();

        btnresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearnotif();
                String strpass = ededpass.getEditText().getText().toString();
                String strnewpass = ednewpass.getEditText().getText().toString();
                String strnewpass2 = ednewpass2.getEditText().getText().toString();
                if(strpass.length() < 8 || strnewpass.length() < 8 || strnewpass2.length() < 8){
                    if(strpass.length() < 8 ){
                        ededpass.getEditText().setError(getString(R.string.error_pass));
                    }
                    if(strnewpass.length() < 8){
                        ednewpass.getEditText().setError(getString(R.string.error_pass));
                    }
                    if(strnewpass2.length() < 8){
                        ednewpass2.getEditText().setError(getString(R.string.error_pass));
                    }
                }
                else if(!strnewpass.equals(strnewpass2)){
                    ednewpass.getEditText().setError("Kata Sandi Harus Sama");
                    ednewpass2.getEditText().setError("Kata Sandi Harus Sama");
                }
                else{
                    prosesubah();
                }
            }
        });
    }

    private void prosesubah() {
        String pass = ededpass.getEditText().getText().toString();
        String newpass = ednewpass.getEditText().getText().toString();
        btnresetpass.setVisibility(View.GONE);
        pgreset.setVisibility(View.VISIBLE);
        ConnectivityManager mconnectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                mconnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED){
            Toast.makeText(getActivity(), "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
        }
        else{
            mauth = FirebaseAuth.getInstance();
            user = mauth.getCurrentUser();
            String email = user.getEmail();
            user.reauthenticate(EmailAuthProvider.getCredential(email, pass))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.updatePassword(newpass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Kata Sandi Berhasil Diubah", Toast.LENGTH_SHORT).show();
                                }
                            };
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Gagal, Kata Sandi Lama Salah", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        btnresetpass.setVisibility(View.VISIBLE);
        pgreset.setVisibility(View.GONE);
    }

    private void clear() {
        clearnotif();
        ededpass.getEditText().setText(null);
        ednewpass.getEditText().setText(null);
        ednewpass2.getEditText().setText(null);
    }

    private void clearnotif() {
        ededpass.getEditText().setError(null);
        ednewpass.getEditText().setError(null);
        ednewpass2.getEditText().setError(null);
    }


}