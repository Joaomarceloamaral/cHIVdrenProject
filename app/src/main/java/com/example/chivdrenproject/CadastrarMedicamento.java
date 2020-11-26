package com.example.chivdrenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.contentcapture.DataShareWriteAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CadastrarMedicamento extends AppCompatActivity {
FirebaseAuth firebaseAuth;
DatabaseReference userDbRef;

EditText nameEt,descrEt,horaEt,minEt;
Button cadastrarMed;
ImageButton voltar;
String name,email,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_medicamento);
        firebaseAuth =FirebaseAuth.getInstance();
        checkUserStatus();
        voltar = findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CadastrarMedicamento.this, MeusMedicamentosActivity.class);
                startActivity(it);
            }
        });

        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    name = ""+ ds.child("name").getValue();
                    email = ""+ ds.child("email").getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nameEt = findViewById(R.id.RnomeTv);
        descrEt = findViewById(R.id.RdescrTv);
        horaEt = findViewById(R.id.RhoraTv);
        minEt = findViewById(R.id.RminTv);
        cadastrarMed = findViewById(R.id.cadastrarMedbt);


        cadastrarMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_remedio = nameEt.getText().toString().trim();
                String descr_remedio = descrEt.getText().toString().trim();
                String hora_remedio = horaEt.getText().toString().trim();
                String min_remedio = minEt.getText().toString().trim();
                if(TextUtils.isEmpty(nome_remedio)){
                    Toast.makeText(CadastrarMedicamento.this, "Adicione nome do Remédio ...", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(TextUtils.isEmpty(descr_remedio)){
                    Toast.makeText(CadastrarMedicamento.this, "Adicione Descrição", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(TextUtils.isEmpty(hora_remedio)){
                    Toast.makeText(CadastrarMedicamento.this, "Adicione Hora", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(TextUtils.isEmpty(min_remedio)){
                    Toast.makeText(CadastrarMedicamento.this, "Adicione Min", Toast.LENGTH_SHORT).show();
                    return ;
                }else{
                    uploadData(nome_remedio,descr_remedio,hora_remedio,min_remedio);

                }
            }
        });

    }

    private void uploadData(String nome_remedio, String descr_remedio, String hora_remedio, String min_remedio) {
        final String  timeStamp = String.valueOf(System.currentTimeMillis());
        HashMap<Object,String> hashMap = new HashMap<>();

        hashMap.put("uid",uid);
        hashMap.put("name",name);
        hashMap.put("email",email);
        hashMap.put("pId",timeStamp);
        hashMap.put("rNome",nome_remedio);
        hashMap.put("rDescr",descr_remedio);
        hashMap.put("rHora",hora_remedio);
        hashMap.put("rMin",min_remedio);
        hashMap.put("pTime",timeStamp);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MeusRemedios");
        ref.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(CadastrarMedicamento.this, "Remédio Cadastrado", Toast.LENGTH_SHORT).show();

                        nameEt.setText("");
                        descrEt.setText("");
                        horaEt.setText("");
                        minEt.setText("");


                        startActivity(new Intent(CadastrarMedicamento.this, MeusMedicamentosActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CadastrarMedicamento.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();



        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}