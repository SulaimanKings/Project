module com.projectpbo.ecofoods {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.logging;

  opens Controller; // Buka paket Controller agar dapat diakses dari luar modul

  exports com.projectpbo.ecofoods to javafx.graphics; // Ekspor paket ke modul javafx.graphics
}
    
