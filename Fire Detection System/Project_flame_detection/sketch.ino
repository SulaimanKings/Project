#include <WiFi.h>
#include <PubSubClient.h>
#define LED_BUILTIN 2 // GPIO untuk LED internal pada ESP32

// Ganti dengan kredensial Wi-Fi Anda
const char* wifi_ssid = "Wokwi-GUEST";
const char* wifi_password = "";

// Konfigurasi broker MQTT
const char* mqtt_host = "broker.hivemq.com"; // Broker publik MQTT
const char* mqtt_channel = "flame_alert";

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

// Fungsi untuk menyambungkan ESP32 ke jaringan Wi-Fi
void connect_to_wifi() {
  Serial.println();
  Serial.println("Menghubungkan ke jaringan Wi-Fi...");

  WiFi.begin(wifi_ssid, wifi_password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("\nWi-Fi terhubung!");
}

// Callback untuk menangani pesan yang diterima dari broker MQTT
void handle_message(char* topic, byte* payload, unsigned int length) {
  Serial.print("Pesan diterima di topik: ");
  Serial.println(topic);

  String message = "";
  for (unsigned int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  Serial.print("Isi pesan: ");
  Serial.println(message);

  if (message == "ON") {
    digitalWrite(LED_BUILTIN, HIGH);
    Serial.println("LED menyala - Api terdeteksi");
  } else if (message == "OFF") {
    digitalWrite(LED_BUILTIN, LOW);
    Serial.println("LED mati - Tidak ada api");
  }
}

// Fungsi untuk memastikan koneksi ke broker MQTT
void connect_to_mqtt() {
  while (!mqttClient.connected()) {
    Serial.println("Menghubungkan ke broker MQTT...");

    if (mqttClient.connect("ESP32_FireSubscriber")) {
      Serial.println("Berhasil terhubung ke broker MQTT!");
      mqttClient.subscribe(mqtt_channel);
    } else {
      Serial.print("Gagal terhubung, rc=");
      Serial.println(mqttClient.state());
      delay(5000);
    }
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(LED_BUILTIN, OUTPUT);

  connect_to_wifi();

  mqttClient.setServer(mqtt_host, 1883);
  mqttClient.setCallback(handle_message);
}

void loop() {
  if (!mqttClient.connected()) {
    connect_to_mqtt();
  }
  mqttClient.loop();

  // Tempatkan logika tambahan jika diperlukan
}
