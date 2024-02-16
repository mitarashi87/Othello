package io.github.mitarashi87.othello;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import io.github.mitarashi87.othello.client.TcpOthelloClient;
import io.github.mitarashi87.othello.player.Player;
import io.github.mitarashi87.othello.server.ServerConsole;

public class App {
	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);

		AppMode mode = AppMode.selectByCui(sc);
		App app = new App();
		switch (mode) {
			case SERVER:
				app.runServer(sc);
				break;
			case CLIENT:
				TcpOthelloClient.run(sc);
				break;
		}


		// ENTERの入力後にアプリケーションを終了する。
		sc.nextLine();
		sc.close();

	}

	public void runServer(Scanner sc) throws IOException, ClassNotFoundException {

		ServerConsole console = new ServerConsole(sc);
		List<Player> players = console.launchMatchingRoom();
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

		game.broadcastStatistics();
		game.broadcastGameEnd();

	}

}

/**
 * Board の squares を受け取って表示するクラス
 */
