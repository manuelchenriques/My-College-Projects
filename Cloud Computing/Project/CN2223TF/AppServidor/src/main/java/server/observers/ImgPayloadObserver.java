package server.observers;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import processor.Identifier;
import processor.ImagePayload;
import server.serializable.MessageRequest;
import server.services.cloudstorage.CloudStorageService;
import server.services.pubsub.PubSubService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImgPayloadObserver implements StreamObserver<ImagePayload> {

    private final CloudStorageService storageController;
    private final PubSubService service;
    private final StreamObserver<Identifier> responseObserver;
    private final String bucketName;
    private final String imageName;
    private final List<Byte> byteList;


    public ImgPayloadObserver(CloudStorageService storageController, PubSubService service, StreamObserver<Identifier> responseObserver, String bucketName, String imageName){
        this.storageController = storageController;
        this.service = service;
        this.responseObserver = responseObserver;
        this.bucketName = bucketName;
        this.imageName = imageName;
        this.byteList =  new ArrayList<>();
    }

    @Override
    public void onNext(ImagePayload imagePayload) {
        try{
            byte[] bytes = imagePayload.getLoad().toByteArray();
            for(byte b : bytes){
                this.byteList.add(b);
            }
        } catch (Exception e){
            System.err.println("An error has occurred, we were unable to complete the operation.\nERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("ERROR: " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        try {
            String id = UUID.randomUUID().toString();
            String blobName = this.imageName + id;

            byte[] array = new byte[this.byteList.size()];
            for(int i = 0; i < this.byteList.size(); ++i){
                array[i] = this.byteList.get(i);
            }
            storageController.uploadImage(this.bucketName, blobName, array);
            MessageRequest request = new MessageRequest(id, this.bucketName, blobName);
            String json = new Gson().toJson(request);
            service.publishMessage(json);

            responseObserver.onNext(Identifier.newBuilder().setId(id).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("An error has occurred, we were unable to complete the operation.\nERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
