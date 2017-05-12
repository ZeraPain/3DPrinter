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
public class PacketWriter {
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

    public PacketWriter() {

    }
    
    public PacketWriter(int opcode) {
        this.opcode = (short)opcode;
    }

    public void authenticate(int id) {
        this.opcode = Opcode.auth;
        this.id = id;
    }

    public void drawPoint(int xStart, int yStart, byte cyan, byte magenta, byte yellow, short size) {
        this.opcode = Opcode.point;
        this.xStart = xStart;
        this.yStart = yStart;
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
    }
    
    public void drawLine(int xStart, int yStart, int xEnd, int yEnd, byte cyan, byte magenta, byte yellow) {
        this.opcode = Opcode.line;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
    }
    
    public void errorPrinter(int errorCode) {
        this.opcode = Opcode.errorPrinter;
        this.errorCode = errorCode;
    }
    
    public void requireColor(int cyan, int magenta, int yellow) {
        this.opcode = Opcode.requireColor;
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
    }
    
    public void requestColor(int cyan, int magenta, int yellow) {
        this.opcode = Opcode.requestColor;
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
    }
    
    public void responseColor(boolean success) {
        this.opcode = Opcode.responseColor;
        this.success = success;
    }
    
    public void fillingColorLevel(int cyan, int magenta, int yellow) {
        this.opcode = Opcode.responsefillingColorLevel;
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
    }

    public byte[] getPacket() {
        try {
            if (opcode == 0) return null;
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(opcode);
            
            switch (opcode) {
                case Opcode.auth:
                    dos.writeInt(id);
                    break;
                case Opcode.errorPrinter:
                    dos.writeInt(errorCode);
                    break;
                case Opcode.requireColor:
                    dos.writeInt(cyan);
                    dos.writeInt(magenta);
                    dos.writeInt(yellow);
                    break;
                case Opcode.requestColor:
                    dos.writeInt(cyan);
                    dos.writeInt(magenta);
                    dos.writeInt(yellow);
                    break;
                case Opcode.responseColor:
                    dos.writeBoolean(success);
                    break;
                case Opcode.responsefillingColorLevel:
                    dos.writeInt(cyan);
                    dos.writeInt(magenta);
                    dos.writeInt(yellow);
                    break;
                case Opcode.point:
                    dos.writeInt(xStart);
                    dos.writeInt(yStart);
                    dos.writeByte(cyan);
                    dos.writeByte(magenta);
                    dos.writeByte(yellow);
                    break;
                case Opcode.line:
                    dos.writeInt(xStart);
                    dos.writeInt(yStart);
                    dos.writeInt(xEnd);
                    dos.writeInt(yEnd);
                    dos.writeByte(cyan);
                    dos.writeByte(magenta);
                    dos.writeByte(yellow);
                    break;
            }
            
            dos.flush();
            
            ByteArrayOutputStream packet_baos = new ByteArrayOutputStream();
            DataOutputStream packet_dos = new DataOutputStream(packet_baos);
            packet_dos.writeInt(baos.toByteArray().length);
            packet_dos.write(baos.toByteArray());
            packet_dos.flush();
            
            return packet_baos.toByteArray();
        } catch (IOException e) {
            //System.out.println("getPacket: " + e);
        }

        return null;
    }
}
