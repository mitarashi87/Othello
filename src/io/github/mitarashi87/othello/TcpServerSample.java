package io.github.mitarashi87.othello;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerSample {
	public static void main(String[] args) throws Exception {

		int port = 9000;
		ServerSocket server = new ServerSocket(port);
		System.out.println("port[%s] でサーバーを起動".formatted(port));

		// 接続を待つ
		Socket socket = server.accept();
		String clientAddress = socket.getInetAddress().getHostAddress();
		System.out.println("接続を確認 : %s".formatted(clientAddress));

		// クライアントからメッセージを
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		String messageFromClient = reader.readLine();
		System.out.println("メッセージを受信 : %s".formatted(messageFromClient));

		System.out.println("サーバーを終了。");

	}
}
