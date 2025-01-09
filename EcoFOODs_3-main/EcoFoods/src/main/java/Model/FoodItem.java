/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;


import java.util.UUID;
import Model.Restaurant;
/**
 *
 * @author A S U S
 */
public class FoodItem {
    private UUID id;
    private String makanan;
    private double price;
    private String namaResto;
    private Restaurant resto;
    private int jumlah;

    public String getNamaResto() {
        return namaResto;
    }
    
    public FoodItem(String makanan, double price,int jumlah, Restaurant resto ) {
        this.id = UUID.randomUUID(); // Membuat UUID secara otomatis
        this.makanan = makanan;
        this.price = price;
        this.jumlah = jumlah;
        this.resto = resto;
    }
    
        public FoodItem(String makanan, double price,int jumlah, String resto ) {
        this.makanan = makanan;
        this.price = price;
        this.jumlah = jumlah;
        this.namaResto = resto;
    }

    public int getJumlah() {
        return jumlah;
    }

    public UUID getId() {
        return id;
    }

    public String getMakanan() {
        return makanan;
    }

    public double getPrice() {
        return price;
    }

    public Restaurant getResto() {
        return resto;
    }
    

    
}
