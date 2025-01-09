import cv2
import numpy as np
import paho.mqtt.client as mqtt

# Konfigurasi MQTT
mqtt_broker = "broker.hivemq.com"  # Anda dapat mengganti ini sesuai kebutuhan
mqtt_port = 1883  # Port standar untuk MQTT
mqtt_topic = "emergencylamp"  # Topik untuk mengirimkan status

# Inisialisasi client MQTT
mqtt_client = mqtt.Client()

# Callback saat terhubung ke broker MQTT
def mqtt_on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("MQTT Connected Successfully")
    else:
        print(f"MQTT Connection Failed with code {rc}")

mqtt_client.on_connect = mqtt_on_connect
mqtt_client.connect(mqtt_broker, mqtt_port, 60)

# Fungsi untuk mendeteksi api menggunakan kamera secara real-time
def realtime_fire_detection():
    # Membuka akses ke kamera
    video_capture = cv2.VideoCapture(0)

    if not video_capture.isOpened():
        print("Kamera tidak dapat diakses")
        return

    while True:
        # Membaca frame dari kamera
        success, frame = video_capture.read()
        if not success:
            print("Gagal membaca frame dari kamera")
            break

        # Mengubah frame ke format HSV
        hsv_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

        # Definisi rentang warna untuk mendeteksi api
        fire_lower_range = np.array([10, 100, 100])  # Warna jingga-kuning
        fire_upper_range = np.array([25, 255, 255])  # Warna api terang

        # Membuat mask berdasarkan warna api
        fire_mask = cv2.inRange(hsv_frame, fire_lower_range, fire_upper_range)

        # Mencari kontur dalam area yang terdeteksi
        contours, _ = cv2.findContours(fire_mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        fire_detected = False

        for contour in contours:
            if cv2.contourArea(contour) > 500:  # Abaikan kontur kecil
                x, y, width, height = cv2.boundingRect(contour)
                cv2.rectangle(frame, (x, y), (x + width, y + height), (0, 0, 255), 2)
                cv2.putText(frame, "Fire Detected", (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)
                fire_detected = True

        # Kirimkan status melalui MQTT
        if fire_detected:
            print("Fire Detected")
            mqtt_client.publish(mqtt_topic, "ON")
        else:
            print("No Fire Detected")
            mqtt_client.publish(mqtt_topic, "OFF")

        # Menampilkan hasil deteksi
        cv2.imshow("Frame", frame)
        cv2.imshow("Fire Mask", fire_mask)

        # Tombol keluar ('q')
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Melepaskan sumber daya
    video_capture.release()
    cv2.destroyAllWindows()

# Memulai loop MQTT
mqtt_client.loop_start()

# Menjalankan deteksi api
realtime_fire_detection()

# Menghentikan loop MQTT
mqtt_client.loop_stop()
