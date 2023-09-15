package storage;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

public class StorageAccess {
    Storage storage;

    public StorageAccess(Storage storage) {
        this.storage = storage;
    }

    public void uploadBlobToBucket(String bucketName, String blobName, String blobUrl) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
        try {
            URL url = new URL(blobUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = conn.getInputStream();
            BufferedInputStream bufIn = new BufferedInputStream(in);
            if (getContentLength(url) > 1_000_000) {
                try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                    int limit;
                    while ((limit = bufIn.read(buffer)) >= 0) {
                        try {
                            writer.write(ByteBuffer.wrap(buffer, 0, limit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else {
                storage.create(blobInfo, bufIn.readAllBytes());
            }
            bufIn.close();
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Blob " + blobName + " created in bucket " + bucketName);
    }

    public void enableBlobPublicAccess(String bucketName, String blobName) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        if (blob == null) {
            System.out.println("No such Blob exists!");
            return;
        }
        blob.createAcl(Acl.newBuilder(Acl.User.ofAllUsers(), Acl.Role.READER).build());
        System.out.println("Blob " + blobName + " has now public access.");
    }

    private long getContentLength(URL url) throws IOException {
        HttpURLConnection getConnSize = (HttpURLConnection) url.openConnection();
        getConnSize.setRequestMethod("HEAD");
        return getConnSize.getContentLength();
    }
}
