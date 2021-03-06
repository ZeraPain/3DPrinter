/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

/**
 *
 * @author debian
 */
import packet.*;

public class GUI extends javax.swing.JFrame {

    private static Server printServer;
    private static GUI gui;
    private static boolean busy;
    private static boolean error;
    private static PacketReader command;

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (printServer != null) {
                    printServer.shutdown();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Button_repair = new javax.swing.JButton();
        Button_break = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextArea_status = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Printer");

        Button_repair.setText("Repair printer");
        Button_repair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_repairActionPerformed(evt);
            }
        });

        Button_break.setText("Break printer");
        Button_break.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_breakActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setAutoscrolls(true);

        TextArea_status.setColumns(20);
        TextArea_status.setRows(5);
        jScrollPane1.setViewportView(TextArea_status);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Button_break, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Button_repair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_break)
                    .addComponent(Button_repair))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Button_repairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_repairActionPerformed
        // TODO add your handling code here:
        error = false;
        print("The printer is working again");
    }//GEN-LAST:event_Button_repairActionPerformed

    private void Button_breakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_breakActionPerformed
        // TODO add your handling code here:
        error = true;
        PacketWriter packet = new PacketWriter();
        packet.errorPrinter(3042);
        if (printServer != null) {
            printServer.sendData(packet.getPacket());
        }
        print("The printer is damaged!");
    }//GEN-LAST:event_Button_breakActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println("### Printer Server ###");
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new GUI();

                error = false;
                printServer = new Server();
                printServer.start(16000);
                printServer.startUDP(16010);

                gui.TextArea_status.setEditable(false);
                print("The Printer is ready");
                gui.setVisible(true);
            }
        });
    }

    public static void Interpret(PacketReader p) {
        if (!busy && !error) {
            switch (p.getOpcode()) {
                case Opcode.auth:
                    print("Authenticate Modul with ID [" + p.getID() + "]");
                    break;
                case Opcode.disconnect:
                    print("Module is disconnected");
                    break;
                case Opcode.errorTest:
                    print("Error was requested for testing purpose");
                    PacketWriter packet = new PacketWriter();
                    packet.errorPrinter(12345);
                    printServer.sendData(packet.getPacket());
                    break;
                case Opcode.line:
                case Opcode.point:
                    command = p;
                    int[] requiredColor = p.getColor();
                    PacketWriter packetColor = new PacketWriter();
                    packetColor.requireColor(requiredColor[0], requiredColor[1], requiredColor[2]);
                    printServer.sendData(packetColor.getPacket());
                    break;
                case Opcode.responseColor:
                    if (p.getSuccess()) {
                        if (command == null) {
                            break;
                        }
                        printCommand(command);
                    } else {
                        print("Missing Colors!");
                    }
                    break;
                case Opcode.stresstestRequest:
                    printServer.sendData(new PacketWriter(Opcode.stresstestResponse).getPacket());
                    break;
            }
        } else if (busy) {
            printServer.sendData(new PacketWriter(Opcode.errorBusy).getPacket());
        } else if (error) {
            PacketWriter packet = new PacketWriter();
            packet.errorPrinter(3042);
            printServer.sendData(packet.getPacket());
        }
    }

    private static void printCommand(final PacketReader command) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    busy = true;
                    if (command.getOpcode() == Opcode.point) {
                        print("Printing Point at X: " + command.getStart()[0] + "  Y: " + command.getStart()[1] + " ...");
                    } else if (command.getOpcode() == Opcode.line) {
                        print("Printing Line from (" + command.getStart()[0] + "/" + command.getStart()[1] + ") to (" + command.getEnd()[0] + "/" + command.getEnd()[1] + ") ...");
                    }
                    Thread.sleep(4000);
                    print("Finished!");
                    busy = false;
                    printServer.sendData(new PacketWriter(Opcode.printFinished).getPacket());
                } catch (InterruptedException e) {
                    //System.out.println("Sleep: " + e);
                }
            }
        }).start();
    }

    private static void print(String status) {
        gui.TextArea_status.append(status + "\n");
        gui.TextArea_status.setCaretPosition(gui.TextArea_status.getDocument().getLength());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_break;
    private javax.swing.JButton Button_repair;
    private javax.swing.JTextArea TextArea_status;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
