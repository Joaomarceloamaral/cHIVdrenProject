package com.example.chivdrenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView tvAddAccount;
    EditText tilEmail;
    EditText tilPassword;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btLogin = findViewById(R.id.btLogin);
        tvAddAccount = findViewById(R.id.tvAddAccount);

        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doLogin();
            }
        });

        tvAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(it);
            }
        });
    }

    private void doLogin(){

        String msg = "Iniciando Login";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        String email = tilEmail.getText().toString();
        String password = tilPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startMainMenuScreen(user);
                        } else {
                            String msg = "falha ao realizar o login";
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),msg,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            startMainMenuScreen(currentUser);
        }
        else
        {
            String msg = "Erro ao autenticar usuário";
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }
    }

    private void startMainMenuScreen(FirebaseUser currentUser) {
        String msg = "Iniciando tela principal do app";
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

        Intent it = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(it);

    }

}

