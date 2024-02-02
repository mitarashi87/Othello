package io.github.mitarashi87.othello.player;

import io.github.mitarashi87.othello.Disc;
import io.github.mitarashi87.othello.Pos;

public abstract class Player {
	private final String icon;
	private String discIcon;



	public Player(String discIcon) {
		this.icon = discIcon;
		this.discIcon = discIcon;
	}

	public Disc playDisc() {
		return new Disc(discIcon);
	}

	public String getIcon() {
		return this.icon;
	}

	public abstract Pos playPos();

	/**
	 * ゲームからメッセージを受け取る
	 */
	public abstract void receiveMessage(String message);

	/**
	 * ゲームから全体メッセージを受け取る
	 */
	public void receiveBroadcastMessage(String message) {
		this.receiveMessage(message);
	}

	/**
	 * ゲーム終了を通知される
	 */
	public abstract void receiveGameEnd();

}
