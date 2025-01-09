from PyQt5.QtWidgets import QApplication, QFileDialog, QLabel, QVBoxLayout, QScrollArea, QWidget, QPushButton, QComboBox, QGridLayout
from PyQt5.QtGui import QPixmap
from PyQt5.QtCore import Qt
from modules.processing import process_edge_detection, process_histogram, process_segmentation, predict_breast_cancer

class MainWindow(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Sistem Diagnosis Gambar")
        self.setStyleSheet("background-color: #EAF9EA; font-family: Arial; font-size: 14px;")

        # Layout utama dengan scroll area
        self.scroll_area = QScrollArea()
        self.scroll_area.setWidgetResizable(True)
        self.main_widget = QWidget()
        self.layout = QVBoxLayout(self.main_widget)

        # Header: Label judul
        self.title_label = QLabel("Sistem Diagnosis Kanker Payudara")
        self.title_label.setStyleSheet("font-size: 20px; font-weight: bold; color: #2E7D32;")
        self.title_label.setAlignment(Qt.AlignCenter)
        self.layout.addWidget(self.title_label)

        # Bagian untuk memilih gambar
        self.image_label = QLabel("Gambar akan muncul di sini")
        self.image_label.setFixedSize(600, 450)
        self.image_label.setStyleSheet("border: 2px dashed #A5D6A7; color: #66BB6A;")
        self.image_label.setAlignment(Qt.AlignCenter)
        self.layout.addWidget(self.image_label, alignment=Qt.AlignCenter)

        self.load_button = QPushButton("Pilih Gambar")
        self.load_button.setStyleSheet("padding: 8px; background-color: #81C784; color: white; border-radius: 5px;")
        self.load_button.clicked.connect(self.load_image)
        self.layout.addWidget(self.load_button, alignment=Qt.AlignCenter)

        # Bagian tombol untuk proses gambar
        self.process_layout = QGridLayout()

        # Dropdown metode deteksi tepi
        self.edge_combo = QComboBox()
        self.edge_combo.addItems(["Canny", "Metode Lain"])
        self.edge_combo.setStyleSheet("padding: 5px; border: 1px solid #A5D6A7; background-color: #C8E6C9;")
        self.process_layout.addWidget(QLabel("Metode Deteksi Tepi:"), 0, 0)
        self.process_layout.addWidget(self.edge_combo, 0, 1)

        self.diagnose_button = QPushButton("Diagnosis Kanker Payudara")
        self.diagnose_button.setStyleSheet("padding: 8px; background-color: #388E3C; color: white; border-radius: 5px;")
        self.diagnose_button.clicked.connect(self.diagnose_cancer)
        self.process_layout.addWidget(self.diagnose_button, 1, 0, 1, 2)

        self.edge_button = QPushButton("Deteksi Tepi")
        self.edge_button.setStyleSheet("padding: 8px; background-color: #4CAF50; color: white; border-radius: 5px;")
        self.edge_button.clicked.connect(self.detect_edges)
        self.process_layout.addWidget(self.edge_button, 2, 0)

        self.histogram_button = QPushButton("Tampilkan Histogram")
        self.histogram_button.setStyleSheet("padding: 8px; background-color: #66BB6A; color: white; border-radius: 5px;")
        self.histogram_button.clicked.connect(self.show_histograms)
        self.process_layout.addWidget(self.histogram_button, 2, 1)

        self.segment_button = QPushButton("Segmentasi Gambar")
        self.segment_button.setStyleSheet("padding: 8px; background-color: #81C784; color: white; border-radius: 5px;")
        self.segment_button.clicked.connect(self.segment_images)
        self.process_layout.addWidget(self.segment_button, 3, 0, 1, 2)

        self.layout.addLayout(self.process_layout)

        # Label untuk hasil diagnosis
        self.result_label = QLabel("Hasil Diagnosis:")
        self.result_label.setStyleSheet("font-size: 14px; font-weight: bold; color: #2E7D32; margin-top: 10px;")
        self.layout.addWidget(self.result_label)

        # Tempat untuk hasil proses gambar, ditata vertikal
        self.result_images_layout = QVBoxLayout()
        self.result_images_layout.setSpacing(20)
        self.result_images_layout.setAlignment(Qt.AlignCenter)

        self.after_labels = []
        for _ in range(3):  # Untuk hasil edge, histogram, segmentasi
            label = QLabel()
            label.setFixedSize(600, 450)
            label.setStyleSheet("border: 2px solid #A5D6A7; background-color: #E8F5E9; border-radius: 5px;")
            label.setAlignment(Qt.AlignCenter)
            self.result_images_layout.addWidget(label)
            self.after_labels.append(label)

        self.layout.addLayout(self.result_images_layout)

        self.scroll_area.setWidget(self.main_widget)
        self.main_layout = QVBoxLayout(self)
        self.main_layout.addWidget(self.scroll_area)
        self.setLayout(self.main_layout)

    def load_image(self):
        file_path, _ = QFileDialog.getOpenFileName(self, "Pilih Gambar", "", "Image Files (*.png *.jpg *.jpeg)")
        if file_path:
            pixmap = QPixmap(file_path).scaled(600, 450, Qt.KeepAspectRatio)
            self.image_label.setPixmap(pixmap)
            self.image_path = file_path

    def diagnose_cancer(self):
        if hasattr(self, 'image_path'):
            class_label, probability = predict_breast_cancer(self.image_path)
            self.result_label.setText(f"Hasil Diagnosis: {class_label} (Probabilitas: {probability:.2f})")
        else:
            self.result_label.setText("Silakan pilih gambar terlebih dahulu.")

    def detect_edges(self):
        if hasattr(self, 'image_path'):
            edge_path = process_edge_detection(self.image_path)
            pixmap = QPixmap(edge_path).scaled(600, 450, Qt.KeepAspectRatio)
            self.after_labels[0].setPixmap(pixmap)

    def show_histograms(self):
        if hasattr(self, 'image_path'):
            histogram_path = process_histogram(self.image_path)
            pixmap = QPixmap(histogram_path).scaled(600, 450, Qt.KeepAspectRatio)
            self.after_labels[1].setPixmap(pixmap)

    def segment_images(self):
        if hasattr(self, 'image_path'):
            segmentation_path = process_segmentation(self.image_path)
            pixmap = QPixmap(segmentation_path).scaled(600, 450, Qt.KeepAspectRatio)
            self.after_labels[2].setPixmap(pixmap)

if __name__ == "__main__":
    import sys
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())
