# Claude Code Instructions

Read `SKILL.md` in this repo root for full project context before helping with any task.
It covers the tech stack, project structure, all three target APIs, key code patterns, and how to run tests.

## Quick reference

- **Build:** `mvn clean install -DskipTests`
- **Run all tests:** `mvn clean test -Dapi-key=<REQRES_API_KEY>`
- **Run specific suite:** `mvn clean test -Dsuite-xml=test-suite/<file>.xml`
- **Local services (Docker):**
  - restful-booker → `docker compose -f docker-compose-restfulbooker.yml up -d` (port 3001)
  - restful-ecommerce → `docker compose -f docker-compose-ecommerce.yml up -d` (port 3004)
- **Java:** 17 | **Test runner:** TestNG | **HTTP client:** Playwright Java API
- **Base package:** `io.github.mfaisalkhatri.api`
