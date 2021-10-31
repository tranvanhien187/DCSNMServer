package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player implements Runnable {
    DataOutputStream dos;
    DataInputStream dis;
    private int turn ;

    public static void main(String[] args) {
        new Thread(new Player()).start();
    }
    public Player() {
        new Thread(() -> {
            try {
                Socket soc = new Socket("192.168.1.9", 8888);
                dos = new DataOutputStream(soc.getOutputStream());
                dis = new DataInputStream(soc.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    @Override
    public void run() {
        try {
//            dis.readUTF();
//            int turn = Integer.parseInt("1");
//            this.turn = turn;
//            Toast.makeText(mContext,"Ready To Play", Toast.LENGTH_SHORT).show();
            while(true) {
                int ix = Integer.parseInt(dis.readUTF());
                int iy = Integer.parseInt(dis.readUTF());
            }
        }catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}