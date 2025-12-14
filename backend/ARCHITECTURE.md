# Architecture et Fonctionnalités du Backend MedBot

## 📁 Structure du Dossier Backend

```
backend/
├── src/main/java/com/medbot/
│   ├── MedBotApplication.java          # Point d'entrée de l'application
│   ├── common/                          # Composants partagés
│   │   ├── config/                      # Configurations (Security, CORS, RabbitMQ)
│   │   ├── controller/                  # Contrôleurs communs (Health, Dashboard)
│   │   ├── dto/                         # DTOs partagés (ApiResponse)
│   │   └── exception/                   # Gestion des erreurs
│   ├── user/                            # Gestion des utilisateurs
│   ├── course/                          # Gestion des cours et formations
│   ├── trainer/                         # Gestion des formateurs
│   ├── notification/                    # Système de notifications
│   ├── support/                         # Tickets de support
│   ├── coach/                           # Configuration du coach IA
│   ├── progress/                        # Suivi de progression
│   ├── document/                        # Gestion des documents médicaux
│   ├── search/                          # Recherche sémantique
│   ├── qa/                              # Questions/Réponses avec IA
│   ├── synthesis/                       # Synthèses médicales
│   ├── deid/                            # Anonymisation des données
│   └── audit/                           # Logs d'audit
├── src/main/resources/
│   └── application.yml                  # Configuration de l'application
└── pom.xml                              # Dépendances Maven
```

## 🏗️ Architecture (Pattern MVC + Services)

Le backend suit l'architecture **MVC (Model-View-Controller)** avec une couche **Service** :

```
Controller (API REST) 
    ↓
Service (Logique métier)
    ↓
Repository (Accès données)
    ↓
Entity (Modèle de données)
    ↓
PostgreSQL (Base de données)
```

### Structure par Module

Chaque module suit cette structure :
- **Controller** : Expose les endpoints REST API
- **Service** : Contient la logique métier
- **Repository** : Interface d'accès aux données (JPA)
- **Entity** : Modèle de données (mappé vers table PostgreSQL)
- **DTO** : Objets de transfert de données (Request/Response)

## 🚀 Point d'Entrée de l'Application

**Fichier** : `MedBotApplication.java`

```java
@SpringBootApplication          // Active Spring Boot
@EnableJpaAuditing             // Active l'audit JPA (created_at, updated_at)
@EnableAsync                   // Active les opérations asynchrones
@EnableScheduling              // Active les tâches planifiées
public class MedBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedBotApplication.class, args);
    }
}
```

## 🔧 Technologies Utilisées

### Framework Principal
- **Spring Boot 3.2.0** : Framework Java pour applications web
- **Java 17** : Version du langage

### Base de Données
- **PostgreSQL** : Base de données relationnelle
- **Hibernate/JPA** : ORM (Object-Relational Mapping)
- **HikariCP** : Pool de connexions

### Sécurité
- **Spring Security** : Authentification et autorisation
- **BCrypt** : Hashage des mots de passe

### Communication
- **RabbitMQ** : Message broker (tâches asynchrones)
- **REST API** : Endpoints HTTP/JSON

### Documentation
- **Swagger/OpenAPI** : Documentation automatique des APIs

### IA et Traitement
- **OpenAI API** : Intégration GPT pour Q&A et synthèses
- **PDFBox** : Parsing de fichiers PDF
- **Vector Embeddings** : Recherche sémantique

## 📋 Fonctionnalités par Module

### 1. 🧑‍💼 User Management (`/api/v1/users`)

**Fonctionnalités** :
- ✅ Création, lecture, mise à jour, suppression d'utilisateurs
- ✅ Gestion des rôles : ADMINISTRATEUR, FORMATEUR, APPRENANT
- ✅ Gestion des statuts : ACTIF, INACTIF, SUSPENDU
- ✅ Recherche et filtrage (par nom, email, formation)
- ✅ Statistiques utilisateurs

**Entité** : `User`
- Rôles : ADMINISTRATEUR, FORMATEUR, APPRENANT
- Niveaux : DEBUTANT, INTERMEDIAIRE, AVANCE
- Statuts : ACTIF, INACTIF, SUSPENDU

### 2. 📚 Course Management (`/api/v1/courses`, `/api/v1/formations`)

**Fonctionnalités** :
- ✅ Gestion des formations (programmes complets)
- ✅ Gestion des cours (modules individuels)
- ✅ Approbation/rejet de cours
- ✅ Filtrage par formation, formateur, statut
- ✅ Statistiques (approuvés, en attente)

**Entités** :
- `Formation` : Programme de formation complet
- `Course` : Cours individuel avec statut (EN_ATTENTE, APPROUVE, REJETE, PUBLIE)

### 3. 👨‍🏫 Trainer Management (`/api/v1/trainers`)

**Fonctionnalités** :
- ✅ Gestion des formateurs
- ✅ Validation/suspension de formateurs
- ✅ Gestion des compétences (skills)
- ✅ Système de notation
- ✅ Statistiques (total, actifs, en attente, note moyenne)

**Entité** : `Trainer`
- Statuts : EN_ATTENTE, ACTIF, SUSPENDU
- Compétences : Liste de skills (ex: "Machine Learning", "Python")

### 4. 🔔 Notification System (`/api/v1/notifications`)

**Fonctionnalités** :
- ✅ Création de notifications
- ✅ Notifications planifiées (scheduled)
- ✅ Notifications par utilisateur ou globales
- ✅ Marquage comme lu/non lu
- ✅ Statistiques (envoyées, planifiées, taux de lecture)

**Entité** : `Notification`
- Types : ANNONCE, RAPPEL, OBJECTIF, MOTIVATION, ALERTE
- Priorités : BASSE, MOYENNE, HAUTE
- Audience : TOUS, APPRENANTS, FORMATEURS

### 5. 🎫 Support Tickets (`/api/v1/support/tickets`)

**Fonctionnalités** :
- ✅ Création de tickets de support
- ✅ Assignation à des administrateurs
- ✅ Gestion des statuts (OUVERT, EN_COURS, RESOLU)
- ✅ Filtrage par catégorie, priorité, statut
- ✅ Statistiques (total, ouverts, en cours, résolus)

**Entité** : `SupportTicket`
- Catégories : TECHNIQUE, PEDAGOGIQUE, PAIEMENT, COMPTE, AUTRE
- Priorités : BASSE, MOYENNE, HAUTE, URGENTE
- Statuts : OUVERT, EN_COURS, EN_ATTENTE_DE_REPONSE, RESOLU

### 6. 🤖 AI Coach (`/api/v1/coach`)

**Fonctionnalités** :
- ✅ Configuration du coach virtuel IA
- ✅ Paramètres de réponse (langue, ton, niveau de détail)
- ✅ Activation/désactivation de fonctionnalités (quiz, résumés, exercices)
- ✅ Statistiques d'interactions
- ✅ Liste des interactions
- ✅ Base de connaissances

**Entité** : `AICoachConfig`
- Langue : Français, Anglais, etc.
- Ton : Amical, Professionnel, Formel
- Niveau de détail : Minimal, Modéré, Détaillé
- Fonctionnalités : Génération de quiz, résumés, exercices

### 7. 📊 Progress Tracking (`/api/v1/progress`)

**Fonctionnalités** :
- ✅ Suivi de progression par utilisateur et cours
- ✅ Pourcentage de complétion
- ✅ Modules complétés / total
- ✅ Temps passé
- ✅ Dernière date d'accès

**Entité** : `Progress`
- Lien entre User et Course
- Calcul automatique du pourcentage de complétion

### 8. 📄 Document Management (`/api/v1/documents`)

**Fonctionnalités** :
- ✅ Upload de documents (PDF, etc.)
- ✅ Parsing automatique des PDFs
- ✅ Stockage des métadonnées
- ✅ Recherche par patient
- ✅ Gestion des versions

**Entités** :
- `Document` : Document principal
- `DocumentMetadata` : Métadonnées du document

### 9. 🔍 Semantic Search (`/api/v1/search`)

**Fonctionnalités** :
- ✅ Recherche sémantique dans les documents
- ✅ Indexation de documents (chunking)
- ✅ Génération d'embeddings (vecteurs)
- ✅ Recherche par similarité cosinus
- ✅ Filtrage par patient

**Entités** :
- `DocumentChunk` : Segments de documents avec embeddings

**Processus** :
1. Document → Chunking (découpage en segments)
2. Chunk → Embedding (vecteur de 1536 dimensions)
3. Stockage dans PostgreSQL (TEXT format JSON)
4. Recherche par similarité cosinus

### 10. 💬 Q&A System (`/api/v1/qa`)

**Fonctionnalités** :
- ✅ Questions/Réponses avec IA (RAG)
- ✅ Recherche dans les documents pertinents
- ✅ Génération de réponses contextuelles
- ✅ Historique des questions
- ✅ Score de confiance

**Processus RAG (Retrieval-Augmented Generation)** :
1. Question utilisateur
2. Recherche sémantique dans les documents
3. Construction du contexte
4. Génération de réponse avec GPT
5. Sauvegarde question/réponse

**Entités** :
- `Question` : Question posée
- `Answer` : Réponse générée avec sources

### 11. 📝 Synthesis (`/api/v1/synthesis`)

**Fonctionnalités** :
- ✅ Génération de synthèses médicales
- ✅ Timeline chronologique
- ✅ Comparaisons entre documents
- ✅ Évolution dans le temps

**Entité** : `Synthesis`
- Types : TIMELINE, SUMMARY, COMPARISON, EVOLUTION

### 12. 🔒 Anonymization (`/api/v1/anonymization`)

**Fonctionnalités** :
- ✅ Détection de PII (Personally Identifiable Information)
- ✅ Anonymisation de textes médicaux
- ✅ Préservation des entités médicales
- ✅ Stratégies d'anonymisation (MASK, SYNTHESIZE, REMOVE)

**Entité** : `AnonymizationLog`
- Types détectés : EMAIL, PHONE, SSN, IP_ADDRESS, PERSON

### 13. 📋 Audit Logs (`/api/v1/audit`)

**Fonctionnalités** :
- ✅ Enregistrement de toutes les actions
- ✅ Traçabilité complète
- ✅ Recherche par utilisateur, document
- ✅ Historique des modifications

**Entité** : `AuditLog`
- Actions : CREATE, READ, UPDATE, DELETE
- Ressources : Document, User, etc.

### 14. 📊 Dashboard (`/api/v1/dashboard`)

**Fonctionnalités** :
- ✅ Statistiques globales
- ✅ Vue d'ensemble de toutes les métriques
- ✅ Agrégation des données de tous les modules

## 🔐 Sécurité

**Fichier** : `SecurityConfig.java`

- ✅ **CSRF désactivé** (pour API REST)
- ✅ **Tous les endpoints `/api/v1/**` sont publics** (pour le développement)
- ✅ **BCrypt** pour le hashage des mots de passe
- ✅ **Sessions stateless** (JWT ready)

## 🌐 Configuration

**Fichier** : `application.yml`

### Base de Données
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/medbot
    username: medbot_user
    password: medbot_pass
  jpa:
    hibernate:
      ddl-auto: update  # Crée/met à jour les tables automatiquement
```

### Server
- Port : **8080**
- Swagger UI : `http://localhost:8080/swagger-ui.html`

### RabbitMQ
- Port : **5672**
- Utilisé pour les tâches asynchrones

## 📡 Endpoints Principaux

### Dashboard
- `GET /api/v1/dashboard/stats` - Statistiques globales

### Users
- `POST /api/v1/users` - Créer utilisateur
- `GET /api/v1/users` - Liste avec filtres
- `GET /api/v1/users/stats` - Statistiques
- `PATCH /api/v1/users/:id` - Mettre à jour
- `POST /api/v1/users/:id/activate` - Activer
- `POST /api/v1/users/:id/deactivate` - Désactiver

### Courses
- `POST /api/v1/courses` - Créer cours
- `GET /api/v1/courses` - Liste avec filtres
- `POST /api/v1/courses/:id/approve` - Approuver

### Trainers
- `POST /api/v1/trainers` - Créer formateur
- `GET /api/v1/trainers` - Liste
- `POST /api/v1/trainers/:id/validate` - Valider

### Notifications
- `POST /api/v1/notifications` - Créer notification
- `GET /api/v1/notifications/scheduled` - Notifications planifiées
- `POST /api/v1/notifications/:id/read` - Marquer comme lu

### Support
- `POST /api/v1/support/tickets` - Créer ticket
- `GET /api/v1/support/tickets` - Liste avec filtres
- `PATCH /api/v1/support/tickets/:id/status` - Mettre à jour statut

### AI Coach
- `GET /api/v1/coach/config` - Configuration
- `PATCH /api/v1/coach/config` - Mettre à jour config
- `GET /api/v1/coach/stats` - Statistiques
- `GET /api/v1/coach/interactions` - Interactions

## 🔄 Flux de Données

### Exemple : Création d'un Utilisateur

```
1. Client → POST /api/v1/users
   Body: { firstName, lastName, email, password, role }
   
2. UserController → reçoit la requête
   
3. UserService → valide les données, hash le mot de passe
   
4. UserRepository → sauvegarde dans PostgreSQL
   
5. User Entity → mappé vers table "users"
   
6. Réponse → UserDTO retourné au client
```

### Exemple : Question/Réponse avec IA

```
1. Client → POST /api/v1/qa/ask
   Body: { question, patientId, userId }
   
2. QaController → reçoit la requête
   
3. RagPipelineService → 
   a. Recherche sémantique dans les documents
   b. Construit le contexte
   c. Appelle LlmService (OpenAI)
   d. Génère la réponse
   
4. QuestionRepository → sauvegarde question/réponse
   
5. Réponse → QuestionResponse avec answer, sources, confidence
```

## 🗄️ Base de Données

**PostgreSQL** avec les tables suivantes :
- `users` - Utilisateurs
- `courses` - Cours
- `formations` - Formations
- `trainers` - Formateurs
- `notifications` - Notifications
- `support_tickets` - Tickets de support
- `progress` - Progression
- `documents` - Documents médicaux
- `document_chunks` - Segments de documents avec embeddings
- `questions` - Questions
- `answers` - Réponses
- `syntheses` - Synthèses
- `ai_coach_config` - Configuration IA
- `audit_logs` - Logs d'audit
- `anonymization_logs` - Logs d'anonymisation

## 🚀 Démarrage

```bash
# 1. Démarrer PostgreSQL (port 5433)
# 2. Créer la base de données "medbot"
# 3. Lancer l'application
cd backend
mvn spring-boot:run
```

L'application démarre sur : `http://localhost:8080`

## 📚 Documentation API

Une fois l'application démarrée :
- **Swagger UI** : `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:8080/api-docs`

## 🧪 Tests

Collection Postman disponible :
- `backend/postman/MedBot-Backend.postman_collection.json`

Importez-la dans Postman pour tester tous les endpoints.


