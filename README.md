# Kinesis scratch pad

Amazon Kinesis scratch pad. `services` directory originally forked from Kinesis learning. `lambda` directory for scaling lambdas.

## Scaling
General notes can be found [here](https://docs.aws.amazon.com/streams/latest/dev/kinesis-record-processor-scaling.html). Note the three scaling strategies:
- Scale up the instance size
- Scale out the number of EC2 instances up to shard count
- Increase the number of shards 

### Changing shard counts
You can either:
- [update the shard count directly](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_UpdateShardCount.html)
- [split a given shard](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_SplitShard.html)
- [merge shards](https://docs.aws.amazon.com/kinesis/latest/APIReference/API_MergeShards.html)

See [here for strategies on sharding](https://docs.aws.amazon.com/streams/latest/dev/kinesis-using-sdk-java-resharding.html)

### ASG actions
When you scale in you should ensure that the EC2 instances finish their work before being terminated. Look at [lifecycle hooks](https://docs.aws.amazon.com/cli/latest/reference/autoscaling/put-lifecycle-hook.html)

### Fully automated horizontal scaling
Scaling events should occur if any of:
- queue depth > threshold
- CPU usage > threshold

#### Metrics for alarms
Prefer stream level over shard level. Assumes no "hot keys". 
- GetRecords.IteratorAgeMilliseconds.Max & .Min. 
- ReadProvisionedThroughputExceeded
- WriteProvisionedThroughputExceeded


#### Naive scaling out

Iterator age
```
if (iterator age > threshold) {
    // Consumers can't keep up with producers. Double number of shards.
}
```

Above should cause CPU usage to increase as more messages processed in parallel. Should cause ASG autoscaling to kick in. At some point this could cause...


... Throughput exceptions
``` 
if (throughput exception errors on read or write) {
    // not enough read/write capacity, increase (double) shards
}
```

   
#### Naive scaling in
```
TODO
```

#### Notes
https://read.acloud.guru/auto-scaling-kinesis-streams-with-aws-lambda-299f9a0512da