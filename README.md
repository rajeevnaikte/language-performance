## Programming Language Runtime performance test
Comparing programming language runtime performance for building REST API:
- Java 15
- NodeJS 14
- more to come...

## Test Environment
- Docker Engine running in 8 core i9 32GB 2.4GHz Macbook Pro.
- The app is deployed inside a docker container with --cpus 1 --memory 2048m
- Two API calls run in sequence - first doing simple data save and fetch, second performing calculations.
- 5000 virtual users at a time, and repeated for 120 seconds.

## Tasks performed
#### Save & Read API (POST request)
1. Receive JWT authentication token from the header and verify.
2. Read the JSON request body and parse into language object structure.
3. Convert the object to string and save in a file.
4. Read data from the file as a string.
5. Convert the string to object.
5. Delete the file asynchronously.
6. Return the JSON.
7. Logs to console.

#### Calculate Monthly Interests API (GET request)
1. Receive JWT authentication token from the header and verify.
2. Read query parameters.
3. Calculate interest amount and balance amount for each month upto specified number of months.
4. Return the list of calculated { monthNum, interest, balance } for each month.

## Commands to run dockers
Run Java app
```
docker pull rajeevnaikte/lp-java
docker run --cpus 1 --memory 2048m -p 8080:8080 rajeevnaikte/lp-java &
```
Run NodeJS app
```
docker pull rajeevnaikte/lp-node
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

## SoapUI Load Test Metric column description (this will assist in understanding images below)
| Column Name | Description |
|-|-|
| min |	The shortest time the step has taken (in milliseconds). |
| max |	The longest time the step has taken (in milliseconds). |
| avg |	The average time for the test step (in milliseconds). |
| last |	The last time for the test step (in milliseconds). |
| cnt |	The number of times the test step has been executed. |
| **tps** |	The number of **transactions per second** for the test step.|
| bytes |	The number of bytes processed by the test step. |
| bps |	The bytes per second processed by the test step. |
| err |	The number of assertion errors for the test step. |
| rat |	Failed requests ratio (the percentage of requests that failed). |

## Performance Metric
#### Calling "Save & Read" and "Calculate monthly interests" - Java serving more TPS.
**Java**
![SaveCalculateJava](./metric/soapui-loadtest-results/Screen%20Shot%202020-10-02%20at%201.05.48%20AM.png)

*TPS changes over time*
![SaveCalculateJavaChart](./metric/soapui-loadtest-results/Screen%20Shot%202020-10-02%20at%2011.51.40%20AM.png)

| CPU % | MEM USAGE / LIMIT | MEM % | NET I/O | BLOCK I/O | PIDS |
|-|-|-|-|-|-|
| 85.01% | 438MiB / 1.944GiB | 22.01% | 744MB / 1GB | 0B / 0B | 233 |

**NodeJS**
![SaveCalculateNodeJS](./metric/soapui-loadtest-results/Screen%20Shot%202020-10-02%20at%201.13.12%20AM.png)

*TPS changes over time*
![SaveCalculateNodeJSChart](./metric/soapui-loadtest-results/Screen%20Shot%202020-10-02%20at%2012.00.21%20PM.png)

| CPU % | MEM USAGE / LIMIT | MEM % | NET I/O | BLOCK I/O | PIDS |
|-|-|-|-|-|-|
| 65.75% | 91.45MiB / 1.944GiB | 4.59% | 398MB / 1.57GB | 0B / 0B | 23 |

#### Calling "Save & Read" only - Java and NodeJS serving equal TPS.
**Java**
![SaveJava](./metric/soapui-loadtest-results/Screen%20Shot%202020-10-02%20at%2012.12.15%20AM.png)
| CPU % | MEM USAGE / LIMIT | MEM % | NET I/O | BLOCK I/O | PIDS |
|-|-|-|-|-|-|
| 43.44% | 420.7MiB / 1.944GiB | 21.13% | 796MB / 630MB | 0B / 0B | 230 |

**NodeJS**
![SaveNodeJS](./metric/soapui-loadtest-results/Screen%20Shot%202020-10-02%20at%2012.02.12%20AM.png)
| CPU % | MEM USAGE / LIMIT | MEM % | NET I/O | BLOCK I/O | PIDS |
|-|-|-|-|-|-|
| 50.35% | 105.3MiB / 1.944GiB | 5.29% | 537MB / 411MB | 0B / 0B | 23 |

## Conclusion
NodeJS has non-blocking IO, but when we use async-await, it will need to remember the outer scope variables 
and call stack. When a Java thread is blocked for an IO, it will remember the call stack and local variables and 
other threads will have availability of CPU time slices. What is exactly non-blocking IO? To understand it well 
will need to look at what is it at OS level. [How does non-blocking IO work under the hood?](https://medium.com/ing-blog/how-does-non-blocking-io-work-under-the-hood-6299d2953c74) 
is a good article explaining OS level executions. Once we understand the OS level operations 
thinking about what NodeJS event-loop does and what happens in Java will bring more clarity about above performance 
metric.
<br><br>
System resource consumption may seem bit more with Java (in the example haven't used the modular build, yet to compare how it will be). 
But Java performance improves progressively from server start-up as it warms up. We can see that in chart above. 
For Java the TPS was lower at the beginning and took time to reach higher value. Whereas NodeJS quickly reached a 
higher TPS, but eventual average TPS is lower than Java. Java is best for long time running applications and/or 
having computations, whereas NodeJS will be good at ephemeral applications, such as, Lambdas.
<br><br>
Next is comparing Python.
