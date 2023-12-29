package io.github.mitarashi87.othello;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpClientSample {
	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 9000;
		Socket socket = new Socket(host, port);

		System.out.println("サーバー[%s:%s]に接続".formatted(host, port));
		ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());

		// 送信する内容を用意
		Pos pos = new Pos(1, 1);

		// サーバーに何かを送信
		writer.writeObject(pos);
		System.out.println("オブジェクトを送信 : %s".formatted(pos));

		System.out.println("クライアントを終了。");

	}
}
