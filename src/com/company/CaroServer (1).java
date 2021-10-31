package com.company;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

class CaroServer {
	public static void main(String[] args) {
		new CaroServer();
	}
	static Vector<Xuly> clients = new Vector<Xuly>();
	static int n = 15;
	static List<Point> dadanh = new Vector<Point>();
	public CaroServer() {
		try {
			ServerSocket server = new ServerSocket(80);
			while (true) {
				Socket soc = server.accept();
				Xuly c = new Xuly(soc);
				clients.add(c);
				if (clients.size()<=2)
					c.start();
			}
		} catch (Exception e) {
		}
	}
}

class Xuly extends Thread {
	Socket soc;
	DataOutputStream dos;
	DataInputStream dis;
	public Xuly(Socket soc) {
		try {
			System.out.println("User:"+soc.getInetAddress());
			this.soc = soc;
			dos = new DataOutputStream(soc.getOutputStream());
			dis = new DataInputStream(soc.getInputStream());
			for (Point d : CaroServer.dadanh) {
				dos.writeUTF(d.x+"");
				dos.writeUTF(d.y+"");
			}
		} catch (Exception e) {
		}
	}

	public void run() {
		try {
loop:		while (true) {
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				System.out.println(soc.getInetAddress()+","+ix+","+iy);
				//Server xu ly
				// 1.Kiểm tra đủ 2 người tham gia hay chưa
				if (CaroServer.clients.size()<2) continue;
				// 2.Kiểm tra lượt đánh có hợp lệ/ client có được quyền đánh hay không!
				if (this==CaroServer.clients.get(0) && CaroServer.dadanh.size()%2!=0) continue;
				if (this==CaroServer.clients.get(1) && CaroServer.dadanh.size()%2!=1) continue;
				
				// 3.Kiểm tra tọa độ client gửi có hợp lệ không
				for (Point d : CaroServer.dadanh) {
					if (d.x == ix && d.y == iy) continue loop;
				}
				
				CaroServer.dadanh.add(new Point(ix,iy));
				//4.Gửi lại tọa độ cho tất cả client biết
				for (Xuly c : CaroServer.clients) {
					try {
						c.dos.writeUTF(ix+"");
						c.dos.writeUTF(iy+"");
					}catch(Exception e) {
					}
				}
			}
		} catch (Exception e) {

		}
	}
}