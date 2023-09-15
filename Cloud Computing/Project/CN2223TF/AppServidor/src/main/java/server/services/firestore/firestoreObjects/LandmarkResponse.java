package server.services.firestore.firestoreObjects;

import java.util.List;

public class LandmarkResponse {
    public String id;

    public String origin;
    public List<Landmark> images;

    public LandmarkResponse(String id,String origin, List<Landmark> landmarks){
        this.id = id;
        this.origin = origin;
        this.images = landmarks;
    }
}
