import cv2
import numpy as np
import os
import matplotlib.pyplot as plt
from tensorflow.keras.models import load_model # type: ignore
from PIL import Image

# Load model (pada awal aplikasi)
model = load_model('breast_cancer_model.h5')

def predict_breast_cancer(image_path):
    """
    Prediksi kanker payudara menggunakan model TensorFlow.
    """
    # Preprocess image (resize, normalize)
    image = Image.open(image_path).convert("RGB").resize((224, 224))
    image_array = np.array(image) / 255.0  # Normalisasi
    image_array = np.expand_dims(image_array, axis=0)  # Tambahkan dimensi batch

    # Prediksi
    prediction = model.predict(image_array)
    class_label = 'Malignant' if prediction[0][0] > 0.5 else 'Benign'
    probability = prediction[0][0] if prediction[0][0] > 0.5 else 1 - prediction[0][0]

    return class_label, probability


def process_edge_detection(image_path):
    """
    Melakukan deteksi tepi pada gambar menggunakan metode Canny.
    """
    # Baca gambar menggunakan OpenCV
    image = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)  # Baca gambar dalam format grayscale
    if image is None:
        raise ValueError(f"Gambar tidak ditemukan di path: {image_path}")

    # Terapkan deteksi tepi Canny
    edges = cv2.Canny(image, 100, 200)

    # Simpan hasil gambar tepi
    edge_image_path = 'edge_detection_result.png'
    cv2.imwrite(edge_image_path, edges)

    return edge_image_path


def diagnose_cancer(self):
    if hasattr(self, 'image_path'):
        # Proses deteksi tepi
        edge_path = process_edge_detection(self.image_path)

        # Proses histogram
        histogram_path = process_histogram(self.image_path)

        # Proses segmentasi
        segmentation_path = process_segmentation(self.image_path)

        # Prediksi kanker payudara
        class_label, probability = predict_breast_cancer(self.image_path)

        # Tampilkan hasil
        self.result_label.setText(f"Hasil Diagnosis: {class_label} (Probabilitas: {probability:.2f})")
    else:
        self.result_label.setText("Silakan pilih gambar terlebih dahulu.")

def process_histogram(image_path):
    # Baca gambar
    image = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    if image is None:
        raise FileNotFoundError(f"File gambar tidak ditemukan di path {image_path}")

    # Hitung histogram
    hist = cv2.calcHist([image], [0], None, [256], [0, 256])

    # Simpan histogram sebagai gambar
    output_path = 'output_histogram.png'
    plt.figure()
    plt.plot(hist)
    plt.title('Histogram')
    plt.savefig(output_path)
    plt.close()

    return output_path

def process_segmentation(image_path):
    # Baca gambar
    image = cv2.imread(image_path)
    if image is None:
        raise FileNotFoundError(f"File gambar tidak ditemukan di path {image_path}")

    # Ubah gambar ke grayscale
    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    # Terapkan thresholding untuk segmentasi
    _, segmented_image = cv2.threshold(gray_image, 127, 255, cv2.THRESH_BINARY)

    # Simpan hasil segmentasi
    output_path = 'output_segmented.png'
    cv2.imwrite(output_path, segmented_image)

    return output_path