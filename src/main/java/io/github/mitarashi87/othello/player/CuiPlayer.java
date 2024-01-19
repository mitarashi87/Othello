package io.github.mitarashi87.othello.player;

import java.util.Scanner;
import io.github.mitarashi87.othello.Pos;

public class CuiPlayer extends Player {

	public static CuiPlayer create() {

		System.out.println("プレイヤーアイコンを入力してください。");
		System.out.print("> ");
		Scanner scanner = new Scanner(System.in);
		String discIcon = scanner.nextLine();

		CuiPlayer player = new CuiPlayer(discIcon);
		return player;
	}

	public CuiPlayer(String discIcon) {
		super(discIcon);
	}

	@Override
	public Pos playPos() {

		Scanner scanner = new Scanner(System.in);
		System.out.print("x:");
		int x = scanner.nextInt();
		System.out.print("y:");
		int y = scanner.nextInt();
		Pos pos = new Pos(x, y);
		return pos;
	}

	@Override
	public void receiveMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void receiveGameEnd() {

	}

}
