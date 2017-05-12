/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.json.*;
import tcpclient.GUI;

/**
 *
 * @author dave
 */
public class MQTTClient implements MqttCallback {

    private MqttClient client;
    private final GUI gui;

    public MQTTClient(GUI gui) {
        this.gui = gui;
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
        try {
            MqttMessage msg = new MqttMessage();
            msg.setPayload(jsonObj.toString().getBytes());
            client.publish(topic, msg);
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
    public void messageArrived(String topic, MqttMessage jsonString) throws Exception {
        //System.out.println(jsonString);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}
