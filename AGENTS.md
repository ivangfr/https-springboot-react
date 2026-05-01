# AGENTS.md — Codebase Guide for AI Coding Agents

## Repository Overview

This is a multi-module monorepo demonstrating a full HTTPS stack. There is **no root-level build system**; each module is built independently.

```
https-springboot-react/
├── movies-api/      # Spring Boot REST API (Java 25, Maven, HTTPS port 8443)
├── movies-shell/    # Spring Boot Shell CLI client (Java 25, Maven)
└── movies-ui/       # React SPA (JavaScript, npm, HTTPS port 3443)
```

- `movies-api`: H2 in-memory DB, Spring Data JPA, Bean Validation, Springdoc OpenAPI (Swagger UI)
- `movies-shell`: Interactive CLI using Spring Shell; calls `movies-api` over HTTPS
- `movies-ui`: Vite + React 19 SPA; calls `movies-api` via axios over HTTPS; MUI component library

---

## Build, Run & Test Commands

### `movies-api` (run from `movies-api/` directory)

```bash
# Start dev server
./mvnw clean spring-boot:run

# Build JAR (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=MoviesApiApplicationTests

# Run a single test method
./mvnw test -Dtest=MoviesApiApplicationTests#contextLoads
```

### `movies-shell` (run from `movies-shell/` directory)

```bash
# Start the interactive shell
./mvnw clean spring-boot:run

# Build JAR (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=MoviesShellApplicationTests
```

### `movies-ui` (run from `movies-ui/` directory)

```bash
# Install dependencies
npm install

# Start dev server (HTTPS, port 3443)
npm start

# Production build
npm run build

# Run all tests (once, CI mode)
npm test

# Run all tests in watch mode
npm run test:watch

# Run a single test file by pattern
npm test -- --reporter=verbose --testPathPattern="App"

# Run tests matching a specific test name
npm test -- --reporter=verbose -t "renders"
```

> HTTPS is enabled via `@vitejs/plugin-basic-ssl` in `vite.config.js`.

---

## Technology Stack

| Module | Language | Framework | Key Version |
|---|---|---|---|
| `movies-api` | Java 25 | Spring Boot 4.0.6 | Lombok, H2, Spring Data JPA, springdoc-openapi 3.0.3 |
| `movies-shell` | Java 25 | Spring Boot 4.0.6 | Spring Shell 4.0.2, Spring RestClient |
| `movies-ui` | JavaScript (ES2020+, no TypeScript) | React 19.2.5 | Vite 8.0.10, @vitejs/plugin-react 6.0.1, @vitejs/plugin-basic-ssl 2.3.0, axios 1.15.2, MUI 9 (@mui/material, @mui/icons-material) |
| Java build | — | Maven 3.9.12 (wrapper) | — |

---

## Java Code Style

### Formatting
- **Indentation**: 2 spaces (Google Java Format default) — never tabs
- **Braces**: opening brace on the same line as the declaration (K&R style)
- **Annotations**: each annotation on its own line, directly above the annotated element
- One blank line between methods; no trailing blank lines inside method bodies

### Imports
- No wildcard imports (enforced by `forbidWildcardImports` Spotless rule) — import each class individually
- Unused imports are automatically removed (enforced by `removeUnusedImports` Spotless rule)
- Annotations are formatted (enforced by `formatAnnotations` Spotless rule)
- Import order (matches Spotless config, blank line between groups):
  1. `java.*`
  2. `jakarta.*`
  3. `org.*`
  4. `com.*`

### Naming
- **Classes/Interfaces**: `PascalCase` — `MoviesController`, `MovieService`
- **Implementations**: `PascalCase` + `Impl` suffix — `MovieServiceImpl`
- **DTOs (records)**: `PascalCase` + `Request`/`Response` suffix — `AddMovieRequest`, `MovieResponse`
- **Exceptions**: `PascalCase` + `Exception` suffix — `MovieNotFoundException`
- **Config classes**: `PascalCase` + `Config` suffix — `CorsConfig`, `SwaggerConfig`
- **Methods/fields**: `camelCase` — `validateAndGetMovie`, `movieRepository`
- **Packages**: all lowercase, domain-organized — `com.ivanfranchin.moviesapi.movie.dto`
- **Test classes**: class name + `Tests` suffix — `MoviesApiApplicationTests`

### Error Handling (Java)
- Use `@ResponseStatus` on custom exceptions to map them to HTTP status codes:
  ```java
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public class MovieNotFoundException extends RuntimeException { ... }
  ```
- Use `orElseThrow` in service layer to raise typed exceptions:
  ```java
  return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
  ```
- No `@ControllerAdvice`/`@ExceptionHandler` — rely on Spring Boot's error pipeline
- In `movies-shell` commands, wrap API calls in `try/catch(Exception e)` and return `"Error: " + e.getMessage()` as a string to the shell

### Types / Records / Lombok
- Prefer Java **records** for DTOs (`AddMovieRequest`, `MovieResponse`)
- Use **Lombok** (`@RequiredArgsConstructor`, `@Data`, `@Getter`, etc.) on JPA entities and services
- Use **Bean Validation** annotations (`@NotBlank`, `@Positive`) on DTO fields; validate at controller with `@Valid`

---

## JavaScript / React Code Style

### Formatting
- **Indentation**: 2 spaces
- **Quotes**: single quotes for all string literals and JSX prop values
- **Semicolons**: omit where possible (no trailing `;` on import/export lines in component files)
- Prettier is used for formatting with `.prettierrc` matching project style (2 spaces, single quotes, no trailing semicolons)
- ESLint conflicts are resolved via `eslint-config-prettier`
- `.editorconfig` ensures editor consistency across IDEs

### Imports
Order within a file (no blank lines required between groups, but follow this sequence):
1. React core — `import React, { ... } from 'react'`
2. Third-party libraries — `import { Button } from '@mui/material'`
3. Local components — `import MovieForm from './MovieForm'`
4. Local utilities/services — `import { moviesApi } from '../misc/MoviesApi'`

No barrel/index files — always use explicit relative paths.

### Naming
- **Component files/classes**: `PascalCase` — `MoviePage.jsx`, `MovieForm.jsx`
- **Service/utility files**: `PascalCase` — `MoviesApi.js`
- **Functions/variables**: `camelCase` — `handleChange`, `movieList`, `formInitialState`
- **Event handlers**: `handle` prefix — `handleSaveMovie`, `handleDeleteMovie`
- **State booleans**: descriptive adjective suffix — `isMovieFormOpen`, `imdbIdError`

### Error Handling (JavaScript)
- End all axios promise chains with `.catch(error => { console.log(error) })`
- Do **not** update UI state or show error messages on API failure (log only)
- Client-side form validation: set `*Error` boolean fields in component state and pass them as the `error` prop to MUI `TextField` components

### Component Patterns
- Use class components (existing code is class-based; do not refactor to hooks unless explicitly asked)
- Store form state as a flat object (`this.state.form = { imdbId: '', title: '', ... }`)
- Use `setState` callbacks for dependent state updates

---

## Testing

### Java
- Framework: **JUnit 5** + **Spring Boot Test** (`@SpringBootTest`)
- Existing tests are stubbed and annotated `@Disabled` — enable and expand them when writing tests
- Test class visibility: `public void` test methods
- Test class naming: `{TargetClass}Tests`
- Package must mirror the main source package

### JavaScript
- Framework: **Vitest** + **@testing-library/react** + **@testing-library/jest-dom**
- No existing component tests — write `.test.jsx` files alongside components
- Setup file: `src/vitest.setup.js` (already imports `@testing-library/jest-dom`)
- Test config lives in `vite.config.js` under the `test` key (`environment: 'jsdom'`, `globals: true`)

---

## TLS / HTTPS Notes

- Both Java modules include a self-signed `keystore.p12` in `src/main/resources/` (password: `secret`)
- `movies-api` uses the keystore as an **SSL server certificate**
- `movies-shell` uses the same cert as a **truststore** to accept the self-signed cert
- `movies-ui` dev server enables HTTPS via Vite's built-in `--https` flag in the npm start script
- Do not commit new keystore files or change the existing ones without updating `application.yml`
