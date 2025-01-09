package DAO;

import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private BaseDAO baseDAO;

    public UserDAO() {
        baseDAO = new BaseDAO();
    }

    public void insertUser(User user) {
        Connection connection = baseDAO.getCon();
        if (connection != null) {
            String query = "INSERT INTO Users (UserID, Nama, Email, KataSandi, Alamat, NomorTelepon) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, user.getId().toString());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setString(5, user.getAlamat());
                preparedStatement.setString(6, user.getNomorTelp());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Registrasi berhasil! Data pengguna telah ditambahkan ke dalam tabel Users.");
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

    public User signIn(String nama, String password) {
    Connection connection = baseDAO.getCon();
    User user = null;

    if (connection != null) {
        String query = "SELECT * FROM Users WHERE Nama = ? AND KataSandi = ?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // If the credentials match, create a User object
                user = new User(
                    resultSet.getString("Nama"),
                    resultSet.getString("Email"),
                    resultSet.getString("KataSandi"),
                    resultSet.getString("Alamat"),
                    resultSet.getString("NomorTelepon")
                );
            } else {
                // If no matching credentials found, set user to null
                user = null;
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

    return user;
}


}
