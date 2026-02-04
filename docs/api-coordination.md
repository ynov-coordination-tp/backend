# Coordination Front/Back — API Hellenic Rides

## URL de base

- **Base path API** : `/api` (préfixe commun à toutes les routes).
- **Port** : aucune propriété `server.port` n’est définie dans `application.properties` ; par défaut Spring Boot écoute sur **8080** (à confirmer si surcharges d’environnement).
- **Exemple local** : `http://localhost:8080/api`.

## Authentification & sécurité

- **JWT requis** pour toutes les routes **sauf** :
  - `POST /api/auth/login`
  - `POST /api/quotes`
  - **toutes** les routes `GET /api/**`
  - `/error` et la doc Swagger (`/swagger-ui/**`, `/v3/api-docs/**`, etc.)
- **Header** attendu : `Authorization: Bearer <token>`.

## Modèles (TypeScript — pour copier/coller)

> Les interfaces ci‑dessous sont dérivées des DTO/entités et exposées par les contrôleurs.
> Les dates sont des **strings** au format `YYYY-MM-DD` et les dates/heures `YYYY-MM-DDTHH:mm:ss`.

```ts
export interface AuthResponse {
  token: string;
}

export interface AdminPost {
  email: string;
  password: string;
}

export interface Admin {
  id: number;
  email: string;
}

export enum OptionTargetType {
  QuoteItem = "QUOTE_ITEM",
  Quote = "QUOTE",
}

export interface Option {
  id: number;
  name: string;
  price?: number; // BigDecimal => number
  targetType?: OptionTargetType | string;
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

export interface TourPrice {
  id: number;
  tourFormula: TourFormula;
  startDate: string; // LocalDate
  endDate: string; // LocalDate
  basePrice: number; // BigDecimal => number
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
  type?: AccommodationType | string;
}

export enum RoomType {
  Single = "SINGLE",
  Couple = "COUPLE",
  Shared = "SHARED",
}

export interface AccommodationPrice {
  id: number;
  accommodationId: number;
  startDate: string; // LocalDate
  endDate: string; // LocalDate
  nightlyPrice: number; // BigDecimal => number
  roomType: RoomType | string;
}

export interface MotoCategory {
  id: number;
  name: string;
}

export interface MotoCategoryPrice {
  id: number;
  motoCategory: MotoCategory;
  country: string;
  startDate: string; // LocalDate
  endDate: string; // LocalDate
  dailyPrice: number; // BigDecimal => number
  kmPrice?: number; // BigDecimal => number
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
  status?: QuoteStatus | string;
}

export interface QuoteItem {
  id: number;
  quoteId: number;
  participantName?: string;
  motoLocationId?: number | null;
  accommodationId?: number;
  lockedUnitPrice?: number; // BigDecimal => number
}

export interface QuoteItemOption {
  id: number;
  quoteItemId: number;
  optionId: number;
  quantity?: number;
  lockedPrice?: number; // BigDecimal => number
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
  status?: QuoteStatus | string;
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
- **401** : identifiants invalides.

Contrôleur : AuthController.

---

### Admins

#### `GET /api/admins`
- **JWT** : non (public).
- **200** : `Admin[]`.

#### `GET /api/admins/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `Admin`.
- **404** : admin introuvable.

#### `POST /api/admins`
- **JWT** : oui.
- **Body** : `AdminPost`.
- **201** : `Admin`.

#### `PUT /api/admins/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `AdminPost`.
- **200** : `Admin`.
- **404** : admin introuvable.

#### `DELETE /api/admins/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : admin introuvable.

Contrôleur : AdminController.

---

### Options

#### `GET /api/options`
- **JWT** : non (public).
- **200** : `Option[]`.

#### `GET /api/options/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `Option`.
- **404** : option introuvable.

#### `POST /api/options`
- **JWT** : oui.
- **Body** : `Option`.
- **201** : `Option`.

#### `PUT /api/options/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `Option`.
- **200** : `Option`.
- **404** : option introuvable.

#### `DELETE /api/options/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : option introuvable.

Contrôleur : OptionController.

---

### Tours

#### `GET /api/tours`
- **JWT** : non (public).
- **200** : `Tour[]`.

#### `GET /api/tours/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `Tour`.
- **404** : tour introuvable.

#### `POST /api/tours`
- **JWT** : oui.
- **Body** : `Tour`.
- **201** : `Tour`.

#### `PUT /api/tours/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `Tour`.
- **200** : `Tour`.
- **404** : tour introuvable.

#### `DELETE /api/tours/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : tour introuvable.

Contrôleur : TourController.

---

### Formulas

#### `GET /api/formulas`
- **JWT** : non (public).
- **200** : `Formula[]`.

#### `GET /api/formulas/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `Formula`.
- **404** : formule introuvable.

#### `POST /api/formulas`
- **JWT** : oui.
- **Body** : `Formula`.
- **201** : `Formula`.

#### `PUT /api/formulas/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `Formula`.
- **200** : `Formula`.
- **404** : formule introuvable.

#### `DELETE /api/formulas/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : formule introuvable.

Contrôleur : FormulaController.

---

### Tour Formulas

#### `GET /api/tour-formulas`
- **JWT** : non (public).
- **200** : `TourFormula[]` (chaque entrée contient `tour` + `formula`).

#### `GET /api/tour-formulas/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `TourFormula`.
- **404** : formule introuvable.

#### `GET /api/tour-formulas/tour/{tourId}`
- **JWT** : non (public).
- **Path params** : `tourId: number`.
- **200** : `TourFormula[]`.

#### `POST /api/tour-formulas`
- **JWT** : oui.
- **Body** : `TourFormula`.
- **201** : `TourFormula`.

#### `PUT /api/tour-formulas/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `TourFormula`.
- **200** : `TourFormula`.
- **404** : formule introuvable.

#### `DELETE /api/tour-formulas/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : formule introuvable.

Contrôleur : TourFormulaController.

---

### Tour Prices

#### `GET /api/tour-prices`
- **JWT** : non (public).
- **200** : `TourPrice[]`.

#### `GET /api/tour-prices/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `TourPrice`.
- **404** : prix introuvable.

#### `GET /api/tour-prices/formula/{tourFormulaId}`
- **JWT** : non (public).
- **Path params** : `tourFormulaId: number`.
- **200** : `TourPrice[]`.

#### `POST /api/tour-prices`
- **JWT** : oui.
- **Body** : `TourPrice`.
- **201** : `TourPrice`.

#### `PUT /api/tour-prices/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `TourPrice`.
- **200** : `TourPrice`.
- **404** : prix introuvable.

#### `DELETE /api/tour-prices/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : prix introuvable.

Contrôleur : TourPriceController.

---

### Accommodations

#### `GET /api/accommodations`
- **JWT** : non (public).
- **200** : `Accommodation[]`.

#### `GET /api/accommodations/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `Accommodation`.
- **404** : hébergement introuvable.

#### `POST /api/accommodations`
- **JWT** : oui.
- **Body** : `Accommodation`.
- **201** : `Accommodation`.

#### `PUT /api/accommodations/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `Accommodation`.
- **200** : `Accommodation`.
- **404** : hébergement introuvable.

#### `DELETE /api/accommodations/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : hébergement introuvable.

Contrôleur : AccommodationController.

---

### Accommodation Prices

#### `GET /api/accommodation-prices`
- **JWT** : non (public).
- **200** : `AccommodationPrice[]`.

#### `GET /api/accommodation-prices/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `AccommodationPrice`.
- **404** : prix introuvable.

#### `GET /api/accommodation-prices/accommodation/{accommodationId}`
- **JWT** : non (public).
- **Path params** : `accommodationId: number`.
- **200** : `AccommodationPrice[]`.

#### `POST /api/accommodation-prices`
- **JWT** : oui.
- **Body** : `AccommodationPrice`.
- **201** : `AccommodationPrice`.

#### `PUT /api/accommodation-prices/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `AccommodationPrice`.
- **200** : `AccommodationPrice`.
- **404** : prix introuvable.

#### `DELETE /api/accommodation-prices/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : prix introuvable.

Contrôleur : AccommodationPriceController.

---

### Moto Categories

#### `GET /api/moto-categories`
- **JWT** : non (public).
- **200** : `MotoCategory[]`.

#### `GET /api/moto-categories/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `MotoCategory`.
- **404** : catégorie introuvable.

#### `POST /api/moto-categories`
- **JWT** : oui.
- **Body** : `MotoCategory`.
- **201** : `MotoCategory`.

#### `PUT /api/moto-categories/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `MotoCategory`.
- **200** : `MotoCategory`.
- **404** : catégorie introuvable.

#### `DELETE /api/moto-categories/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : catégorie introuvable.

Contrôleur : MotoCategoryController.

---

### Moto Category Prices

#### `GET /api/moto-category-prices`
- **JWT** : non (public).
- **200** : `MotoCategoryPrice[]`.

#### `GET /api/moto-category-prices/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `MotoCategoryPrice`.
- **404** : prix introuvable.

#### `GET /api/moto-category-prices/category/{categoryId}`
- **JWT** : non (public).
- **Path params** : `categoryId: number`.
- **200** : `MotoCategoryPrice[]`.

#### `POST /api/moto-category-prices`
- **JWT** : oui.
- **Body** : `MotoCategoryPrice`.
- **201** : `MotoCategoryPrice`.

#### `PUT /api/moto-category-prices/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `MotoCategoryPrice`.
- **200** : `MotoCategoryPrice`.
- **404** : prix introuvable.

#### `DELETE /api/moto-category-prices/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : prix introuvable.

Contrôleur : MotoCategoryPriceController.

---

### Moto Locations

#### `GET /api/moto-locations`
- **JWT** : non (public).
- **200** : `MotoLocation[]` (chaque entrée contient `motoCategory`).

#### `GET /api/moto-locations/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `MotoLocation`.
- **404** : moto introuvable.

#### `POST /api/moto-locations`
- **JWT** : oui.
- **Body** : `MotoLocation`.
- **201** : `MotoLocation`.

#### `PUT /api/moto-locations/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `MotoLocation`.
- **200** : `MotoLocation`.
- **404** : moto introuvable.

#### `DELETE /api/moto-locations/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : moto introuvable.

Contrôleur : MotoLocationController.

---

### Quote Items

#### `GET /api/quote-items`
- **JWT** : non (public).
- **200** : `QuoteItem[]`.

#### `GET /api/quote-items/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `QuoteItem`.
- **404** : ligne introuvable.

#### `GET /api/quote-items/quote/{quoteId}`
- **JWT** : non (public).
- **Path params** : `quoteId: number`.
- **200** : `QuoteItem[]`.

#### `POST /api/quote-items`
- **JWT** : oui.
- **Body** : `QuoteItem`.
- **201** : `QuoteItem`.

#### `PUT /api/quote-items/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `QuoteItem`.
- **200** : `QuoteItem`.
- **404** : ligne introuvable.

#### `DELETE /api/quote-items/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : ligne introuvable.

Contrôleur : QuoteItemController.

---

### Quote Item Options

#### `GET /api/quote-item-options`
- **JWT** : non (public).
- **200** : `QuoteItemOption[]`.

#### `GET /api/quote-item-options/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `QuoteItemOption`.
- **404** : option introuvable.

#### `GET /api/quote-item-options/quote-item/{quoteItemId}`
- **JWT** : non (public).
- **Path params** : `quoteItemId: number`.
- **200** : `QuoteItemOption[]`.

#### `POST /api/quote-item-options`
- **JWT** : oui.
- **Body** : `QuoteItemOption`.
- **201** : `QuoteItemOption`.

#### `PUT /api/quote-item-options/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `QuoteItemOption`.
- **200** : `QuoteItemOption`.
- **404** : option introuvable.

#### `DELETE /api/quote-item-options/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : option introuvable.

Contrôleur : QuoteItemOptionController.

---

### Quotes

#### `POST /api/quotes`
- **JWT** : non (public).
- **Body** : `QuoteCreateDto`.
- **201** : `Quote` (entité persistée).

#### `GET /api/quotes`
- **JWT** : non (public).
- **200** : `Quote[]`.

#### `GET /api/quotes/{id}`
- **JWT** : non (public).
- **Path params** : `id: number`.
- **200** : `QuoteResponseDto`.
- **404** : devis introuvable.

#### `PUT /api/quotes/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **Body** : `QuoteCreateDto`.
- **200** : `Quote` mis à jour.
- **404** : devis introuvable.

#### `DELETE /api/quotes/{id}`
- **JWT** : oui.
- **Path params** : `id: number`.
- **204** : supprimé.
- **404** : devis introuvable.

Contrôleur : QuoteController.
