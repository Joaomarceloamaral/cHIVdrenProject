package com.example.chivdrenproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

FirebaseAuth auth;
FirebaseUser user;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
ImageButton  remedios,sair;
Button eu;
TextView welcome;
String email, uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        welcome = findViewById(R.id.welcomeTv);
        remedios = findViewById(R.id.remedios);
        eu = findViewById(R.id.eu);
        sair = findViewById(R.id.sair);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                checkUserStatus();
            }
        });

        eu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, PerfilActivity.class);
                startActivity(i);
            }
        });

        remedios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HomeActivity.this, MeusMedicamentosActivity.class);
                startActivity(it);
            }
        });

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String user = ""+ds.child("name").getValue();
                    welcome.setText("Olá, Seja Bem-Vindo(a) "+user+"!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       checkUserStatus();

    }

    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();



        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
