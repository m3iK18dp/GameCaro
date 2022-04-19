package caro.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import caro.view.GameCaro;
import caro.view.ShowMess;

public class Message {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader reader;

	public Message(Socket socket, GameCaro gameCaro) throws IOException {
		out = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		receive(gameCaro);
	}

	private void receive(GameCaro gameCaro) {
		Thread th = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						String line = reader.readLine();
						if (line != null) {
							if (line.equals("1")) {
								gameCaro.dispose();
								new GameCaro((gameCaro.ngChoi + 1) % 2, Message.this);
							} else if (line.equals("2")) {
								gameCaro.dispose();
								new ShowMess("Doi phuong da thoat game. Ban la nguoi chien thang");
							} else {
								System.out.println(line);
								String point[] = line.split(" ");
								gameCaro.addPoint(Integer.parseInt(point[0]), Integer.parseInt(point[1]), 1);
							}
						}
					} catch (Exception e) {
					}
				}
			}
		};
		th.start();
	}

	public void send(String msg) {
		out.println(msg);
		out.flush();
	}

	public void close() {
		try {
			out.close();
			reader.close();
			socket.close();
		} catch (Exception e) {
		}
	}
}
