package io.github.mitarashi87.othello.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import io.github.mitarashi87.othello.player.CuiPlayer;
import io.github.mitarashi87.othello.player.Player;

public class ServerConsole {
	private Scanner sc;

	public ServerConsole(Scanner sc) {
		this.sc = sc;
	}

	public List<Player> launchMatchingRoom() {
		List<CuiPlayer> cuiPlayers = new ArrayList<>();
		CommandActions actions = new CommandActions(cuiPlayers);

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
		private List<CuiPlayer> cuiPlayers;
		private Boolean finished = false;

		public CommandActions(List<CuiPlayer> cuiPlayers) {
			this.cuiPlayers = cuiPlayers;
		}

		public void invite(String args) {
			cuiPlayers.add(CuiPlayer.create(sc));
		}

		public void start(String args) {
			System.out.println("プレイヤーの待ち受けを終了します。");
			this.finished = true;
		}
	}
}

