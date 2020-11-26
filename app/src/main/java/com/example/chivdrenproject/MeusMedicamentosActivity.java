package com.example.chivdrenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;

import com.example.chivdrenproject.model.ModelMedicamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MeusMedicamentosActivity extends AppCompatActivity {
ImageButton cadastrMed,voltar;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<ModelMedicamento> medicamentoList;
    AdapterRemedios adapterRemedios;

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_medicamentos);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.postsRecyclerview);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        medicamentoList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        medicamentoList = new ArrayList<>();



        loadMyMed();
        checkUserStatus();
voltar = findViewById(R.id.voltar);

voltar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(MeusMedicamentosActivity.this, HomeActivity.class);
        startActivity(i);
    }
});

        cadastrMed = findViewById(R.id.cadastrarMedbt);
        cadastrMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MeusMedicamentosActivity.this, CadastrarMedicamento.class);
                startActivity(it);
            }
        });

        loadMyMed();
        checkUserStatus();
    }

    private void loadMyMed() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MeusRemedios");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicamentoList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelMedicamento modelMedicamento = ds.getValue(ModelMedicamento.class);
                     medicamentoList.add(modelMedicamento);
                    adapterRemedios = new AdapterRemedios(MeusMedicamentosActivity.this,medicamentoList);
                    recyclerView.setAdapter(adapterRemedios);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


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
    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){

            uid = user.getUid();

        }else{
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
    }
}