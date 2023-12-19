package com.example.minorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText idNumber = findViewById(R.id.idNumber);
        EditText rollNo = findViewById(R.id.rollNo);
        Button searchButton = findViewById(R.id.searchButton);

        database = FirebaseDatabase.getInstance().getReference("Students");

        searchButton.setOnClickListener(view -> {
            String id = idNumber.getText().toString();
            String roll = rollNo.getText().toString();

            if (!id.isEmpty() && !roll.isEmpty()) {
                database.child(id).child(roll).get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        // Show the password change UI
                        showPasswordChangeUI(id, roll);
                    } else {
                        Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, "Enter both ID and Roll Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPasswordChangeUI(String id, String roll) {
        setContentView(R.layout.activity_password_change);

        EditText newPassword = findViewById(R.id.newPassword);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        Button changeButton = findViewById(R.id.changeButton);
        ImageButton showHideButton = findViewById(R.id.showHideButton);
        changeButton.setOnClickListener(view -> {
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();

            if (newPass.equals(confirmPass)) {
                database.child(id).child(roll).child("PASSWORD").setValue(newPass)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
        showHideButton.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHideButton.setImageResource(R.drawable.ic_visibility);
                } else {
                    newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showHideButton.setImageResource(R.drawable.ic_visibility_off);
                }
            }
        });







    }
}
