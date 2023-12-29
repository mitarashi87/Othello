package io.github.mitarashi87.othello;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws Exception {
		AppMode mode = AppMode.selectByCui();
		App app = new App();
		switch (mode) {
			case SERVER:
				app.runServer();
				break;
			case CLIENT:
				app.runClient();
				break;
		}

	}

	public void runServer() throws IOException, ClassNotFoundException {

		int port = 25565;

		ServerSocket server = new ServerSocket(port);
		System.out.println("port[%s] でサーバーを起動".formatted(port));

		System.out.println("プレイヤーを募集。");
		List<Player> players = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Socket socket = server.accept();
			ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
			String discIcon = (String) reader.readObject();
			System.out.println(discIcon);
			Player player = new TcpPlayer(discIcon, socket, reader);
			players.add(player);
		}

		playOthello(players);

		System.out.println("サーバー終了");
	}

	public void runClient() throws IOException {
		String host = "localhost";
		int port = 25565;
		Socket socket = new Socket(host, port);
		System.out.println("サーバー[%s:%s]に接続".formatted(host, port));

		ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());

		System.out.println("プレイヤーアイコンを入力してください。");
		System.out.print("> ");
		Scanner scanner = new Scanner(System.in);
		String discIcon = scanner.nextLine();

		writer.writeObject(discIcon);
		Player player = new Player(discIcon);

		Pos pos = player.playPos();

		writer.writeObject(pos);
		System.out.println("オブジェクトを送信 : %s".formatted(pos));

		// 切断処理
		socket.shutdownOutput();
		socket.close();
	}

	public void playOthello(List<Player> players) {
		System.out.println("Othello initialize");

		Othello game = Othello.creatOthello(players);

		System.out.println("");
		System.out.println("Game start!");
		System.out.println();

		while (!game.finished) {
			System.out.println(game);
			game.nextTurn();
		}

		System.out.println();
		System.out.println("Game end...");

	}

}

/**
 * Board の squares を受け取って表示するクラス
 */
