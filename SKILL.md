# Project Context for Claude

This file helps Claude quickly understand the project so assistance can start immediately in any session.

---

## Project Overview

**Name:** API Testing with Playwright Java + TestNG
**Author:** Faisal Khatri (`io.github.mfaisalkhatri`)
**Purpose:** A learning/demo framework for REST API test automation using Playwright's Java API testing capabilities.
**Java version:** 17
**Build tool:** Maven

---

## Tech Stack

| Concern | Library / Tool |
|---|---|
| HTTP client | Playwright (`com.microsoft.playwright`) v1.58+ |
| Test runner | TestNG v7.12+ |
| Assertions | TestNG built-in (`org.testng.Assert`) |
| JSON parsing | `org.json` (JSONObject/JSONArray) |
| Data models | Lombok (`@Builder`, `@Getter`, `@Setter`) |
| Fake test data | DataFaker (`net.datafaker`) |
| Serialization | Jackson Databind, Gson |
| Logging | Log4j2 + custom `Logger` wrapper |
| String utilities | Apache Commons Lang3 |

---

## Project Structure

```
src/test/java/io/github/mfaisalkhatri/api/
├── logger/
│   └── Logger.java              # Custom response logger (pretty-prints JSON via Jackson)
├── manager/
│   └── RequestManager.java      # Thin wrapper around Playwright APIRequestContext
│                                #   Methods: get/post/put/patch/delete (with & without RequestOptions)
│                                #   Lifecycle: createPlaywright(), setApiRequestContext(), disposeAPIRequestContext(), closePlaywright()
├── reqres/
│   ├── data/
│   │   ├── EmployeeData.java        # POJO (Lombok @Builder/@Getter/@Setter)
│   │   └── EmployeeDataBuilder.java # Uses DataFaker to build random EmployeeData
│   └── tests/
│       ├── BaseTest.java            # Sets up RequestManager; reads api-key from system property -Dapi-key
│       └── ApiTests.java            # Tests GET/POST/PUT/PATCH/DELETE against reqres.in
├── restfulbooker/
│   ├── data/
│   │   ├── BookingData.java / BookingDataBuilder.java
│   │   ├── BookingDates.java
│   │   ├── PartialBookingData.java
│   │   ├── Tokencreds.java / TokenBuilder.java
│   └── tests/
│       ├── BaseTest.java                      # Sets up RequestManager for restful-booker
│       └── RestfulBookerEndToEndTests.java     # Full E2E: create → get → auth → update → partial-update → delete → verify-deleted
└── restfulecommerce/
    ├── testdata/
    │   ├── OrderData.java / OrderDataBuilder.java
    │   ├── TokenData.java / TokenBuilder.java
    ├── BaseTest.java           # Sets up Playwright directly (no RequestManager); base URL = http://localhost:3004
    ├── HappyPathTests.java     # Health check, CRUD orders, token generation (happy path)
    └── SadPathTests.java       # Validation errors, missing tokens, invalid credentials (sad path)

src/main/resources/
└── log4j2.xml                  # Log4j2 configuration

test-suite/
├── testng.xml                              # Master suite (includes all three below)
├── testng-reqres.xml                       # reqres.in tests
├── testng-restfulbooker.xml                # restful-booker tests
├── testng-restfulecommerce.xml             # restful-ecommerce (full suite)
├── testng-restfulecommerce-deleteorders.xml
├── testng-restfulecommerce-partialupdateorder.xml
├── testng-restfulecommerce-postandgetorder.xml
└── testng-restfulecommerce-updateorder.xml
```

---

## Target APIs

| API | Base URL | Auth | Notes |
|---|---|---|---|
| reqres.in | `https://reqres.in` | API key via `x-api-key` header | Key passed as `-Dapi-key=<value>` at runtime; stored in GitHub secret `REQRES_API_KEY` |
| restful-booker | `https://restful-booker.herokuapp.com` | Cookie token from `/auth` endpoint | Token stored in `this.token`; passed as `Cookie: token=<value>` |
| restful-ecommerce | `http://localhost:3004` | Bearer token from `/auth` endpoint | Requires Docker; token passed as `Authorization` header |

---

## Key Patterns

### RequestManager (reqres + restfulbooker)
- Instantiated in `BaseTest`, configured once per class via `@BeforeClass`/`@AfterClass`
- All HTTP calls go through `this.manager.getRequest(...)`, `this.manager.postRequest(...)`, etc.

### Direct Playwright context (restfulecommerce)
- `BaseTest` creates `Playwright` and `APIRequestContext` directly (no `RequestManager`)
- Tests use `this.request.get(...)`, `this.request.post(...)`, etc.
- Provides `logResponse(APIResponse)` helper via the shared `Logger` class

### Data Builders
- Static factory methods (e.g. `getNewOrder()`, `getBookingData()`) using DataFaker for randomness
- Lombok `@Builder` on POJOs — fields map directly to JSON request bodies

### Response Parsing
- Raw `response.text()` → `new JSONObject(...)` or `new JSONArray(...)`
- No Jackson deserialization into typed objects on the response side; manual field extraction

---

## Running Tests

### Run all tests (default suite)
```bash
mvn clean test -Dapi-key=<REQRES_API_KEY>
```

### Run a specific TestNG suite
```bash
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce.xml
```

### Run restful-ecommerce locally (needs Docker)
```bash
docker compose -f docker-compose-ecommerce.yml up -d
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce.xml
docker compose -f docker-compose-ecommerce.yml down
```

### Run restful-booker locally (needs Docker)
```bash
docker compose -f docker-compose-restfulbooker.yml up -d
mvn clean test -Dsuite-xml=test-suite/testng-restfulbooker.xml
docker compose -f docker-compose-restfulbooker.yml down
```

---

## CI/CD

- GitHub Actions workflow: `.github/workflows/maven.yml`
- Triggers on push to `main` or `issue-*` branches, and on PRs to `main`
- Workflow: starts Docker services → builds → runs tests → stops Docker → publishes test report via `dorny/test-reporter`
- Test report source: `target/surefire-reports/TEST-TestSuite.xml`

---

## Common Tasks Claude Can Help With

- Adding new test methods (follow existing GET/POST/PUT/PATCH/DELETE patterns)
- Adding new POJO data classes with Lombok `@Builder`
- Adding new DataFaker-based builder methods
- Extending `RequestManager` with new HTTP helpers
- Creating new TestNG XML suite files
- Debugging assertion failures (response body parsing with `JSONObject`)
- Adding new sad-path / happy-path test scenarios for restful-ecommerce
