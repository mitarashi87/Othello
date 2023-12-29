package io.github.mitarashi87.othello;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpPlayer extends Player {
	private final ObjectInputStream reader;
	private final ObjectOutputStream writer;
	private final Socket socket;


	public TcpPlayer(String discIcon, Socket socket, ObjectInputStream reader) throws IOException {
		super(discIcon);
		this.socket = socket;
		this.reader = reader;
		this.writer = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public Pos playPos() {

		try {
			Pos pos = (Pos) reader.readObject();
			return pos;
		} catch (ClassNotFoundException e) {
			this.receiveMessage("座標として読み取れませんでした。再入力してください。");
			return playPos();
		} catch (IOException e) {
			this.receiveMessage("通信に失敗しました。再入力してください。");
			return playPos();
		}

	}

	@Override
	public void receiveMessage(String message) {
		try {
			writer.writeObject(message);
		} catch (IOException e) {
			String logMessage = "クライアントへのメッセージ送信に失敗 : icon - %s, message - %s, socket - %s"
					.formatted(this.playDisc(), message, socket);
			throw new RuntimeException(logMessage, e); // TODO アプリがいきなり落ちる。怖い。
		}
	}
}
