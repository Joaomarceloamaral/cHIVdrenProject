package com.example.chivdrenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditarActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    TextView remediotv, descricaotv,horaTv , minTv;
    ImageButton voltar ;
    Button atualizar;




    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);



        remediotv = findViewById(R.id.RnomeTv);
        descricaotv = findViewById(R.id.RdescrTv);
        horaTv =findViewById(R.id.RhoraTv);
        minTv =findViewById(R.id.RminTv);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("MeusRemedios");

        voltar = findViewById(R.id.voltar);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditarActivity.this, MeusMedicamentosActivity.class));
                finish();
            }
        });


        atualizar = findViewById(R.id.cadastrarMedbt);
        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfile();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        uid = intent.getStringExtra("pId");

        Query query = FirebaseDatabase.getInstance().getReference("MeusRemedios").orderByChild("pId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren() ){
                    String remedio = ""+ds.child("rNome").getValue();
                    String descricao = ""+ds.child("rDescr").getValue();
                    String hora = ""+ds.child("rHora").getValue();
                    String min = ""+ds.child("rMin").getValue();



                    remediotv.setText(remedio);
                    descricaotv.setText(descricao);
                    horaTv.setText(hora);
                    minTv.setText(min);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        checkUserStatus();





    }

    private void showEditProfile() {

        String options[]= {"Editar nome do medicamento","Editar descrção","Editar Hora","Editar minuto"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);

        builder.setTitle("Editar");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){

                    showNameDescriUpdateDialog("Nome");
                }else if (i ==1)
                {

                    showNameDescriUpdateDialog("Descrição");
                }else if (i ==2)
                {

                    showNameDescriUpdateDialog("Hora");

                }else if(i==3){
                    showNameDescriUpdateDialog("Minuto");
                }
            }


        });

        builder.create().show();
    }

    private void showNameDescriUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atualizar  "+key);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint(" "+key);
        linearLayout.addView(editText);


        builder.setView(linearLayout);

        builder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)){

                    HashMap<String,Object> result = new HashMap<>();
                    result.put(key,value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(EditarActivity.this, "Atualizado", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(EditarActivity.this, "falhou", Toast.LENGTH_SHORT).show();
                                }
                            });

                    if(key.equals("Nome")){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MeusRemedios");
                        Query query = ref.orderByChild("pId").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()){
                                    String child = ds.getKey();
                                    snapshot.getRef().child(child).child("rNome").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    if(key.equals("Descrição")){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MeusRemedios");
                        Query query = ref.orderByChild("pId").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()){
                                    String child = ds.getKey();
                                    snapshot.getRef().child(child).child("rDescr").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    if(key.equals("Hora")){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MeusRemedios");
                        Query query = ref.orderByChild("pId").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()){
                                    String child = ds.getKey();
                                    snapshot.getRef().child(child).child("rHora").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    if(key.equals("Minuto")){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MeusRemedios");
                        Query query = ref.orderByChild("pId").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()){
                                    String child = ds.getKey();
                                    snapshot.getRef().child(child).child("rMin").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }else{
                    Toast.makeText(EditarActivity.this, "Por favor insira "+key, Toast.LENGTH_SHORT).show();

                }


            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();


    }


    private void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){


        }else{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}