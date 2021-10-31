package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        din=new DataInputStream(socket.getInputStream());
        dout=new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while(true){
            try {
                String msg=din.readUTF();
                System.out.println("client ---> "+msg);
                dout.writeUTF("concak");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("socket was died");
            }

        }
    }
}
