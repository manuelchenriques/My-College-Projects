package server.serializable;

public class MessageRequest {
    public String id;
    public String bucket;
    public String blob;

    public MessageRequest(String id, String bucket, String blob){
        this.id = id;
        this.bucket = bucket;
        this.blob = blob;
    }
}
