# The Phonebook app

## The Chalice app

````
chalice local
````

## Working with DynamoDB

Download the local version of DynamoDB

To list the tables in the local system:

````
aws dynamodb list-tables --endpoint-url http://localhost:8000
````

To create the tables:

````
aws dynamodb create-table --cli-input-json file://dynamodb-schema/phonebook-contact.json --endpoint-url http://localhost:8000 
````

To delete the tables:

````
aws dynamodb delete-table --table-name phonebook-contact --endpoint-url http://localhost:8000
````