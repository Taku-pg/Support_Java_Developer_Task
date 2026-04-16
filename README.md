## Setup

### Tech
- Java 25
- Spring boot 3.5.13
- PostgreSQL 18.3
- Maven 3.9.14
- Json-Patch 1.13
- Mockito 5.23.0

### Instruction
>[!NOTE]
>Following commands are based on Windows. Please replace adequate commands as your host OS.

>[!NOTE]
>Using Spring and PostgreSQL port as default (8080 for Spring, 5432 for PostgreSQL).
>
>If your local environment setting is different from default port, please modify application.yaml file with proper ports.
 
#### Database Setup
1. Create User and Database on local PostgreSQL
   
   ```
   psql -U yourPostgreSuperUser -c "CREATE USER test WITH PASSWORD 'test';"
   createdb -U yourPostgreSuperUser -O test task_db
   ```
   
#### Project Setup
1. Clone this repositiry
   
   ```
   git clone https://github.com/Taku-pg/Support_Java_Developer_Task.git
   ```
   
2. Go to the project folder
   
   ```
   cd Support_Java_Developer_Task
   ```

3. Build the project
   
   ```
   mvn clean install
   ```

4. Run application

   ```
   mvn spring-boot:run
   ```
>[!NOTE]
>Please make sure to free port 8080 and 5432.
>
>Dummy producer data is already included into project folder.

### Endpoints

Base URL: http://localhost:8080/api/v1

#### Products
1. `Get` /products

##### Description
- Retreive all products information.

##### Example response
```
[
    {
        "id": 1,
        "producerName": "MSG Networks Inc.",
        "attributes": {
            "name": "Portable PC",
            "price": "100$",
            "color": "silver"
        }
    }
]
```

2. `Post` /products

##### Description
- Create new product.

##### Example post request
```
{
    "producerId": 1,
    "products": [
        {
            "name": "Product 1",
            "description": "This is product 1"
        },
        {
            "name": "Product2",
            "price": "20$"
        }
    ]
}
```
##### Parameter

|Field|Type|Required|
|---|---|---|
| producerId | integer | true |
| products | object[] | true |
   

3. `Patch` /products/{id}

##### Description
- Modify product's attribute data with given product id.
- You need to specify http header Content-Type as "application/json-patch+json".
- "op" is represent type of operation such as "add", "replace" or "remove".
- "path" is target attrbute which you want to add/replace/remove.
- "value" needs to specify when "op" is "add" or "replace".

##### Example patch request
```
[
    {
        "op": "replace",
        "path": "/name",
        "value": "Updated Product Name"
    },
    {
        "op": "add",
        "path": "/price",
        "value": "99.99$"
    }
]
```

4. `Delete` /products/{id}

##### Description
- Delete product with given product id.

#### Producers
1. `Get` /producers

##### Description
- Retreive all producers information with their products.

##### Example response
```
[
    {
        "id": 1,
        "producerName": "MSG Networks Inc.",
        "producerProducts": [
            {
                "id": 1,
                "attributes": {
                    "name": "Product Name",
                    "price": "100$",
                    "description": "This is product 1"
                }
            }
        ]
    }
]
```

2. `Post` /producers

##### Description
- Create new producer.
- It is possible to add products.

##### Example post requset
```
{
    "producerName": "new company",
    "products": [
        {
            "name": "new phone"
        }
    ]
}
```

##### Parameter

|Field|Type|Required|
|---|---|---|
| producerName | string | true |
| products | object[] | false |

3. `Put` /producers/{id}

##### Description
- Change producer's name with given producer id.
- You specify only string value.

##### Example put request
```
new company name
```

4. `Delete` /producers/id

##### Description
- Delete producer with given producer id.
- This producer's products are also deleted.
