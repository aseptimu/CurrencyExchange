# CurrencyExchange API

CurrencyExchange is a REST API that implements CRUD interface over a database. It provides methods to create, read and update currencies and exchange rates.

## API Endpoints

### GET /currencies

Returns a list of all currencies in the database.

### GET /currency/USD

Returns the currency record for the given currency code.

### POST /currencies

Adds a new currency record to the database. The data is passed in the request body as form fields (x-www-form-urlencoded). Form fields - name, code, sign. The response contains a JSON representation of the record inserted into the database, including its ID.

### GET /exchangeRates

Returns a list of all exchange rates in the database.

### GET /exchangeRate/USDRUB

Returns the exchange rate record for the given currency pair. The currency pair is specified by consecutive currency codes in the request address.

### POST /exchangeRates

Adds a new exchange rate record to the database. The data is passed in the request body as form fields (x-www-form-urlencoded). Form fields - baseCurrencyCode, targetCurrencyCode, rate.

### PATCH /exchangeRate/USDGBP

Updates the existing exchange rate record in the database. The currency pair is specified by consecutive currency codes in the request address. The data is passed in the request body as form fields (x-www-form-urlencoded). The only form field is rate.

### GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT

Calculates the transfer of a certain amount of funds from one currency to another. The API attempts to get the exchange rate in one of three scenarios:

1. There is a currency pair AB in the ExchangeRates table - it takes its rate
2. There is a BA currency pair in the ExchangeRates table - it takes its rate and calculates the reverse to get AB
3. There are USD-A and USD-B currency pairs in the ExchangeRates table - it calculates the AB rate from these rates

## Error Handling

In case of an error, the API returns a JSON response with a "message" key that describes the error. Possible error messages include:

- "Currency not found": when a currency is not found in the database
- "Exchange rate not found": when an exchange rate is not found in the database
- "Database error": when the database is not available or query to the database failed

## Response Codes

The API supports the following HTTP response codes:

- 200: successful request
- 400: invalid request parameters
- 404: resource not found
- 409: resource already exists
- 500: internal server error