## Language performance test
Comparing languages performance for building REST API:
- Java
- NodeJS
- more to come...
## Tasks performed
#### Save API (POST request)
1. Receive JWT authentication token from the header and verify.
2. Read the JSON request body into language object structure.
3. Convert the object to string and save in a file.
4. Read data from the file as a string.
5. Convert the string to object.
5. Delete the file asynchronously.
6. Return the JSON.
7. Logs to console.
#### Calculate Interest API (GET request)
1. Receive JWT authentication token from the header and verify.
2. Read query parameters.
3. Calculate interest amount and balance amount for each month upto specified number of months.
4. Return the list of calculated { monthNum, interest, balance } for each month.

## Commands to run dockers
Build & Run Java app
```
docker build --no-cache --build-arg JAR_FILE="build/libs/*.jar" -t rajeevnaikte/lp-java .
docker run --cpus 1 --memory 2048m -p 8080:8080 rajeevnaikte/lp-java &
```
Run NodeJS app
```
docker build --no-cache -t rajeevnaikte/lp-node .
docker run --cpus 1 --memory 2048m -p 3000:3000 rajeevnaikte/lp-node &
```
## API
Headers common for all APIs
```
// Below JWT token has been hardcoded, please use the same.
Headers:
    Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MDE1OTc5MDAsImV4cCI6MTYzMzEzMzkwMCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.9fc6nHuQdPr5aDtBTMLd3nLK4lDKUacQY0-_1ZcZymA
```
POST /save
```
Body:
    // Any JSON data. A sample file is in root of this repo - request.json

Response:
    // same JSON
```
GET /calculate/monthly/interest
```
Query parameters:
    months - number of months
    principalAmount - initial amount
    annualRate - annual interest rate

Response:
    [
        {
            month: <month number>,
            interest: <interst amount for the month>,
            balance: <balance at the month>
        }
    ]
```

## SoapUI Load Test Metric description
| min |	The shortest time the step has taken (in milliseconds). |
| max |	The longest time the step has taken (in milliseconds). |
| avg |	The average time for the test step (in milliseconds). |
| last |	The last time for the test step (in milliseconds). |
| cnt |	The number of times the test step has been executed. |
| tps |	The number of transactions per second for the test step, see Calculation of TPS/BPS below.|
| bytes |	The number of bytes processed by the test step. |
| bps |	The bytes per second processed by the test step. |
| err |	The number of assertion errors for the test step. |
| rat |	Failed requests ratio (the percentage of requests that failed). |

## Performance Metric
| Metric | Java | NodeJS |
|-|-|-|
