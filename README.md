# Currency Exchanger Demo App

A demo Android application for exchanging currencies.

The user has a multi-currency account with starting balance of 1000 Euros (EUR). He can convert any currency to any if the rate is provided by the API but the balance can't fall below zero.

For example, user inputs 100.00, picks Euros to sell and Dollars to buy.

Also, there may be a commission fee for the currency exchange operation. The first five currency exchanges are free of charge but afterwards they're charged 0.7% of the currency being traded. If user exceeds 15 conversions per day, then the commission for the next conversions will be [1.2% + 0.3 Euros value equivalent of the converted currency].

For example:

You have converted 100.00 EUR to 110.00 USD. Commission Fee - 0.70 EUR.

--------

Rates API
URL: https://api.apilayer.com/fixer/latest

## Documentation

### Libraries used in this app:
- For Network and serialization -> Retrofit, okhttp3, Moshi

- For UI -> Jetpack Compose

- For dependency injection -> Hilt

- For Unit Tests -> Junit, MockitoKotlin, Coroutines, Arch core

- For Logs -> Timber

- For Asynchronous code -> Coroutines


### Architecture used in this app:
The whole project is modularized and based on clean architecture pattern.

For the presentation layer, MVVM + MVI architecture is selected.
