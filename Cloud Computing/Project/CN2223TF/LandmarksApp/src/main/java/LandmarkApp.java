import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.pubsub.v1.ProjectSubscriptionName;
import storage.StorageAccess;
import sub.message.MessageReceiverLandmark;
import vision.VisionAPIHandler;

public class LandmarkApp {
    private static final String COLLECTION_NAME = "cn2223-g001-final";
    private static final String SUBS_NAME = "cn2223-g001-final-sub";
    private static final int ZOOM = 20;
    private static final String SIZE = "1920x1080";

    public static void main(String[] args) throws Exception {
        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        Storage storage = storageOptions.getService();
        String projID = storageOptions.getProjectId();
        if (projID != null) System.out.println("Current Project ID:" + projID);
        else {
            System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
            System.exit(-1);
        }

        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(credentials).build();
        Firestore db = options.getService();

        try {
            createSubscriber(projID, db, new StorageAccess(storage), new VisionAPIHandler(ZOOM, SIZE, System.getenv("MAPS_API_KEY")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        while(true);
    }

    private static void createSubscriber(String projID, Firestore db, StorageAccess stAcc, VisionAPIHandler visionHandle) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projID, SUBS_NAME);
        ExecutorProvider executorProvider = InstantiatingExecutorProvider
                .newBuilder()
                .setExecutorThreadCount(1)
                .build();
        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, new MessageReceiverLandmark(db, COLLECTION_NAME, stAcc, visionHandle))
                .setExecutorProvider(executorProvider)
                .build();
        subscriber.startAsync().awaitRunning();
    }
}
