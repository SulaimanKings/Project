/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import DAO.BaseDAO;
import Model.FoodItem;
import Model.Restaurant;
import DAO.FoodDAO;
import com.projectpbo.ecofoods.App;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button home_btn;

    @FXML
    private Button invent_addbtn;

    @FXML
    private Button invent_btn;

    @FXML
    private Button invent_clearbtn;

    @FXML
    private TableColumn<FoodItem, Double> invent_col_harga;

    @FXML
    private TableColumn<FoodItem, Integer> invent_col_jumlah;

    @FXML
    private TableColumn<FoodItem, String> invent_col_namaMakanan;

    @FXML
    private TableColumn<FoodItem, String> invent_col_rest;

    @FXML
    private Button invent_deletebtn;

    @FXML
    private AnchorPane invent_form;

    @FXML
    private TextField invent_harga;

    @FXML
    private ImageView invent_imgView;

    @FXML
    private Button invent_importbtn;

    @FXML
    private TextField invent_jumlah;

    @FXML
    private TextField invent_namaMakanan;

    @FXML
    private TextField invent_rest;

    @FXML
    private TableView<FoodItem> invent_tblView;

    @FXML
    private Button invent_updatebtn;

    @FXML
    private ImageView logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField rest_bayar;

    @FXML
    private Button rest_bayarBtn;

    @FXML
    private Button rest_btn;

    @FXML
    private TableColumn<?, ?> rest_col_harga;

    @FXML
    private TableColumn<?, ?> rest_col_jumlah;

    @FXML
    private TableColumn<?, ?> rest_col_namaProuk;

    @FXML
    private AnchorPane rest_form;

    @FXML
    private GridPane rest_gridPane;

    @FXML
    private Button rest_hapusBtn;

    @FXML
    private Label rest_kembalian;

    @FXML
    private Button rest_notaBtn;

    @FXML
    private ScrollPane rest_scrollPane;

    @FXML
    private TableView<?> rest_tableView;

    @FXML
    private Label rest_total;

    @FXML
    private Button rst1_btn;

    @FXML
    private Button rst2_btn;

    @FXML
    private Button rst3_btn;

    @FXML
    private Button rst4_btn;

    private Alert alert;

    private BaseDAO baseDAO;

    public ObservableList<FoodItem> getFoodItemList() {
        ObservableList<FoodItem> foodItemList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM makanan";

        Connection connection = baseDAO.getCon();

        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    FoodItem foodItem = new FoodItem(
                            resultSet.getString("NamaMakanan"),
                            resultSet.getDouble("Harga"),
                            resultSet.getInt("Jumlah"),
                            resultSet.getString("RestaurantID")
                    );

                    foodItemList.add(foodItem);

                    // Tambahkan pesan untuk melacak setiap item yang dimasukkan ke dalam list
                    System.out.println("Item ditambahkan: " + foodItem.getMakanan());
                    System.out.println("Item ditambahkan: " + foodItem.getPrice());
                    System.out.println("Item ditambahkan: " + foodItem.getJumlah());
                    System.out.println("Item ditambahkan: " + foodItem.getNamaResto());

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return foodItemList;
    }

// Untuk menampilkan data pada tabel
    private ObservableList<FoodItem> foodItemList;

    public void showFoodData() {
        foodItemList = getFoodItemList();

        // Sesuaikan dengan ID dari kolom di tabel Anda
        // Misalnya, inventory_col_foodID, inventory_col_foodName, dan sebagainya
        invent_col_rest.setCellValueFactory(new PropertyValueFactory<>("namaResto"));
        invent_col_namaMakanan.setCellValueFactory(new PropertyValueFactory<>("makanan"));
        invent_col_harga.setCellValueFactory(new PropertyValueFactory<>("price"));
        invent_col_jumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        invent_tblView.setItems(foodItemList);
    }


    public void addInvent() throws SQLException {
        if (invent_rest.getText().isEmpty() || invent_namaMakanan.getText().isEmpty() || invent_harga.getText().isEmpty() || invent_jumlah.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Pesan Error");
            alert.setHeaderText(null);
            alert.setContentText("Tolong isi seluruh data!!");
            alert.showAndWait();
        } else {
            String temp = invent_harga.getText();
            String temp1 = invent_jumlah.getText();
            double harga = Double.parseDouble(temp);
            int jumlah = Integer.parseInt(temp1);
            Restaurant resto = new Restaurant(invent_rest.getText());
            FoodItem food = new FoodItem(invent_namaMakanan.getText(), harga, jumlah, resto);
            FoodDAO fd = new FoodDAO();
            fd.insertFood(food, resto);

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("add sukses");
            alert.setHeaderText(null);
            alert.setContentText("makanan sudah terdata");
            alert.showAndWait();
        }
    }

    public void updateInvent() throws SQLException {
        if (invent_rest.getText().isEmpty() || invent_namaMakanan.getText().isEmpty() || invent_harga.getText().isEmpty() || invent_jumlah.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Pesan Error");
            alert.setHeaderText(null);
            alert.setContentText("Tolong isi seluruh data!!");
            alert.showAndWait();
        } else {
            String temp = invent_harga.getText();
            String temp1 = invent_jumlah.getText();
            double harga = Double.parseDouble(temp);
            int jumlah = Integer.parseInt(temp1);
            Restaurant resto = new Restaurant(invent_rest.getText());
            FoodItem food = new FoodItem(invent_namaMakanan.getText(), harga, jumlah, resto);
            FoodDAO fd = new FoodDAO();
            fd.updateFood(food);

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("update sukses");
            alert.setHeaderText(null);
            alert.setContentText("makanan sudah diubah");
            alert.showAndWait();
        }
    }

    public void deleteInvent() throws SQLException {
        if (invent_namaMakanan.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Pesan Error");
            alert.setHeaderText(null);
            alert.setContentText("Tolong isi nama untuk pencarian!!");
            alert.showAndWait();
        } else {
            FoodDAO fd = new FoodDAO();
            fd.deleteFood(invent_namaMakanan.getText());

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("hapus sukses");
            alert.setHeaderText(null);
            alert.setContentText("makanan sudah dihapus");
            alert.showAndWait();
        }
    }

    public void clearInvent() throws SQLException {

        FoodDAO fd = new FoodDAO();
        fd.deleteAllFoods();

        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("hapus sukses");
        alert.setHeaderText(null);
        alert.setContentText("semua makanan sudah dihapus");
        alert.showAndWait();
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            dashboard_form.setVisible(true);
            invent_form.setVisible(false);
            rest_form.setVisible(false);
            //customers_form.setVisible(false);

            //dashboardDisplayNC();
            //dashboardDisplayTI();
            //dashboardTotalI();
            //dashboardNSP();
            //dashboardIncomeChart();
            //dashboardCustomerChart();
        } else if (event.getSource() == invent_btn) {
            dashboard_form.setVisible(false);
            invent_form.setVisible(true);
            rest_form.setVisible(false);
            //customers_form.setVisible(false);

            //inventoryTypeList();
            //inventoryStatusList();
            //inventoryShowData();
        } else if (event.getSource() == rest_btn) {
            dashboard_form.setVisible(false);
            invent_form.setVisible(false);
            rest_form.setVisible(true);
            //customers_form.setVisible(false);

            //menuDisplayCard();
            //menuDisplayTotal();
            //menuShowOrderData();
            //} else if (event.getSource() == customers_btn) {
            //dashboard_form.setVisible(false);
            //inventory_form.setVisible(false);
            //menu_form.setVisible(false);
            //customers_form.setVisible(true);
            //customersShowData();
        } else if (event.getSource() == rst1_btn) {
            dashboard_form.setVisible(false);
            invent_form.setVisible(false);
            rest_form.setVisible(true);
        } else if (event.getSource() == rst2_btn) {
            dashboard_form.setVisible(false);
            invent_form.setVisible(false);
            rest_form.setVisible(true);
        } else if (event.getSource() == rst3_btn) {
            dashboard_form.setVisible(false);
            invent_form.setVisible(false);
            rest_form.setVisible(true);
        } else if (event.getSource() == rst4_btn) {
            dashboard_form.setVisible(false);
            invent_form.setVisible(false);
            rest_form.setVisible(true);
        }

    }

    public void logout() {
        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Log out");
            alert.setHeaderText(null);
            alert.setContentText("Ingin Logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                logout_btn.getScene().getWindow().hide();

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 320, 240);
                stage.setTitle("EcoFoods");
                stage.setMinHeight(700);
                stage.setMinWidth(1100);
                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseDAO = new BaseDAO(); // Inisialisasi objek baseDAO
        showFoodData(); // Panggil metode showFoodData saat aplikasi dimulai
    }
}
