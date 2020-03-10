package com.example.agendor_timeline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendor_timeline.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewuserActivity extends AppCompatActivity {

    private Button button_newuser;
    private EditText editText_newemail;
    private EditText editText_newpassword;
    private EditText editText_name;
    private SharedPreferences prefs;
    private FirebaseFirestore db_firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);

        button_newuser = findViewById(R.id.button_newuser);
        editText_newemail = findViewById(R.id.edit_newemail);
        editText_newpassword = findViewById(R.id.edit_newpassword);
        editText_name = findViewById(R.id.edit_nameuser);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        db_firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        button_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createuser();
            }
        });
    }

    private void createuser() {

        if (editText_newemail.getText().toString().isEmpty() || editText_newpassword.getText().toString().isEmpty()
                || editText_name.getText().toString().isEmpty()){

            Toast.makeText(this,"Preencha todos os campos",Toast.LENGTH_LONG).show();

        }else {

            final String email = editText_newemail.getText().toString();
            final String password = editText_newpassword.getText().toString();

            //passa os parametros informados para criar um novo user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //caso a task retorne sucesso
                            if (task.isSuccessful()){
                                //constroi um objeto user
                                User user = new User(editText_name.getText().toString(), email,
                                        task.getResult().getUser().getUid(), password);

                                //grava no firestore
                                db_firestore.collection("users")
                                        .document(user.getId())//cria o doc com id do user
                                        .set(user);
                                setResult(1, getIntent());
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(NewuserActivity.this,"Houve uma falha ao tentar registrar, tente novamente",Toast.LENGTH_LONG).show();
                            Toast.makeText(NewuserActivity.this,"Msg de erro:"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed(){

        setResult(2, getIntent());
        finish();
    }
}
