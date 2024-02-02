package io.github.mitarashi87.othello.player;

import java.util.Scanner;
import io.github.mitarashi87.othello.Pos;

public class CuiPlayer extends Player {
	Scanner scanner;

	public static CuiPlayer create(Scanner sc) {

		System.out.println("プレイヤーアイコンを入力してください。");
		System.out.print("> ");
		String discIcon = sc.nextLine();

		CuiPlayer player = new CuiPlayer(discIcon, sc);
		return player;
	}

	public CuiPlayer(String discIcon, Scanner sc) {
		super(discIcon);
		this.scanner = sc;
	}

	@Override
	public Pos playPos() {


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
