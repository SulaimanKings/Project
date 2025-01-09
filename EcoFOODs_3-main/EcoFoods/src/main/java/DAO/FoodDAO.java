/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.FoodItem;
import Model.Restaurant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author sakhron isyama h
 */
public class FoodDAO {
    
    
    private BaseDAO baseDAO;

    public FoodDAO() {
        baseDAO = new BaseDAO();
    }

public void insertFood(FoodItem food, Restaurant resto) {
    Connection connection = baseDAO.getCon();

    if (connection != null) {
        try {
            // Check if the restaurant exists
            String checkRestaurantQuery = "SELECT * FROM Restoran WHERE NamaRestoran = ?";
            PreparedStatement checkRestaurantStatement = connection.prepareStatement(checkRestaurantQuery);
            checkRestaurantStatement.setString(1, resto.getName());

            ResultSet restaurantResult = checkRestaurantStatement.executeQuery();

            if (restaurantResult.next()) {
                // Restaurant exists, perform food insertion
                String insertFoodQuery = "INSERT INTO makanan (FoodItemID, NamaMakanan, Harga, Jumlah, RestaurantID) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertFoodStatement = connection.prepareStatement(insertFoodQuery);

                insertFoodStatement.setString(1, food.getId().toString());
                insertFoodStatement.setString(2, food.getMakanan());
                insertFoodStatement.setDouble(3, food.getPrice());
                insertFoodStatement.setInt(4, food.getJumlah());
                insertFoodStatement.setString(5, restaurantResult.getString("RestaurantID"));

                int rowsInserted = insertFoodStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Registrasi berhasil! Data telah ditambahkan ke dalam tabel makanan.");
                }
            } else {
                // Restaurant does not exist, insert the restaurant first
                String insertRestaurantQuery = "INSERT INTO Restoran (RestaurantID, NamaRestoran) VALUES (?, ?)";
                PreparedStatement insertRestaurantStatement = connection.prepareStatement(insertRestaurantQuery, Statement.RETURN_GENERATED_KEYS);

                insertRestaurantStatement.setString(1, resto.getId().toString());
                insertRestaurantStatement.setString(2, resto.getName());

                int rowsInserted = insertRestaurantStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Restoran baru telah ditambahkan.");

                    // Retrieve the generated RestaurantID
                    ResultSet generatedKeys = insertRestaurantStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        String restaurantID = generatedKeys.getString(1);

                        // Perform food insertion for the new restaurant
                        String insertFoodQueryNewRestaurant = "INSERT INTO makanan (FoodItemID, NamaMakanan, Harga, Jumlah, RestaurantID) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement insertFoodStatementNewRestaurant = connection.prepareStatement(insertFoodQueryNewRestaurant);

                        insertFoodStatementNewRestaurant.setString(1, food.getId().toString());
                        insertFoodStatementNewRestaurant.setString(2, food.getMakanan());
                        insertFoodStatementNewRestaurant.setDouble(3, food.getPrice());
                        insertFoodStatementNewRestaurant.setInt(4, food.getJumlah());
                        insertFoodStatementNewRestaurant.setString(5, restaurantID);

                        int rowsInsertedFood = insertFoodStatementNewRestaurant.executeUpdate();
                        if (rowsInsertedFood > 0) {
                            System.out.println("Registrasi berhasil! Data telah ditambahkan ke dalam tabel makanan.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Tidak dapat menutup koneksi: " + e.getMessage());
            }
        }
    } else {
        System.out.println("Koneksi ke database tidak tersedia.");
    }
}

public void updateFood(FoodItem updatedFood) {
 Connection connection = baseDAO.getCon();

    if (connection != null) {
        try {
            // Check if the food item exists based on its name
            String checkFoodQuery = "SELECT * FROM makanan WHERE NamaMakanan = ?";
            PreparedStatement checkFoodStatement = connection.prepareStatement(checkFoodQuery);
            checkFoodStatement.setString(1, updatedFood.getMakanan());

            ResultSet foodResult = checkFoodStatement.executeQuery();
            boolean found = false;

            while (foodResult.next()) {
                found = true;
                // Food item found, perform update
                String updateFoodQuery = "UPDATE makanan SET Harga = ?, Jumlah = ? WHERE FoodItemID = ?";
                PreparedStatement updateFoodStatement = connection.prepareStatement(updateFoodQuery);

                updateFoodStatement.setDouble(1, updatedFood.getPrice());
                updateFoodStatement.setInt(2, updatedFood.getJumlah());
                updateFoodStatement.setString(3, foodResult.getString("FoodItemID"));

                int rowsUpdated = updateFoodStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Update berhasil! Data makanan dengan nama " + updatedFood.getMakanan() + " telah diperbarui.");
                }
            }

            if (!found) {
                System.out.println("Makanan dengan nama tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Tidak dapat menutup koneksi: " + e.getMessage());
            }
        }
    } else {
        System.out.println("Koneksi ke database tidak tersedia.");
    }
}

public void deleteFood(String foodName) {
    Connection connection = baseDAO.getCon();

    if (connection != null) {
        try {
            // Check if the food item exists based on its name
            String checkFoodQuery = "SELECT * FROM makanan WHERE NamaMakanan = ?";
            PreparedStatement checkFoodStatement = connection.prepareStatement(checkFoodQuery);
            checkFoodStatement.setString(1, foodName);

            ResultSet foodResult = checkFoodStatement.executeQuery();
            boolean found = false;

            while (foodResult.next()) {
                found = true;
                // Food item found, perform deletion
                String deleteFoodQuery = "DELETE FROM makanan WHERE FoodItemID = ?";
                PreparedStatement deleteFoodStatement = connection.prepareStatement(deleteFoodQuery);

                deleteFoodStatement.setString(1, foodResult.getString("FoodItemID"));

                int rowsDeleted = deleteFoodStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Penghapusan berhasil! Data makanan dengan nama " + foodName + " telah dihapus.");
                }
            }

            if (!found) {
                System.out.println("Makanan dengan nama tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Tidak dapat menutup koneksi: " + e.getMessage());
            }
        }
    } else {
        System.out.println("Koneksi ke database tidak tersedia.");
    }
}

public void deleteAllFoods() {
    Connection connection = baseDAO.getCon();

    if (connection != null) {
        try {
            // Delete all records from the makanan table
            String deleteAllFoodsQuery = "DELETE FROM makanan";
            PreparedStatement deleteAllFoodsStatement = connection.prepareStatement(deleteAllFoodsQuery);

            int rowsDeleted = deleteAllFoodsStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Penghapusan berhasil! Semua data dari tabel makanan telah dihapus.");
            } else {
                System.out.println("Tabel makanan sudah kosong.");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Tidak dapat menutup koneksi: " + e.getMessage());
            }
        }
    } else {
        System.out.println("Koneksi ke database tidak tersedia.");
    }
}




}
