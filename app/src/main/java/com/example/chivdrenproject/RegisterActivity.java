package com.example.chivdrenproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends Activity {
    EditText tilBirthDate;
    EditText tilName;
    EditText tilEmail;
    EditText tilPassword;
    Button btRegister;



    FirebaseAuth mAuth;
    String TAG = "RegisterActivity";
        private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilBirthDate = findViewById(R.id.tivDateAccount);
        tilName = findViewById(R.id.tivNameAccount);
        tilEmail = findViewById(R.id.tivEmailAccount);
        tilPassword = findViewById(R.id.tivPasswordAccount);
        btRegister = findViewById(R.id.btRegisterAccount);

        mAuth = FirebaseAuth.getInstance();

        final DatePickerDialog datePicker = getDatePickerDialogForToday(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormated = getDateFormatted(dayOfMonth, month, year);
                tilBirthDate.setText(dateFormated);
            }
        });

        tilBirthDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePicker.show();
                return true;
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tilName.getText().toString().trim();
                String birthDate = tilBirthDate.getText().toString().trim();
                String email = tilEmail.getText().toString().trim();
                String password = tilPassword.getText().toString().trim();

                registerNewUser(name, birthDate, email, password);
            }
        });
    }

    private DatePickerDialog getDatePickerDialogForToday(DatePickerDialog.OnDateSetListener onDateSetListener){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =  new DatePickerDialog(RegisterActivity.this, onDateSetListener, year, month, day);
        return datePickerDialog;
    }

    private String getDateFormatted(int day, int month, int year){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);

        Date date = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = dateFormat.format(date);

        return dateStr;
    }

    private void registerNewUser(final String name, final String date, String email, String password){
mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //////

                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uidemail", uid);
                            hashMap.put("name",name);
                            hashMap.put("date", date);
                            // firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this,"Registrando...\n"+user.getEmail(),Toast.LENGTH_SHORT);

                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


}
