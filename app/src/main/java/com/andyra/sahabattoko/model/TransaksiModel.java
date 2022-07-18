package com.andyra.sahabattoko.model;

import java.util.Date;

public class TransaksiModel {
    public Date Tgl_beli;
    public String Nama_barang, Kategori;
    public int Jumlah, Hrg_Beli_PerItem, Hrg_jual_PerItem;

    public  TransaksiModel(){

    }
    public TransaksiModel(Date tgl_beli, String Nama_barang, String kategori,
                          Integer jumlah, Integer biaya_beli_satuan, Integer harga_jual_satuan){
        this.Tgl_beli = tgl_beli;
        this.Nama_barang = Nama_barang;
        this.Kategori = kategori;
        this.Jumlah = jumlah;
        this.Hrg_Beli_PerItem = biaya_beli_satuan;
        this.Hrg_jual_PerItem = harga_jual_satuan;
    }
}
