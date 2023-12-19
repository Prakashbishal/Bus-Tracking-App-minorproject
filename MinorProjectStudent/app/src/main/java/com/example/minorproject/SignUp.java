package com.example.minorproject;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText name = findViewById(R.id.fullname);
        EditText roll = findViewById(R.id.rollno);
        EditText id_num = findViewById(R.id.Id_Num);
        EditText password = findViewById(R.id.reg_password);
        EditText email = findViewById(R.id.email);
        Button signUp = findViewById(R.id.sign_upbtn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString();
                String Roll = roll.getText().toString().toUpperCase();
                String ID = id_num.getText().toString();
                String PassWord = password.getText().toString();
                String Email = email.getText().toString();

                if (isValidInput(Name, ID, Email, PassWord, Roll)) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Students");

                    reference.child(ID).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "ID already exists, try to login", Toast.LENGTH_SHORT).show();
                            } else {
                                Students student = new Students(Name, ID, Roll, Email, PassWord);
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Students");

                                database.child(ID).child(Roll).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        name.setText("");
                                        roll.setText("");
                                        id_num.setText("");
                                        password.setText("");
                                        email.setText("");
                                        Toast.makeText(getApplicationContext(), "Adding New User", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }
    private boolean isValidInput(String name, String id, String email, String password, String roll) {
        if (!name.isEmpty() && !roll.isEmpty()) {
            // Check if the ID number is 10 digits long
            if (isValidRollFormat(roll)){
                if(isValidIdLength(id)) {
                    if(isValidEmail(email)) {
                        if(isValidPassword(password)) {
                            return true;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid Password, Password must be alphanumeric, 8 digits long, should have at least 1 special character, 1 uppercase and 1 lowercase", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid ID Number", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), " Enter Correct Roll No", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Enter all Credentials", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    // Function to check if the ID number is 10 digits long
        private boolean isValidIdLength(String id) {
            return id.length() == 10;
        }
    // Function to check if the roll number follows the specified format
    private boolean isValidRollFormat(String roll) {
        // Modified regex to allow one or two digits after the branch code
        String regex = "\\d{2}(BTECH|MTECH|BARCH)(IT|LIT|ECE|LECE|BME|LBME|EE|LEE)\\d{1,2}$";
        return roll.matches(regex);
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
    private boolean isValidPassword(String password) {
        // Password should be alphanumeric and 8 digits long
        // It should have at least 1 special character, 1 uppercase, and 1 lowercase
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }
}
