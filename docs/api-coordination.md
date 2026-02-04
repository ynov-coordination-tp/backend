# Coordination Front/Back — API Hellenic Rides

## URL de base

- **Base path API** : `/api` (préfixe commun à toutes les routes).
- **Port** : aucune propriété `server.port` n’est définie dans `application.properties` ; par défaut Spring Boot écoute sur **8080** (à confirmer si surcharges d’environnement).
- **Exemple local** : `http://localhost:8080/api`.

## Authentification & sécurité

- **JWT requis** pour **toutes** les routes **sauf** `/api/auth/**` et `/error`.
- **Header** attendu : `Authorization: Bearer <token>` (filtre JWT).

## Modèles (TypeScript — pour copier/coller)

> Les interfaces ci‑dessous sont dérivées des DTO et entités utilisées par les contrôleurs.

```ts
export interface AuthResponse {
  token: string;
}

export interface AdminPost {
  email: string;
  password: string;
}

export enum OptionTargetType {
  QuoteItem = "QUOTE_ITEM",
  Quote = "QUOTE",
}

export interface Option {
  id: number;
  name: string;
  price?: number; // BigDecimal => number
  targetType?: OptionTargetType;
}

export interface Tour {
  id: number;
  name: string;
  country: string;
  durationDays?: number;
  description?: string;
}

export interface Formula {
  id: number;
  name: string;
  includesMoto?: boolean;
  includesAccommodation?: boolean;
  includesMeals?: boolean;
}

export interface TourFormula {
  id: number;
  tour: Tour;
  formula: Formula;
  isActive?: boolean;
}

export enum AccommodationType {
  Hotel = "HOTEL",
  Airbnb = "AIRBNB",
  Guesthouse = "GUESTHOUSE",
}

export interface Accommodation {
  id: number;
  name: string;
  city?: string;
  country?: string;
  type?: AccommodationType;
}

export interface MotoCategory {
  id: number;
  name: string;
}

export interface MotoLocation {
  id: number;
  motoCategory: MotoCategory;
  brand: string;
  model: string;
  count: number;
}

export interface QuoteItemOptionCreateDto {
  optionId: number;
  quantity?: number;
}

export interface QuoteItemCreateDto {
  participantName?: string;
  motoLocationId?: number | null; // nullable for Poseidon formula
  accommodationId?: number;
  options?: QuoteItemOptionCreateDto[];
}

export interface QuoteCreateDto {
  customerId?: number;
  tourPackageId?: number;
  departureDate?: string; // LocalDate (YYYY-MM-DD)
  returnDate?: string; // LocalDate (YYYY-MM-DD)
  items?: QuoteItemCreateDto[];
}

export enum QuoteStatus {
  Draft = "DRAFT",
  Confirmed = "CONFIRMED",
  Cancelled = "CANCELLED",
}

export interface Quote {
  id: number;
  quoteNumber?: string;
  createdAt?: string; // LocalDateTime
  departureDate?: string; // LocalDate
  returnDate?: string; // LocalDate
  customerId?: number;
  tourPackageId?: number;
  participantCount?: number;
  lockedTotalPrice?: number; // BigDecimal => number
  status?: QuoteStatus;
}

export interface QuoteItemOptionResponseDto {
  id: number;
  quoteItemId: number;
  optionId: number;
  quantity?: number;
  lockedPrice?: number; // BigDecimal => number
}

export interface QuoteItemResponseDto {
  id: number;
  quoteId: number;
  participantName?: string;
  motoLocationId?: number | null;
  accommodationId?: number;
  lockedUnitPrice?: number; // BigDecimal => number
  options?: QuoteItemOptionResponseDto[];
}

export interface QuoteResponseDto {
  id: number;
  quoteNumber?: string;
  createdAt?: string; // LocalDateTime
  departureDate?: string; // LocalDate
  returnDate?: string; // LocalDate
  customerId?: number;
  tourPackageId?: number;
  participantCount?: number;
  lockedTotalPrice?: number; // BigDecimal => number
  status?: QuoteStatus;
  items?: QuoteItemResponseDto[];
}
```

---

## Routes API

### Auth

#### `POST /api/auth/login`
- **JWT** : non (public).
- **Body** : `AdminPost`.
- **200** : `AuthResponse`.
- **Erreurs** : `401` si identifiants invalides.

Contrôleur : AuthController.

---

### Options

#### `GET /api/options`
- **JWT** : oui.
- **200** : `Option[]`.

#### `GET /api/options/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **200** : `Option`.
- **404** : option introuvable.

Contrôleur : OptionController.

---

### Tours

#### `GET /api/tours`
- **JWT** : oui.
- **200** : `Tour[]`.

#### `GET /api/tours/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **200** : `Tour`.
- **404** : tour introuvable.

Contrôleur : TourController.

---

### Tour Formulas

#### `GET /api/tour-formulas`
- **JWT** : oui.
- **200** : `TourFormula[]` (chaque entrée contient `tour` + `formula`).

#### `GET /api/tour-formulas/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **200** : `TourFormula`.
- **404** : formule introuvable.

#### `GET /api/tour-formulas/tour/{tourId}`
- **JWT** : oui.
- **Path params** : `tourId: number`.
- **200** : `TourFormula[]`.

Contrôleur : TourFormulaController.

---

### Accommodations

#### `GET /api/accommodations`
- **JWT** : oui.
- **200** : `Accommodation[]`.

#### `GET /api/accommodations/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **200** : `Accommodation`.
- **404** : hébergement introuvable.

Contrôleur : AccommodationController.

---

### Moto Locations

#### `GET /api/moto-locations`
- **JWT** : oui.
- **200** : `MotoLocation[]` (chaque entrée contient `motoCategory`).

Contrôleur : MotoLocationController.

---

### Moto Categories

#### `GET /api/moto-categories`
- **JWT** : oui.
- **200** : `MotoCategory[]`.

Contrôleur : MotoCategoryController.

---

### Quotes

#### `POST /api/quotes`
- **JWT** : oui.
- **Body** : `QuoteCreateDto`.
- **201** : `Quote` (entité persistée).

#### `GET /api/quotes/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **200** : `QuoteResponseDto`.
- **404** : devis introuvable.

#### `PUT /api/quotes/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `QuoteCreateDto`.
- **200** : `Quote` mis à jour.
- **404** : devis introuvable.

Contrôleur : QuoteController.
