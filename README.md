# Periplus Shopping Cart Automation Test

This project provides an automation script utilizing **Selenium WebDriver** and **TestNG** to test the login functionality and the process of adding products to the shopping cart on the [Periplus](https://www.periplus.com/) website.

## Tools & Dependencies

- Java
- Maven
- Selenium WebDriver
- TestNG
- WebDriverManager
- Dotenv Java (`io.github.cdimascio:dotenv-java`)

## How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/safira1003/periplus-cart-test.git
cd periplus-cart-test
```

### 2. Create a `.env` File

In the root directory of the project, create a `.env` file to store your Periplus account credentials, which will be used during testing:

```
PERIPLUS_EMAIL=your_email@example.com
PERIPLUS_PASSWORD=your_password
```

### 3. Run with Maven

```bash
mvn clean test
```

The test will initiate the browser (Chrome), log in to the Periplus website, search for a product by ISBN, add the product to the shopping cart, and verify that the product has been successfully added.

---
