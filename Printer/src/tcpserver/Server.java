/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.*;
import java.net.*;
import packet.*;

/**
 *
 * @author debian
 */
public class Server {

    private ServerSocket socket;
    private InputStream fromClient;
    private DataOutputStream toClient;
    private boolean connected;

    public Server() {
        connected = false;
    }

    public void start(int port) {
        try {
            socket = new ServerSocket(port);
            connected = true;
        } catch (IOException e) {
            //System.out.println("startServer: " + e);
            connected = false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (connected) {
                    try {
                        Socket client = socket.accept(); // creat communication socket
                        //System.out.println("Connection with: " + client.getRemoteSocketAddress());
                        handleRequests(client);
                    } catch (IOException e) {
                        //System.out.println("startServer: " + e);
                    }
                }
            }
        }).start();
    }

    public void startUDP(final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket serverSocket = new DatagramSocket(port);
                    byte[] receiveData = new byte[1024];

                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        serverSocket.receive(receivePacket);
                        InputStream input = new ByteArrayInputStream(receiveData);
                        DataInputStream dis = new DataInputStream(input);
                        int length = dis.readInt();
                        if (length > 0) {
                            byte[] bytes = new byte[length];
                            dis.readFully(bytes, 0, length);

                            GUI.Interpret(new PacketReader(bytes));
                        }
                    }
                } catch (Exception e) {
                    //System.out.println("startServer: " + e);
                }
            }
        }).start();
    }

    public void shutdown() {
        try {
            if (connected) {
                connected = false;
                socket.close();
            }
        } catch (IOException e) {
            //System.out.println("shutdown: " + e);
        }
    }

    private void handleRequests(Socket socket) {
        try {
            fromClient = socket.getInputStream();
            toClient = new DataOutputStream(socket.getOutputStream());

            while (receiveData()) {              // As long as connection exists
                //sendResponse();
            }

            fromClient.close();
            toClient.close();
            socket.close();
            //System.out.println("Session ended, Server remains active");
        } catch (IOException e) {
            //System.out.println("handleRequests: " + e);
        }
    }

    private boolean receiveData() throws IOException {
        try {
            DataInputStream dis = new DataInputStream(fromClient);

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

    public void sendData(byte[] data) {
        try {
            if (data == null || toClient == null) {
                return;
            }
            toClient.write(data, 0, data.length);
        } catch (IOException e) {
            //System.out.println("sendData: " + e);
        }
    }
}
