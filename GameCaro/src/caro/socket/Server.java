package caro.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import caro.view.GameCaro;
import caro.view.WaitRoom;

public class Server {
	Message msg;
	GameCaro gameCaro = new GameCaro();
	ServerSocket listener = null;
	Socket socketOfServer = null;
	WaitRoom waitRoom = null;

	public Server() {
		try {
			int port = (int) (Math.random() * 8975) + 1024;
			listener = new ServerSocket(port);
			waitRoom = new WaitRoom(port);
			Thread th = new Thread() {
				@Override
				public void run() {
					try {
						socketOfServer = listener.accept();
						msg = new Message(socketOfServer, gameCaro);
						waitRoom.dispose();
						gameCaro = new GameCaro(0, msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			th.start();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}