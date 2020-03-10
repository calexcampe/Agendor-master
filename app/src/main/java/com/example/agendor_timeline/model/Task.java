package com.example.agendor_timeline.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Task implements Parcelable {

    public Task(Parcel in) {
        categoria = in.readString();
        hora = in.readString();
        cliente = in.readString();
        descricao = in.readString();
        user = in.readString();
        online = in.readInt() == 1;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    private String categoria;
    private Date data;
    private String hora;
    private String cliente;
    private String descricao;
    private String user;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    private boolean online;

    public Task(String categoria, Date data, String hora, String cliente, String descricao, String user) {
        this.categoria = categoria;
        this.data = data;
        this.hora = hora;
        this.cliente = cliente;
        this.descricao = descricao;
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(categoria);
        dest.writeString(String.valueOf(data));
        dest.writeString(hora);
        dest.writeString(descricao);
        dest.writeString(user);
        dest.writeInt(online ? 1 : 0);

    }
}
