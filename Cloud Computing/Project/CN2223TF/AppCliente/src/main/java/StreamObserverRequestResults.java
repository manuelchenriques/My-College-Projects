import io.grpc.stub.StreamObserver;
import processor.Coordinates;
import processor.Data;
import processor.Landmark;

public class StreamObserverRequestResults implements StreamObserver<Data> {

    private boolean isCompleted;

    public StreamObserverRequestResults(){
        this.isCompleted = false;
    }

    public boolean isComplete() {
        return isCompleted;
    }

    @Override
    public void onNext(Data data) {
        System.out.println("\n-------Results fo the search-------");
        System.out.println("\nId of the data: " + data.getId());
        System.out.println("\n-------------Landmarks-------------");
        for (Landmark lm : data.getLandmarksList()){
            System.out.println("\nName: " + lm.getName());
            System.out.println("Precision: " + lm.getPrecision());
            Coordinates loc = lm.getCoordinates();
            System.out.println("Latitude: " + loc.getX());
            System.out.println("Longitude: " + loc.getY());
        }
        System.out.println();
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
        isCompleted = true;
    }

    @Override
    public void onCompleted() {
        System.out.println("Done");
        isCompleted = true;
    }
}