package com.company;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChineseChessServer {
    public static void main(String[] args) throws IOException {
        new ChineseChessServer();
    }
    private static final int PORT=8888;
    static Vector<Handle> clients = new Vector<Handle>();
    private int[] firstBoard = {
            1, 3, 5, 7,16, 8, 6, 4, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 9, 0, 0, 0, 0, 0,10, 0,
            11, 0,12, 0,13, 0,14, 0,15,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            27, 0,28, 0,29, 0,30, 0,31,
            0,25, 0, 0, 0, 0, 0,26, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            17,19,21,23,32,24,22,20,18
    };
    List<int[]> record = new ArrayList<>();

    public ChineseChessServer() {
        try {
            ServerSocket server = new ServerSocket(8888);
            while (true) {
                Socket soc = server.accept();
                Handle c = new Handle(soc);
                clients.add(c);
                if (clients.size()==2){
                    System.out.println("Ok");
                    clients.get(0).start();
                    clients.get(1).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
class Handle extends Thread {
    Socket soc;
    DataOutputStream dos;
    DataInputStream dis;
    public Handle(Socket soc) {
        try {
            System.out.println("User : "+soc.getInetAddress());
            this.soc = soc;
            dos = new DataOutputStream(soc.getOutputStream());
            dis = new DataInputStream(soc.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        for (int i = 0;i<  ChineseChessServer.clients.size();i++) {
            try {
                if(i%2==0) {
                    System.out.println(i+"");
                    dos.writeUTF("1");
                }else {
                    dos.writeUTF("2");
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {
loop:		while (true) {
                //Server nhan tu client
//                int indexTo = Integer.parseInt(dis.readUTF());
//                int indexFrom = Integer.parseInt(dis.readUTF());
//                System.out.println(soc.getInetAddress()+","+indexTo+","+indexTo);
                //Server xu ly
                // 1.Kiểm tra đủ 2 người tham gia hay chưa
//                if (.clients.size()<2) continue;
                // 2.Kiểm tra lượt đánh có hợp lệ/ client có được quyền đánh hay không!
//                if (this==CaroServer.clients.get(0) && CaroServer.dadanh.size()%2!=0) continue;
//                if (this==CaroServer.clients.get(1) && CaroServer.dadanh.size()%2!=1) continue;

                // 3.Kiểm tra tọa độ client gửi có hợp lệ không
                // Lưu vào lịch sử

                //4.Gửi lại tọa độ cho tất cả client biết
//                for (Handle c : .clients) {
//                    try {
//                        c.dos.writeUTF("message");
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
