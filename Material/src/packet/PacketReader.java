/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packet;

import java.io.*;

/**
 *
 * @author debian
 */
public class PacketReader {

    DataInputStream dis;
    final private char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final byte[] bytes;
    
    private short opcode;
    
    private int id;
    private int errorCode;
    
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    
    private int cyan;
    private int magenta;
    private int yellow;
    
    private boolean success;

    public PacketReader(byte[] bytes) {
        this.bytes = bytes;
        InputStream input = new ByteArrayInputStream(bytes);
        dis = new DataInputStream(input);
        ReadPacketData();
    }

    private void ReadPacketData() {
        try {
            opcode = dis.readShort();

            switch (opcode) {
                case Opcode.auth:
                    id = dis.readInt();
                    break;
                case Opcode.errorPrinter:
                    errorCode = dis.readInt();
                    break;
                case Opcode.requireColor:
                    cyan = dis.readInt();
                    magenta = dis.readInt();
                    yellow = dis.readInt();
                    break;
                case Opcode.requestColor:
                    cyan = dis.readInt();
                    magenta = dis.readInt();
                    yellow = dis.readInt();
                    break;
                case Opcode.responseColor:
                    success = dis.readBoolean();
                    break;
                case Opcode.responsefillingColorLevel:
                    cyan = dis.readInt();
                    magenta = dis.readInt();
                    yellow = dis.readInt();
                    break;
                case Opcode.point:
                    xStart = dis.readInt();
                    yStart = dis.readInt();
                    cyan = dis.readUnsignedByte();
                    magenta = dis.readUnsignedByte();
                    yellow = dis.readUnsignedByte();
                    break;
                case Opcode.line:
                    xStart = dis.readInt();
                    yStart = dis.readInt();
                    xEnd = dis.readInt();
                    yEnd = dis.readInt();
                    cyan = dis.readUnsignedByte();
                    magenta = dis.readUnsignedByte();
                    yellow = dis.readUnsignedByte();
                    break;
            }
        } catch (IOException e) {
            //System.out.println("Interpret: " + e);
        }
    }

    public void printData() {
        System.out.println("Opcode: " + opcode + " ID: " + id + " errorCode: " + errorCode
                + "\nxStart " + xStart + " yStart " + yStart
                + "\nxEnd " + xEnd + " yEnd " + yEnd
                + "\ncyan " + cyan + " magenta " + magenta + " yellow " + yellow
                + "\nlength " + bytes.length + " success " + success);
    }

    public String getHexString() {
        return bytesToHex(bytes);
    }
    
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public short getOpcode() {
        return opcode;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
    
    public int getID() {
        return id;
    }
    
    public int[] getStart() {
        int[] coord = new int[2];
        coord[0] = xStart;
        coord[1] = yStart;
        return coord;
    }
    
    public int[] getEnd() {
        int[] coord = new int[2];
        coord[0] = xEnd;
        coord[1] = yEnd;
        return coord;
    }
    
    public int[] getColor() {
        int[] color = new int[3];
        color[0] = cyan;
        color[1] = magenta;
        color[2] = yellow;
        return color;
    }
    
    public boolean getSuccess() {
        return success;
    }
}
