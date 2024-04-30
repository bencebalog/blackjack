package com.example.bj;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.PrimitiveIterator;

public class HelloController {

    @FXML private TextField txF;
    @FXML private TextField txDepo;
    @FXML private Label lbBalance;
    @FXML private Label lbBet;
    @FXML private Button btHit;
    @FXML private Button btStand;
    @FXML private Label jatek;
    @FXML private Pane pnD;
    @FXML private Pane pnJ;



        int m = 0;
        String uk = "";
        boolean friss = false;
        int bet = 0;

        String kc = "";
        ArrayList<String> kartyak = new ArrayList<String>();

        Thread reciveThread = null;
        DatagramSocket socket = null;
        double ujX = 100;
        double ujY = 100;





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
        btHit.setDisable(false);
        btStand.setDisable(false);

    }

    @FXML private void onJoinClick(){
        String uzenet = "join:"+txDepo.getText();
        String joinip = txF.getText();
        kuld(uzenet, joinip, 678);
        btHit.setDisable(false);
        btStand.setDisable(false);
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

    private void onfogad(String uzenet) {
        System.out.printf(uzenet + "\n");
        String[] u = uzenet.split(":");
        uk = u[0];
        kc = u[0];
        friss = true;
        if (u[0].equals("joined")) {
            m = Integer.parseInt(u[1]);
            lbBalance.setText(m + "");
        }
        if (u[0].equals("start")) {
            kartyak.clear();
            kc = "";
            uk="";
            bet=0;
            pnD.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("gray_back.png"))));
            pnJ.getChildren().clear();
            pnD.getChildren().clear();
            lbBalance.setText(m+"");
            lbBet.setText("");
        }
        if (u[0].equals("s")) {
            uk = u[1];
            ImageView uj = new ImageView(new Image(getClass().getResourceAsStream(uk + ".png")));
            uj.setTranslateX(ujX);
            uj.setTranslateY(ujY);
            ujX=uj.getTranslateX()+70;
            ujX=uj.getTranslateY()+30;
            pnD.getChildren().add(uj);
        }
        if (u[0].equals("k")) {
            kartyak.add(u[1]);
            kc = u[1];
            ImageView uj = new ImageView(new Image(getClass().getResourceAsStream(kc + ".png")));
            uj.setTranslateX(ujX);
            uj.setTranslateY(ujY);
            ujX=uj.getTranslateX()+70;
            ujX=uj.getTranslateY()+30;
            pnJ.getChildren().add(uj);
        }
        if (u[0].equals("paid")) {
            m = Integer.parseInt(u[1]);
            lbBalance.setText(m + "");
        }
        if (u[0].equals("balance")){
            m=Integer.parseInt(u[1]);
            lbBalance.setText(m+"");
        }
    }

        @FXML private void onHitClick () {
            String ip = txF.getText();
            kuld("hit", ip, 678);
        }

        @FXML private void onStandClick () {
            String ip = txF.getText();
            kuld("stand", ip, 678);
        }
        @FXML private void onExitClick () {
            String ip = txF.getText();
            kuld("exit", ip, 678);
            btHit.setDisable(true);
            btStand.setDisable(true);
        }

        @FXML private void onBet100 () {
            bet+=100;
            lbBet.setText(bet+"");
            lbBalance.setText(m-bet+"");
        }
        @FXML private void onBet50 () {
            bet+=50;
            lbBet.setText(bet+"");
            lbBalance.setText(m-bet+"");
        }
        @FXML private void onBet25 () {
            bet+=25;
            lbBet.setText(bet+"");
            lbBalance.setText(m-bet+"");
        }
        @FXML private void onBet5 () {
            bet+=5;
            lbBet.setText(bet+"");
            lbBalance.setText(m-bet+"");
        }
        @FXML private void onAllin () {
            String ip = txF.getText();
            lbBet.setText(m+"");
            lbBalance.setText(m-bet+"");
            kuld("bet:" + m, ip, 678);
        }
        @FXML private void onBet(){
            String ip = txF.getText();
            kuld("bet:" + bet, ip, 678);
        }
    }
