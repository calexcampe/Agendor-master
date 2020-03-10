package com.example.agendor_timeline.model;

import com.example.agendor_timeline.interfaces.TimelineObject;

public class TaskConstructor implements TimelineObject {

    public TaskConstructor(long timeline, String name, String url, String descricao, String hora) {
        this.timeline = timeline;
        this.name = name;
        this.url = url;
        this.descricao = descricao;
        this.hora = hora;
    }

    long timeline;
    String name;
    String url;
    String descricao;
    String hora;

    @Override
    public long getTimestamp() {
        return timeline;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return url;
    }

    @Override
    public String getdescricao() {
        return descricao;
    }

    @Override
    public String gethora() {
        return hora;
    }
}
