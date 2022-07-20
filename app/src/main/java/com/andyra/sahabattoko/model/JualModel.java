package com.andyra.sahabattoko.model;

import java.util.Date;

public class JualModel {
    public Date Tgl_beli;
    public String Nama_barang, Kategori;
    public int Jumlah, Hrg_Satuan, Total_Harga;

    public JualModel(){

    }
    public JualModel(Date tgl_beli, String Nama_barang, String kategori,
                     Integer jumlah, Integer harga_satuan, Integer total_harga){
        this.Tgl_beli = tgl_beli;
        this.Nama_barang = Nama_barang;
        this.Kategori = kategori;
        this.Jumlah = jumlah;
        this.Hrg_Satuan = harga_satuan;
        this.Total_Harga = total_harga;

    }
}
