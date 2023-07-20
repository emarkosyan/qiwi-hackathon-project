# Qiwi Hackathon project

**Summary**:

Implement a console utility that displays the CBR currency rates for a certain date. 
To get the rates, use the official API of the Central Bank of the Russian Federation https://www.cbr.ru/development/sxml/.

**Inteface**:
currency_rates --code=USD --date=2022-10-08

**Excepted output**
USD (Доллар США): 61,2475

### How to run this CLI

1) clone project

`https://github.com/emarkosyan/qiwi-hackathon-project.git`

2) compile

`javac .\src\main\HackatonMain.class`

3) run with this template

`java main.HackatonMain currency_rates --code=<your_currency_code> --date=<your_date>`
