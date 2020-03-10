package com.example.agendor_timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.agendor_timeline.tasks.NewTask;

public class ViewTask extends AppCompatActivity {

    private TextView textV_hora;
    private TextView textV_descricao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        textV_descricao = findViewById(R.id.textV_descricaotask);
        textV_hora = findViewById(R.id.textV_timetask);

        Bundle bundle = getIntent().getExtras();
        textV_hora.setText(bundle.getString("hora"));
        textV_descricao.setText(bundle.getString("descricao"));
    }

    @Override
    public void onBackPressed(){
       finish();
    }
}
