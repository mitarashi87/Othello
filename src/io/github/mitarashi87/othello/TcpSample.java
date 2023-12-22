package io.github.mitarashi87.othello;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpSample {
	public static void main(String[] args) throws Exception {

		int port = 9000;
		ServerSocket server = new ServerSocket(port);
		System.out.println("port[%s] でサーバーを起動".formatted(port));

		// 接続を待つ
		Socket socket = server.accept();
		String clientAddress = socket.getInetAddress().getHostAddress();
		System.out.println("接続を確認 : %s".formatted(clientAddress));
	}
}
