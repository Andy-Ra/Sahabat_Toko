package com.andyra.sahabattoko.model;

public class ListBarangModel {
    public String Nama_barang, Kategori;
    public int Stok;

    public  ListBarangModel(){

    }
    public ListBarangModel(String nama, String kategori, int jumlah){
        this.Nama_barang = nama;
        this.Kategori = kategori;
        this.Stok = jumlah;
    }
}
