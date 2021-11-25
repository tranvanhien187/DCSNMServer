package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandle extends Thread {
    private int turn;
    private int indexRoom;
    Socket soc;
    DataOutputStream dos;
    DataInputStream dis;
    String idAddress;
    boolean isReady = false;
    private String namePlayer;

    public ClientHandle(Socket soc, int turn, int indexRoom) {
        try {
            idAddress = soc.getInetAddress().toString();
            System.out.println("User : " + soc.getInetAddress());
            this.soc = soc;
            dos = new DataOutputStream(soc.getOutputStream());
            dis = new DataInputStream(soc.getInputStream());
            this.turn = turn;
            this.indexRoom = indexRoom;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.namePlayer = dis.readUTF();
            while (true){
//                System.out.println("Size "+Server.roomList.get(indexRoom).getClients().size());
                if(Server.roomList.get(indexRoom).getClients().size()==2){
                    if(Server.roomList.get(indexRoom).getClients().get(0).getNamePlayer()!=null
                            && Server.roomList.get(indexRoom).getClients().get(1).getNamePlayer()!=null){
                        for (int i =0; i < Server.roomList.get(indexRoom).getClients().size(); i++) {
                            if (!Server.roomList.get(indexRoom).getClients().get(i).idAddress.equals(idAddress)) {
                                continue;
                            }
                            dos.writeUTF(Room.KEY_START + "");
                            dos.writeUTF( Server.roomList.get(indexRoom).getClients().get(0).getNamePlayer());
                            dos.writeUTF( Server.roomList.get(indexRoom).getClients().get(1).getNamePlayer());
                            dos.writeUTF(turn + "");
                        }
                        break;
                    }
                }
            }
            while (true) {
                int key = Integer.parseInt(dis.readUTF());
                switch (key) {
                    case ChineseChessServer.KEY_START -> {
                    }
                    case ChineseChessServer.KEY_MOVE -> {
                        handleActionMove();
                    }
                    case ChineseChessServer.KEY_RED_WIN -> {
                        handleActionWin(ChineseChessServer.KEY_RED_WIN);
                    }
                    case ChineseChessServer.KEY_BLACK_WIN -> {
                        handleActionWin(ChineseChessServer.KEY_BLACK_WIN);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void handleActionMove() throws IOException {
        int indexFrom = Integer.parseInt(dis.readUTF());
        int indexTo = Integer.parseInt(dis.readUTF());
        System.out.println(soc.getInetAddress() + " : " + indexFrom + " -> " + indexTo);
        if (this.turn%2 == Server.roomList.get(indexRoom).getRecordList().size()%2) {
            return;
        }
        // 3.Kiểm tra tọa độ client gửi có hợp lệ không
        // Lưu vào lịch sử
        Server.roomList.get(indexRoom).board[indexTo] = Server.roomList.get(indexRoom).board[indexFrom];
        Server.roomList.get(indexRoom).board[indexFrom] = 0;
        Server.roomList.get(indexRoom).addRecord(Server.roomList.get(indexRoom).board.clone());
        System.out.println(indexFrom + " gui ne" + indexTo);

        // 4.Gửi lại tọa độ cho tất cả client biết
        for (ClientHandle c : Server.roomList.get(indexRoom).getClients()) {
            try {
                c.dos.writeUTF(Room.KEY_MOVE + "");
                c.dos.writeUTF(indexFrom + "");
                c.dos.writeUTF(indexTo + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleActionWin(int action) throws IOException {
        for (ClientHandle c : Server.roomList.get(indexRoom).clients) {
            try {
                c.dos.writeUTF(action + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }
}
