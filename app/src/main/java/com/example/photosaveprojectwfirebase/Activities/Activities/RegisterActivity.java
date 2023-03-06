package com.example.photosaveprojectwfirebase.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.photosaveprojectwfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private AppCompatButton mRegisterButton;

    FirebaseAuth auth;




    private void init(){

        mEmailEditText = findViewById(R.id.editTextEmail);
        mPasswordEditText = findViewById(R.id.editTextPassword);
        mConfirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        mRegisterButton = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        getSupportActionBar().hide();


        // Kayıt butonunu başlangıçta pasif yap
        mRegisterButton.setEnabled(false);
        mRegisterButton.setVisibility(View.INVISIBLE);


        // Şifre ve şifre tekrar alanları değiştiğinde doğruluğunu kontrol et
        mEmailEditText.addTextChangedListener(textWatcher);
        mPasswordEditText.addTextChangedListener(textWatcher);
        mConfirmPasswordEditText.addTextChangedListener(textWatcher);




    }



    // Şifre alanları değiştiğinde kontrolü sağla
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = mEmailEditText.getText().toString().trim();
            String password = mPasswordEditText.getText().toString().trim();
            String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();


            // Şifreler eşleşiyorsa kayıt butonunu aktif yap
            if (!email.isEmpty() && !password.isEmpty() && password.equals(confirmPassword)) {
                mRegisterButton.setEnabled(true);
                mRegisterButton.setVisibility(View.VISIBLE);
            } else {
                mRegisterButton.setEnabled(false);
                mRegisterButton.setVisibility(View.INVISIBLE);

            }
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    // Kullanıcı Kayıt Etme Fonksiyonu
    public void registerClick(View view) {

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void loginClick(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}










