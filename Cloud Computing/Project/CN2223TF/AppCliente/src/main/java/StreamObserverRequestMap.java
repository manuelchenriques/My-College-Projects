import io.grpc.stub.StreamObserver;
import processor.ImagePayload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StreamObserverRequestMap implements StreamObserver<ImagePayload> {

    final ArrayList<Byte> byteList = new ArrayList<>();
    private String path;
    private boolean isComplete;
    public StreamObserverRequestMap(String path){
        this.path = path;
        this.isComplete = false;
    }

    public boolean isComplete(){
        return this.isComplete;
    }

    @Override
    public void onNext(ImagePayload imagePayload) {
        byte[] bytes = imagePayload.getLoad().toByteArray();
        for(byte b : bytes){
            this.byteList.add(b);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println();
        System.err.println(throwable.getMessage());
        this.isComplete = true;
    }

    @Override
    public void onCompleted() {
        ByteArrayInputStream mapBytes = new ByteArrayInputStream(toByteArray(byteList));
        try {
            BufferedImage map = ImageIO.read(mapBytes);
            ImageIO.write(map,"jpg",new File(this.path));
            System.out.println("Map image created");
            this.isComplete = true;
        } catch (IOException e) {
            this.onError(e);
        }
    }

    private byte[] toByteArray(ArrayList<Byte> bytes){
        byte[] array = new byte[bytes.size()];
        for(int i = 0; i < bytes.size(); ++i){
            array[i] =bytes.get(i);
        }
        return array;
    }
}