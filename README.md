# 🚀 UniTrade: Your Campus Marketplace

UniTrade is a full-stack e-commerce platform built for students, by students. It's designed to be the go-to place for buying and selling second-hand goods—like textbooks, furniture, and electronics—within a trusted platform across universities. It's built with a modern, robust tech stack designed for security, scalability, and a slick user experience.

## 🤔 The Problem

Let's be real—student life is expensive. We often need things for a short time and have perfectly good stuff to sell when we're done with it. But finding a buyer or seller on campus is a pain. You either shout into the void on social media or deal with the chaos of public marketplaces.

UniTrade cuts through the noise by creating a dedicated, student-only space for these exchanges.

## ✨ Core Features

*   **Secure User Accounts:** Log in securely with JWT-based authentication. Only students from the university can join.
*   **Browse & Search Listings:** Easily find what you need with a clean, responsive interface.
*   **AI-Powered Visual Search:** Got a picture of what you want? Upload it, and our AI will find visually similar items listed for sale.
*   **Persistent Shopping Cart:** Add items to your cart and come back later—they'll still be there.
*   **User Profiles & Listing Management:** Manage your own listings, view your purchase history, and update your profile.
*   **Secure Payments:** Integrated with Stripe for safe and reliable transactions.
*   **Admin vs. User Roles:** A secure backend allows for different user permissions (e.g., an admin can manage the platform, while users can buy and sell).

## 🛠️ Tech Stack & Architecture

The platform is built as a full-stack, decoupled application. The backend is a secure REST API, and the frontend is a dynamic single-page application.

| Component                 | Technology                                                                                                  | Purpose                                                                |
| ------------------------- | ----------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------- |
| **Backend**               | [**Spring Boot**](https://spring.io/projects/spring-boot) (Java)                                            | Powers the secure REST API, business logic, and data management.       |
| **Frontend**              | [**React**](https://reactjs.org/) with [**Redux Toolkit**](https://redux-toolkit.js.org/)                     | Creates a fast, responsive, and modern user interface.                 |
| **Database**              | [**PostgreSQL**](https://www.postgresql.org/)                                                               | Stores all user, product, and order data reliably.                     |
| **Payment Gateway**       | [**Stripe**](https://stripe.com/)                                                                           | Handles secure, PCI-compliant credit card transactions.                |
| **AI & Vector Search**    | [**Spring AI**](https://github.com/spring-projects/spring-ai) & [**Chroma DB**](https://www.trychroma.com/) | Generates vector embeddings from images for our visual search feature. |
| **Security**              | [**Spring Security**](https://spring.io/projects/spring-security) with JWT                                  | Secures the entire application, protecting user data and endpoints.    |
| **Deployment**            | **AWS**                                                                                                     | The entire application will be deployed to a cloud environment on AWS. |

### Architecture

The backend follows a classic layered **Controller-Service-Repository** architecture to ensure a clean separation of concerns. The frontend is built on a **component-based architecture**, making the UI modular and easy to maintain. All communication between the two happens via a **REST API using JSON**.
