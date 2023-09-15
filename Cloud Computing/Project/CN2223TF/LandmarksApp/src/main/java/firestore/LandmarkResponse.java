package firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LandmarkResponse {
    public String id;

    public String origin;
    public List<Landmark> images;

    public void insertLandmarkDocument(Firestore db, String collectionName, LandmarkResponse landmarkResponse) throws ExecutionException, InterruptedException {
        CollectionReference colRef = db.collection(collectionName);
        DocumentReference docRef = colRef.document(String.valueOf(landmarkResponse.id));
        ApiFuture<WriteResult> resultFut = docRef.set(landmarkResponse);
        WriteResult result = resultFut.get();
        System.out.println("Update time : " + result.getUpdateTime());
    }
}
