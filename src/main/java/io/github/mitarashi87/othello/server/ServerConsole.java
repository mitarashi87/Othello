package io.github.mitarashi87.othello.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import io.github.mitarashi87.othello.Config;
import io.github.mitarashi87.othello.player.CuiPlayer;
import io.github.mitarashi87.othello.player.Player;
import io.github.mitarashi87.othello.player.TcpPlayer;

public class ServerConsole {
	private Scanner sc;

	public ServerConsole(Scanner sc) {
		this.sc = sc;
	}

	public List<Player> launchMatchingRoom() {
		List<Player> players = Collections.synchronizedList(new ArrayList<>());
		CommandActions actions = new CommandActions(players);

		// TCPプレイヤー待ち受けスレッドを用意
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> waitingInviteTcpPlayer(players));

		String helpCommand = "help";
		Map<String, Consumer<String>> actionMap = this.getActionMap(helpCommand, actions);

		// 標準入力
		System.out.println();
		System.out.println("サーバーコンソールを起動しました");
		actionMap.get(helpCommand).accept("");

		while (!actions.finished) {
			System.out.print("> ");
			String input = sc.nextLine();
			this.tryParseAndExsecCommand(input, actionMap, helpCommand);
		}

		this.suppressCuiplayersBroadcastMessageOtherThanOnce(players);

		return players;

	}

	/**
	 * コマンド群の作成
	 */
	private Map<String, Consumer<String>> getActionMap(String helpCommand, CommandActions actions) {

		Map<String, Consumer<String>> actionMap = new HashMap<>();

		actionMap.put(helpCommand, (args) -> {
		});
		actionMap.put("invite", actions::invite);
		actionMap.put("start", actions::start);
		actionMap.put(helpCommand, (args) -> {
			System.out.println("使用可能コマンド一覧");
			String commandlist = String.join(", ", actionMap.keySet());
			System.out.println(commandlist);
		});
		return actionMap;
	}

	/**
	 * 与えられた文字列をコマンドと引数に分割して実行する
	 * 
	 * @param input "command arg1 arg2"
	 * @param actionMap コマンド挙動実装群
	 * @param helpCommand "help"
	 * @return inputがコマンドならtrue
	 */
	private Boolean tryParseAndExsecCommand(
			String input,
			Map<String, Consumer<String>> actionMap,
			String helpCommand) {
		String commandPrefix = "/";
		String commandSeparator = " ";
		Boolean isCommand = input.startsWith(commandPrefix);

		if (isCommand) {
			// "/command arg1 arg2" -> "command arg1 arg2"
			String withoutPrefix = input.substring(commandPrefix.length());
			// "command arg1 arg2" -> "command" "arg1" "arg2"
			String[] commandAndArgs = withoutPrefix.split(commandSeparator);
			// "command" "arg1" "arg2" -> "command"
			String command = commandAndArgs[0];
			if (actionMap.containsKey(command)) {
				Consumer<String> action = actionMap.get(command);
				// "command arg1 arg2" -> "command"+" " -> 8
				Integer argsPartFirstIndex = command.length() + commandSeparator.length();
				// "command arg1 arg2" -> "arg1 arg2"
				String commandArgs =
						withoutPrefix.length() < argsPartFirstIndex
								? ""
								: withoutPrefix.substring(argsPartFirstIndex);
				// run action
				action.accept(commandArgs);
			} else {
				System.out.println("該当するコマンドがありません。");
				actionMap.get(helpCommand).accept("");
			}
		}
		return isCommand;
	}

	/**
	 * コマンド挙動実装群
	 */
	private class CommandActions {
		private List<Player> players;
		private Boolean finished = false;

		public CommandActions(List<Player> players) {
			this.players = players;
		}

		public void invite(String args) {
			players.add(CuiPlayer.create(sc));
		}

		public void start(String args) {
			System.out.println("プレイヤーの待ち受けを終了します。");
			this.finished = true;
		}
	}

	private void waitingInviteTcpPlayer(List<Player> players) {
		int port = Config.port;
		try (ServerSocket server = new ServerSocket(port)) {
			System.out.println("port[%s] でサーバーを起動".formatted(port));
			System.out.println("プレイヤーを募集");
			while (true) {
				Socket socket = server.accept();
				ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
				String discIcon = (String) reader.readObject();
				System.out.println("新規プレイヤーが入室 : " + discIcon);
				Player player = new TcpPlayer(discIcon, socket, reader, writer);
				players.add(player);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 1人のCUIプレイヤー以外は全体メッセージを受け取っても表示しない
	 */
	private void suppressCuiplayersBroadcastMessageOtherThanOnce(List<Player> players) {
		List<CuiPlayer> cuiPlayers = new ArrayList<>();
		for (Player player : players) {
			if (player instanceof CuiPlayer cuiPlayer) {
				cuiPlayers.add(cuiPlayer);
			}
		}
		for (int i = 0; i < cuiPlayers.size(); i++) {
			if (i != 0) {
				CuiPlayer cuiPlayer = cuiPlayers.get(i);
				cuiPlayer.setShowBroadcastMessage(false);
			}
		}
	}

}

