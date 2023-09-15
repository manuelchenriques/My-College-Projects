package server;


import com.google.protobuf.ByteString;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import processor.*;
import server.observers.ImgPayloadObserver;
import server.services.cloudstorage.CloudStorageService;
import server.services.firestore.FirestoreService;
import server.services.firestore.firestoreObjects.LandmarkResponse;
import server.services.pubsub.PubSubService;

import java.util.HashMap;
import java.util.Map;

public class GrpcServer extends ProcessorGrpc.ProcessorImplBase {

    private static CloudStorageService gcsManager;
    private static PubSubService pubSubService;
    private static FirestoreService firestoreService;
    private static final int svcPort = 8000;
    private static final String PROJECT_ID = "cn2223-t1-g01";
    private static final String TOPIC_NAME = "cn2223-g001-final-pubsub";
    private static final String BUCKET_NAME = "cn2223-g001-final";
    private static final String IMAGE_NAME = "images/img-";
    private static final String COLLECTION_NAME = "cn2223-g001-final";

    public static void main(String[] args) {
        try{
            gcsManager = new CloudStorageService();
            pubSubService = new PubSubService(PROJECT_ID, TOPIC_NAME);
            firestoreService = new FirestoreService();

            io.grpc.Server svc = ServerBuilder.forPort(svcPort).addService(new GrpcServer()).build();
            svc.start();
            System.out.println("gRPC Server started, listening on " + svcPort);
            while(true);
        }catch (Exception ex){
            System.err.println("Unable to initialize server and services.");
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public StreamObserver<ImagePayload> submitImage(StreamObserver<Identifier> responseObserver) {
        return new ImgPayloadObserver(gcsManager, pubSubService, responseObserver, BUCKET_NAME, IMAGE_NAME);
    }

    @Override
    public void requestMap(Identifier request, StreamObserver<ImagePayload> responseObserver) {
        try {
            System.out.println("Im here");
            String blobName = firestoreService.getMap(COLLECTION_NAME, request.getId());
            ByteString[] content = gcsManager.downloadImage(BUCKET_NAME, blobName);

            for(ByteString b : content){
                ImagePayload payload = ImagePayload.newBuilder()
                        .setLoad(b)
                        .build();
                responseObserver.onNext(payload);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("An error has occurred, we were unable to complete the operation.\nERROR: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void requestData(Identifier request, StreamObserver<Data> responseObserver) {
        try {
            String requestID = request.getId();
            LandmarkResponse response = firestoreService.readData(COLLECTION_NAME, requestID);

            Data.Builder data = Data.newBuilder()
                    .setId(requestID);
            for (server.services.firestore.firestoreObjects.Landmark lm : response.images){
                Landmark landmark = Landmark.newBuilder()
                        .setName(lm.landmarkName)
                        .setCoordinates(Coordinates.newBuilder()
                                .setX((float) lm.imageLocation.latitude)
                                .setY((float) lm.imageLocation.longitude)
                        )
                        .setPrecision(lm.score).build();
                data.addLandmarks(landmark);
            }

            responseObserver.onNext(data.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("An error has occurred, we were unable to complete the operation.\nERROR: " + e.getMessage());
            responseObserver.onError(e);
        }
    }

    @Override
    public void requestRelatedImages(Precision request, StreamObserver<RelatedResults> responseObserver) {
        try{
            HashMap<String,String> result = firestoreService.getRelatedResults(COLLECTION_NAME, request.getValue());

            for(Map.Entry<String, String> entry: result.entrySet()) {
                RelatedResults relatedResults = RelatedResults.newBuilder()
                        .setId(entry.getKey())
                        .setName(entry.getValue())
                        .build();
                responseObserver.onNext(relatedResults);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("An error has occurred, we were unable to complete the operation.\nERROR: " + e.getMessage());
            responseObserver.onError(e);
        }
    }
}

