package com.example.minorproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    EditText id;
    EditText passwrd;
    private ProgressBar progressBar;
    TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        id = findViewById(R.id.Id);
        passwrd = findViewById(R.id.pass);
        forgotPass=findViewById(R.id.forgotPass);
        ImageButton showHideButton = findViewById(R.id.showHideButton);
        showHideButton.setTag(R.drawable.ic_visibility_off);
        Button signin = findViewById(R.id.sign_inBtn);
        TextView signup = findViewById(R.id.sign_upnow);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = id.getText().toString();
                String Password = passwrd.getText().toString();
                authenticate(ID, Password);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibilityIcon = (Integer) showHideButton.getTag();
                if (visibilityIcon == R.drawable.ic_visibility_off) {
                    passwrd.setTransformationMethod(null);
                    showHideButton.setImageResource(R.drawable.ic_visibility);
                    showHideButton.setTag(R.drawable.ic_visibility);
                } else {
                    passwrd.setTransformationMethod(new PasswordTransformationMethod());
                    showHideButton.setImageResource(R.drawable.ic_visibility_off);
                    showHideButton.setTag(R.drawable.ic_visibility_off);
                }
            }
        });
    }
    void authenticate(String userID, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Students");
        DatabaseReference userReference = databaseReference.child(userID);

        userReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot rollSnapshot : dataSnapshot.getChildren()) {
                        String idNo = rollSnapshot.child("ID_NO").getValue(String.class);
                        String pass = rollSnapshot.child("PASSWORD").getValue(String.class);
                        if (idNo != null && pass != null) {
                            Log.d("AUTH", "Retrieved ID_NO: " + idNo);
                            Log.d("AUTH", "Retrieved PASSWORD: " + pass);
                            if (password.equals(pass)) {
                                id.setText("");
                                passwrd.setText("");
                                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);

                                startActivity(intent);
                                progressBar.setVisibility(View.GONE);
                                return;
                            } else {
                                Toast.makeText(getApplicationContext(), "UserID & Password Do Not Match", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), "ID number not found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "User ID Does Not Exist", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressbar);
        // Make the progress bar visible
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Closes the app completely
    }
}
