Rate Calculator
=======

Command line application that calculates the monthly installments of a load collected from a pool of lenders. 

Run
---
Using gradle 
```bash
gradle run -Pargs="./src/main/resources/market.csv 1000"
```

or
```bash
gradle build
java -jar ./build/libs/ratecalculator-1.0.jar ./src/main/resources/market.csv 1000
```