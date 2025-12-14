# 📋 Résumé des Fonctionnalités Backend - MedBot Intelligence

## 🎯 Vue d'ensemble

Le backend MedBot est une API REST Spring Boot qui fournit des services pour la gestion de documents médicaux, l'intelligence artificielle, et l'analyse de données cliniques.

**Base URL** : `http://localhost:8080/api/v1`

---

## 🔧 1. Health Check

### Endpoint
- **GET** `/api/v1/health`

### Description
Vérifie l'état de santé du backend.

### Réponse
```json
{
  "status": "UP",
  "service": "MedBot Backend"
}
```

---

## 📄 2. Gestion des Documents

### Endpoints

#### 2.1 Upload Document
- **POST** `/api/v1/documents/upload`
- **Body** : `multipart/form-data`
  - `file` : Fichier (PDF, DOCX, TXT, HL7, JSON, XML)
  - `patient_id` : ID du patient (optionnel)
  - `document_type` : Type de document (optionnel)
  - `author` : Auteur (optionnel)

#### 2.2 Liste des Documents
- **GET** `/api/v1/documents?page=0&size=20`
- **Query Parameters** :
  - `page` : Numéro de page (défaut: 0)
  - `size` : Taille de la page (défaut: 20)

#### 2.3 Document par ID
- **GET** `/api/v1/documents/{id}`
- **Path Parameter** : `id` (UUID)

#### 2.4 Documents par Patient
- **GET** `/api/v1/documents/patient/{patientId}?page=0&size=20`
- **Path Parameter** : `patientId`
- **Query Parameters** : `page`, `size`

#### 2.5 Mise à jour Document
- **PATCH** `/api/v1/documents/{id}`
- **Body** : JSON avec les métadonnées à mettre à jour

#### 2.6 Suppression Document
- **DELETE** `/api/v1/documents/{id}`

### Fonctionnalités
- ✅ Upload de fichiers (PDF, DOCX, TXT, HL7, FHIR)
- ✅ Extraction de texte automatique
- ✅ Extraction de métadonnées (auteur, date, type)
- ✅ Stockage sécurisé des fichiers
- ✅ Pagination des résultats
- ✅ Recherche par patient

---

## 🔒 3. Anonymisation (De-identification)

### Endpoints

#### 3.1 Anonymiser un Texte
- **POST** `/api/v1/anonymization/anonymize`
- **Body** :
```json
{
  "text": "Patient John Smith (SSN: 123-45-6789)...",
  "strategy": "SYNTHESIZE",
  "preserveMedical": true
}
```
- **Stratégies disponibles** : `REDACTION`, `REPLACEMENT`, `HASHING`, `SYNTHESIZE`

#### 3.2 Analyser PII (sans anonymiser)
- **POST** `/api/v1/anonymization/analyze`
- **Body** : Texte à analyser (string)

#### 3.3 Liste des Stratégies
- **GET** `/api/v1/anonymization/strategies`
- Retourne : `["REDACTION", "REPLACEMENT", "HASHING", "SYNTHESIZE"]`

#### 3.4 Types d'Entités Détectables
- **GET** `/api/v1/anonymization/entities`
- Retourne : `["EMAIL", "PHONE", "SSN", "IP_ADDRESS", "PERSON"]`

### Fonctionnalités
- ✅ Détection automatique de PII (Personally Identifiable Information)
- ✅ Anonymisation avec préservation des données médicales
- ✅ Plusieurs stratégies d'anonymisation
- ✅ Logs d'anonymisation pour audit

---

## 🔍 4. Recherche Sémantique

### Endpoints

#### 4.1 Recherche Sémantique
- **POST** `/api/v1/search/semantic`
- **Body** :
```json
{
  "query": "diabète traitement",
  "topK": 10,
  "similarityThreshold": 0.7,
  "patientId": "PAT001"
}
```

#### 4.2 Indexer un Document
- **POST** `/api/v1/search/index/{documentId}`
- **Body** : Contenu du document à indexer (string)
- **Path Parameter** : `documentId` (UUID)

### Fonctionnalités
- ✅ Recherche vectorielle sémantique
- ✅ Génération d'embeddings pour les documents
- ✅ Découpage intelligent des documents (chunking)
- ✅ Calcul de similarité cosinus
- ✅ Filtrage par patient
- ✅ Indexation automatique des documents

---

## 💬 5. Question-Réponse (Q&A) avec IA

### Endpoints

#### 5.1 Poser une Question
- **POST** `/api/v1/qa/ask`
- **Body** :
```json
{
  "question": "Quel est le diagnostic du patient PAT001?",
  "patientId": "PAT001",
  "userId": "USER001",
  "maxSources": 5
}
```

#### 5.2 Historique des Questions
- **GET** `/api/v1/qa/history?patientId=PAT001&page=0&size=20`
- **Query Parameters** :
  - `patientId` : Filtrer par patient (optionnel)
  - `userId` : Filtrer par utilisateur (optionnel)
  - `page`, `size` : Pagination

#### 5.3 Historique par Patient
- **GET** `/api/v1/qa/history/{patientId}?page=0&size=20`

### Fonctionnalités
- ✅ Pipeline RAG (Retrieval-Augmented Generation)
- ✅ Intégration LLM (OpenAI GPT-4 ou autres)
- ✅ Génération de réponses basées sur le contexte
- ✅ Citations des sources utilisées
- ✅ Score de confiance des réponses
- ✅ Historique des questions/réponses
- ✅ Streaming de réponses (SSE) - en développement

---

## 📊 6. Synthèse Comparative

### Endpoints

#### 6.1 Générer une Synthèse
- **POST** `/api/v1/synthesis/patient/{patientId}?type=SUMMARY`
- **Path Parameter** : `patientId`
- **Query Parameter** : `type` (SUMMARY, TIMELINE, COMPARISON, EVOLUTION)

#### 6.2 Générer une Timeline
- **POST** `/api/v1/synthesis/timeline`
- **Body** :
```json
{
  "patientId": "PAT001",
  "synthesisType": "TIMELINE"
}
```

### Fonctionnalités
- ✅ Synthèse automatique des documents d'un patient
- ✅ Génération de timeline chronologique
- ✅ Comparaison entre documents
- ✅ Analyse de l'évolution médicale

---

## 📝 7. Audit et Logs

### Endpoints

#### 7.1 Liste des Logs d'Audit
- **GET** `/api/v1/audit/logs?page=0&size=20&userId=USER001&action=CREATE`
- **Query Parameters** :
  - `userId` : Filtrer par utilisateur (optionnel)
  - `action` : Filtrer par action (optionnel)
  - `page`, `size` : Pagination

#### 7.2 Logs par Utilisateur
- **GET** `/api/v1/audit/user/{userId}?page=0&size=20`

#### 7.3 Logs par Document
- **GET** `/api/v1/audit/document/{documentId}?page=0&size=20`

### Fonctionnalités
- ✅ Traçabilité complète des actions
- ✅ Logs de toutes les opérations (CREATE, UPDATE, DELETE, READ)
- ✅ Enregistrement de l'IP, timestamp, utilisateur
- ✅ Recherche et filtrage des logs
- ✅ Conformité pour audit médical

---

## 🛠️ Technologies Utilisées

### Framework
- **Spring Boot 3.2.0**
- **Java 17**

### Base de Données
- **PostgreSQL** (port 5433)
- **JPA/Hibernate** pour l'ORM
- **HikariCP** pour le connection pooling

### Sécurité
- **Spring Security**
- **JWT** (préparé pour authentification)

### IA et NLP
- **LLM Integration** (OpenAI GPT-4)
- **Embeddings** pour recherche vectorielle
- **RAG Pipeline** pour Q&A

### Messaging
- **RabbitMQ** pour la communication asynchrone

### Documentation
- **Swagger/OpenAPI** : `http://localhost:8080/swagger-ui.html`

---

## 📈 Statistiques

### Modules Implémentés
- ✅ **7 Controllers** principaux
- ✅ **6 Services** métier
- ✅ **8 Entités** JPA
- ✅ **6 Repositories** Spring Data
- ✅ **Gestion d'erreurs** centralisée
- ✅ **Validation** des données
- ✅ **Pagination** sur tous les endpoints de liste

### Endpoints Totaux
- **~20 endpoints** REST disponibles
- Tous documentés avec Swagger
- Tous testables via Postman

---

## 🔐 Sécurité

### Configuration Actuelle
- ✅ CORS configuré
- ✅ Spring Security activé
- ✅ Tous les endpoints `/api/v1/**` accessibles sans authentification (pour les tests)
- ⚠️ Authentification JWT préparée mais non activée

---

## 📦 Structure des Réponses

Toutes les réponses suivent le format standard :

```json
{
  "success": true,
  "message": "Message de succès",
  "data": { ... }
}
```

En cas d'erreur :
```json
{
  "success": false,
  "message": "Message d'erreur",
  "error": "Détails de l'erreur"
}
```

---

## 🚀 État d'Implémentation

### ✅ Fonctionnalités Complètes
- Gestion des documents (CRUD complet)
- Anonymisation de texte
- Recherche sémantique
- Q&A avec IA
- Synthèse de documents
- Audit et logs
- Health check

### 🔄 En Développement
- Streaming de réponses Q&A (SSE)
- Authentification JWT complète
- Filtrage avancé par patient dans la recherche

### 📝 À Implémenter
- Gestion des utilisateurs (authentification complète)
- Rôles et permissions (Admin, Formateur, Apprenant)
- Notifications
- Tableau de bord avec statistiques
- Génération automatique de contenu pédagogique

---

## 📞 Support

Pour tester les fonctionnalités :
1. **Postman** : Importer `backend/postman/MedBot-Backend.postman_collection.json`
2. **Swagger UI** : `http://localhost:8080/swagger-ui.html`
3. **Health Check** : `http://localhost:8080/api/v1/health`

---

**Dernière mise à jour** : 13 décembre 2025

