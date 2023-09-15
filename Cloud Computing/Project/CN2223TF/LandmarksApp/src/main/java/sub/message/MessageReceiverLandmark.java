package sub.message;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.gson.Gson;
import com.google.pubsub.v1.PubsubMessage;
import firestore.Coordinates;
import firestore.Landmark;
import firestore.LandmarkResponse;
import storage.StorageAccess;
import vision.VisionAPIHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageReceiverLandmark implements MessageReceiver {

    private final Firestore db;
    private final String collectionName;
    private final StorageAccess stAcc;
    private final VisionAPIHandler visionHandle;

    public MessageReceiverLandmark(Firestore db, String collectionName, StorageAccess stAcc, VisionAPIHandler visionHandle) {
        this.db = db;
        this.collectionName = collectionName;
        this.stAcc = stAcc;
        this.visionHandle = visionHandle;
    }

    public void receiveMessage(PubsubMessage msg, AckReplyConsumer ackReply)
    {
        String data = msg.getData().toStringUtf8();

        MessageRequest message = new Gson().fromJson(data, MessageRequest.class);

        try {
            List<AnnotateImageResponse> responses = visionHandle.detectLandmarksGcs("gs://" + message.bucket +"/" + message.blob);

            LandmarkResponse landmarkResponse = new LandmarkResponse();
            landmarkResponse.id = message.id;
            landmarkResponse.origin = message.blob;
            landmarkResponse.images = new ArrayList<>();

            for(AnnotateImageResponse response : responses) {
                for (EntityAnnotation annotation : response.getLandmarkAnnotationsList()) {
                    Landmark landmark = new Landmark();
                    landmark.landmarkName = annotation.getDescription();
                    landmark.score = annotation.getScore();

                    LocationInfo info = annotation.getLocationsList().listIterator().next();

                    String blobName = "maps/static_map_"+ UUID.randomUUID() + ".png";
                    stAcc.uploadBlobToBucket(message.bucket, blobName, visionHandle.getStaticMapUrl(info.getLatLng()));
                    stAcc.enableBlobPublicAccess(message.bucket, blobName);

                    Coordinates coordinates = new Coordinates();
                    coordinates.latitude = info.getLatLng().getLatitude();
                    coordinates.longitude = info.getLatLng().getLongitude();
                    landmark.imageLocation = coordinates;
                    landmark.blobId = blobName;

                    landmarkResponse.images.add(landmark);
                }
            }

            landmarkResponse.insertLandmarkDocument(this.db, this.collectionName, landmarkResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ackReply.ack();
    }
}
