package com.domwong.lambda;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.ScalingType;
import com.amazonaws.services.kinesis.model.UpdateShardCountRequest;
import com.amazonaws.services.kinesis.model.UpdateShardCountResult;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;

/**
 * Reshard is a simple lambda that listens for ALARM and updates the shard count for the given lambda
 */
public class Reshard implements RequestHandler<SNSEvent, Object> {
    public Object handleRequest(SNSEvent request, Context context){
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        context.getLogger().log("Invocation started: " + timeStamp);
        String msg = request.getRecords().get(0).getSNS().getMessage();
        context.getLogger().log(msg);

        Gson gson = new Gson();
        SNSAlarm alarm = gson.fromJson(msg, SNSAlarm.class);
        if (!"ALARM".equals(alarm.getNewStateValue())) {
            // nothing to do here
            context.getLogger().log("Alarm state is not ALARM, is "+alarm.getNewStateValue());
            return null;
        }
        Optional<SNSAlarm.Dimension> dim = Arrays.stream(alarm.getTrigger().getDimensions())
                .filter(d -> "StreamName".equals(d.getName()))
                .findFirst();
        if (!dim.isPresent()) {
            context.getLogger().log("Could not find stream name from alarm");
            return null;
        }
        String streamName = dim.get().getValue();
        AmazonKinesis client = AmazonKinesisClientBuilder.defaultClient();
        DescribeStreamResult descRes = client.describeStream(streamName);
        int currShardCount = (int)descRes.getStreamDescription() .getShards().stream()
                .filter(s -> s.getSequenceNumberRange().getEndingSequenceNumber() == null) // get only open shards
                .count();
        context.getLogger().log("Current shard count is " + currShardCount);
        UpdateShardCountResult updateRes = client.updateShardCount(new UpdateShardCountRequest()
                .withTargetShardCount(currShardCount*2 > 500 ? 500 : currShardCount*2)
                .withStreamName(streamName)
                .withScalingType(ScalingType.UNIFORM_SCALING));
        context.getLogger().log("Shard count updated to "+ updateRes.getTargetShardCount());

        timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        context.getLogger().log("Invocation completed: " + timeStamp);
        return null;
    }


}