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
                    case Room.KEY_START -> {
                    }
                    case Room.KEY_MOVE -> {
                        handleActionMove();
                    }
                    case Room.KEY_RED_WIN -> {
                        handleActionWin(Room.KEY_RED_WIN);
                    }
                    case Room.KEY_BLACK_WIN -> {
                        handleActionWin(Room.KEY_BLACK_WIN);
                    }
                    case Room.KEY_SEND_REQUEST_PLAY_AGAIN -> {
                        handleActionSendRequestPlayAgain();
                    }
                    case Room.KEY_ACCEPT_REQUEST_PLAY_AGAIN -> {
                        handleActionAcceptRequestPlayAgain();
                    }
                    case Room.KEY_DECLINE_REQUEST_PLAY_AGAIN -> {
                        handleActionDeclineRequestPlayAgain();
                    }
                    case Room.KEY_QUIT -> {
                        handleActionQuit();
                    }
                }
                if(this.dis==null || this.dos==null) {
                    System.out.println("Room "+indexRoom + " break");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void handleActionQuit() {
        System.out.println("Room "+indexRoom + " handleActionQuit");
        try {
            for (ClientHandle c : Server.roomList.get(indexRoom).getClients()) {
                if(c.turn!=turn){
                    c.dos.writeUTF(Room.KEY_QUIT + "");
                }
                c.dos = null;
                c.dis = null;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleActionDeclineRequestPlayAgain() {
        System.out.println("Room "+indexRoom + " handleActionDeclineRequestPlayAgain");
        try {
            for (ClientHandle c : Server.roomList.get(indexRoom).getClients()) {
                if(c.turn!=turn){
                    c.dos.writeUTF(Room.KEY_DECLINE_REQUEST_PLAY_AGAIN + "");
                }
                c.dos = null;
                c.dis = null;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void handleActionAcceptRequestPlayAgain() {
        try {
            for (ClientHandle c : Server.roomList.get(indexRoom).getClients()) {
                c.dos.writeUTF(Room.KEY_PLAY_AGAIN + "");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleActionSendRequestPlayAgain() {
        try {
            int turn = Integer.parseInt(dis.readUTF());
            for (ClientHandle c : Server.roomList.get(indexRoom).getClients()) {
                if(c.turn!=turn){
                    c.dos.writeUTF(Room.KEY_SEND_REQUEST_PLAY_AGAIN + "");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
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
        System.out.println("Room "+indexRoom + " handleActionWin");
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
