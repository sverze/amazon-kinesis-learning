# Lambdas

Scaling lambda. Reacts to Cloud Watch alarms (via SNS) that trigger based off `GetRecords.IteratorAgeMilliseconds`

## Deploying
Build the jar

```
mvn clean package
``` 

Then upload to AWS Lambda. Make sure you have the timeout and memory requirements set to something sensible and an appropriate IAM role is attached.
`