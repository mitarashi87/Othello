package io.github.mitarashi87.othello.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import io.github.mitarashi87.othello.Config;
import io.github.mitarashi87.othello.Pos;
import io.github.mitarashi87.othello.message.TcpMessage;
import io.github.mitarashi87.othello.player.CuiPlayer;
import io.github.mitarashi87.othello.player.Player;

public class TcpOthelloClient {

	public static void run(Scanner sc) throws IOException {
		String host = Config.host;
		int port = Config.port;
		Socket socket = new Socket(host, port);
		System.out.println("サーバー[%s:%s]に接続".formatted(host, port));
		ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

		TcpOthelloClient client = new TcpOthelloClient();
		client.standBy(sc, reader, writer);

		// 切断処理
		socket.shutdownOutput();
		socket.close();
	}

	/**
	 * サーバーからメッセージを受信し続ける処理
	 * 
	 * メッセージによっては応答を返す
	 */
	public void standBy(Scanner sc, ObjectInputStream reader, ObjectOutputStream writer) {
		try {
			System.out.println("readThread実行");
			Player player = CuiPlayer.create(sc);
			writer.writeObject(player.getIcon());

			Boolean connectionAlive = true;
			while (connectionAlive) {
				Object messageFromServer = reader.readObject();

				if (messageFromServer instanceof String message) {
					System.out.println(message);
				}

				if (messageFromServer instanceof TcpMessage tcpMessage) {
					switch (tcpMessage) {
						// 座標入力をする
						case PLAY_POS:
							Pos pos = player.playPos();
							writer.writeObject(pos);
							break;

						case GAMESET:
							connectionAlive = false;
							break;

					}
				}
			}

			System.out.println("クライアント終了。");

		} catch (IOException e) {
			throw new RuntimeException("サーバーとの通信にトラブル", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("受信したObjectがプロジェクトに見当たらない", e);
		}
	}
}


