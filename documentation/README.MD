# API Documentation of the WebShop

The API is modeled with the RESTful API Modeling Language [RAML](https://raml.org/). The file `api.raml` contains the documentation of the api.

## Generating the API-Documentation

There are **two** ways to generate the API-Documentation

1. Docker
    - `docker-compose up`
    - Open (http://localhost:8080) to view the documentation
2. raml2html
    - *(first time)* npm install
    - `npm run build` or `npm run build:watch` to automatically build on changes
    - open the generated file `api-docs.html`