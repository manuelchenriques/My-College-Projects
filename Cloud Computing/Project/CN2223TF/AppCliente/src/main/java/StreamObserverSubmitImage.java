import io.grpc.stub.StreamObserver;
import processor.Identifier;

public class StreamObserverSubmitImage implements StreamObserver<Identifier> {

    private String imagePath;
    private boolean isComplete;

    public StreamObserverSubmitImage(String path) {
        this.imagePath = path;
        this.isComplete = false;
    }

    public boolean isComplete(){
        return this.isComplete;
    }

    @Override
    public void onNext(Identifier identifier) {
        System.out.println();
        System.out.println("Identifier: " + identifier.getId());
    }

    @Override
    public void onError(Throwable throwable) {
        this.isComplete = true;
        System.out.println();
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        this.isComplete = true;
        System.out.println("Completed");
    }
}
