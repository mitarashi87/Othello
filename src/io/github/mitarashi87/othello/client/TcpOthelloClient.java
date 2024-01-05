package io.github.mitarashi87.othello.client;

import java.io.IOException;
import java.io.ObjectInputStream;
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
		ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

		TcpOthelloClient client = new TcpOthelloClient();
		client.standBy(reader, writer);

		// 切断処理
		socket.shutdownOutput();
		socket.close();
	}

	/**
	 * サーバーからメッセージを受信し続ける処理
	 * 
	 * メッセージによっては応答を返す
	 */
	public void standBy(ObjectInputStream reader, ObjectOutputStream writer) {
		try {
			System.out.println("readThread実行");
			Player player = CuiPlayer.create();
			writer.writeObject(player.getIcon());

			while (true) {
				String messageFromServer = (String) reader.readObject();
				System.out.println(messageFromServer);
				if (messageFromServer.equals("PLAY_POS")) {
					// 座標入力をする
					Pos pos = player.playPos();
					writer.writeObject(pos);
				}

			}
		} catch (IOException e) {
			throw new RuntimeException("サーバーとの通信にトラブル", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("受信したObjectがプロジェクトに見当たらない", e);
		}
	}
}


