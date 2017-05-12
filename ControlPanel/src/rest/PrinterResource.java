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
import tcpclient.GUI;

@Path("/printer")
public class PrinterResource {

    private final GUI gui;

    public PrinterResource(GUI gui) {
        this.gui = gui;
    }

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public String status() {
        System.out.println("statusRequest");
        int[] colorStatus = gui.getColorStatus();
        int queueLength = gui.getQueueLength();

        JSONObject message = new JSONObject();

        try {
            if (colorStatus != null && colorStatus.length == 3) {
                message.put("cyan", colorStatus[0]);
                message.put("magenta", colorStatus[1]);
                message.put("yellow", colorStatus[2]);
                message.put("queue", queueLength);
            } else {
                message.put("error", "Timeout");
            }
        } catch (Exception ex) {

        }

        return message.toString();
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public void receiveData(InputStream incomingData) {
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
                case Opcode.point:
                    gui.drawPoint(jsonObj.getInt("xStart"),
                            jsonObj.getInt("yStart"),
                            jsonObj.getInt("cyan"),
                            jsonObj.getInt("magenta"),
                            jsonObj.getInt("yellow"),
                            (short) jsonObj.getInt("size"));
                    break;
                case Opcode.line:
                    gui.drawLine(jsonObj.getInt("xStart"),
                            jsonObj.getInt("yStart"),
                            jsonObj.getInt("xEnd"),
                            jsonObj.getInt("yEnd"),
                            jsonObj.getInt("cyan"),
                            jsonObj.getInt("magenta"),
                            jsonObj.getInt("yellow"),
                            (short) jsonObj.getInt("size"));
                    break;
                case Opcode.mqttrequest:  
                    gui.mqttrequest(jsonObj.getString("clientId"), jsonObj.getJSONObject("payload"));
                    break;
                default:
                    System.out.println("Unknown Opcode!");
            }

        } catch (JSONException ex) {
            System.out.println("(POST) Error jSON: " + ex.getMessage());
        }

    }
}
