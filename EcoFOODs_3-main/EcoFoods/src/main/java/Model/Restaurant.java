package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Restaurant {
    private UUID id;
    private String address;
    private String nomor;
    private String name;

     public Restaurant(String nama ) {
        this.id = UUID.randomUUID(); // Membuat UUID secara otomatis
        this.name = nama;
    }   

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getNomor() {
        return nomor;
    }

    public String getName() {
        return name;
    }



}