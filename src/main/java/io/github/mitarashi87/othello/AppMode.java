package io.github.mitarashi87.othello;

import java.util.Scanner;

public enum AppMode {
	SERVER, CLIENT;

	public static AppMode selectByCui() {
		System.out.println("アプリケーションの起動モードを選択してください。");
		AppMode[] modes = AppMode.values();
		for (int index = 0; index < modes.length; index++) {
			System.out.println("%s: %s".formatted(index, modes[index]));
		}

		System.out.print(">");
		Scanner scanner = new Scanner(System.in);
		int input = scanner.nextInt();
		return AppMode.values()[input];

	}
}
