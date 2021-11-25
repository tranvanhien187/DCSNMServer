package com.company;

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

    public static final int KEY_RED_WIN = 103;
    public static final int KEY_BLACK_WIN = 104;
    public static final int KEY_START = 100;
    public static final int KEY_MOVE = 101;
    private static final String ip = "192.168.1.4";
    private static final int PORT=8888;
    static Vector<Handle> clients = new Vector<Handle>();
    public static int[] board = {
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
    static List<int[]> recordList = new ArrayList<>();

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
                    recordList.add(board.clone());
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
    String idAddress;
    public Handle(Socket soc) {
        try {
            idAddress = soc.getInetAddress().toString();
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

                // Send action start game to client
                if(!ChineseChessServer.clients.get(i).idAddress.equals(idAddress)){
                    continue;
                }
                if(i%2==0) {
                    dos.writeUTF(ChineseChessServer.KEY_START+"");
                    dos.writeUTF("1");
                }else {
                    dos.writeUTF(ChineseChessServer.KEY_START+"");
                    dos.writeUTF("2");
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        try {
loop:		while (true) {
                int key = Integer.parseInt(dis.readUTF());
                switch (key){
                    case ChineseChessServer.KEY_START:
                    {
                        break ;
                    }
                    case ChineseChessServer.KEY_MOVE:
                    {
                        handleActionMove();
                        break ;
                    }
                    case ChineseChessServer.KEY_RED_WIN:
                    {
                        handleActionWin(ChineseChessServer.KEY_RED_WIN);
                        break ;
                    }
                    case ChineseChessServer.KEY_BLACK_WIN:
                    {
                        handleActionWin(ChineseChessServer.KEY_BLACK_WIN);
                        break ;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleActionMove() throws IOException {
        int indexFrom = Integer.parseInt(dis.readUTF());
        int indexTo = Integer.parseInt(dis.readUTF());
        System.out.println(soc.getInetAddress()+","+indexFrom+","+indexTo);
        if (this==ChineseChessServer.clients.get(0) && ChineseChessServer.recordList.size()%2!=1) {
            return;
        }
        if (this==ChineseChessServer.clients.get(1) && ChineseChessServer.recordList.size()%2!=0) return;

        // 3.Kiểm tra tọa độ client gửi có hợp lệ không
        // Lưu vào lịch sử
        ChineseChessServer.board[indexTo] = ChineseChessServer.board[indexFrom];
        ChineseChessServer.board[indexFrom] = 0;
        ChineseChessServer.recordList.add(ChineseChessServer.board.clone());
        System.out.println(indexFrom+" gui ne"  +indexTo);

        // 4.Gửi lại tọa độ cho tất cả client biết
        for (Handle c : ChineseChessServer.clients) {
            try {
                c.dos.writeUTF(ChineseChessServer.KEY_MOVE+"");
                c.dos.writeUTF(indexFrom+"");
                c.dos.writeUTF(indexTo+"");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void handleActionWin(int action) throws IOException{
        for (Handle c : ChineseChessServer.clients) {
            try {
                c.dos.writeUTF(action+"");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
