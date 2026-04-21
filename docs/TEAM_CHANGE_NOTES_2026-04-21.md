# Team Change Notes (2026-04-21)

Dette dokument opsummerer de ændringer, der er lavet i login, adgangskontrol og frontend-flow, så teamet kan gennemgå dem samlet.

## 1) Routing og admin-adgang til create movie

### `src/main/java/com/kinoxp/movie/MovieController.java`
- Tilføjet `GET /kino/movies/create` som redirecter til `"/html/index.html?openCreateMovie=true"`.
- Tilføjet `POST /kino/movies/create` som bruger samme opret-logik som almindelig movie-oprettelse.
- Opdateret id-ruter til kun at matche tal med regex:
  - `GET /{movieId:\d+}`
  - `PUT /{movieId:\d+}`
  - `DELETE /{movieId:\d+}`

**Hvorfor:** Undgår at `"create"` fejlagtigt tolkes som `movieId`, og giver et stabilt admin-flow.

## 2) Spring Security regler og fejlhåndtering

### `src/main/java/com/kinoxp/security/SecurityConfig.java`
- Whitelist for root/static indhold (`/`, `/index.html`, `/html/**`, `/css/**`, `/js/**`).
- Rollebeskyttelse for admin-endpoints:
  - movies `POST/PUT/DELETE`
  - showings `POST/PUT/DELETE`
  - `GET /kino/movies/create` kræver `ADMIN`.
- Tilføjet custom `authenticationEntryPoint`/`accessDeniedHandler`:
  - Ved `/kino/movies/create` får ikke-admin/ulogget bruger besked + redirect til startside.
- CSRF aktiveret med cookie repository:
  - `CookieCsrfTokenRepository.withHttpOnlyFalse()`
  - Login/registrering undtaget (`/kino/users/login`, `/kino/users`).
- Forbedret 403-besked for CSRF-problemer vs rolle-problemer.

**Hvorfor:** Mere korrekt adgangsstyring og sikrere write-requests i browserflow.

## 3) UserDetailsService og login-opslag

### `src/main/java/com/kinoxp/security/JpaUserDetailService.java`
- Login-opslag er case-insensitive via repository-metode.
- Fjernet ekstra `@Service` annotation (for at undgå dobbelt UserDetailsService bean).

### `src/main/java/com/kinoxp/user/UserRepository.java`
- Erstattet single-resultat navneopslag med:
  - `findFirstByNameIgnoreCaseOrderByUserIdAsc`
  - `findAllByNameIgnoreCaseOrderByUserIdAsc`

### `src/main/java/com/kinoxp/user/UserService.java`
- `findByName(...)` bruger nu `findFirst...` case-insensitive.

**Hvorfor:** Undgår fejl ved forskel på store/små bogstaver og ved dublette navne i data.

## 4) Data-initialisering og migration af passwords

### `src/main/java/com/kinoxp/shared/DataInitializer.java`
- Migrerer plaintext-passwords til BCrypt ved startup.
- Sikrer at standardbrugere findes (`Alice`, `Birthe`, `Mikkel`).
- Håndterer dublette navne robust uden startup-crash (vælger første match).

**Hvorfor:** Login bliver stabilt i eksisterende databaser med historiske data.

## 5) Fjernet `userId` som sikkerhedsparameter i requests

### `src/main/java/com/kinoxp/showing/ShowingController.java`
- Fjernet `@RequestParam Long userId` fra create/update/delete.
- Autorisation håndteres af Spring Security rolle-regler.

### `src/main/java/com/kinoxp/user/UserController.java`
- Tilføjet `GET /kino/users/me` for aktiv session-bruger.
- `DELETE /kino/users/{userId}` bruger ikke længere `adminUserId` query-param.

**Hvorfor:** Undgår manipulerbar autorisation via URL-parametre.

## 6) Frontend session og CSRF flow

### `src/main/resources/static/js/login.js`
- Efter login hentes `/kino/users/me`, og session-bruger gemmes i `localStorage`.
- Mere robust håndtering af non-JSON svar.

### `src/main/resources/static/js/register.js`
- Efter registrering forsøges auto-login (`/kino/users/login`) og derefter `/kino/users/me`.
- Fallback til sikkert brugerobjekt i `localStorage` uden password-data.
- Robust mod ikke-JSON svar.

### `src/main/resources/static/js/index.js`
- Læser `openCreateMovie=true` og åbner create-form for admin.
- Fjernet `?userId=` fra movie/showing write-kald.
- Tilføjet CSRF-header på write-kald.
- Logout kalder backend `/kino/users/logout`.

### `src/main/resources/static/js/movie.js`
- Logout kalder backend + CSRF-header.

### `src/main/resources/static/js/my-reservations.js`
- Logout og delete reservation sender CSRF-token.

### `src/main/resources/static/js/booking.js`
- Create reservation sender CSRF-token.

**Hvorfor:** Frontend og backend session er nu synkroniseret, og CSRF-beskyttelse fungerer med fetch-kald.

## 7) Kendt sideeffekt / opfølgning

### `src/main/resources/static/js/admin.js`
- Filen er markeret som ændret i git, men bruges ikke i hovedflowet (`index.js` styrer admin-panelet).
- Teamet bør bekræfte om `admin.js` stadig skal bruges, eller fjernes for at undgå forvirring.

## 8) Team test-checkliste

1. Login/logout med eksisterende bruger (`Alice/pw`) flere gange i træk.
2. Registrer ny `ADMIN`, log ind, test `/kino/movies/create`.
3. Log ind som `CUSTOMER`, åbn `/kino/movies/create`:
   - vis “ingen adgang”
   - redirect til startside.
4. Opret/rediger/slet film som admin.
5. Opret/rediger/slet showings som admin.
6. Opret/slet reservation som kunde.
7. Verificer at ingen write-kald bruger `?userId=...`.

