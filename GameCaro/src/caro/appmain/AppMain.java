package caro.appmain;

import java.awt.EventQueue;

import caro.view.Start;

public class AppMain {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new Start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
