package com.example.agendor_timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button button_login;
    private TextView textView_newuser;
    private EditText edit_email;
    private EditText edit_password;
    private SharedPreferences prefs;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_login = findViewById(R.id.button_login);
        textView_newuser = findViewById(R.id.textView_newuser);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        auth = FirebaseAuth.getInstance();

        textView_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, NewuserActivity.class);
                startActivityForResult(intent,0);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_email.getText().toString().isEmpty() || edit_password.getText().toString().isEmpty()){

                    Toast.makeText(LoginActivity.this,"Confira o email e senha",Toast.LENGTH_LONG).show();
                }else {

                    auth.signInWithEmailAndPassword(edit_email.getText().toString(), edit_password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("email",edit_email.getText().toString());
                                    editor.putString("password",edit_password.getText().toString());
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, AgendorActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(LoginActivity.this,"Confira o e-mail e senha",Toast.LENGTH_LONG).show();
                                }
                            });

                }

            }
        });

    }


    protected void onActivityResult(int codigo, int resultado, Intent i) {
        super.onActivityResult(codigo, resultado, i);

        //se for cadastrado um novo usuario com sucesso, o restultao será = 1
        if (resultado == 1){

            Intent intent = new Intent(LoginActivity.this, AgendorActivity.class);
            startActivity(intent);
            finish();
        }

        //se o resultado for = 2 o usuario retornou da tela sem efetuar um novo registro
    }

}
