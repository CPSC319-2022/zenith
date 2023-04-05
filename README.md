# This is the README for CPSC 319 Project Zenith.

For this project, the team will be building a CI/CD pipeline primarily in the Google Cloud Platform.

This project will involve each team building a simple blog application (supports authentication, commenting, posting new blogs), and running that blog platform through an automated testing at https://zenithblog.ca and deployment process. The blog project itself should be containerized (i.e. run in Docker). The goal is for teams to get exposure to continuous improvement (CI) and continuous deployment (CD) processes in modern software teams. The blog solution is to help showcase the core CI/CD and integration aspects of your solution. Given the purpose and the simplicity of the blog solution, teams are not allowed to use prebuild public blog libraries solutions for the development of the blog.

The team members for the project are:
Muhammad Saad Shahid |  Param Tully | Ruchir Malik | Shawn Zhu | Andrew Liu | Anusha Saleem | Cheryl Yu | Eric Wong | Anthony Baek

### The Documents Folder in our repo contains all our documents for this project(TOR, Test Plan, Requirements etc) including the Installation and User Guides

## Project Goals
### MVP
- :white_check_mark:The blog platform should support the ability for readers and contributors to login. The account login creation should be a simple form       for any users and once a user creates a post, that user will be classified as a contributor.
- :white_check_mark: Allow contributors (only) to create new blog posts
- :white_check_mark: Allow readers and contributors to comment on blog posts
- :white_check_mark: Allow contributors to delete blog posts
- :white_check_mark: Have unit tests with 80% code coverage, minimum
- :white_check_mark: The development team will be able to check in code to their branch on their revision control system and have their unit tests automatically run. The teams should build a simple web UI to show the various stages of the CI/CD progression of checked in code.
- :white_check_mark: The results of these tests should be made available to the software development team in a common Slack channel.
- :white_check_mark: There should be three environments to deploy to: dev, qa, and prod
- :white_check_mark: Pushes to non-feature branches (dev, qa, prod) will deploy the solution to the appropriate environment.


# Local Environment
## 1. Start the Server (from root)
```cd blog```
```./mvnw spring-boot:run```

## 2. Start the Frontend (from root)
```cd frontend```
```npm start```

## 3. Start the Cypress GUI (from root)
```cd frontend```
```npx cypress open```
Both e2e and component tests are included

# Deployed Website
## https://zenithblog.ca
