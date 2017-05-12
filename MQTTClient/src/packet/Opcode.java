/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packet;

/**
 *
 * @author debian
 */
public class Opcode {
    final public static short auth = 15;
    final public static short disconnect = 16;
    final public static short errorTest = 17;
    final public static short errorPrinter = 20;
    final public static short errorColorC = 21;
    final public static short errorColorM = 22;
    final public static short errorColorY = 23;
    final public static short errorBusy = 25;
    final public static short printFinished = 30;
    final public static short requireColor = 40;
    final public static short requestColor = 41;
    final public static short responseColor = 42;
    final public static short requestfillingColorLevel = 45;
    final public static short responsefillingColorLevel = 46;
    final public static short stresstestRequest = 50;
    final public static short stresstestResponse = 51;
    final public static short point = 100;
    final public static short line = 101;
    final public static short mqttrequest = 110;
    final public static short mqttfinish = 111;
    final public static short mqttpayment = 112;
}
