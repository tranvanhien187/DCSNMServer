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
    static Vector<ClientHandle> clients = new Vector<ClientHandle>();
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
                //ClientHandle c = new ClientHandle(soc);
                //clients.add(c);
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
