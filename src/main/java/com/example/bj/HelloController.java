package com.example.bj;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.*;

public class HelloController {

    @FXML private TextField txF;
    @FXML private TextField txDepo;
    @FXML private HBox d;
    @FXML private HBox j;


    DatagramSocket socket = null;

    public void initialize(){
        try {
            socket = new DatagramSocket(678);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                fogad();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML private void onJoinClick(){
        String uzenet = "join:"+txDepo.getText();
        String joinip = txF.getText();
        kuld(uzenet, joinip, 678);
    }

    private void kuld(String uzenet, String ip, int port) {
        try {
            byte[] adat = uzenet.getBytes("utf-8");
            InetAddress ipv4 = Inet4Address.getByName(ip);
            DatagramPacket packet = new DatagramPacket(adat, adat.length, ipv4, port);
            socket.send(packet);
            System.out.printf("%s:%d -> %s\n", ip, port, uzenet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fogad() {
        byte[] data = new byte[256];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (true){
            try {
                socket.receive(packet);
                String uzenet = new String(packet.getData(), 0, packet.getLength(), "utf-8");
                Platform.runLater(() -> onfogad(uzenet));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onfogad(String uzenet){
        System.out.printf(uzenet);
    }

    @FXML private void onHitClick(){

    }

    @FXML private void onStandClick(){

    }


}