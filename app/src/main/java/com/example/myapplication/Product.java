package com.example.myapplication;


public class Product {
    private int _id;
    private String _productname;
    private int prix;


    private String code;


    public Product() {

    }

    public Product(int id, String productname, int prix) {
        this._id = id;
        this._productname = productname;
        this.prix = prix;
    }
    public Product( String productname, int pri, String code) {
        this._productname = productname;
        this.prix = prix;
        this.code =code;
    }

    public Product(String productname, int prix) {
        this._productname = productname;
        this.prix = prix;
    }


    public Product(int _id, String _productname, int prix, float quantite, String unite) {
        this._id = _id;
        this._productname = _productname;
        this.prix = prix;
        this.quantite = quantite;
        this.unite = unite;
    }

    private float quantite ;
    private String unite ;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_productname() {
        return _productname;
    }

    public void set_productname(String _productname) {
        this._productname = _productname;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public float getQuantite() {
        return quantite;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}


