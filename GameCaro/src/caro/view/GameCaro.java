package caro.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import caro.socket.Message;

public class GameCaro extends JFrame implements ActionListener {
	public GameCaro() {

	}

	public GameCaro(int value, Message msg) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GameCaro.class.getResource("/caro/icon/logo.png")));
		setType(Type.POPUP);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(GameCaro.class.getResource("/caro/icon/logo.png")));
		this.msg = msg;
		ngChoi = value;
		cW = false;
		b = new JButton[soDong + 2][soCot + 2];
		banCo = new char[soDong + 2][soCot + 2];
		setBounds(new Rectangle(5, 5, 5, 5));
		setTitle("GAME CARO");
		setIgnoreRepaint(true);
		setForeground(SystemColor.textText);
		cn = this.getContentPane();
		pn = new JPanel();
		pn.setFont(new Font("Times New Roman", Font.BOLD, 18));
		pn.setLayout(new GridLayout(soDong, soCot));

		for (int i = 0; i <= soDong + 1; i++)
			for (int j = 0; j <= soCot + 1; j++) {
				b[i][j] = new JButton(" ");
				b[i][j].setActionCommand(i + " " + j);
				b[i][j].setBackground(Color.white);
				b[i][j].addActionListener(this);
			}
		for (int i = 1; i <= soDong; i++)
			for (int j = 1; j <= soCot; j++)
				pn.add(b[i][j]);
		lb = new JLabel("X Đánh Trước");
		lb.setForeground(new Color(255, 0, 0));
		lb.setFont(new Font("Times New Roman", Font.BOLD, 16));
		pn2 = new JPanel();
		pn2.setFont(new Font("Times New Roman", Font.BOLD, 16));
		pn2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		pn2.setBackground(SystemColor.inactiveCaptionBorder);
		cn.add(pn);
		cn.add(pn2, "North");
		btnNewButton = new JButton("New Game");
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GameCaro((++ngChoi) % 2, msg);
				msg.send("1");
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton.setBackground(Color.RED);

		btnNewButton_1 = new JButton("Thoát");
		btnNewButton_1.setBackground(Color.BLACK);
		btnNewButton_1.setForeground(Color.MAGENTA);
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Start();
				msg.send("2");
				dispose();
			}
		});
		GroupLayout gl_pn2 = new GroupLayout(pn2);
		gl_pn2.setHorizontalGroup(gl_pn2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pn2.createSequentialGroup().addGap(440).addComponent(lb).addGap(228)
						.addComponent(btnNewButton).addGap(659).addComponent(btnNewButton_1).addContainerGap()));
		gl_pn2.setVerticalGroup(gl_pn2.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pn2.createSequentialGroup().addGap(5).addGroup(gl_pn2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton).addComponent(btnNewButton_1).addComponent(lb))));
		pn2.setLayout(gl_pn2);
		this.setVisible(true);
		this.setSize(1650, 1080);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void addPoint(int x, int y, int value) {
		b[x][y].setText("" + quanCo[(ngChoi + value) % 2]);
		b[x][y].setForeground(cl[(ngChoi + value) % 2]);
		b[x][y].setFont(new Font("Times New Roman", Font.BOLD, 10));
		banCo[x][y] = quanCo[(ngChoi + value) % 2];
		if (checkWin(x, y, quanCo[(ngChoi + value) % 2])) {
			lb.setBackground(Color.MAGENTA);
			if (value == 0)
				lb.setText("Bạn là người chiến thắng");
			else
				lb.setText("Bạn là người thua cuộc");
		}
		if (!cW && count == 159) {
			lb.setBackground(Color.MAGENTA);
			lb.setText("HÒA");
			endGame(false);
		}
		if (!cW) {
			if ((ngChoi + count) % 2 == 0)
				lb.setText("Lượt của bạn");
			else
				lb.setText("Lượt của đối thủ");
		}
		count++;
		endGame(false);
	}

	public static boolean checkWin(int x, int y, char co) {
		// Kiem tra hang ngang
		for (int i = y - 4 >= 1 ? y - 4 : 1, dem = 0; i <= (y + 4 <= soCot ? y + 4 : soCot); i++)
			if (banCo[x][i] == co) {
				dem++;
				if (dem >= 5)
					return true;
			} else
				dem = 0;
		// Kiem tra hang doc
		for (int i = x - 4 >= 1 ? x - 4 : 1, dem = 0; i <= (x + 4 <= soDong ? x + 4 : soDong); i++)
			if (banCo[i][y] == co) {
				dem++;
				if (dem >= 5)
					return true;
			} else
				dem = 0;
		// Kiem tra hang song song voi duong cheo chinh
		int stX = x - 4, stY = y - 4;
		while (stX < 1 || stY < 1) {
			stX++;
			stY++;
		}
		int edX = x + 4, edY = y + 4;
		while (edX > soDong || edY > soCot) {
			edX--;
			edY--;
		}
		for (int i = stX, j = stY, dem = 0; i <= edX; i++, j++)
			if (banCo[i][j] == co) {
				dem++;
				if (dem >= 5)
					return true;
			} else
				dem = 0;
		// Kiem tra hang song song voi duong cheo phu
		stX = x + 4;
		stY = y - 4;
		while (stX > soDong || stY < 1) {
			stX--;
			stY++;
		}
		edX = x - 4;
		edY = y + 4;
		while (edX < 1 || edY > soCot) {
			edX++;
			edY--;
		}
		for (int i = stX, j = stY, dem = 0; i >= edX; i--, j++)
			if (banCo[i][j] == co) {
				dem++;
				if (dem >= 5)
					return true;
			} else
				dem = 0;
		return false;
	}

	public void endGame(boolean bl) {
		for (int i = 1; i <= soDong; i++)
			for (int j = 1; j <= soCot; j++)
				if (banCo[i][j] == '\u0000')
					b[i][j].setEnabled(bl);
		cW = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		msg.send(s);
		String[] k = s.split(" ");
		int x = Integer.parseInt(k[0]);
		int y = Integer.parseInt(k[1]);
		if (banCo[x][y] == '\u0000') {
			addPoint(x, y, 0);
		}
	}

	Message msg;
	private static JButton b[][];
	private Container cn;
	private JPanel pn, pn2;
	private static JLabel lb;
	private static final int soDong = 30;
	private static final int soCot = 30;
	private static final char[] quanCo = { 'X', 'O' };
	private static final Color[] cl = { Color.RED, Color.BLUE };
	private static char[][] banCo;
	public static int ngChoi;
	private static boolean cW;
	private JButton btnNewButton;
	int count = 0;
	private JButton btnNewButton_1;
}
