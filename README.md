# Intellisense test project

# How to build and deploy
1. Download the project from Github
2. Build the docker image using docker build -t demointelli .
3. Run using docker run -p 8080:8080 demointelli
4. Run using postman, I will attach the collection to import in Postman and also I included the curl script

curl --location --request POST 'http://localhost:8080/api/data' \
--header 'Content-Type: application/json' \
--data-raw '{"period":60}'