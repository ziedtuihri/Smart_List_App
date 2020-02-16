package com.example.myapplication;

public class ListProductHistorique {
    private int id;
    private String name;
    private String list;
    private String date;
    private Float price;


    public ListProductHistorique() {
    }

    public ListProductHistorique(int id, String name, String list) {
        this.id=id;
        this.name=name;
        this.list=list;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }



}
