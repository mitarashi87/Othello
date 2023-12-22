package io.github.mitarashi87.othello;

public class App {
	public static void main(String[] args) {

		System.out.println("Othello initialize");

		Othello game = Othello.creatOthello();

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
