package io.github.mitarashi87.othello;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpClientSample {
	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 9000;
		Socket socket = new Socket(host, port);
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

		Scanner scanner = new Scanner(System.in);
		System.out.println("通信内容を入力してください。");
		System.out.print(">");
		String input = scanner.nextLine();

		writer.println(input);
		System.out.println("送信を完了しました。");

	}
}
