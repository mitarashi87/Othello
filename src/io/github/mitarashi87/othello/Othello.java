package io.github.mitarashi87.othello;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import io.github.mitarashi87.othello.player.Player;

public class Othello {
	/**
	 * true ならゲームが終了している。
	 */
	public Boolean finished = false;

	private Board board;
	private int currentTurn;
	private int skipCount;
	private List<Player> players;

	/**
	 * 規則に則った Othello の生成
	 */
	public static Othello creatOthello(List<Player> players) {

		Player host = players.get(0);

		Pos boardSize = null;
		while (boardSize == null) {
			host.receiveMessage("ボードサイズを入力してください。");
			host.receiveMessage("プレイヤーの数に合わせて、以下の条件を満たす必要があります");
			host.receiveMessage("・プレイヤーの数と同様に [偶数/奇数] であること。");
			host.receiveMessage("・プレイヤーの数より大きい数値であること。");

			Pos mayBeBoardSize = host.playPos();

			if (validateBoadSize(mayBeBoardSize, players.size())) {
				boardSize = mayBeBoardSize;
			} else {
				host.receiveMessage("\n値が正常ではありません。\nもう一度入力してください。\n");
			}
		}

		return new Othello(boardSize, players);
	}

	/**
	 * boardSize と Players が正規か判定
	 */
	private static boolean validateBoadSize(Pos boardSize, int playerCount) {
		if (!validateBoadSizeOnceAxis(boardSize.x(), playerCount))
			return false;
		if (!validateBoadSizeOnceAxis(boardSize.y(), playerCount))
			return false;
		return true;
	}

	private static boolean validateBoadSizeOnceAxis(int onceAxisSize, int playersCount) {
		if (onceAxisSize < playersCount)
			return false;
		if (playersCount % 2 != (onceAxisSize % 2))
			return false;
		return true;
	}

	/**
	 * オセロの作成と初期配置
	 */
	private Othello(Pos boardSize, List<Player> players) {
		this.players = players;
		this.board = new Board(boardSize);
		this.currentTurn = 0;
		this.skipCount = 0;
		this.initialPlacement();
	}

	private void initialPlacement() {
		Pos boardSize = board.getSize();

		// 初期配置の左上(起点)
		// 例) 偶数:(8 - 2) / 2 = 3 奇数:(7 - 3) / 2 = 2
		Pos leftTopPos = new Pos(
				((boardSize.x() - players.size()) / 2),
				((boardSize.y() - players.size()) / 2));

		// 初期配置の大きさの分の範囲(2人なら2x2)の座標(x,y)を列挙
		for (int x = 0; x < players.size(); x++) {
			for (int y = 0; y < players.size(); y++) {
				Pos pos = new Pos(
						leftTopPos.x() + x,
						leftTopPos.y() + y);
				// (x + y) の結果、一列ごとに参照 Player が1つずれる。
				int playerIndex = (x + y) % players.size();
				Player player = players.get(playerIndex);
				Disc disc = player.playDisc();
				this.board.setDisc(pos, disc);
			}
		}
	}

	public void nextTurn() {
		Player player = this.getCurrentPlayer();
		Disc disc = player.playDisc();

		// 置ける場所があるかチェック
		List<Pos> potisions = board.canDropPositions(disc);
		if (!potisions.isEmpty()) {
			Pos pos = requestValidPlayPos(player, disc);
			this.board.dropDisc(pos, disc);
			skipCount = 0;
		} else {
			skipCount++;
		}

		currentTurn += 1;

		if (skipCount == players.size()) {
			finished = true;
		}
	}

	/**
	 * 有効な座標が入力されるまで要求を繰り返す
	 */
	private Pos requestValidPlayPos(Player player, Disc disc) {
		while (true) {
			Pos pos = player.playPos();

			if (!board.canDrop(pos, disc)) {
				player.receiveMessage("入力をやり直してください");
				continue;
			}

			return pos;
		}
	}

	private Player getCurrentPlayer() {
		Player player = players.get(currentTurn % players.size());
		return player;
	}

	/**
	 * 全てのプレイヤーにメッセージを送信する
	 * 
	 */
	public void broadcastMassage(String message) {
		for (Player player : players) {
			player.receiveMessage(message);
		}
	}

	/**
	 * 全てのプレイヤーに集計情報を送信する
	 */
	public void broadcastStatistics() {
		Set<Disc> playerDiscSet = new LinkedHashSet<>();
		for (Player player : players) {
			Disc disc = player.playDisc();
			playerDiscSet.add(disc);
		}

		Map<Disc, Integer> countParDisc = board.getCountParDisc(playerDiscSet);
		String message = "";
		for (Entry<Disc, Integer> discCountPair : countParDisc.entrySet()) {
			Disc disc = discCountPair.getKey();
			Integer count = discCountPair.getValue();
			message += "%s: %s\n".formatted(disc, count);
		}

		broadcastMassage(message);
	}

	@Override
	public String toString() {
		Disc currentPlayerDisc = getCurrentPlayer().playDisc();

		return "現在のターン数 : " + (currentTurn + 1) + "\n" +
				(currentPlayerDisc + "のプレイヤーは石を置いてください。") +
				"\n\n" + board.toString();

	}

}
