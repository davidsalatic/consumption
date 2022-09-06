# Consumption application

## Architecture and running the application

- Since this is a monolith spring-boot application with a H2 database, no special setup is required other than building
  & starting the application
- Runs on default port: `8080`
- If necessary, the H2 database can be explored using the console: http://localhost:8080/h2-console after running the
  application
    - username: `sa`
    - no password
    - JDBC url: `jdbc:h2:mem:testdb`

## Testing the application

- Since testing should start by uploading the CSV files, a basic `index.html` page was created for browse/upload file
  requirements
    - the page can be accessed by navigating to http://localhost:8080

## Step 1

- Start by uploading Profiles and Fractions in CSV format in the upper section<br>
  **CSV rules:**
    - by default, the first row will be skipped as it is assumed that the first row is a headline
    - by default, the expected format for profiles and fractions is:<br>
      **month,profile,fraction**
    - expected values are:
        - month is a 3-letter abbreviation (JAN,FEB,MAR...)
        - profile is text
        - fraction is a decimal number
        - example: `JAN,A,0.5`
    - note that additional configuration is possible by changing the `application.yml` file, where you can:
        - change the order of the columns
        - skip or include the first row of the file
        - change the delimiter from `,` to any custom delimiter
        - change the month mapping (if your months in the file aren't in format (JAN,FEB,MAR...)
    - you do not need to update the `application.yml` file if your format is the default one


- When you upload the file, **the application will save the profiles & fractions from the file to the database**
- Return to the homepage at http://localhost:8080

## Step 2

- Upload Meter Readings in CSV format in the lower section<br>
  **CSV rules:**
    - by default, the first row will be skipped as it is assumed that the first row is a headline
    - by default, the expected format for meter readings is:<br>
      **meterID,profile,month,meter reading**
    - expected values are:
        - meterID is text
        - profile is text
        - month is a 3-letter abbreviation (JAN,FEB,MAR...)
        - meter reading is a number
        - example: `0001,A,JAN,10`
    - note that additional configuration is possible as described in Step 1


- When you upload the file, **the application will save the meters & meter readings from the file to the database**

## Step 3

- Consumptions can be retrieved by making a GET request with URL:
  ### http://localhost:8080/meters/{meterId}/month/{month}/consumption
- where:
    - **{meterId}** is the meterID that was in the CSV meter readings file
    - **{month}** is the full name of the month (january,february,march...)

#### Example:

- If you want to retrieve consumptions for meter `0001` in february, the URL should look like:<br>
  `http://localhost:8080/meters/0001/month/february/consumption`

## Brief summary of the implementation

### Model

- The model contains 4 entities:
    - Profile
    - Fraction (which represents a ratio [0.0 - 1.0] for a profile in a month)
    - Meter
    - Meter reading (which represents a number displayed on a meter in a given month for a given profile)
- Entities **Profile** & **Meter** contain a `publicId` (which comes from the CSV file) as well as a database "private"
  id

### CSV parsing

- CSV parsing was done manually to avoid external dependencies & to allow more detailed customization in the future
- CSV parsing saves the data to the database, so there is not some init SQL script that inserts the data, everything is
  inserted by parsing the CSV files
- Additional configuration was created for CSV parsing in `appliation.yml` to allow parsing of CSV files that maybe have
  a different order of the columns

### Tests

- All code logic is covered by unit tests (excluding getters&setters and similar utility methods)

## Future steps

- Since the entities are solidly connected, we could generate various statistics & measures by creating more endpoints
  in the API
- Migrate from H2 to a permanent database to persist data
- Implement user login & security
- Explore 3rd party tools for CSV parsing
- Think about designing a microservice structure (i.e. move parsing logic to a CSVParseMicroservice)
- Write feature & automation tests
- Introduce lombok to reduce boilerplate (it wasn't used in this project as sometimes IDEs need additional configuration
  to allow annotation processing, so it wasn't used for easier testing)
- Use big decimal instead of double to increase accuracy


*Example CSV files are located in `src/test/test files`
