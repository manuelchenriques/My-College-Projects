package server.services.cloudstorage;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import com.google.cloud.WriteChannel;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CloudStorageService {

    private final Storage storage;

    public CloudStorageService(){
        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        Storage storage = storageOptions.getService();
        String projID = storageOptions.getProjectId();
        if (projID != null) System.out.println("CloudStorage Service: " + projID);
        else {
            System.out.println("The environment variable GOOGLE_APPLICATION_CREDENTIALS isn't well defined!!");
            System.exit(-1);
        }
        this.storage = storage;
    }

    public void uploadImage(String bucket, String blob, byte[] content){

        BlobId blobId = BlobId.of(bucket, blob);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("jpg").build();
        if (content.length < 1000000){
            storage.create(blobInfo, content);
        }else{
            try (WriteChannel writer = storage.writer(blobInfo)){

                int chunkSize = 1000;
                int offset = 0;
                while (offset < content.length){
                    int chunkWrite = Math.min(chunkSize, content.length - offset);
                    writer.write(ByteBuffer.wrap(content, offset, chunkWrite));
                    offset += chunkWrite;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("BLOB created.");
    }

    public ByteString[] downloadImage(String bucket, String blob){
        BlobId blobId = BlobId.of(bucket, blob);
        Blob myBlob = storage.get(blobId);
        if (myBlob.getSize() < 1_000_000) {
            return toByteString(myBlob.getContent());
        }else {
            byte[] content = new byte[Math.toIntExact(myBlob.getSize())];
            try (ReadChannel reader = myBlob.reader()) {
                ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
                for (int i = 0; reader.read(bytes) > 0; i++){
                    bytes.flip();
                    content[i] = bytes.get();
                    bytes.clear();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return toByteString(content);
        }
    }

    private ByteString[] toByteString(byte[] array){
        List<ByteString> result = new ArrayList<>();
        int chunkSize = 1500;
        int offset = 0;

        while (offset < array.length){
            int chunkRead = Math.min(chunkSize, array.length - offset);
            result.add(ByteString.copyFrom(array, offset, chunkRead));
            offset = offset + chunkRead;
        }
        return  result.toArray(new ByteString[0]);
    }
}
