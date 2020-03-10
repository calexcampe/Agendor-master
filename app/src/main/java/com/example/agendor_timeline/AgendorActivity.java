package com.example.agendor_timeline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendor_timeline.View.TimelineFragment;
import com.example.agendor_timeline.model.Category;
import com.example.agendor_timeline.model.Task;
import com.example.agendor_timeline.model.TaskConstructor;
import com.example.agendor_timeline.interfaces.TimelineObject;
import com.example.agendor_timeline.interfaces.TimelineObjectClickListener;
import com.example.agendor_timeline.model.TimelineGroupType;
import com.example.agendor_timeline.model.Urls;
import com.example.agendor_timeline.tasks.NewTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendorActivity extends AppCompatActivity implements TimelineObjectClickListener {

    TimelineFragment mFragment;
    private FirebaseFirestore db_firestore;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences prefs;
    private ProgressDialog pDialog;
    private FloatingActionButton flbutton;
    private TextView txtv_email;
    private TextView txtv_ligar;
    private TextView txtv_outros;
    private TextView txtv_proposta;
    private TextView txtv_reuniao;
    private TextView txtv_visita;
    private Urls urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendor);

        txtv_email = findViewById(R.id.textV_email);
        txtv_ligar = findViewById(R.id.textV_call);
        txtv_outros = findViewById(R.id.textV_outros);
        txtv_proposta = findViewById(R.id.textV_proposta);
        txtv_reuniao = findViewById(R.id.textV_reuniao);
        txtv_visita = findViewById(R.id.textV_visita);

        flbutton = findViewById(R.id.flbutton);

        flbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AgendorActivity.this, NewTask.class);
                startActivityForResult(intent,0);

            }
        });


        //verifica se está autorizado, caso sim carrega tarefas
        verifyauth();
    }


    public void verifyauth(){

        final ArrayList<TimelineObject> objs = new ArrayList<>();

        // instancia do TimelineFragment
        mFragment = new TimelineFragment();
        //Set configurations
        mFragment.addOnClickListener(AgendorActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        db_firestore = FirebaseFirestore.getInstance();

        //caso nao esteja auteticado abre a tela de login
        if (firebaseAuth.getUid() == null){

            Intent intent = new Intent(AgendorActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {

            //pega a data atual
            Date date = new Date(System.currentTimeMillis());
            final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //instancias do calendar
            Calendar calend = Calendar.getInstance();
            Calendar calend2 = Calendar.getInstance();
            //seta a data atual no calendar
            calend.setTime(date);
            calend2.setTime(date);
            //adicionar 7 dias no calendar
            calend.set(Calendar.DAY_OF_MONTH,calend.get(Calendar.DAY_OF_MONTH)+7);
            calend2.set(Calendar.DAY_OF_MONTH,calend2.get(Calendar.DAY_OF_MONTH)-1);
            // passa as datas para as variaveis
            Date df1 = calend2.getTime();
            Date df2 = calend.getTime();

            final Category category = new Category();

            //carrega todas as tarefas do intervalo de 7 dias
            String id = firebaseAuth.getUid();//id do user
            db_firestore.collection("tarefas")
                    .whereEqualTo("user", id)//where id
                    .whereGreaterThan("data", df1)//where data inicial
                    .whereLessThan("data", df2)//where data final
                    .orderBy("data", Query.Direction.ASCENDING)
                    //cria um listener para ouvir quando um novo documento for adicionado
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                          if (e != null){
                              Log.e("error tetste",e.getMessage());
                              return;
                          }

                            for (DocumentChange dc: queryDocumentSnapshots.getDocumentChanges()) {
                                switch (dc.getType()){
                                    case ADDED:
                                        carregaurls();
                                        long mls = 0;
                                        //converte data para milisegundos para alientar o objeto da timeline
                                        Date date = dc.getDocument().getDate("data");
                                        dateFormat.format(date);
                                        mls = date.getTime();

                                        //separa tarefas por categoria
                                        switch (dc.getDocument().get("categoria").toString()){
                                            case "E-mail":

                                                mFragment.addSingleObject(new TaskConstructor(mls, dc.getDocument().get("categoria").toString(),
                                                        urls.getUrlemail(),dc.getDocument().get("descricao").toString(),
                                                        dc.getDocument().get("hora").toString()),TimelineGroupType.DAY);

                                                category.setQtd_email(category.getQtd_email()+1);
                                                break;
                                            case "Ligar":

                                                mFragment.addSingleObject(new TaskConstructor(mls, dc.getDocument().get("categoria").toString(),
                                                        urls.getUrlcall(),dc.getDocument().get("descricao").toString(),
                                                        dc.getDocument().get("hora").toString()),TimelineGroupType.DAY);

                                                category.setQtd_call(category.getQtd_call()+1);
                                                break;
                                            case "Outros":

                                                mFragment.addSingleObject(new TaskConstructor(mls, dc.getDocument().get("categoria").toString(),
                                                        urls.getUrloutros(),dc.getDocument().get("descricao").toString(),
                                                        dc.getDocument().get("hora").toString()),TimelineGroupType.DAY);

                                                category.setQtd_outros(category.getQtd_outros() +1);
                                                break;
                                            case "Proposta":

                                                mFragment.addSingleObject(new TaskConstructor(mls, dc.getDocument().get("categoria").toString(),
                                                        urls.getUrlproposta(),dc.getDocument().get("descricao").toString(),
                                                        dc.getDocument().get("hora").toString()),TimelineGroupType.DAY);

                                                category.setQtd_proposta(category.getQtd_proposta()+1);
                                                break;
                                            case "Reuniao":

                                                mFragment.addSingleObject(new TaskConstructor(mls, dc.getDocument().get("categoria").toString(),
                                                        urls.getUrlreuniao(),dc.getDocument().get("descricao").toString(),
                                                        dc.getDocument().get("hora").toString()),TimelineGroupType.DAY);

                                                category.setQtd_reuniao(category.getQtd_reuniao()+1);
                                                break;
                                            case "Visita":

                                                mFragment.addSingleObject(new TaskConstructor(mls, dc.getDocument().get("categoria").toString(),
                                                        urls.getUrlvisita(),dc.getDocument().get("descricao").toString(),
                                                        dc.getDocument().get("hora").toString()),TimelineGroupType.DAY);

                                                category.setQtd_visita(category.getQtd_visita()+1);
                                                break;
                                        }

                                        Log.d("tipo add","tipo add");
                                        break;
                                    case MODIFIED:
                                        Log.d("tipo modificado","tipo modificado");
                                        break;
                                }
                            }

                            //repassa a quantidade de tarefas de cada categoria
                            txtv_email.setText(String.valueOf(category.getQtd_email()));
                            txtv_ligar.setText(String.valueOf(category.getQtd_call()));
                            txtv_outros.setText(String.valueOf(category.getQtd_outros()));
                            txtv_proposta.setText(String.valueOf(category.getQtd_proposta()));
                            txtv_reuniao.setText(String.valueOf(category.getQtd_reuniao()));
                            txtv_visita.setText(String.valueOf(category.getQtd_visita()));

                            //mFragment.setData(objs, TimelineGroupType.DAY);
                            loadFragment(mFragment);
                            Toast.makeText(AgendorActivity.this,"Agenda Atualizada",Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    private void carregaurls() {

        urls = new Urls();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference visitaRef = firebaseStorage.getReference();
        //
        /*usado apenas para verificar a url das imagens
        visitaRef.child("images/call.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                urls.setUrlcall(uri.toString());

            }
        });*/
        urls.setUrlemail("https://firebasestorage.googleapis.com/v0/b/agendor-timeline.appspot.com/o/images%2Femail.png?alt=media&token=0a55f826-708e-4de5-bb7f-094613801423");
        urls.setUrlcall("https://firebasestorage.googleapis.com/v0/b/agendor-timeline.appspot.com/o/images%2Fcall.png?alt=media&token=123579e8-9082-4473-a70d-bd208034b24d");
        urls.setUrloutros("https://firebasestorage.googleapis.com/v0/b/agendor-timeline.appspot.com/o/images%2Foutros.png?alt=media&token=feaa6208-6ae1-4c2f-bc4a-84e904b04d70");
        urls.setUrlproposta("https://firebasestorage.googleapis.com/v0/b/agendor-timeline.appspot.com/o/images%2Fproposta.png?alt=media&token=b35def42-3535-47a3-ac17-5f81381e30c3");
        urls.setUrlreuniao("https://firebasestorage.googleapis.com/v0/b/agendor-timeline.appspot.com/o/images%2Freuniao.png?alt=media&token=146302cf-f9cb-49b0-9eae-683d1a979627");
        urls.setUrlvisita("https://firebasestorage.googleapis.com/v0/b/agendor-timeline.appspot.com/o/images%2Fvisita.png?alt=media&token=09766dc3-93e3-45de-886c-20261b6dbde5");

    }

    private void loadFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.commit();
    }

    @Override
    public void onTimelineObjectClicked(TimelineObject timelineObject) {

      Intent intent = new Intent(AgendorActivity.this, ViewTask.class);
      intent.putExtra("hora", timelineObject.gethora());
      intent.putExtra("descricao", timelineObject.getdescricao());
      startActivity(intent);


    }

    @Override
    public void onTimelineObjectLongClicked(TimelineObject timelineObject) {

    }

    @Override
    protected void onResume() {
        super.onResume();
       // verifyauth();
    }

    protected void onActivityResult(int codigo, int resultado, Intent i) {
        super.onActivityResult(codigo, resultado, i);

        if (resultado != 1){
            return;
        }
        Date date = new Date();
        date.setDate(i.getIntExtra("data",-1));

        Task task = new Task(i.getStringExtra("categoria")
                    ,date
                    ,i.getStringExtra("hora")
                    ,i.getStringExtra("cliente")
                    ,i.getStringExtra("descricao")
                    ,i.getStringExtra("user")
        );

        db_firestore.collection("tarefas")
                .add(task)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("erronewtask",e.getMessage());

                    }
                });

    }

}
