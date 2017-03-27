package main.java.edu.tomr.client.generate;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import main.java.edu.tomr.client.model.GenerateResponse;
import main.java.edu.tomr.client.model.PostRequestPayload;


/**
 * Created by muchhals on 3/25/17.
 */
@Path("/upload")
public class RequestGenerator {

    private static int lbPort=6000;
    private static int servicerNodePort=5003;
    private String ipAddress;
    //ExecutorService executor = Executors.newFixedThreadPool(10);

    private String getIpAddress() throws Exception {
        String ipAddress = null;
        Enumeration<NetworkInterface> net = null;
        try {
            net = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        while(net.hasMoreElements()){
            NetworkInterface element = net.nextElement();
            Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements()){
                InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address){
                    if (ip.isSiteLocalAddress()){
                        ipAddress = ip.getHostAddress();
                    }
                }
            }
        }
        if(ipAddress==null){
            throw new Exception("Error initializing IP address. Try defining the IP address in the config file.");
        }
        return ipAddress;
    }

    public RequestGenerator() {
        super();
        try {
            ipAddress = getIpAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("/data")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateRequests(String postData) {

        int length = send(postData);
        return Response.status(200).entity(new GenerateResponse(length)).build();
    }

    private int send(String postData) {
        PostRequestPayload load = new PostRequestPayload();
        ObjectMapper mapper = new ObjectMapper();
        try {
            load = mapper.readValue(postData, PostRequestPayload.class);
        } catch (IOException e) {
            System.out.println("Error while mapping POST json to object");
            e.printStackTrace();
        }
        /*for (int i = 0; i < load.getData().length; ++i) {
            DataTuple kv = load.getData()[i];
            Runnable worker = new ClientRunner("lb", lbPort, servicerNodePort,
                    kv.getKey(), kv.getValue().getBytes(), ClientRequestType.GET, utils.getSelfIP());
            executor.execute(worker);
        }*/
        return load.getData().length;

    }

    @GET
    @Path("/")
    public Response test() {
        return Response.status(200).build();
    }

}
