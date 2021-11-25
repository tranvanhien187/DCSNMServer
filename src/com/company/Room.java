package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Room {
    Vector<ClientHandle> clients = new Vector<>(2);
    public static final int KEY_RED_WIN = 103;
    public static final int KEY_BLACK_WIN = 104;
    public static final int KEY_START = 100;
    public static final int KEY_MOVE = 101;
    public static final int KEY_READY = 99;
    private static final String ip = "192.168.1.4";
    private static final int PORT=8888;
    private int indexRoom;
    public int[] board = {
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
    private List<int[]> recordList = new ArrayList<>();

    public Room(int indexRoom) {
        this.indexRoom = indexRoom;
    }

    public Vector<ClientHandle> getClients() {
        return clients;
    }

    public void setClients(Vector<ClientHandle> clients) {
        this.clients = clients;
    }
    public void addClient(ClientHandle client){
        this.clients.add(client);
    }
    public void startGame(){
        clients.get(0).start();
        clients.get(1).start();
    }

    public int getIndexRoom() {
        return indexRoom;
    }

    public List<int[]> getRecordList() {
        return recordList;
    }


    public void addRecord(int[] board){
        recordList.add(board);
    }
}
