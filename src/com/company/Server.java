package com.company;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static Room currentRoom;
    static int indexRoom = 0;
    static List<Room> roomList =  new ArrayList<>();
    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        currentRoom = new Room(indexRoom);
        try {
            int turn =1;
            ServerSocket server = new ServerSocket(8888);
            while (true) {
                Socket soc = server.accept();
                ClientHandle client = new ClientHandle(soc,turn,indexRoom);
                currentRoom.addClient(client);
                turn++;
                if (currentRoom.getClients().size()==2){
                    currentRoom.startGame();
                    roomList.add(currentRoom);
                    indexRoom++;
                    currentRoom = new Room(indexRoom);
                    turn=1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
