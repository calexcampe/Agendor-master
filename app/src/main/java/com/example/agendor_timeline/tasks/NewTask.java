package com.example.agendor_timeline.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendor_timeline.AgendorActivity;
import com.example.agendor_timeline.R;
import com.example.agendor_timeline.model.Task;
import com.example.agendor_timeline.utils.MaskEditUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewTask extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView imgemail;
    private CircleImageView imgcall;
    private CircleImageView imgproposta;
    private CircleImageView imgreuniao;
    private CircleImageView imgvisita;
    private CircleImageView imgoutros;
    EditText edit_categoria;
    EditText edit_date;
    EditText edit_time;
    EditText edit_cliente;
    EditText edit_descricao;
    private FirebaseFirestore db_firestore;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences prefs;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        imgemail = findViewById(R.id.img_email);
        imgcall = findViewById(R.id.img_call);
        imgoutros = findViewById(R.id.img_outros);
        imgproposta = findViewById(R.id.img_proposta);
        imgreuniao = findViewById(R.id.img_reuniao);
        imgvisita = findViewById(R.id.img_visita);
        edit_categoria = findViewById(R.id.edit_categoria);
        edit_date = findViewById(R.id.edit_date);
        edit_time = findViewById(R.id.edit_time);
        edit_cliente = findViewById(R.id.edit_cliente);
        edit_descricao = findViewById(R.id.edit_descricao);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        db_firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        edit_date.addTextChangedListener(MaskEditUtil.mask(edit_date, MaskEditUtil.FORMAT_DATE));
        edit_time.addTextChangedListener(MaskEditUtil.mask(edit_time, MaskEditUtil.FORMAT_HOUR));

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){

            case R.id.img_email:
                edit_categoria.setText("E-mail");
                imgemail.setBorderColorResource(R.color.colorRed);

                imgcall.setBorderColorResource(R.color.colorBlack);
                imgoutros.setBorderColorResource(R.color.colorBlack);
                imgvisita.setBorderColorResource(R.color.colorBlack);
                imgproposta.setBorderColorResource(R.color.colorBlack);
                imgreuniao.setBorderColorResource(R.color.colorBlack);
                break;

            case R.id.img_call:
                edit_categoria.setText("Ligar");
                imgcall.setBorderColorResource(R.color.colorRed);

                imgemail.setBorderColorResource(R.color.colorBlack);
                imgoutros.setBorderColorResource(R.color.colorBlack);
                imgvisita.setBorderColorResource(R.color.colorBlack);
                imgproposta.setBorderColorResource(R.color.colorBlack);
                imgreuniao.setBorderColorResource(R.color.colorBlack);
                break;

            case R.id.img_outros:
                edit_categoria.setText("Outros");
                imgoutros.setBorderColorResource(R.color.colorRed);

                imgemail.setBorderColorResource(R.color.colorBlack);
                imgcall.setBorderColorResource(R.color.colorBlack);
                imgvisita.setBorderColorResource(R.color.colorBlack);
                imgproposta.setBorderColorResource(R.color.colorBlack);
                imgreuniao.setBorderColorResource(R.color.colorBlack);
                break;

            case R.id.img_proposta:
                edit_categoria.setText("Proposta");
                imgproposta.setBorderColorResource(R.color.colorRed);

                imgemail.setBorderColorResource(R.color.colorBlack);
                imgcall.setBorderColorResource(R.color.colorBlack);
                imgvisita.setBorderColorResource(R.color.colorBlack);
                imgoutros.setBorderColorResource(R.color.colorBlack);
                imgreuniao.setBorderColorResource(R.color.colorBlack);
                break;

            case R.id.img_reuniao:
                edit_categoria.setText("Reuniao");
                imgreuniao.setBorderColorResource(R.color.colorRed);

                imgemail.setBorderColorResource(R.color.colorBlack);
                imgcall.setBorderColorResource(R.color.colorBlack);
                imgvisita.setBorderColorResource(R.color.colorBlack);
                imgoutros.setBorderColorResource(R.color.colorBlack);
                imgproposta.setBorderColorResource(R.color.colorBlack);
                break;

            case R.id.img_visita:
                    edit_categoria.setText("Visita");
                imgvisita.setBorderColorResource(R.color.colorRed);

                imgemail.setBorderColorResource(R.color.colorBlack);
                imgcall.setBorderColorResource(R.color.colorBlack);
                imgreuniao.setBorderColorResource(R.color.colorBlack);
                imgoutros.setBorderColorResource(R.color.colorBlack);
                imgproposta.setBorderColorResource(R.color.colorBlack);
                break;

            case R.id.button_agendar:
                String categoria = edit_categoria.getText().toString();
                String data = edit_date.getText().toString();
                String hora = edit_time.getText().toString();
                String cliente = firebaseAuth.getUid();
                String descricao = edit_descricao.getText().toString();
                String user = firebaseAuth.getUid();


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = simpleDateFormat.parse(data);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(NewTask.this,"Verifique a data",Toast.LENGTH_LONG).show();
                    return;
                }

                if (categoria.isEmpty() || data.isEmpty() || hora.isEmpty() || cliente.isEmpty() || descricao.isEmpty()){

                    Toast.makeText(NewTask.this,"Preencha todos os campos",Toast.LENGTH_LONG).show();
                    return;
                }
                Task task = new Task(categoria, date, hora, cliente, descricao, user);

                Agendar(task);
        }
    }

    public void Agendar(final Task task){

        Intent intent = new Intent();

        intent.putExtra("categoria",task.getCategoria());
        intent.putExtra("data",task.getData().getDate());
        intent.putExtra("descricao",task.getDescricao());
        intent.putExtra("hora",task.getHora());
        intent.putExtra("cliente",task.getCliente());
        intent.putExtra("user",task.getUser());
        setResult(1,intent);
        finish();

    }
    @Override
    public void onBackPressed(){

        setResult(2, getIntent());
        finish();
    }
}
