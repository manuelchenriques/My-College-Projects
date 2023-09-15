package vision;

import com.google.cloud.vision.v1.*;
import com.google.type.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisionAPIHandler {
    private final int zoom;
    private final String size;
    private final String apiKey;

    public VisionAPIHandler(int zoom, String size, String apiKey) {
        this.zoom = zoom;
        this.size = size;
        this.apiKey = apiKey;
    }

    public List<AnnotateImageResponse> detectLandmarksGcs(String blobGsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(blobGsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return null;
                }

                for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
                    for(LocationInfo info : annotation.getLocationsList()) {
                        System.out.format("Landmark: %s(%f)%n %s%n",
                                annotation.getDescription(),
                                annotation.getScore(),
                                info.getLatLng());
                    }
                }
            }

            return responses;
        }
    }

    public String getStaticMapUrl(LatLng latLng) {
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?"
                + "center=" + latLng.getLatitude() + "," + latLng.getLongitude()
                + "&zoom=" + this.zoom
                + "&size=" + this.size
                + "&key=" + this.apiKey;
        System.out.println(mapUrl);
        return mapUrl;
    }
}
