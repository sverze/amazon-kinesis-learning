# Learning Amazon Kinesis Development

Originally forked from Kinesis learning.

### Notes
Assumes instance profile credentials on your EC2 instance. Required permissions as per instructions [here](https://docs.aws.amazon.com/streams/latest/dev/learning-kinesis-module-one-iam.html)

## Running it
### Build
```
mvn package

```

### Run consumer
```
java  -cp kinesis-prototype.jar com.amazonaws.services.kinesis.samples.stocktrades.processor.StockTradesProcessor StockTradesProcessor <stream name> <region>

```

### Run producer
```
java  -cp kinesis-prototype.jar com.amazonaws.services.kinesis.samples.stocktrades.writer.StockTradesWriter <stream name> <region>

```
