package io.github.mitarashi87.othello.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
		ExecutorService worker = Executors.newCachedThreadPool();
		worker.execute(() -> client.readThread(reader));

		client.writeThread(writer);

		// 切断処理
		socket.shutdownOutput();
		socket.close();
	}

	public void readThread(ObjectInputStream reader) {
		try {
			System.out.println("readThread実行");

			while (true) {
				System.out.println("サーバーから受信" + reader.readObject());
			}
		} catch (IOException e) {
			throw new RuntimeException("サーバーとの通信にトラブル", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("受信したObjectがプロジェクトに見当たらない", e);
		}
	}

	public void writeThread(ObjectOutputStream writer) {
		try {
			System.out.println("writeThread実行");
			Player player = CuiPlayer.create();
			writer.writeObject(player.getIcon());

			while (true) {
				Pos pos = player.playPos();
				writer.writeObject(pos);
			}
		} catch (IOException e) {
			throw new RuntimeException("サーバーとの通信にトラブル", e);
		}
	}
}


