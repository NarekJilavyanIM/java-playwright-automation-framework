# Playwright Java Automation Framework

UI test automation for [Automation Exercise](https://www.automationexercise.com), an e-commerce practice site that injects Google Vignettes, overlay iframes, and other ads at unpredictable times.

The framework is built with Playwright for Java, JUnit 5, and Allure. Ads are neutralized without relying on brittle close-button locators. Every test records a screenshot, the final page URL, and a Playwright trace, and all three are attached to the Allure report automatically.

## Tech stack

| Layer | Choice |
| --- | --- |
| Language | Java 17+ |
| Build | Maven |
| Browser automation | Playwright for Java `1.49.0` |
| Test runner | JUnit 5 |
| Reporting | Allure (`allure-junit5`) |
| Target application | https://www.automationexercise.com |

## Prerequisites

- JDK 17 or newer
- Maven 3.8+

- Network access on first run so Maven can resolve dependencies and Playwright can download Chromium
- Optional: [Allure Commandline](https://docs.qameta.io/allure/) if you prefer `allure serve` over the Maven plugin

Install Playwright browsers once after cloning:

## Project structure

```
src/
├── main/java/com/framework/
│   ├── ads/AdHandler.java              Ad-request blocking, vignette URL scrub, popup close
│   ├── base/
│   │   ├── BasePage.java               Shared navigation, safe click/fill, URL fallback
│   │   └── PageContext.java            ThreadLocal Playwright page for the current test
│   ├── config/ConfigManager.java       Properties + system/env overrides
│   ├── constants/
│   │   ├── Constants.java              Timeouts, defaults, routes, paths, enums
│   │   ├── Locators.java               Element selectors, grouped by page
│   │   ├── Users.java                  Generated-user defaults
│   │   ├── Messages.java               UI text and assertion messages
│   │   ├── PaymentData.java            Card details and order comment
│   │   └── AdsConstants.java           Vignette hash and ad URL fragments
│   ├── logging/CatchHandler.java       Logs catch-block errors and skips the current test
│   ├── data/AccountData.java           Registration payload for a new account
│   ├── utils/ProductSearch.java        Case-insensitive product-name matching
│   └── pages/                          Singleton page objects
│       ├── HomePage.java
│       ├── LoginPage.java
│       ├── SignupPage.java
│       ├── ConfirmationPage.java
│       ├── ProductsPage.java
│       ├── CartPage.java
│       ├── CheckoutPage.java
│       └── PaymentPage.java
└── test/
    ├── java/com/framework/
    │   ├── base/BaseTest.java          Browser lifecycle and tracing
    │   ├── listeners/
    │   │   └── TestWatcherExtension.java  Screenshot, URL, and trace attachments
    │   └── tests/
    │       ├── RegisterUserTest.java
│       ├── SearchProductTest.java
│       ├── PlaceOrderLoginBeforeCheckoutTest.java
    │       └── ScrollUpUsingArrowTest.java
    └── resources/
        ├── config.properties
        └── allure.properties
```

Generated at runtime (git-ignored):

```
target/
├── allure-results/     Raw Allure files, including attached screenshots and traces
├── allure-report/      HTML report after `mvn allure:report`
└── traces/             Playwright `.zip` traces, one per test
```

## Architecture

### Configuration

`ConfigManager` reads `src/test/resources/config.properties`. A JVM system property wins first, then an environment variable, then the file.

| Property | Environment | Default | Purpose |
| --- | --- | --- | --- |
| `base.url` | `BASE_URL` | `https://www.automationexercise.com` | Application under test |
| `headless` | `HEADLESS` | `true` | Headless Chromium |
| `timeout` | `TIMEOUT` | `30000` | Default Playwright timeout (ms) |
| `browser` | `BROWSER` | `chromium` | `chromium`, `firefox`, or `webkit` |

The checked-in `config.properties` sets `headless=false`, so `mvn test` opens a visible browser. If that key is missing, `ConfigManager` falls back to `true`.

New accounts use `Users.Generated`. Test Case 9 searches for `Constants.TestData.SEARCH_PRODUCT` (`tshirt`) and keeps product names that contain any of `SEARCH_PRODUCT_OPTIONS` (`tshirt`, `t-shirt`, `t shirt`). Locators, UI text, routes, timeouts, and payment details are in `com.framework.constants`.

Examples:

```bash
mvn test -Dheadless=false
mvn test -Dbrowser=firefox
HEADLESS=false mvn test
```

### Test lifecycle (`BaseTest`)

Each test gets a fresh Playwright instance, browser, context, and page. `BaseTest` stores the page in `PageContext` (a `ThreadLocal`) so parallel execution stays isolated. Page objects do not keep a `Page` field; they read the current thread's page.

`@BeforeEach`:

1. Launch the configured browser (viewport `1920x1080`).
2. Start a Playwright trace (screenshots, DOM snapshots, sources).
3. Register the `#google_vignette` URL listener.
4. Attach `AdHandler` (request blocking, popup closer).

`@AfterEach`:

1. Stop tracing if the listener did not already stop it.
2. Close context, browser, and Playwright.
3. Clear `ThreadLocal` values.

### Page objects (`BasePage` + `pages`)

Page classes are singletons (`getInstance()`). They encapsulate locators and actions and do not expose raw selectors to tests. Allure steps are declared in the test methods with `Allure.step`, not on page-object methods.

`BasePage` retries interactions that fail:

- `safeClick` retries up to three times, then skips the test.
- `safeClickAndExpectUrl` retries navigation and, if the click still does not reach the expected URL, opens that URL directly.

### Ad handling (`AdHandler`)

Automation Exercise injects ads in two ways that this framework handles:

1. **Network** — `AdHandler` aborts requests whose URL contains a fragment from `AdsConstants.AD_URL_FRAGMENTS` (`googleads`, `googlesyndication`, `doubleclick.net`, `google_vignette`, and the other entries in that list).
2. **URL hash** — `AdHandler` strips `#google_vignette` with `history.replaceState` on load and on main-frame navigation. `BaseTest` also strips that hash with `history.pushState` on load. Popups opened by the page are closed.

`safeClick` does not click ad dismiss buttons. A click that is still covered after three attempts skips the test.

### Error handling (`CatchHandler`)

There is no logging library. A skipped test carries the reason on its `TestAbortedException`, and JUnit prints that message.

- **Test actions** (`BasePage` clicks, fills, waits, navigation): a click is retried up to three times. After the last failure a `TestAbortedException` is thrown. JUnit **skips** that test and continues with the next one.
- **Ad cleanup / Playwright event callbacks**: a skip is scheduled. The next page action aborts that test; remaining tests still run.
- **Teardown and Allure attachments**: a failure is ignored so a reporting problem cannot change a pass into a skip.

### Reporting (`TestWatcherExtension`)

JUnit runs `AfterTestExecutionCallback` **before** `@AfterEach`, while the page and context are still open. After every test (pass or fail) the listener:

1. Takes a full-page screenshot and attaches it to Allure.
2. Attaches the current page URL.
3. Stops Playwright tracing, writes `target/traces/<test-name>-<timestamp>.zip`, and attaches that zip to Allure.

Failed tests are labeled `Failure screenshot`. Passed tests are labeled `Screenshot`.

## Automated test cases

Each case is its own class under `src/test/java/com/framework/tests/`.

| ID | Class | What it verifies |
| --- | --- | --- |
| 1 | `RegisterUserTest` | Sign up a unique user, confirm `Logged in as`, delete the account |
| 9 | `SearchProductTest` | Open Products, search for `tshirt`, confirm the Searched Products title, and confirm every shown name matches `tshirt`, `t-shirt`, or `t shirt` |
| 16 | `PlaceOrderLoginBeforeCheckoutTest` | `@BeforeEach` registers a user and logs out. The test logs in as that user, adds a product, checks out, pays, then deletes the account |
| 25 | `ScrollUpUsingArrowTest` | Scroll to `SUBSCRIPTION`, click `#scrollUp`, confirm the hero heading |

Test Case 1 and Test Case 16 each register a unique email (`ae.user.<timestamp>@mailsac.com`). Test Case 16 deletes that account at the end.

## Usage

### Run all tests

```bash
mvn clean test
```

### Run a single test

```bash
mvn test -Dtest=SearchProductTest
```

### Run headed (visible browser)

```bash
mvn test -Dheadless=false
```

### First-time browser install

```bash
mvn exec:java -Dexec.args="install chromium"
# or all engines:
mvn exec:java -Dexec.args="install"
```

## Allure report

Raw results land in `target/allure-results` (configured in `allure.properties` and Surefire).

Generate a static HTML report:

```bash
mvn allure:report
```

The report is written to `target/allure-report`.

Serve an interactive report (run tests first):

```bash
mvn allure:serve
```

Each test in the report includes:

- Steps declared with `Allure.step` inside each test method
- A PNG screenshot of the last page
- The last page URL
- A Playwright trace zip

## Playwright traces

Traces are written under `target/traces/` and also attached inside Allure. They contain screenshots, DOM snapshots, network activity, and console output for the full test.

Open a trace from the command line:

```bash
mvn exec:java -Dexec.args="show-trace target/traces/<file>.zip"
```

Or download the **Playwright Trace** attachment from the Allure test page and open it with:

```bash
npx playwright show-trace path/to/trace.zip
```

## Parallel execution

`mvn test` runs one class at a time. Pass `-Dparallel` to run classes concurrently. The default thread count is `parallel.threads` in `pom.xml` (currently 4):

```bash
mvn test -Dparallel
mvn test -Dparallel -Dparallel.threads=4
```

Each test still starts its own browser, context, and page. Page objects read the page for the current thread.
