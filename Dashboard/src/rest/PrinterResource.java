/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.json.*;
import packet.Opcode;
import restclient.GUI;

@Path("/dashboard")
public class PrinterResource {

    private final GUI gui;

    public PrinterResource(GUI gui) {
        this.gui = gui;
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String receiveData(InputStream incomingData) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception ex) {
            System.out.println("(POST) Error stringBuilder: " + ex.getMessage());
        }

        System.out.println("(POST) Received: " + stringBuilder.toString());

        try {
            JSONObject jsonObj = new JSONObject(stringBuilder.toString());

            switch (jsonObj.getInt("opcode")) {
                case Opcode.auth:
                    gui.addPrinter(jsonObj.getString("address"), 
                            jsonObj.getInt("port"), 
                            jsonObj.getString("path"), 
                            jsonObj.getInt("company"));
                    break;
                case Opcode.disconnect:
                    gui.removePrinter(jsonObj.getString("address"));
                    break;
                default:
                    System.out.println("Unknown Opcode!");
            }
            
        } catch (JSONException ex) {
            System.out.println("(POST) Error jSON: " + ex.getMessage());
        }
        
        return "ok";
    }
}
