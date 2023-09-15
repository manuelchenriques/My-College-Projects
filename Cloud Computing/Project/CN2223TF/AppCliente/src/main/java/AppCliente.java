import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import processor.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class AppCliente {

    private static int svcPort = 8000;
    private static ProcessorGrpc.ProcessorStub nonBlockingStub;
    private static ManagedChannel channel;

    public static void main(String[] args){
        try {
            if(args.length > 0){
                svcPort = Integer.parseInt(args[0]);
            }

            showMenu();
            Scanner in = new Scanner(System.in);
            in.next();
        }catch (Exception e){
            System.err.println("Something went wrong.");
            System.exit(-1);
        }
    }
    static void serverConnection(String serverIP,int serverPort){
        channel = ManagedChannelBuilder.forAddress(serverIP,serverPort)
                .usePlaintext()
                .build();

        nonBlockingStub = ProcessorGrpc.newStub(channel);
    }
    static void showMenu() throws IOException, InterruptedException {

        String[] servers = getAvailableServers();
        String server;


        if (servers != null && servers.length > 0){
            server = selectServer(servers);
        }else {
            server = "localhost";
        }
        System.out.println("Ip selected: " + server);

        serverConnection(server,svcPort);

        //TODO: Call menu here with all the options
        while (true){
            int commandOption = menuOptions();
            switch (commandOption){
                case 0:
                    System.exit(0);
                case 1:
                    submitImage(getPath());
                    break;
                case 2:
                    requestResults(getIdentifier());
                    break;
                case 3:
                    requestMap(getIdentifier(),getPath());
                    break;
                case 4:
                    requestRelatedImages(getPrecision());
                    break;
                default:
                    System.out.println("Not a valid command");
            }
        }
    }

    private static int menuOptions() {
        Scanner scan = new Scanner(System.in);
        System.out.println("-------------------------------------------------");
        System.out.println("What do you want to do?");
        System.out.println("1. Submit Image;");
        System.out.println("2. Get results from identifier;");
        System.out.println("3. Get a map;");
        System.out.println("4. Get the names of the photos and respective landmarks.");
        System.out.println("Press 0 to exit the application.");
        System.out.print("> ");
        return scan.nextInt();
    }

    private static String selectServer(String[] servers) {
        int picker = new Random().nextInt(servers.length);
        return servers[picker];
    }

    static String[] getAvailableServers() throws IOException, InterruptedException {
        String cloudFun = "https://europe-west1-cn2223-t1-g01.cloudfunctions.net/getInstancesIps?";
        cloudFun += "name=grpc-instance-group";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cloudFun))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            System.out.println(response.body());
            return response.body().split(",");
        }
        System.out.println("Error: " + response.statusCode());
        return null;
    }
    private static Path getPath(){
        Scanner scan = new Scanner(System.in);

        do {
            System.out.println("Type image path");
            try {
                return Paths.get(scan.nextLine());
            }catch (InvalidPathException | NullPointerException e){
                System.out.println("Typed path doesn't correspond to file");
            }
        }while (true);
    }

    private static float getPrecision() {
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Type precision value from 0.0 to 1.0");
            try {
                float value = scan.nextFloat();
                if (value < 0.0 || value > 1.0) throw new InputMismatchException();
                return value;
            }catch (InputMismatchException e){
                System.out.println("Not a valid number");
                scan.nextLine();
            }
        }while (true);
    }
    private static String getIdentifier(){
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Type identifier");
            try {
                return scan.nextLine();
            }catch (NoSuchElementException e){
                System.out.println("No identifier founded");
            }
        }while (true);
    }

    private static void requestResults(String identifier) throws InterruptedException {
        Identifier request = Identifier.newBuilder().setId(identifier).build();
        StreamObserverRequestResults resultsObserver = new StreamObserverRequestResults();

        nonBlockingStub.requestData(request, resultsObserver);
        System.out.println("Processing...");
        while (!resultsObserver.isComplete()){
            Thread.sleep(100);
        }
    }

    private static void requestMap(String identifier, Path path) throws InterruptedException {
        Identifier request = Identifier.newBuilder().setId(identifier).build();
        StreamObserverRequestMap requestObserver = new StreamObserverRequestMap(path.toString());

        nonBlockingStub.requestMap(request, requestObserver);
        System.out.println("Processing...");
        while (!requestObserver.isComplete()){
            Thread.sleep(100);
        }
    }

    private static void requestRelatedImages(Float value) throws InterruptedException {
        Precision precision = Precision.newBuilder().setValue(value).build();
        StreamObserverRequestRelatedResults resultsObserver = new StreamObserverRequestRelatedResults();

        nonBlockingStub.requestRelatedImages(precision,resultsObserver);
        System.out.println("Processing...");
        while (!resultsObserver.isComplete()){
            Thread.sleep(100);
        }
    }

    private static void submitImage(Path imagePath) throws IOException, InterruptedException {
        StreamObserverSubmitImage submitObserver = new StreamObserverSubmitImage(imagePath.toString());
        StreamObserver<ImagePayload> streamObserver = nonBlockingStub.submitImage(submitObserver);

        byte[] bytes = new byte[4096];
        int size;
        InputStream inputStream = Files.newInputStream(imagePath);
        while ((size = inputStream.read(bytes)) > 0){
            ImagePayload imagePayload = ImagePayload.newBuilder()
                    .setLoad(
                            ByteString.copyFrom(bytes,0,size)
                    ).build();
            streamObserver.onNext(imagePayload);
        }
        inputStream.close();
        streamObserver.onCompleted();
        System.out.println("Processing...");
        while (!submitObserver.isComplete()){
            Thread.sleep(100);
        }

    }
}
