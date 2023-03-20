package com.example.budget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText email,password;
    private Button RegistrationBtn;
    private TextView LogIn;
    private FirebaseAuth Auth;
    private ProgressDialog rogressDial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email= findViewById(R.id.Register_email);
        password= findViewById(R.id.Register_password);
        RegistrationBtn= findViewById(R.id.RegisterBtn);
        LogIn= findViewById(R.id.LogQ);
        Auth = FirebaseAuth.getInstance();
        rogressDial = new ProgressDialog(this);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,Login.class);
                startActivity(intent);
            }
        });
        RegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString= email.getText().toString();
                String errorPassword= getString(R.string.enter_password_reminder);
                String errorEmail= getString(R.string.enter_mail_remainder);
                String passwordString= password.getText().toString();
                if(TextUtils.isEmpty(emailString)){
                    email.setError(errorEmail);
                }
                if(TextUtils.isEmpty(passwordString)){
                    password.setError(errorPassword);
                }   else { rogressDial.setMessage(" in procces");
                    rogressDial.setCanceledOnTouchOutside(false);
                    rogressDial.show();
                    Auth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegistrationActivity.this, UserGuideActivity.class);
                                startActivity(intent);
                                finish();
                                rogressDial.dismiss();
                            }else {
                                Toast.makeText(RegistrationActivity.this, R.string.error_auth,Toast.LENGTH_LONG).show();
                                rogressDial.dismiss();

                            }
                        }
                    });

                }
            }
        });
    }
}