/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.json.*;
import packet.Opcode;
import restclient.GUI;

/**
 *
 * @author dave
 */
public class MQTTClient implements MqttCallback {

    private MqttClient client;
    private final GUI gui;
    private int costs;

    public MQTTClient(GUI gui) {
        this.gui = gui;
        costs = 0;
    }

    public void start(String broker, String clientId) {
        try {
            client = new MqttClient(broker, clientId);
            client.connect();
        } catch (MqttException ex) {
            System.out.println("MQTTClient() Error: " + ex.getMessage());
        }
    }

    public void subscribe(String topic) {
        try {
            client.setCallback(this);
            client.subscribe(topic);
        } catch (MqttException ex) {
            System.out.println("subscribe() Error: " + ex.getMessage());
        }
    }

    public void send(String topic, JSONObject jsonObj) {
        MqttDeliveryToken token;
        MqttTopic mqttTopic = client.getTopic(topic);
        
        try {
            token = mqttTopic.publish(new MqttMessage(jsonObj.toString().getBytes()));
        } catch (MqttException ex) {
            System.out.println("send() Error: " + ex.getMessage());
        }
    }

    public void exit() {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException ex) {
            System.out.println("close() Error: " + ex.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage jsonString)  {
        try {
            JSONObject jsonObj = new JSONObject(jsonString.toString());
            
            switch (jsonObj.getInt("opcode")) {
                case Opcode.mqttrequest:
                    costs += 300;
                    gui.mqttrequest(jsonObj);
                    break;
                case Opcode.mqttfinish:
                    JSONObject paymentObj = new JSONObject();
                    paymentObj.put("opcode", Opcode.mqttpayment);
                    paymentObj.put("price", costs);
                    costs = 0;
                    send(jsonObj.getString("clientId"), paymentObj);
                    break;
                default:
                    System.out.println("Unknown Opcode!");
            }
        } catch (JSONException ex) {
            System.out.println("messageArrived() Error: " + ex.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
