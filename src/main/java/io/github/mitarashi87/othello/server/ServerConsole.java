package io.github.mitarashi87.othello.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import io.github.mitarashi87.othello.player.CuiPlayer;
import io.github.mitarashi87.othello.player.Player;

public class ServerConsole {
	private Scanner sc;

	public ServerConsole(Scanner sc) {
		this.sc = sc;
	}

	public List<Player> launchMatchingRoom() {
		List<CuiPlayer> cuiPlayers = new ArrayList<>();

		// 標準入力
		System.out.println();
		System.out.println("サーバーコンソールを起動しました");

		while (true) {
			System.out.print("> ");
			String command = sc.nextLine();
			if (command.equals("/help")) {
				System.out.println("TODO_ここでヘルプを表示");
			}
			if (command.equals("/start")) {
				System.out.println("プレイヤーの待ち受けを終了します。");
				break;
			}
			if (command.equals("/invite")) {
				cuiPlayers.add(CuiPlayer.create(sc));
			}
		}

		// 1人のCUIプレイヤー以外は全体メッセージを受け取っても表示しない
		for (int i = 0; i < cuiPlayers.size(); i++) {
			if (i != 0) {
				CuiPlayer cuiPlayer = cuiPlayers.get(i);
				cuiPlayer.setShowBroadcastMessage(false);
			}
		}

		List<Player> players = List.copyOf(cuiPlayers);
		return players;
	}
}
