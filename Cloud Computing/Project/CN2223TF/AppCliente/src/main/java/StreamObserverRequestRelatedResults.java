import io.grpc.stub.StreamObserver;
import processor.RelatedResults;

public class StreamObserverRequestRelatedResults implements StreamObserver<RelatedResults> {

    private boolean isComplete;

    public StreamObserverRequestRelatedResults(){
        this.isComplete = false;
        System.out.println("Results:");
    }

    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public void onNext(RelatedResults relatedResults) {
        System.out.println();
        System.out.println("Origin photo blob: " + relatedResults.getId());
        System.out.println("Landmark: " + relatedResults.getName());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println();
        System.err.println(throwable.getMessage());
        this.isComplete = true;
    }

    @Override
    public void onCompleted() {
        System.out.println("End of results");
        this.isComplete = true;
    }
}
