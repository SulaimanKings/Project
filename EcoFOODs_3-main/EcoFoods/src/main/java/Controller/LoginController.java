/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import DAO.UserDAO;
import Model.User;
import com.projectpbo.ecofoods.App;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private Button goto_si;

    @FXML
    private Button goto_su;

    @FXML
    private Button si_bttn;

    @FXML
    private AnchorPane si_form;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_alamat;

    @FXML
    private Button su_bttn;

    @FXML
    private TextField su_email;

    @FXML
    private AnchorPane su_form;

    @FXML
    private TextField su_nama;

    @FXML
    private TextField su_notelp;

    @FXML
    private PasswordField su_pass;
    
    private Connection connect;
    private PreparedStatement prepare;
     private ResultSet result;
     
     private Alert alert;
     
     //ini controller untuk sign up 
     public void subtn(){
         TranslateTransition slider = new TranslateTransition();
         if (su_nama.getText().isEmpty() || su_email.getText().isEmpty()||su_pass.getText().isEmpty()||su_alamat.getText().isEmpty()||su_notelp.getText().isEmpty()){
             alert= new Alert(AlertType.ERROR);
             alert.setTitle("Pesan Error");
             alert.setHeaderText(null);
             alert.setContentText("Tolong isi seluruh data!!");
             alert.showAndWait();
         } else{
           User u = new User(su_nama.getText(),su_email.getText(),su_pass.getText(),su_alamat.getText(),su_notelp.getText());
           UserDAO ud = new UserDAO();
           ud.insertUser(u);
           alert= new Alert(AlertType.INFORMATION);
             alert.setTitle("sign up sukses");
             alert.setHeaderText(null);
             alert.setContentText("silahkan login kembali");
             alert.showAndWait();
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                goto_si.setVisible(false);
                goto_su.setVisible(true);
            });
            slider.play();
         }
     }
//ini controller untuk sign in
public void sibtn() throws IOException {
    if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Pesan Error");
        alert.setHeaderText(null);
        alert.setContentText("Tolong isi username dan password");
        alert.showAndWait();
    } else {
        UserDAO ud = new UserDAO();
        User signIn = ud.signIn(si_username.getText(), si_password.getText());
        
        if (signIn != null) {
            // Berhasil login
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("sign in sukses");
            alert.setHeaderText(null);
            alert.setContentText("Selamat datang " + signIn.getName());
            alert.showAndWait();
            
                
                si_bttn.getScene().getWindow().hide();
                
                Stage stage = new Stage();
                
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("EcoFoods");
        stage.setMinHeight(700);
        stage.setMinWidth(1100);
        stage.setScene(scene);
        stage.show();

                
        } else {
            // Gagal login, tampilkan pesan kesalahan
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("sign in gagal");
            alert.setHeaderText(null);
            alert.setContentText("username dan Password salah. coba lagi");
            alert.showAndWait();
        }
    }
}

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == goto_su) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                goto_si.setVisible(true);
                goto_su.setVisible(false);
            });
            slider.play();
        } else if (event.getSource() == goto_si) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                goto_si.setVisible(false);
                goto_su.setVisible(true);
            });
            slider.play();
        }
    }
}
