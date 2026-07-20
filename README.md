# Shopping List Coding Challenge

## Overview

This is a Spring Boot MVC shopping list application built for the Cyber Media developer coding challenge.

The application allows users to manage shopping lists, add manually entered items, record item prices, track the total cost of a list, set a budget, and receive a warning when the budget is exceeded.

This feature branch adds Story 10 by introducing Spring Security login support. Three users are seeded for review and testing, and each user is associated with their own shopping list.

The original submitted branch contains the core shopping list implementation without authentication. This branch keeps the Story 10 work separate so that it can be reviewed independently.

## Tech Stack

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- Spring Security
- H2 in-memory database
- Maven
- JUnit 5
- Mockito

## How to Run

### Prerequisites

- Java 21
- Maven 3.6.3 or later

### Run the Application

From the project root:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

### Login Details

This feature branch adds Spring Security login support.

The application seeds three users for testing:

- `aaron`
- `david`
- `graham`

Password for all seeded users:

```text
password
```

After login, each user is associated with their own seeded shopping list.

### Run the Tests

```bash
mvn test
```

## H2 Database Console

The application uses an H2 in-memory database for simplicity during the coding challenge.

If enabled in `application.properties`, the H2 console can be accessed at:

```text
http://localhost:8080/h2-console
```

JDBC URL:

```text
jdbc:h2:mem:shoppingdb
```

When Spring Security is enabled, the H2 console needs a small development-only security exception because it uses browser frames and does not follow the normal application CSRF flow.

CSRF remains enabled for the main application, but is ignored for the H2 console only. Frame options are configured as same-origin so that the H2 console can render correctly during local development.

## Features Implemented

- Spring Security login support
- Seeded test users: `aaron`, `david`, and `graham`
- User-specific shopping lists after login
- H2 console access configured for local development with Spring Security enabled
- Display shopping list name from the database
- Add free-text shopping list items
- Store item prices internally in pence
- Enter item prices in the UI as pounds and pence, for example `1.45`
- Display item prices as pounds and pence
- Prevent duplicate items using a normalised item name
- Delete items
- Mark items as purchased or not purchased
- Reorder items using move up and move down buttons
- Persist item display order
- Calculate total list price
- Set a shopping list budget
- Display budget
- Warn the user when the total price exceeds the budget
- Basic Thymeleaf view styling
- Unit tests for key service logic

## Assumptions

Shopping list items are added manually as free-text names. Prices are also entered manually by the user because the challenge brief does not require supermarket integration, automatic product lookup, or a product catalogue.

Prices are stored as integer pence values rather than decimal pounds. This avoids floating point precision issues when dealing with money-like values.

Each item has a persisted display order so that the user's chosen order is retained when the list is reloaded.

The application uses seeded users for challenge review and demonstration purposes. It does not include user registration, password reset, or production-level account management.

## Validation and Error Handling

The application includes validation for common user input issues:

- Item name is required
- Duplicate item names are rejected after normalisation
- Item price cannot be negative
- Budget cannot be negative
- Prices and budgets are converted from pounds to pence before being saved

Duplicate checking is performed using a normalised item name, so entries such as `Milk`, `milk`, and `MILK` are treated as the same item.

## Testing Approach

JUnit and Mockito have been used to test the main service-layer behaviour without needing to start the full Spring application context for every test.

The tests cover key business rules such as:

- Adding an item
- Saving item price in pence
- Rejecting duplicate items
- Rejecting invalid prices
- Calculating total list price
- Updating budget
- Rejecting invalid budget values
- Toggling purchased state

This level of testing was chosen to give confidence in the core business logic while keeping the solution appropriate for the time-limited nature of the challenge.

## Stories Completed on This Feature Branch

Story 10 has been implemented on this feature branch by adding Spring Security login support and seeded test users.

This work has been kept separate from the originally submitted branch so that the original submission remains stable and reviewable, while the additional Story 10 implementation is also available for assessment.

## Stories Not Completed

Story 9 has not been implemented.

## Technology Choices and Time Taken

I spent a little longer than originally planned because I chose to use Thymeleaf, Mockito, and Spring Security, which were less familiar to me than the core Java and Spring Boot parts of the stack.

I made this choice deliberately after learning that these technologies are relevant to Cyber Media. I felt it would be more beneficial to investigate and use them rather than avoiding them purely to save time.

The result is a solution that covers the core shopping list requirements while also showing targeted learning around technologies likely to be useful in the role.

## Use of AI Assistance

For transparency, I used AI assistance as a pair-programming aid during the challenge.

I used it to:

- Help check that I had not missed important requirements
- Discuss design options and trade-offs
- Get started with areas I was less familiar with
- Generate some example code for speed and efficiency
- Review and refine parts of the implementation and README

All code included in this solution has been reviewed, adapted, and understood by me before check-in.

## Known Limitations

- Users are seeded for challenge/demo purposes rather than registered dynamically
- The application uses Spring Security default form login rather than a custom login page
- Passwords and seeded credentials are intended for local review/demo use only
- The database is in-memory and resets when the application restarts
- Item prices are manually entered by the user
- There is no external product catalogue
- There is no supermarket/product API integration
- Error handling is intentionally simple
- The UI is functional rather than highly polished

## Future Improvements

Possible future improvements include:

- Implement Story 9
- Add user registration
- Add password reset/change password support
- Add support for multiple shopping lists per user
- Add product catalogue support
- Seed products from an external open dataset
- Add editing of existing item names and prices
- Add better form-level validation feedback
- Improve responsive/mobile styling
- Add controller tests using MockMvc
- Add integration tests using the H2 database

## Design Notes

The application separates the main responsibilities into controller, service, repository, and entity layers.

- The controller handles HTTP requests and form submissions.
- The service layer contains the business logic, including validation, duplicate checks, total price calculation, budget updates, and item ordering.
- The repository layer uses Spring Data JPA for database access.
- Spring Security handles authentication for the seeded users.

The current shopping list is resolved through the service layer based on the logged-in user. This keeps the controller focused on handling web requests and keeps the user/list lookup logic away from the view layer.

## Money Handling

Prices and budgets are stored as integer pence values.

For example:

```text
£1.45 is stored as 145
£25.00 is stored as 2500
```

The UI allows the user to enter values in pounds and pence using a decimal point. The controller converts those values into pence before passing them to the service layer.

This avoids floating point rounding problems and keeps calculations simple.

## Duplicate Item Handling

Item names are normalised before duplicate checking.

For example, these are treated as duplicates:

```text
Milk
 milk
MILK
```

This prevents the same item being added multiple times with slightly different spacing or casing.

## Display Ordering

Each shopping list item has a `displayOrder` value.

New items are added to the bottom of the list. When items are moved up or down, their display order values are swapped. When an item is deleted, the remaining items are renumbered so the ordering remains clean and predictable.

## Branch Notes

The original submitted branch contains the completed core implementation.

This feature branch adds Story 10 login support and keeps that additional work separate from the original submission.
