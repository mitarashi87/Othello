package io.github.mitarashi87.othello;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import io.github.mitarashi87.othello.client.TcpOthelloClient;
import io.github.mitarashi87.othello.player.CuiPlayer;
import io.github.mitarashi87.othello.player.Player;
import io.github.mitarashi87.othello.player.TcpPlayer;

public class App {
	public static void main(String[] args) throws Exception {
		AppMode mode = AppMode.selectByCui();
		App app = new App();
		switch (mode) {
			case SERVER:
				app.runServer();
				break;
			case CLIENT:
				TcpOthelloClient.run();
				break;
		}

	}

	public void runServer() throws IOException, ClassNotFoundException {

		List<Player> players = new ArrayList<>();

		// ホストはCUIから参加
		players.add(CuiPlayer.create());

		// 他のプレイヤーをTCP通信から受け付ける
		int port = 25565;

		System.out.println("port[%s] でサーバーを起動".formatted(port));
		ServerSocket server = new ServerSocket(port);
		System.out.println("プレイヤーを募集。");
		for (int i = 0; i < 1; i++) {
			Socket socket = server.accept();
			ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
			String discIcon = (String) reader.readObject();
			System.out.println(discIcon);
			Player player = new TcpPlayer(discIcon, socket, reader, writer);
			players.add(player);
		}

		playOthello(players);

		System.out.println("サーバー終了");
	}

	public void playOthello(List<Player> players) {
		System.out.println("Othello initialize");

		Othello game = Othello.creatOthello(players);

		game.broadcastMassage("");
		game.broadcastMassage("Game start!");
		game.broadcastMassage("");

		while (!game.finished) {
			game.broadcastMassage(game.toString());
			game.nextTurn();
		}

		game.broadcastMassage("");
		game.broadcastMassage("Game end...");

	}

}

/**
 * Board の squares を受け取って表示するクラス
 */
