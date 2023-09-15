package server.services.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import server.services.firestore.firestoreObjects.Coordinates;
import server.services.firestore.firestoreObjects.Landmark;
import server.services.firestore.firestoreObjects.LandmarkResponse;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirestoreService {

    private final Firestore db;

    public FirestoreService() throws Exception {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirestoreOptions options = FirestoreOptions.newBuilder().setCredentials(credentials).build();
        String projID = options.getProjectId();
        if (projID != null) System.out.println("Firestore Service: " + projID);
        else {
            System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
            throw new Exception();
        }
        this.db = options.getService();
    }

    public LandmarkResponse readData(String collectionName, String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionName).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        String origin = (String) document.get("origin");
        String docId = (String) document.get("id");
        ArrayList<HashMap<String,Object>> images = (ArrayList<HashMap<String,Object>>) document.get("images");
        ArrayList<Landmark> landmarks = new ArrayList<>();
        if (images != null){
            for (int i = 0; i < images.size(); i++) {
                String blobId = (String) images.get(i).get("blobId");
                String landmarkName = (String) images.get(i).get("landmarkName");
                Float score = ((Double) images.get(i).get("score")).floatValue();
                HashMap<String,Double> coord = (HashMap<String, Double>) images.get(i).get("imageLocation");
                double x = coord.get("latitude");
                double y = coord.get("longitude");
                Coordinates loc = new Coordinates(x,y);
                Landmark lm = new Landmark(blobId,loc,landmarkName,score);
                landmarks.add(lm);
            }
        }

        LandmarkResponse rsp = new LandmarkResponse(docId,origin,landmarks);
        return rsp;
    }

    public String getMap(String collectionName, String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionName).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        ArrayList<HashMap<String,Object>> images = (ArrayList<HashMap<String,Object>>) document.get("images");
        ArrayList<String> listMaps = new ArrayList<>();
        if (images != null){
            for (int i = 0; i < images.size(); i++) {
                String mapId = (String) images.get(i).get("blobId");
                listMaps.add(mapId);
            }
        }
        return listMaps.get(new Random().nextInt(listMaps.size()));
    }

    public HashMap<String,String> getRelatedResults(String collectionName, float precision) throws ExecutionException, InterruptedException {
        Iterator<DocumentReference> docRefIte = db.collection(collectionName).listDocuments().iterator();
        HashMap<String,String> ret = new HashMap<>();
        while (docRefIte.hasNext()){
            ApiFuture<DocumentSnapshot> future = docRefIte.next().get();
            DocumentSnapshot document = future.get();
            String origin = (String) document.get("origin");
            ArrayList<HashMap<String,Object>> images = (ArrayList<HashMap<String,Object>>) document.get("images");
            if (images != null) {
                for (int i = 0; i < images.size(); i++){
                    if (((Double)images.get(i).get("score")).floatValue() >= precision) {
                        ret.put(origin, (String) images.get(i).get("landmarkName"));
                        break;
                    }
                }
            }
        }
        return ret;
    }
}
