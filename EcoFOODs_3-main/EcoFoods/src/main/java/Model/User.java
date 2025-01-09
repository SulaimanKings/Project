/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
/**
 *
 * @author A S U S
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String alamat;
    private String nomorTelp;
    private Restaurant restaurant;
    
    public User(String username, String email, String password, String alamat, String nomorTelp) {
        this.id = UUID.randomUUID(); // Membuat UUID secara otomatis
        name = username;
        this.email = email;
        this.password = password;
        this.alamat = alamat;
        this.nomorTelp = nomorTelp;
    }
    
    public String getName() {
        return name;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }


       public String getEmail() {
        return email;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNomorTelp(String nomorTelp) {
        this.nomorTelp = nomorTelp;
    }


    public void setPassword(String password) {
        this.password = password;
    }
    



   public String getAlamat() {
    return alamat;
}
   
   public String getNomorTelp() {
    return nomorTelp;
}

}
