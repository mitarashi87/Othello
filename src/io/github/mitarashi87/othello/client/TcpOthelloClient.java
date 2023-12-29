package io.github.mitarashi87.othello.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import io.github.mitarashi87.othello.Pos;
import io.github.mitarashi87.othello.player.CuiPlayer;
import io.github.mitarashi87.othello.player.Player;

public class TcpOthelloClient {

	public static void run() throws IOException {
		String host = "localhost";
		int port = 25565;
		Socket socket = new Socket(host, port);
		System.out.println("サーバー[%s:%s]に接続".formatted(host, port));

		ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());

		Player player = CuiPlayer.create();
		writer.writeObject(player.getIcon());

		Pos pos = player.playPos();

		writer.writeObject(pos);
		System.out.println("オブジェクトを送信 : %s".formatted(pos));

		// 切断処理
		socket.shutdownOutput();
		socket.close();
	}
}
