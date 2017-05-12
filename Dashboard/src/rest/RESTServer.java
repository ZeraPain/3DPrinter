/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.*;
import restclient.GUI;

/**
 *
 * @author dave
 */
public class RESTServer {
    private final GUI gui;
    private HttpServer server;
    
    public RESTServer(GUI gui) {
        this.gui = gui;
        server = null;
    }
    
    public void start(String host, int port) {
        if (server != null) 
            return;
        
        URI uri = UriBuilder.fromUri("http://" + host + "/").port(port).build();
        PrinterResource exampleResource = new PrinterResource(gui);
        ResourceConfig config = new ResourceConfig();
        config.register(exampleResource);
        
        server = JdkHttpServerFactory.createHttpServer(uri, config);
    }
    
    public boolean send(String targetURL, JSONObject jsonObj) {
        try {
            URL url = new URL(targetURL + "/post");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            try (OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream())) {
                out.write(jsonObj.toString());
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                while (in.readLine() != null) {
                }
            }
            
            return true;
        } catch (Exception ex) {
            System.out.println("send() Error: " + ex.getMessage());
            return false;
        }
    }
    
    public JSONObject get(String targetURL, String path){
        Client client = javax.ws.rs.client.ClientBuilder.newClient();
        WebTarget webTarget = client.target(targetURL).path(path);
        String jsonString = webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        
        JSONObject jsonObj = null;
        
        try {
           jsonObj = new JSONObject(jsonString);
        } catch (JSONException ex) {
            System.out.println("Error get: " + ex.getMessage());
        }

        return jsonObj;
    }
}
