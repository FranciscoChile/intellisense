# Intellisense test project
The purpose of this project is evaluate technical skills

# How to build and deploy
1. Download the project from Github
2. Build the docker image using docker build -t demointelli .
3. Run using docker run -p 8080:8080 demointelli
4. Run using postman, you can import this curl

curl --location --request POST 'http://localhost:8080/api/data' \
--header 'Content-Type: application/json' \
--data-raw '{"period":60}'