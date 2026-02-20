
## :question: What is this Repository about?

This project is the outcome of my self-learning about the API Testing Automation framework - [Playwright with Java](https://playwright.dev/java/docs/api-testing#writing-api-test)

## :briefcase: What does this repo contain?
- This repo contains example codes of API Tests using Playwright with Java.
- [TestNG](https://testng.org) is used as Test Runner and for performing assertions in the test.
- `Log4j` is used to capture logs.
- [Lombok](https://projectlombok.org/) has been used to generate Getter and Setters automatically for post body requests.
- FAKE Rest APIs on [Reqres.in](https://reqres.in/) has been used for testing.
- End to End scenarios have been added for the [restful booker APIs](https://restful-booker.herokuapp.com/apidoc/index.html).
- Happy and Sad Path Scenarios have been added for the [restful-ecommerce APIs](https://github.com/mfaisalkhatri/restful-ecommerce)

## :hammer_and_wrench: Talking more about the Scenarios Covered in this project:
You will get the answers to the following questions and its respective working code example with [Playwright Java](https://playwright.dev/java/docs/api-testing#writing-api-test) framework in this repository:
- How to write tests for `GET` requests?
- How to write tests for `POST` requests?
- How to write tests for `PUT` requests?
- How to write tests for `PATCH` requests?
- How to write tests for `DELETE` requests?
- How to handle the `authentication` requests?
- How to verify the Response Body?
- How to verify the Response Status Code?
- How to extract value from Response Body?
- How to create `POJO` for passing values to request body?
- How to use `Lombok` to generate `Getters` and `Setters`?
- How to use `Lombok` for writing the builder pattern code?
- How to use Builder Pattern for test data generation using [Data Faker](https://github.com/datafaker-net/datafaker)?
- How to write end-to-end api tests?
- How to write Happy Path scenarios for the APIs?
- How to write Sad Path scenarios for the APIs?
- How to log the Response ?

---

## :wrench: Prerequisites

Make sure the following are installed before running the project:

| Tool | Version | Notes |
|---|---|---|
| Java (JDK) | 17+ | Tested with Temurin 17 |
| Maven | 3.0+ | Required by the enforcer plugin |
| Docker | Latest | Required to run restful-booker and restful-ecommerce locally |
| Git | Any | To clone the repository |

---

## :package: Project Structure

```
api-testing-playwright-java-testng/
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── log4j2.xml                  # Log4j2 logging configuration
│   └── test/
│       └── java/io/github/mfaisalkhatri/api/
│           ├── logger/
│           │   └── Logger.java             # Custom response logger (pretty-prints JSON)
│           ├── manager/
│           │   └── RequestManager.java     # Playwright APIRequestContext wrapper
│           ├── reqres/
│           │   ├── data/                   # EmployeeData POJO + DataFaker builder
│           │   └── tests/                  # GET/POST/PUT/PATCH/DELETE tests → reqres.in
│           ├── restfulbooker/
│           │   ├── data/                   # Booking/Token POJOs + DataFaker builders
│           │   └── tests/                  # End-to-end booking tests
│           └── restfulecommerce/
│               ├── testdata/               # Order/Token POJOs + DataFaker builders
│               ├── HappyPathTests.java     # Happy path order scenarios
│               └── SadPathTests.java       # Sad path / negative scenarios
├── test-suite/
│   ├── testng.xml                          # Master suite (runs all suites below)
│   ├── testng-reqres.xml
│   ├── testng-restfulbooker.xml
│   ├── testng-restfulecommerce.xml
│   └── testng-restfulecommerce-*.xml       # Granular ecommerce suites
├── docker-compose-restfulbooker.yml        # Docker setup for restful-booker (port 3001)
├── docker-compose-ecommerce.yml            # Docker setup for restful-ecommerce (port 3004)
└── pom.xml
```

---

## :rocket: Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/tijonthomas/api-testing-playwright-java-testng.git
cd api-testing-playwright-java-testng
```

### 2. Build the project

```bash
mvn clean install -DskipTests
```

### 3. Start local services (Docker)

The restful-booker and restful-ecommerce tests run against locally hosted APIs via Docker.

```bash
# Start restful-booker (runs on http://localhost:3001)
docker compose -f docker-compose-restfulbooker.yml up -d

# Start restful-ecommerce (runs on http://localhost:3004)
docker compose -f docker-compose-ecommerce.yml up -d
```

---

## :test_tube: Running Tests

### Run all tests (all three APIs)

> **Note:** reqres.in requires an API key. Get yours at [reqres.in](https://reqres.in/) and pass it via `-Dapi-key`.

```bash
mvn clean test -Dapi-key=<YOUR_REQRES_API_KEY>
```

### Run tests for a specific API

```bash
# reqres.in tests only
mvn clean test -Dsuite-xml=test-suite/testng-reqres.xml -Dapi-key=<YOUR_REQRES_API_KEY>

# restful-booker tests only
mvn clean test -Dsuite-xml=test-suite/testng-restfulbooker.xml

# restful-ecommerce tests only (full suite)
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce.xml

# restful-ecommerce granular suites
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce-postandgetorder.xml
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce-updateorder.xml
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce-partialupdateorder.xml
mvn clean test -Dsuite-xml=test-suite/testng-restfulecommerce-deleteorders.xml
```

### Stop local services after testing

```bash
docker compose -f docker-compose-restfulbooker.yml down
docker compose -f docker-compose-ecommerce.yml down
```

---

## :gear: Configuration

| Parameter | How to pass | Used by |
|---|---|---|
| `api-key` | `-Dapi-key=<value>` on Maven command | reqres.in tests (`x-api-key` header) |
| `suite-xml` | `-Dsuite-xml=test-suite/<file>.xml` | Selects which TestNG suite to run (default: `test-suite/testng.xml`) |

In CI, the reqres.in API key is stored as the GitHub Actions secret `REQRES_API_KEY`.

---

## :bar_chart: CI/CD

Tests run automatically on GitHub Actions on every push to `main` or `issue-*` branches and on pull requests targeting `main`.

The pipeline:
1. Spins up restful-booker and restful-ecommerce via Docker
2. Builds the project
3. Runs all tests
4. Shuts down Docker services
5. Publishes a test report via [dorny/test-reporter](https://github.com/dorny/test-reporter)

---

## :writing_hand: Blog Links
- [What is API Testing?](https://mfaisalkhatri.github.io/2020/08/08/apitesting/)
- [How to perform End to End API Testing using Playwright with Java and TestNG](https://medium.com/@iamfaisalkhatri/how-to-perform-end-to-end-api-testing-using-playwright-with-java-and-testng-26b318927115)
- [How to test POST API requests with Playwright Java](https://medium.com/@iamfaisalkhatri/playwright-java-api-testing-how-to-test-post-requests-4c9102d3ab03)
- [How to test GET API requests with Playwright Java](https://medium.com/@iamfaisalkhatri/playwright-java-api-testing-how-to-test-get-requests-c036b984cc6d)
- [How to test PUT API requests with Playwright Java](https://medium.com/@iamfaisalkhatri/playwright-java-api-testing-how-to-test-put-requests-d6b1d054d64b)
- [How to test PATCH API requests with Playwright Java](https://medium.com/@iamfaisalkhatri/playwright-java-api-testing-how-to-test-patch-requests-f6b0867d91e7)
- [How to test DELETE API requests with Playwright Java](https://medium.com/@iamfaisalkhatri/playwright-java-api-testing-how-to-test-delete-requests-2ff77feb0383)
- [How to Create a Custom Logger for Logging Response Details with Playwright Java](https://medium.com/@iamfaisalkhatri/playwright-java-api-testing-creating-custom-logger-for-logging-response-details-771e961d9faa)


