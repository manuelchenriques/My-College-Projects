package server.services.pubsub;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class PubSubService {
    TopicName topicName;

    public PubSubService(String project, String tName) {
        this.topicName = TopicName.ofProjectTopicName(project, tName);
    }

    public void publishMessage(String message) throws ExecutionException, InterruptedException, IOException {
        Publisher publisher = Publisher.newBuilder(topicName).build();

        ByteString msgData = ByteString.copyFromUtf8(message);
        PubsubMessage pubMessage = PubsubMessage.newBuilder()
                .setData(msgData)
                .build();
        ApiFuture<String>  future = publisher.publish(pubMessage);
        String msgID = future.get();
        System.out.println("Message Published with ID=" + msgID);
        publisher.shutdown();
    }
}
