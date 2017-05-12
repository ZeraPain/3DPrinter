/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;

import java.io.*;
import java.net.*;
import packet.*;

/**
 *
 * @author debian
 */
public class Client {

    private boolean connected;
    private Socket socket;
    private InputStream fromServer;
    private DataOutputStream toServer;

    public Client() {
        connected = false;
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            toServer = new DataOutputStream(socket.getOutputStream());
            fromServer = socket.getInputStream();
            connected = socket.isConnected();
        } catch (IOException e) {
            //System.out.println("connect: " + e);
        }

        new Thread(new Runnable() {
            @Override public void run() {
                while (connected && receiveData()) {

                }
            }
        }).start();
        
        return connected;
    }

    public void disconnect() {
        try {
            if (connected) {
                connected = false;
                socket.close();
                toServer.close();
                fromServer.close();
            }
        } catch (IOException e) {
            //System.out.println("disconnect: " + e);
        }
    }

    public void sendData(byte[] data) {
        try {
            if (data == null || toServer == null) return;
            toServer.write(data, 0, data.length);
        } catch (IOException e) {
            //System.out.println("sendData: " + e);
        }
    }

    private boolean receiveData() {
        try {
            DataInputStream dis = new DataInputStream(fromServer);

            int length = dis.readInt();
            if (length > 0) {
                byte[] bytes = new byte[length];
                dis.readFully(bytes, 0, length);

                GUI.Interpret(new PacketReader(bytes));
                
                return true;
            }
        } catch (IOException e) {
            //System.out.println("receiveData: " + e);
        }

        return false;
    }
}
