/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import java.util.Scanner;
import org.json.*;
import packet.Opcode;

/**
 *
 * @author dave
 */
public class MQTT {

    private static final String clientId = "MQTTClient1";
    private static final String MQTTbroker = "tcp://192.168.178.34:1883";
    private static MQTTClient mqtt;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        mqtt = new MQTTClient();
        mqtt.start(MQTTbroker, clientId);
        mqtt.subscribe(clientId);
        
        Scanner input = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nMQTTClient: " + clientId +
                    "\n(1) Send request" +
                    "\n(2) Exit");
            
            switch (input.nextInt()) {
                case 1:
                    drawPoint(37, 44, 255, 0, 0, (short) 11);
                    break;
                case 2:
                    mqtt.exit();
                    System.exit(0);
                default:
                    System.out.println("Ungültige Eingabe!");
            }
        }
    }
    
    public static void drawPoint(int xStart, int yStart, int cyan, int magenta, int yellow, short size) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("opcode", Opcode.point);
            jsonObj.put("xStart", xStart);
            jsonObj.put("yStart", yStart);
            jsonObj.put("cyan", cyan);
            jsonObj.put("magenta", magenta);
            jsonObj.put("yellow", yellow);
            jsonObj.put("size", size);

            mqttrequest(jsonObj);
            System.out.println("Print request was sent!");
        } catch (Exception ex) {
            System.out.println("drawPoint() Error: " + ex.getMessage());
        }
    }
    
    public static void mqttrequest(JSONObject payload) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("opcode", Opcode.mqttrequest);
            jsonObj.put("clientId", clientId);
            jsonObj.put("payload", payload);
            
            mqtt.send("PrintRequest", jsonObj);
        } catch (Exception ex) {
            System.out.println("mqttrequest() Error: " + ex.getMessage());
        }
    }
    
    public static void received(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);

            switch (jsonObj.getInt("opcode")) {
                case Opcode.mqttfinish:
                    System.out.println(jsonObj.getString("message"));
                    break;
                case Opcode.mqttpayment:
                    System.out.println("Dashboard: It costs " + jsonObj.getInt("price") + " €");
                    break;
                default:
                    System.out.println("Unknown Opcode!");
            }

        } catch (JSONException ex) {
            System.out.println("received Error: " + ex.getMessage());
        }
    }
    
}
