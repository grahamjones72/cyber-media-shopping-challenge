# Shopping List Coding Challenge

## Overview

This is a Spring Boot MVC shopping list application built for the Cyber Media developer coding challenge.

The application allows a user to manage a shopping list, add manually entered items, record item prices, track the total cost of the list, set a budget, and receive a warning when the budget is exceeded.

The submitted version uses a seeded default user and default shopping list. The data model links shopping lists to users so that the application can be extended later to support authenticated multi-user access.

## Tech Stack

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
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

## Features Implemented

- Seeded default user
- Seeded default shopping list
- Display shopping list name from the database
- Add free-text shopping list items
- Store item prices internally in pence
- Enter item prices in the UI as pounds and pence (e.g. `1.45`)
- Display item prices as pounds and pence
- Prevent duplicate items using a normalised item name
- Delete items
- Mark items as purchased / not purchased
- Reorder items using move up / move down buttons
- Persist item display order
- Calculate total list price
- Set a shopping list budget
- Display budget
- Warn the user when the total price exceeds the budget
- Basic Thymeleaf view styling
- Unit tests for key service logic

## Assumptions

Shopping list items are added manually as free-text names. Prices are also entered manually by the user because the challenge brief does not require supermarket integration, automatic product lookup, or a product catalogue in the submitted version.

Prices are stored as integer pence values rather than decimal pounds. This avoids floating point precision issues when dealing with money-like values.

Each item has a persisted display order so that the user's chosen order is retained when the list is reloaded.

The application currently uses one seeded default user and one seeded default shopping list. This keeps the submitted solution simple while leaving the model ready for authenticated multi-user access later.

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

## Stories Not Completed

I decided not to implement Stories 9 and 10 in the submitted main branch.

This was a scope-control decision. I wanted to focus on producing a working, understandable, and tested implementation of the core shopping list functionality rather than rushing additional features and reducing the overall quality of the submission.

I intend to continue Stories 9 and 10 separately on a feature branch for my own learning and interest after submission.

## Technology Choices and Time Taken

I spent a little longer than originally planned because I chose to use Thymeleaf and Mockito, both of which were less familiar to me than the core Java and Spring Boot parts of the stack.

I made this choice deliberately after learning that these technologies are relevant to Cyber Media. I felt it would be more beneficial in the long term to investigate and use them rather than avoiding them purely to save time.

The result is that the submitted solution reflects both the coding challenge requirements and some targeted learning around technologies that are likely to be useful in the role.

## Use of AI Assistance

For transparency, I used AI assistance as a pair-programming aid during the challenge.

I used it to:

- Help check that I had not missed important requirements
- Discuss design options and trade-offs
- Get started with areas I was less familiar with
- Generate some example code for speed and efficiency
- Review and refine parts of the implementation and README

All code included in the submitted solution has been reviewed, adapted, and understood by me before check-in.

## Known Limitations

- Authentication is not implemented
- The application currently uses a single seeded default user
- The database is in-memory and resets when the application restarts
- Item prices are manually entered by the user
- There is no external product catalogue
- There is no supermarket/product API integration
- Error handling is intentionally simple
- The UI is functional rather than highly polished

## Future Improvements

Possible future improvements include:

- Add Spring Security authentication
- Allow each authenticated user to have their own shopping list
- Add support for multiple shopping lists per user
- Implement Stories 9 and 10 on a feature branch
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

The current shopping list is resolved through `ShoppingListService`, which currently returns the seeded default shopping list. This keeps the rest of the application isolated from how the current list is selected and should make future authentication support easier to add.

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

## Submitted Branch

The submitted branch contains the completed core implementation described above.

Additional exploratory or unfinished work will be kept separate from the submitted solution.
