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

public class Login extends AppCompatActivity {
        private EditText email,password;
        private Button loginBtn;
        private TextView SignUp;
        private FirebaseAuth Auth;
        private ProgressDialog rogressDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        loginBtn= findViewById(R.id.LoginBtn);

        Auth = FirebaseAuth.getInstance();
        rogressDial = new ProgressDialog(this);

        SignUp= findViewById(R.id.RegistrationQ);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString= email.getText().toString();
                String errorPassword= getString(R.string.enter_password_reminder);
                String errorEmail= getString(R.string.enter_mail_remainder);
                String passwordString= password.getText().toString();
                if(TextUtils.isEmpty(emailString)){

                    email.setError(errorEmail);
                }else if(TextUtils.isEmpty(passwordString)){

                    password.setError(errorPassword);
                }else if(emailString==null||passwordString == null){
                    email.setError(errorEmail);
                    password.setError(errorPassword);
                }else { rogressDial.setMessage("Login in procces");
                    rogressDial.setCanceledOnTouchOutside(false);
                    rogressDial.show();
                    Auth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                rogressDial.dismiss();
                            }else {Toast.makeText(Login.this,"Wrong auth",Toast.LENGTH_LONG).show();
                                rogressDial.dismiss();

                            }
                        }
                    });

                }
            }
        });
    }
}