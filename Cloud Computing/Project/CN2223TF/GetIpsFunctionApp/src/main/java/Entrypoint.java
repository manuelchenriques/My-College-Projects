import com.google.cloud.compute.v1.Instance;
import com.google.cloud.compute.v1.InstancesClient;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;

public class Entrypoint implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        String projectId = "cn2223-t1-g01";
        String zone = "europe-southwest1-a";
        String instanceName = request.getFirstQueryParameter("name").orElse("grpc-instance-group");
        try(InstancesClient client = InstancesClient.create()) {
            for (Instance instance : client.list(projectId,zone).iterateAll()){
                if (instance.getName().contains(instanceName) && instance.getStatus().compareTo("RUNNING") == 0){
                    String ip = instance.getNetworkInterfaces(0).getAccessConfigs(0).getNatIP();
                    writer.write(ip+",");
                }
            }
        }

    }
}