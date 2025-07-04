# 💱 Connect Exchange - Application Temps Réel avec Kafka, Elasticsearch & Kibana

## 📌 Contexte

Dans une entreprise où de nombreuses équipes consomment des données de taux de change via une API externe (facturée), cette application agit comme un **proxy centralisé**.
Elle publie les taux en temps réel via **Kafka**, les stocke dans **Elasticsearch**, et les visualise dynamiquement dans **Kibana**.

---

## 🏗️ Architecture

```plaintext
com.learn.kafka
├── consumer           → Kafka consumer pour consommer les taux
├── model              → Modèle ExchangeModel indexé dans Elasticsearch
├── producer           → Kafka producer pour publier les messages
├── repository         → Interface Spring Data pour Elasticsearch
├── service            → Service métier pour appel API + tâches programmées
├── KafkaApplication   → Classe principale Spring Boot
├── ProducerController → Endpoint HTTP pour tester manuellement
```

```plaintext
+---------------------+
|  ExchangeService    | <-- Récupère les taux toutes les minutes (API externe)
+---------------------+
            |
+---------------------+
| MessageProducer     | <-- Publie dans Kafka (topic: mon-tunnel-topic)
+---------------------+
            |
+---------------------+
| Kafka (Docker)      |
+---------------------+
            |
+---------------------+
| MessageConsumer     | <-- Consomme les messages Kafka
+---------------------+
            |
+---------------------+
| ExchangeRepository  | <-- Persiste les données dans Elasticsearch
+---------------------+
            |
+---------------------+
| Kibana Dashboard    | <-- Visualise les taux (EUR, USD, etc.)
+---------------------+
```

---

## ⚙️ Technologies

* **Java 17**
* **Spring Boot**
* **Apache Kafka**
* **Spring Data Elasticsearch**
* **Elasticsearch 8+**
* **Kibana**
* **Docker / Docker Compose**

---

## 📀 Arborescence du code

```
com.learn.kafka
├── consumer           → Kafka consumer pour consommer les taux
├── model              → Modèle ExchangeModel indexé dans Elasticsearch
├── producer           → Kafka producer pour publier les messages
├── repository         → Interface Spring Data pour Elasticsearch
├── service            → Service métier pour appel API + tâches programmées
├── KafkaApplication   → Classe principale Spring Boot
├── ProducerController → Endpoint HTTP pour tester manuellement
```

---

## 🚀 Lancement du projet

### 1. Lancer les conteneurs

```bash
docker-compose up -d
```

Cela démarre :

* Elasticsearch sur `http://localhost:9200`
* Kibana sur `http://localhost:5601`

---

### 2. Créer l’index dans Kibana

Accéder à Kibana > **Dev Tools** :

```http
PUT /exchange-live
```

Vérifier avec :

```http
GET _cat/indices?v
```

---

### 3. Démarrer l’application Spring Boot

```bash
./mvnw spring-boot:run
```

⏱️ Les taux seront publiés automatiquement toutes les **60 secondes**.

---

### 4. Tester avec Postman (optionnel)

```http
POST http://localhost:8080/connect-exchange?content=USD
```

---

## 📊 Visualiser dans Kibana

* Aller dans **Discover** → créer un **index pattern** : `exchange-live*`
* Accéder à **Dashboard**

    * Importer le fichier `kibana/dashboard.ndjson` si présent
* Visualiser les variations de taux (EUR, USD, etc.)

---

## 🧐 Fonctionnalités

* 📆 Récupération automatique des taux (API ExchangeRate)
* 🔄 Simulation de variations pour affichage dynamique
* 📨 Publication en Kafka
* 📅 Persistance dans Elasticsearch
* 📊 Dashboard temps réel avec Kibana
* 🔀 Programmation automatique via `@Scheduled`

---

## 🧰 Idées d’améliorations possibles

* Ajout de statistiques (volatilé, min/max)
* Affichage de tendances (hausse/baisse)
* Endpoint REST public pour exposer les derniers taux
* Authentification sur les endpoints REST
* Nettoyage/rotation des données dans Elasticsearch

---

## 👨‍💼 Auteur

Projet IRIS M1 — Big Data
Développé par **Darino29**

