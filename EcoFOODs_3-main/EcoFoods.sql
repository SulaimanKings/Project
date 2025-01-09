CREATE TABLE Users (
    UserID VARCHAR(100) PRIMARY KEY,
    Nama VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    KataSandi VARCHAR(50) NOT NULL,
    Alamat VARCHAR(100),
    NomorTelepon VARCHAR(20)
);
CREATE TABLE Restoran (
    RestaurantID VARCHAR(100) PRIMARY KEY,
    NamaRestoran VARCHAR(100) NOT NULL,
    Alamat VARCHAR(100),
    NomorTelepon VARCHAR(20)
);
CREATE TABLE Pemesanan (
    OrderID VARCHAR(100) PRIMARY KEY,
    UserID VARCHAR(100),
    RestaurantID VARCHAR(100),
    TanggalPemesanan DATE,
    StatusPemesanan VARCHAR(50),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (RestaurantID) REFERENCES Restoran(RestaurantID)
);
CREATE TABLE Makanan (
    FoodItemID VARCHAR(100) PRIMARY KEY,
    NamaMakanan VARCHAR(100) NOT NULL,
    Harga DECIMAL(10, 2),
    Jumlah int,
    RestaurantID VARCHAR(100),
    FOREIGN KEY (RestaurantID) REFERENCES Restoran(RestaurantID)
);
CREATE TABLE Cart (
    OrderID VARCHAR(100),
    FoodItemID VARCHAR(100),
    FOREIGN KEY (OrderID) REFERENCES pemesanan(OrderID),
    FOREIGN KEY (FoodItemID) REFERENCES Makanan(FoodItemID)
);