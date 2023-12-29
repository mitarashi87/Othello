package io.github.mitarashi87.othello;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerSample {
	public static void main(String[] args) throws Exception {

		int port = 25565;
		ServerSocket server = new ServerSocket(port);
		System.out.println("port[%s] でサーバーを起動".formatted(port));

		// 接続を待つ
		Socket socket = server.accept();
		String clientAddress = socket.getInetAddress().getHostAddress();
		System.out.println("接続を確認 : %s".formatted(clientAddress));

		ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
		// クライアントからメッセージを
		Pos pos = (Pos) reader.readObject();
		System.out.println("オブジェクトを受信 : %s".formatted(pos));

		System.out.println("サーバーを終了。");

	}
}
