# JavaDynamoDBService

A Java project template providing CRUD services for a DynamoDB table, using AWS Lambda and API Gateway, deployed with AWS SAM and GitHub Actions.

This project is based on the AWS Java SDK for Amazon DynamoDB:
https://mvnrepository.com/artifact/software.amazon.awssdk/dynamodb
https://github.com/aws/aws-sdk-java-v2

### Requirements

-   Java 1.8+
-   Apache Maven
-   [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
-   Docker

### Customize

<!-- -   Find and replace `Typeahead` with the name of the app (upper and lowercase A) -->
<!-- -   Find and replace `Searchitem`/`Searchitems` with the table name (upper and lowercase A) -->
<!-- -   Find and replace `us-east-1` with the AWS region -->

-   Update the class variables and methods in `BaseEntity.java` to suit your table structure
-   Update the methods in `EntityUtils.java` to suit your table structure and logic.

### Deploy manually

-   `make deploy`

### Run locally

-   `make build && sam local start-api --port 8000`

### Setup GitHub actions

Once the repo is setup on GitHub, add AWS secrets to GitHub Actions for this repo:

-   `gh secret set AWS_ACCESS_KEY_ID`
-   `gh secret set AWS_SECRET_ACCESS_KEY`
