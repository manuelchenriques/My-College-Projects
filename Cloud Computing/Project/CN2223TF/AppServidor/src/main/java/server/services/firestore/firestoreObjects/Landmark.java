package server.services.firestore.firestoreObjects;

public class Landmark {
    public String blobId;
    public Coordinates imageLocation;
    public String landmarkName;
    public float score;

    public Landmark(String blobId, Coordinates imageLocation, String landmarkName, float score){
        this.blobId = blobId;
        this.imageLocation = imageLocation;
        this.score = score;
        this.landmarkName = landmarkName;
    }
}
